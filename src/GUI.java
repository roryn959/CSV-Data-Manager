import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class GUI extends JFrame {
    private JPanel backPanel;
    private JPanel leftPanel;
    private JPanel checkboxPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private JPanel sortButtonsPanel;

    private JTextArea textArea;
    private JScrollPane scrollpane;

    private JButton loadButton;
    private JButton refreshButton;

    private JFileChooser fileExplorer;

    private ArrayList<Checkbox> checkboxes;

    private Model model;

    private boolean showWindowBuilt;

    private Color backgroundColour;
    private Color textboxColour;
    private Color textColour;
    private Color buttonColour;

    public GUI(String[] args){
        super("Database");

        if (args.length != 0 && args[0].equals("dark")){
            this.backgroundColour = Color.DARK_GRAY;
            this.textboxColour = Color.DARK_GRAY;
            this.textColour = Color.WHITE;
            this.buttonColour = Color.LIGHT_GRAY;
        }
        else{
            this.backgroundColour = Color.WHITE;
            this.textboxColour = Color.WHITE;
            this.textColour = Color.BLACK;
            this.buttonColour = Color.WHITE;
        }

        this.model = new Model();
        this.buildDefaultWindow();

        setLocationRelativeTo(null); //Centres window on page

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildDefaultWindow(){
        this.backPanel = new JPanel();
        this.backPanel.setLayout(new BorderLayout());

        this.createFileExplorer();

        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new BorderLayout());
        this.backPanel.add(this.buttonPanel, BorderLayout.SOUTH);

        this.loadButton = new JButton("Load File");
        this.loadButton.addActionListener(event -> this.loadSequence());
        this.loadButton.setBackground(this.buttonColour);
        buttonPanel.add(this.loadButton, BorderLayout.CENTER);

        add(this.backPanel);
        pack();
    }

    private void buildShowWindow(){
        this.tablePanel = new JPanel();
        this.tablePanel.setBackground(this.backgroundColour);

        this.leftPanel = new JPanel(new BorderLayout()); //leftPanel contains the checkboxes and the sorting buttons, all kept to the left of the window.

        this.sortButtonsPanel = new JPanel(new GridLayout(0, 2));
        this.sortButtonsPanel.setBackground(this.backgroundColour);

        this.checkboxPanel = new JPanel(new GridLayout(0, 1));
        this.checkboxPanel.setBackground(this.backgroundColour);

        this.textArea = new JTextArea(31, 118);
        this.textArea.setEditable(false); //User cannot change the data
        this.textArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        this.textArea.setBackground(this.textboxColour);
        this.textArea.setForeground(this.textColour);

        this.scrollpane = new JScrollPane(this.textArea);
        this.tablePanel.add(this.scrollpane);

        this.backPanel.add(this.tablePanel, BorderLayout.CENTER);

        this.backPanel.add(this.leftPanel, BorderLayout.WEST);
        this.leftPanel.add(this.checkboxPanel, BorderLayout.CENTER);
        this.leftPanel.add(this.sortButtonsPanel, BorderLayout.EAST);

        this.refreshButton = new JButton("Refresh");
        this.refreshButton.addActionListener(event -> this.updateDisplay());
        this.refreshButton.setBackground(this.buttonColour);
        this.buttonPanel.add(this.refreshButton, BorderLayout.WEST);

        pack();
        this.showWindowBuilt = true; //Show window now built - don't need to build again.
    }

    private void buildCheckBoxes(){
        ArrayList<String> names = this.model.getNames();
        this.checkboxes = new ArrayList<>();
        this.checkboxPanel.removeAll();

        for (String name : names){
            Checkbox newBox = new Checkbox(name, true);
            newBox.setForeground(this.textColour);
            this.checkboxes.add(newBox);
            this.checkboxPanel.add(newBox);
        }
    }

    private void buildSortButtons(){
        ArrayList<String> names = this.model.getNames();
        this.sortButtonsPanel.removeAll();
        JButton ascButton, descButton;

        for (String name : names){
            //Ascending order button
            ascButton = new JButton("/\\");
            ascButton.addActionListener(event -> this.sortSequence(name, true));
            ascButton.setBackground(this.buttonColour);
            this.sortButtonsPanel.add(ascButton);

            //Descending order button
            descButton = new JButton("\\/");
            descButton.addActionListener(event -> this.sortSequence(name, false));
            descButton.setBackground(this.buttonColour);
            this.sortButtonsPanel.add(descButton);
        }
    }

    private void sortSequence(String name, boolean ascending){
        try {
            this.model.sortData(name, ascending);
        } catch (NoSuchFieldException e){
            this.textArea.setText("An error occurred while sorting the data");
        }
        this.updateDisplay();

        pack();
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH); //Maximise window
    }

    private void updateDisplay(){
        this.model.resetIteration();
        ArrayList<String> fields = this.model.getNames();
        ArrayList<String> activeFields = new ArrayList<>(); //A list of all the relevant fields
        this.textArea.setText(""); //Reset text area to be empty

        for (int i = 0; i<this.checkboxes.size(); i++){ //For each checkbox
            if (this.checkboxes.get(i).getState()){ //If checkbox is selected
                activeFields.add(fields.get(i)); //Add field to active fields which will actually be shown
            }
        }

        this.model.setActiveFields(activeFields);

        try {
            this.model.setFieldTextSizes();

            //Add the fields at the top of the page
            this.textArea.append(this.model.getTitlesText());

            while (this.model.hasNext()){
                this.textArea.append(this.model.getNext());
                }

            } catch (NoSuchFieldException e){
            this.textArea.setText("An error occurred while displaying data");
        }

        this.textArea.select(0, 0);
    }

    private void loadSequence() {
        int approved = this.fileExplorer.showOpenDialog(null);

        if (approved == JFileChooser.APPROVE_OPTION) {
            String load_path = this.fileExplorer.getSelectedFile().getAbsolutePath();

            try {
                this.model.load(load_path);
            } catch (Exception e) {
                this.textArea.setText("Failed to open the file");
                return;
            }

            if (!(this.showWindowBuilt)) {
                this.buildShowWindow();
            }

            this.buildCheckBoxes();
            this.buildSortButtons();
            this.updateDisplay();

            pack();
            setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH); //Maximise window
        }
    }

    private void createFileExplorer(){
        this.fileExplorer = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        this.fileExplorer.setAcceptAllFileFilterUsed(false); //Set chooser to *not* accept every file type
        File current_directory = new File(System.getProperty("user.dir")); //The current directory of the user - likely to be closer to desired file
        this.fileExplorer.setCurrentDirectory(current_directory); //Opens in user's current directory
        this.fileExplorer.addChoosableFileFilter(new FileNameExtensionFilter(".csv", "csv")); //Only files ending with .csv
    }
}

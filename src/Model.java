import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Model {
    private DataFrame frame;
    private ArrayList<String> activeFields;
    private int counter;
    private ArrayList<Integer> fieldTextSizes;

    public void load(String path) throws IOException, NoSuchFieldException {
        this.frame = DataLoader.loadFile(path);
    }

    public ArrayList<String> getNames(){
        return this.frame.getColumnNames();
    }

    public void setActiveFields(ArrayList<String> activeFields){
        this.activeFields = activeFields;
    }

    public void resetIteration(){
        this.counter = 0;
    }

    public boolean hasNext(){
        return counter < this.frame.getRowCount();
    }

    public String getNext() throws NoSuchFieldException{

        String currentString = "";

        for (int i=0; i<this.activeFields.size(); i++){
            String currentWord = this.frame.getValue(this.activeFields.get(i), counter);
            currentString += currentWord;

            //Add n spaces to pad the columns to display fields in the same place
            for (int n=0; n<this.fieldTextSizes.get(i) - currentWord.length(); n++){
                currentString += ' ';
            }

            currentString += " | ";
        }

        counter++;
        return currentString + '\n';
    }

    public String getTitlesText(){
        String line = "";
        for (int i=0; i<this.activeFields.size(); i++){
            String word = this.activeFields.get(i);
            line += word;

            //Add appropriate number of spaces
            for (int n=0; n<this.fieldTextSizes.get(i) - word.length(); n++){
                line += ' ';
            }

            line += " | ";
        }
        line += '\n';

        return line;
    }

    public void setFieldTextSizes() throws NoSuchFieldException {
        this.fieldTextSizes = new ArrayList<>();

        for (String field : this.activeFields){
            this.fieldTextSizes.add(this.frame.getLongestText(field));
        }
    }

    public void sortData(String name, boolean ascending) throws NoSuchFieldException {
        this.frame.sort(name, ascending);
    }
}

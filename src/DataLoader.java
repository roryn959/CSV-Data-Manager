import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class DataLoader {

    public static DataFrame loadFile(String fileName) throws IOException, NoSuchFieldException {
        DataFrame frame = new DataFrame();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String[] titles = reader.readLine().split(",");
        for (String title : titles) {
            Column newColumn = new Column(title);
            frame.addColumn(newColumn);
        }

        String line;
        while ((line = reader.readLine()) != null){
            line += "#"; //An 'end of line' character, to prevent empty fields at the end of line from being lost by regex.
            String[] lineArray = line.split(",");

            lineArray[lineArray.length - 1] = removeLast(lineArray[lineArray.length - 1]);

            for (int i=0; i< lineArray.length; i++){
                frame.addValue(titles[i], lineArray[i]);
            }
        }

        return frame;
    }

    private static String removeLast(String s){
        return s.substring(0, s.length() -1);
    }
}
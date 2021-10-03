import java.util.ArrayList;

public class DataFrame {
    private ArrayList<Column> array;

    public DataFrame(){
        this.array = new ArrayList<>();
    }

    public void addColumn(Column c){
        this.array.add(c);
    }

    public ArrayList<String> getColumnNames(){
        ArrayList<String> names = new ArrayList<>();

        for (Column c : this.array){
            names.add(c.getName());
        }

        return names;
    }
    public Column getColumn(String name) throws NoSuchFieldException {
        for (Column c : this.array){
            if (c.getName().equals(name)){
                return c;
            }
        }
        throw new NoSuchFieldException("There are no rows with name " + name + " in this dataframe");
    }
    public int getRowCount(String name) throws NoSuchFieldException {
        Column c = this.getColumn(name);
        return c.getSize();
    }

    public int getRowCount(){
        return this.array.get(0).getSize();
    }

    public String getValue(String name, int index) throws NoSuchFieldException, ArrayIndexOutOfBoundsException {
        Column c = this.getColumn(name);
        return c.getRowValue(index);
    }

    public void putValue(String name, int index, String value) throws NoSuchFieldException, ArrayIndexOutOfBoundsException {
        Column c = this.getColumn(name);
        c.setRowValue(index, value);
    }
    public void addValue(String name, String value) throws NoSuchFieldException {
        Column c = this.getColumn(name);
        c.addRowValue(value);
    }

    public int getLongestText(String name) throws NoSuchFieldException{
        int max = name.length();
        Column col = this.getColumn(name);

        for (int i=0; i<col.getSize(); i++){
            int length = col.getRowValue(i).length();
            if (length > max){
                max = length;
            }
        }
        return max;
    }

    public void sort(String name, boolean ascending) throws NoSuchFieldException {
        this.bubbleSort(name, ascending);
    }

    private void bubbleSort(String name, boolean ascending) throws NoSuchFieldException {
        int n = this.getRowCount();
        boolean swapped = true;
        boolean condition;
        int comparison;
        String s1, s2;

        while (swapped){
            swapped = false;
            for (int i=0; i<n-1; i++){
                s1 = this.getValue(name, i);
                s2 = this.getValue(name, i+1);
                comparison = s1.compareTo(s2);

                if (ascending){
                    condition = comparison>0;
                }
                else{
                    condition = comparison<0;
                }

                if (condition) {
                    this.swap(i, i + 1);
                    swapped = true;
                }
            }
        }
    }

    private void swap(int x, int y){
        String holdValue;

        for (Column col : this.array){
            holdValue = col.getRowValue(x);
            col.setRowValue(x, col.getRowValue(y));
            col.setRowValue(y, holdValue);
        }
    }
}

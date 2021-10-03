import java.util.ArrayList;
import java.util.Iterator;

public class Column {
    private String name;
    private ArrayList<String> array;
    private int size;
    public Iterator iterator;

    public Column(String name){
        this.name = name;
        this.array = new ArrayList<>();
        this.size = 0;
        this.iterator = this.array.iterator();
    }

    public String getName(){
        return this.name;
    }

    public int getSize(){
        return this.size;
    }

    public String getRowValue(int index) throws ArrayIndexOutOfBoundsException {
        if (index >= this.size){
            throw new ArrayIndexOutOfBoundsException("Column size: " + this.size + " - Access attempted: " + index);
        }
        else{
            return this.array.get(index);
        }
    }

    public void setRowValue(int index, String new_value) throws ArrayIndexOutOfBoundsException {
        if (index >= this.size){
            throw new ArrayIndexOutOfBoundsException("Column size: " + this.size + " - Access attempted: " + index);
        }
        else{
            this.array.set(index, new_value);
        }
    }

    public void addRowValue(String value){
        this.array.add(value);
        this.size++;
    }

    public void display(){
        for (String s : this.array){
            System.out.println(s);
        }
    }
}

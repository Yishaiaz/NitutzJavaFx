package EntriesObject;

import java.util.ArrayList;

public abstract class ATableEntry implements IEntry{
    private int length=0;
    private ArrayList<AEntry> entries= new ArrayList<>();
    public ATableEntry() {
    }

    public void insertEntry(AEntry entry){
        this.entries.add(entry);
        length++;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<AEntry> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return ""+ this.getClass().getName()+"{" +
                "lenght=" + length +
                ", entries=" + entries +
                '}';
    }
}

package EntriesObject;

import DataBaseConnection.IdbConnection;

import java.lang.reflect.Field;

/**
 * an abstract class implementing the IEntry interface.
 * this is to avoid code duplication between IEntry implementation.
 *
 * for more documentation:
 * @see IEntry - interface
 * @see User - as an example
 */
public abstract class AEntry implements IEntry {
    protected String[] entryColumnNames;

    /**
     * @deprecated - mainly for testing
     * a constructor. creates a field that holds all the column names of the implementing class.
     * @param entryColumnNames
     */
    public AEntry(String[] entryColumnNames) {
        this.entryColumnNames = entryColumnNames;
    }

    /**
     * an empty constructor.
     * populates the field that holds all the column names of the implementing class.
     *
     */
    public AEntry() {
        this.entryColumnNames=new String[this.getClass().getDeclaredFields().length];
        int i=0;
        for (Field field:this.getClass().getDeclaredFields()) {
            entryColumnNames[i]= field.getName();
            i++;
        }

    }

    public String[] getColumnsTitles() {
        return entryColumnNames;
    }

    public void insertToDb(IdbConnection idbConnection) throws Exception{
        idbConnection.insert(this);
    }

    public void updateToDb(IdbConnection idbConnection, IEntry entry, String[] newValues) throws Exception{
        idbConnection.updateEntry(entry, newValues);
    }

    @Override
    public String toString() {
        String columnString="";
//        System.out.println(this.getClass());

        for (int i = 1; i < entryColumnNames.length; i++) {
            if(i!=entryColumnNames.length-1){
                columnString+=entryColumnNames[i]+",";
            }
            else{
                columnString+=entryColumnNames[i];
            }

        }
        return "Entry Type="+this.getClass().getName()+"\nEntry Data Fields: " + columnString;
    }
}

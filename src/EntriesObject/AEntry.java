package EntriesObject;

import DataBaseConnection.IdbConnection;

import java.lang.reflect.Field;

public abstract class AEntry implements IEntry {
    protected String[] entryColumnNames;

    public AEntry(String[] entryColumnNames) {
        this.entryColumnNames = entryColumnNames;
    }

    public AEntry() {
        this.entryColumnNames=new String[this.getClass().getDeclaredFields().length];
        int i=0;
        for (Field field:this.getClass().getDeclaredFields()) {
            entryColumnNames[i]= field.getName();
            i++;
        }

    }

    @Override
    public String[] getColumnsTitles() {
        return entryColumnNames;
    }

    @Override
    public void insertToDb(IdbConnection idbConnection) throws Exception{
        idbConnection.insert(this);
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
        return "entryColumnNames=" + columnString;
    }
}

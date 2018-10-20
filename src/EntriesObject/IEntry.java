package EntriesObject;

import DataBaseConnection.IdbConnection;

public interface IEntry {
    String[] getColumnsTitles();
    boolean validateEntry() ;
    String getTableName() throws Exception;
    String[] getAllData() throws Exception;
    String getIdentifiers() throws Exception;
    String toString();
    String getIdentifierValue() throws Exception;
    void insertToDb(IdbConnection idbConnection) throws Exception;
    void deleteFromDb(IdbConnection idbConnection) throws Exception;


}

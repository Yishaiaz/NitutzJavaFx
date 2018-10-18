package EntriesObject;

import DataBaseConnection.IdbConnection;

public interface IEntry {
    String[] getColumnsTitles();
    String getTableName();
    String[] getAllData();
    String getIdentifiers();
    String toString();
    String getIdentifierValue();
    void insertToDb(IdbConnection idbConnection);
    void deleteFromDb(IdbConnection idbConnection);


}

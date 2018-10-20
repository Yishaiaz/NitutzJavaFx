package DataBaseConnection;

import EntriesObject.IEntry;

import java.util.ArrayList;
import java.util.LinkedList;

public interface IdbConnection {

    void connectToDb() throws Exception;

    void createNewTable(IEntry entry) throws Exception;

    String[] getEntryById(String entryId, IEntry entry) throws Exception;

    LinkedList<String[]> getAllFromTable(IEntry entry) throws Exception;

    void insert(IEntry entry) throws Exception;//should db connection use be implemented inside an entry object?

    void updateEntry(IEntry entry, String[] newValues) throws Exception;

    void deleteAllFromTable(String tableName) throws Exception;

    void deleteDb(String dbName);

    void deleteById(IEntry entry) throws Exception;

   ArrayList<String> getSpecificData(IEntry entry, String entryId, String[] namesOfSpecificField) throws Exception;

    void closeConnection() throws Exception;
}
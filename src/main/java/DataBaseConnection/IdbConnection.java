package DataBaseConnection;

import EntriesObject.IEntry;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * an interface to define the basic functions required from a database communicating module.
 * i.e a communication module with Sqlite local db, or a mongoDB JSON cloud based data base.
 * all functions are able to throw an exception according to their implementation in the inheriting class.
 * as an example,
 * @see SqliteDbConnection
 */
public interface IdbConnection {

    /**
     * connects to the DB instance, implemented in the class inheriting lvl
     * @throws Exception - an SQL type exceptio
     */
    void connectToDb() throws Exception;

    /**
     * creates a new table according to the type of the entry.
     * this uses the method IEntry.getTableName() in order to get the Entry's table name.
     * @param entry - type IEntry
     * @throws Exception - an SQL type exception
     */
    void createNewTable(IEntry entry) throws Exception;
    /**
     * returns an array of strings that contain all the data in string format of the entry, given the entry ID.
     * throws exception if the entry was not found in matching table
     * @param entry - type IEntry
     * @throws Exception - an SQL type exception
     */
    String[] getEntryById(String entryId, IEntry entry) throws Exception;

    /**
     * returns all the entries in a table given one entry of the table
     * (the function uses the entry type to know from which table to get the data)
     * @param entry - type IEntry
     * @return LinkedLis of arrays of strings
     * @throws Exception - an SQL type exception
     */
    LinkedList<String[]> getAllFromTable(IEntry entry) throws Exception;
    /**
     * inserts an entry to the appropriate table, if an error occurs throws an exception.
     * this uses the method IEntry.getTableName() in order to get the Entry's table name.
     * @param entry - type IEntry
     * @throws Exception - an SQL type exception
     */
    void insert(IEntry entry) throws Exception;//should db connection use be implemented inside an entry object?

    /**
     * updates an entry in the appropriate table, if an error occurs throws an exception.
     * this uses the method IEntry.getTableName() in order to get the Entry's table name
     *
     * @param entry - type IEntry
     * @param newValues
     * @throws Exception - an SQL type exception
     */
    void updateEntry(IEntry entry, String[] newValues) throws Exception;

    /**
     * deletes all data from given table, but not the table itself
     * @param tableName - String
     * @throws Exception - an SQL type exception
     */
    void deleteAllFromTable(String tableName) throws Exception;

    /**
     * Throws not implemented exception! not implemented right now.
     * @param dbName
     * @throws Exception - NotImplementedException
     */
    void deleteDb(String dbName) throws Exception;

    /**
     * deletes an entry from DB given the Entry object required for deletion.
     * this uses the method IEntry.getTableName() in order to get the Entry's table name.
     * @param entry - type IEntry
     * @throws Exception - an SQL type exception
     */
    void deleteById(IEntry entry) throws Exception;

    /**
     * returns data of specific fields of an entry, i.e a user's password.
     * it can return multiple specific data fields, when each string inside String[] namesOfSpecificFields is a field
     * it's data the user would like to receive in return.
     * if an error occurred, this function throws an SQL type exception.
     * this uses the method IEntry.getTableName() in order to get the Entry's table name.
     *
     * @param entry - type IEntry
     * @param entryId - String
     * @param namesOfSpecificField - String[]
     * @return Arraylist of Strings
     * @throws Exception - an SQL type exception
     */
    ArrayList<String> getSpecificData(IEntry entry, String entryId, String[] namesOfSpecificField) throws Exception;

    /**
     * closes the connection to the DB, to be used if the user doesn't need any more communications with the DB itself.
     *
     * @throws Exception - an SQL type exception
     */
    void closeConnection() throws Exception;

    void deleteTable(IEntry entry) throws Exception;

}
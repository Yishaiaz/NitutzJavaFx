package EntriesObject;

import DataBaseConnection.IdbConnection;

/**
 * an interface to define the basic functions required from a entry representation class/module.
 * i.e a class to represent a User or Flight db entry.
 * some functions are able to throw an exception according to their implementation in the inheriting class.
 * as an example:
 * @see User
 *
 */
public interface IEntry {
    /**
     * returns an array of strings with the names of the fields of the class.
     *
     * @return - String[]
     */
    String[] getColumnsTitles();

    /**
     * a deprecated function, can be used in the future to validate entries data integrity and validity.
     *
     * @return - booleanm, true if data is OK, else False.
     */
    boolean validateEntry() ;

    /**
     * returns a String, the name of the table, suitable for this entry.
     * @return String - name of the table
     */
    String getTableName();

    /**
     * returns all the data of the entry Object! not the entry inside a local or remote db
     *
     * @return String[]
     */
    String[] getAllData() ;

    /**
     * returns the name of the field used as the primary key of the entry type.
     * i.e this will return 'user_name' for the User instance.
     * @return String
     * @throws Exception
     */
    String getIdentifiers();

    /**
     *  an override to the Objects toString function.
     * @return
     */
    String toString();

    /**
     * returns the identifying's field's value itself, and not the name
     * @return String
     * @throws Exception
     */
    String getIdentifierValue();

    /**
     * uses a given db connection object.
     * inserts itself to the suitable table and db.
     * may throw an exception according to the db connection module used by user.
     *
     * @see IdbConnection
     * @param idbConnection - this needs to be an actual class hiding behind polymorphism
     * @throws Exception
     */
    void insertToDb(IdbConnection idbConnection) throws Exception;

    /**
     * uses a given db connection object.
     * deletes itself from the suitable table and db.
     * may throw an exception according to the db connection module used by user.
     *
     * @see IdbConnection
     * @param idbConnection - this needs to be an actual class hiding behind polymorphism
     * @throws Exception
     */
    void deleteFromDb(IdbConnection idbConnection) throws Exception;


}

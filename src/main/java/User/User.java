package User;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;
import Flight.FlightEntry;
import EntriesObject.IEntry;
import User.MailBox.AMessage;

import java.sql.Date;

/**
 * a full implementation of the IEntry interface.
 * represents a User Object with the following fields: [user_name, user_password, user_birthdate, user_firstname, user_lastname, user_city ].
 * extends the abstract class AEntry.
 *
 * for more documentation:
 * @see IEntry - interface
 * @see AEntry - abstract implementation
 */
public class User extends AEntry{

    private String user_name;
    private String user_password;
    private Date user_birthdate;
    private String user_firstname;
    private String user_lastname;
    private String user_city;
    private String user_mailbox_addresss;

    /**
     * constructor - create a user instance with all the data known
     * @param user_name
     * @param user_password
     * @param user_birthdate
     * @param user_firstname
     * @param user_lastname
     * @param user_city
     */
    public User(String user_name, String user_password, Date user_birthdate, String user_firstname, String user_lastname, String user_city) {
        super();
        this.user_name = user_name;
        this.user_password = user_password;
        this.user_birthdate =user_birthdate;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.user_city = user_city;
        this.user_mailbox_addresss = user_name + "_mailbox";
    }

    /**
     * constructor for an empty user with the field names but not values
     */
    public User() {
        super();

    }

    public void createAllTables(IdbConnection db){
        createFlightsTable(db);
    }

    private void deleteAllSubTables(IdbConnection db){
        deleteFlightsTable(db);
    }

    private void createFlightsTable(IdbConnection db){
        try{
            FlightEntry flightEntry = new FlightEntry(user_name);
            db.createNewTable(flightEntry);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteFlightsTable(IdbConnection db){
        try {
            FlightEntry flightEntry = new FlightEntry(user_name);
            db.deleteTable(flightEntry);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String getTableName() {
        return "Users";
    }

    /**
     * @deprecated
     * @return
     */
    public boolean validateEntry() {
        return true;
    }

    public String getIdentifierValue() {
        return user_name;
    }

    public String[] getAllData() {
        String[] ans= new String[getColumnsTitles().length];
        ans[0]= user_name;
        ans[1]= user_password;
        ans[2]= user_birthdate.toString();
        ans[3]= user_firstname;
        ans[4]= user_lastname;
        ans[5]= user_city;
        ans[6] = user_mailbox_addresss;
        return ans;
    }


    public String getIdentifiers() {
        return "user_name";//change to user_name
    }


    public void deleteFromDb(IdbConnection idbConnection) throws Exception{
        deleteAllSubTables(idbConnection);
        idbConnection.deleteById(this);
    }


    @Override
    public String toString() {
    return "user fields: {"+super.toString()+"} "+
            "User{" +
            " user_name='" + user_name + '\'' +
            ", user_password='" + user_password + '\'' +
            ", user_birthdate=" + user_birthdate +
            ", user_firstname='" + user_firstname + '\'' +
            ", user_lastname='" + user_lastname + '\'' +
            ", user_city='" + user_city + '\'' +
            '}';
    }

    /**
     * getter for the user name
     * @return String
     */
    public String getUser_name() {
        return user_name;
    }
    /**
     * getter for the user password
     * @return String
     */
    public String getUser_password() {
        return user_password;
    }
    /**
     * getter for the user birthdate
     * @return String
     */
    public Date getUser_birthdate() {
        return user_birthdate;
    }
    /**
     * getter for the user first name
     * @return String
     */
    public String getUser_firstname() {
        return user_firstname;
    }
    /**
     * getter for the user last name
     * @return String
     */
    public String getUser_lastname() {
        return user_lastname;
    }
    /**
     * getter for the user city
     * @return String
     */
    public String getUser_city() {
        return user_city;
    }

    public void insert(IEntry user){

    }
}

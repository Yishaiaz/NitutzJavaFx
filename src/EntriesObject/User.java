package EntriesObject;

import DataBaseConnection.IdbConnection;

import java.sql.Date;

public class User extends AEntry{
//    private String[] entryColumnNames= new String[6];
    private String user_name;
    private String user_password;
    private Date user_birthdate;
    private String user_firstname;
    private String user_lastname;
    private String user_city;

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
    }

    /**
     * constructor for an empty user with the field names but not values
     */
    public User() {
        super();

    }

    @Override
    public String getTableName() {
        return "Users";
    }

    @Override
    public boolean validateEntry() {
        return false;
    }


    @Override
    public String getIdentifierValue() {
        return user_name;
    }

    @Override
    public String[] getAllData() {
        String[] ans= new String[getColumnsTitles().length];
        ans[0]= user_name;
        ans[1]= user_password;
        ans[2]= user_birthdate.toString();
        ans[3]= user_firstname;
        ans[4]= user_lastname;
        ans[5]= user_city;

        return ans;
    }

    @Override
    public String getIdentifiers() {
        return "user_name";//change to user_name
    }


    @Override
    public void deleteFromDb(IdbConnection idbConnection) throws Exception{
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

    public String getUser_name() {
        return user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public Date getUser_birthdate() {
        return user_birthdate;
    }

    public String getUser_firstname() {
        return user_firstname;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public String getUser_city() {
        return user_city;
    }
}

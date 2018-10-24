package Model;

import EntriesObject.IEntry;
import EntriesObject.User;

public interface IModel {
    boolean CreateAccount(String username, String password, String birthdate, String fName, String lName, String city);
    IEntry SearchUser(String username);
    void UpdateAccount(String[] data);
    IEntry getLoggedUser();


    /**
     * login the input user by the username and password
     * @param username- the user name to login.
     * @param password- the user password to loin.
     * @return the user name if the user name and password are valid, otherwise returns null.
     */
    IEntry LogIn(String username, String password);

    /**
     * logout the current user in the system.
     */
    void logOut();

    /**
     * deletes user from db and logging out
     * @return true if succeed.
     */
    boolean DeleteUser();

    String[] getLogedInUserDetails();
}

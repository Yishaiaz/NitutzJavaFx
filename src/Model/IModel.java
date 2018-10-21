package Model;

import EntriesObject.User;

public interface IModel {
    boolean CreateAccount(String username, String password, String birthdate, String fName, String lName, String city);
    User SearchUser(String username);
    void UpdateAccount(String username, String password, String birthdate, String fName, String lName, String city);
    User LogIn(String username, String password);
    String getLoggedUser();
}

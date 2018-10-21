package Controller;

import EntriesObject.User;
import Model.Model;
import View.View;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Observable;
import java.util.Observer;
import Model.IModel;

public class Controller extends Observable implements Observer {
    private IModel model;
    public StringProperty  loggedUser=new SimpleStringProperty("");

    public Controller(IModel model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }

    void CreateAccount(String username, String password, String birthdate, String fName, String lName, String city){

    }

    public void addUser(String username, String password, String birthdate, String fName, String lName, String city){

        model.CreateAccount(username,password,birthdate,fName,lName,city);

    }

    /**
     * given a user name will check if the user exists in the DB
     * @param userName - the user name to search in the DB
     * @return - true if the User exists in the DB
     */
    public boolean checkIfUserExists(String userName) {
        User user = model.SearchUser(userName);
        if (user==null)
            return false;
        return true;
    }
    /**
     * login the input user by the username and password
     * @param username- the user name to login.
     * @param password- the user password to loin.
     * @return the user name if the user name and password are valid, otherwise returns null.
     */
    public String logIn(String username, String password){
        return model.LogIn(username,password);

    }

    /**
     * logout the current user in the system.
     */
    public void logOut(){
        model.logOut();
    }

    public String getLoggedUser() {
        return model.getLoggedUser();
    }
}

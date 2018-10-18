package Model;

import DataBaseConnection.IdbConnection;
import EntriesObject.IEntry;
import EntriesObject.User;

import java.util.Observable;

public class Model extends Observable implements IModel {
    private IdbConnection db;
    private String loggedUser;

    public Model(IdbConnection db) {
        this.db = db;
    }

    @Override
    public String getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(String loggedUser) {
        this.loggedUser = loggedUser;
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean CreateAccount(String username, String password, String birthdate, String fName, String lName, String city) {
        return false;
    }

    @Override
    public User SearchUser(String username) {
        return null;
    }

    @Override
    public void UpdateAccount(String username, String password, String birthdate, String fName, String lName, String city) {

    }

    @Override
    public User LogIn(String username, String password) {
        return null;
    }
}

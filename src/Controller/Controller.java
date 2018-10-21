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
    public StringProperty loggedUser = new SimpleStringProperty("");

    public Controller(IModel model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        loggedUser.set(model.getLoggedUser());
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
}

package Model;

import DataBaseConnection.IdbConnection;
import EntriesObject.IEntry;
import EntriesObject.User;


import java.sql.Date;
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
        boolean ans = false;
        Date d = Date.valueOf(birthdate);
        IEntry user = new User(username,password, d,fName,lName,city);
        try{
            user.insertToDb(this.db);
            ans=true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            // @Yaniv -  i don't really know what to do here once the user is in the db, so for now i just print to the console "hello [user_firstname+user_lastname]"
            System.out.println("Hello "+((User) user).getUser_firstname()+" "+ ((User) user).getUser_lastname());
        }

        return ans;
    }

    @Override
    //**************************************need to check exceptions!!!!!!!!!!!!!!**********************
    /**
     * given a user name will check if the user exists in the DB and return the User
     * @param userName - the user name to search in the DB
     * @return - the User if found, NULL if user does not exist.
     */
    public User SearchUser(String username) {

        try {
            String[] entry = db.getEntryById(username, new User());
            return new User(entry[0],entry[1],Date.valueOf(entry[2]),entry[3],entry[4],entry[5]);
        }
        catch (Exception e){
            return null;
        }

    }

    @Override
    public void UpdateAccount(String username, String password, String birthdate, String fName, String lName, String city) {

    }

    @Override
    public String LogIn(String username, String password) {
        try {
           User userFromDB= SearchUser(username);
           if(userFromDB.getUser_password().equals(password)) {
               loggedUser = username;
               return loggedUser;
           }
           return null;
        }
        catch (Exception e){
            return null;
        }
    }

    @Override
    public void logOut() {
        loggedUser=null;
    }
}

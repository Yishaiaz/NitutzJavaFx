package Model;

import DataBaseConnection.IdbConnection;
import EntriesObject.IEntry;
import EntriesObject.User;


import java.sql.Date;
import java.util.Observable;

public class Model extends Observable implements IModel {
    private IdbConnection db;
    private IEntry loggedUser;

    public Model(IdbConnection db) {
        this.db = db;
    }

    @Override
    public IEntry getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(IEntry loggedUser) {
        this.loggedUser = loggedUser;
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean CreateAccount(String username, String password, String birthdate, String fName, String lName, String city) {
        boolean ans = false;
        Date d = Date.valueOf(birthdate);
        IEntry user = new User(username,password, d,fName,lName,city);
        //this.loggedUser=(IEntry)user;
        setLoggedUser(user);
        try{
            user.insertToDb(this.db);
            ans=true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
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
    public void UpdateAccount(User user) {
        this.loggedUser = user;
        try{
            db.updateEntry(user, user.getAllData());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public IEntry LogIn(String username, String password){
        try {
           User userFromDB= SearchUser(username);
           if(userFromDB!= null && userFromDB.getUser_password().equals(password)) {
               loggedUser = userFromDB;
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

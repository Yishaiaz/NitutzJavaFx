package Model;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;
import EntriesObject.IEntry;
import Flight.FlightEntry;
import User.MailBox.MailBox;
import User.MailBox.Message;
import User.User;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Observable;

public class Model extends Observable implements IModel {
    private IdbConnection db;
    private IEntry loggedUser;

    public Model(IdbConnection db) {
        this.db = db;
        try {
            db.createNewTable(new FlightEntry(""));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error create flight table");
        }
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
        try {
            db.createNewTable(new User());//creates user table if not exists
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        boolean ans = false;
        Date d = Date.valueOf(birthdate);
        IEntry user = new User(username,password, d, fName,lName,city);
        setLoggedUser(user);
        try{
            user.insertToDb(this.db);
            ans=true;
            ((User) user).createAllTables(db);
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
    public void UpdateAccount(String[] data) {
        setLoggedUser(new User(loggedUser.getIdentifierValue(),data[0],Date.valueOf(data[1]),data[2],data[3],data[4]));
        try{
            db.updateEntry(loggedUser, loggedUser.getAllData());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public IEntry LogIn(String username, String password){
        try {
           User userFromDB= SearchUser(username);
           if(userFromDB!= null && userFromDB.getUser_password().equals(password)) {
               setLoggedUser(userFromDB);
               return loggedUser;
           }
           return null;
        }
        catch (Exception e){
            return null;
        }
    }

    public boolean addPost(Map<String,String> fields) {
        if (fields == null)
            return false;

        try {
            boolean returnFlight = false;
            if (fields.get("returnFlight").equals("true"))
                returnFlight = true;
//            System.out.println(fields.get("airline")+"123");
//            Date d = Date.valueOf(fields.get("depDate"));


            AEntry post = new FlightEntry(loggedUser.getIdentifierValue(), fields.get("airline"), Date.valueOf(fields.get("depDate")), Date.valueOf(fields.get("arrDate")), fields.get("luggage"), (int) Integer.valueOf(fields.get("numOfTickets")), fields.get("from"), returnFlight, fields.get("ticketType"), (double) Double.valueOf(fields.get("price")), "For Sale", fields.get("to"),db);
            post.insertToDb(db);
            return true;
        } catch (Exception e) {
            System.out.println("model add post error");
            return false;
        }
    }

    @Override
    public void logOut() {
        setLoggedUser(null);
    }

    @Override
    public boolean DeleteUser() {
        try {
            loggedUser.deleteFromDb(db);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        logOut();
        return true;
    }

    @Override
    public String[] getLogedInUserDetails() {
        String[] ans;
        try {
            ans = db.getEntryById(loggedUser.getIdentifierValue(), new User());
        }
        catch (Exception e){
            return null;
        }
        return ans;
    }

    @Override
    public Collection<Message> getUsersMessages() {
        if (loggedUser == null)
            return null;
        return new MailBox(loggedUser.getIdentifierValue(), db).getAllMessages().values();
    }
}

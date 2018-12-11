package Controller;

import EntriesObject.IEntry;
import Flight.FlightEntry;
import Model.IModel;
import User.MailBox.Message;

import java.util.Collection;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Controller extends Observable implements Observer {
    private IModel model;

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
    public boolean postVacation(Map fields) {
        return model.addPost(fields);
    }

    /**
     * given a user name will check if the user exists in the DB
     * @param userName - the user name to search in the DB
     * @return - true if the User exists in the DB
     */
    public boolean checkIfUserExists(String userName) {
        IEntry user = model.SearchUser(userName);
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
    public IEntry logIn(String username, String password){
        return model.LogIn(username,password);

    }

    /**
     * logout the current user in the system.
     */
    public void logOut(){
        model.logOut();
    }

    public String getLoggedUser() {
        if(model.getLoggedUser()!=null)
            return model.getLoggedUser().getIdentifierValue();
        return null;
    }

    public void updateUser(String[] data){
        model.UpdateAccount(data);
    }

    public boolean DeleteAccount(){
        return model.DeleteUser();
    }

    /**
     * searches if a given user name exists and return all its information
     * @param userName - the user name that is searched
     * @return - null if user doesnt exist, a string array of all the data if exists in the DB
     */
    public String[] searchUser(String userName){
        IEntry user = model.SearchUser(userName);
        if (user==null)
            return null;
        else return user.getAllData();

    }

    public String[] getLogedInUserDetails(){
       return model.getLogedInUserDetails();
    }

    /**
     * gets the user's messages
     * @return a collection of all the logged user's messages in mailbox
     */
    public Collection<Message> getUsersMessages() {
        return model.getUsersMessages();
    }

    public Collection<String[]> getFlightBoard() {
        return model.getFlightBoard();
    }

    public void purchaseFlight(String flightID){
        model.purchaseFlight(flightID);
    }

    public void acceptPurchaseOffer(String transactionID){
        model.acceptPurchaseOffer(transactionID);
    }

    public void declinePurchaseOffer(String transactionID){
        model.declinePurchaseOffer(transactionID);
    }

    public String getTransactionStatus(String transactionID) {
        return model.getTransactionStatus(transactionID);
    }

    public void paymentAccepted(String transactionID, String cardNumber, String expYear,String expMonth, String csv, String payments,String ownerName) {
        model.confirmPayment(transactionID,cardNumber,expYear,expMonth,csv,payments,ownerName);
    }
}

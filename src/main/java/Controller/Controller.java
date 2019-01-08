package Controller;

import EntriesObject.IEntry;
import Model.IModel;
import User.MailBox.Message;
import java.util.Collection;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * this class connects the View (GUI) to the Model
 */
public class Controller extends Observable implements Observer {
    private IModel model;

    /**
     * constructor - holds the pointer to the model
     * @param model - the model
     */
    public Controller(IModel model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }

    /**
     * makes a request to the Model to add the user
     * @param username
     * @param password
     * @param birthdate
     * @param fName
     * @param lName
     * @param city
     */
    public void addUser(String username, String password, String birthdate, String fName, String lName, String city){
        model.CreateAccount(username,password,birthdate,fName,lName,city);
    }

    /**
     * makes a request to the Model to post a new vacation
     * @param fields - a map with all the vacation details
     * @return - true if model successfully added the vacation
     */
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
     * logs out the current user in the system.
     */
    public void logOut(){
        model.logOut();
    }

    /**
     * returns the user ID of the logged in user
     * @return - user ID
     */
    public String getLoggedUser() {
        if(model.getLoggedUser()!=null)
            return model.getLoggedUser().getIdentifierValue();
        return null;
    }

    /**
     * updates the users information
     * @param data
     */
    public void updateUser(String[] data){
        model.UpdateAccount(data);
    }

    /**
     * deletes the logged in users account and logs out
     * @return - true if account successfully deleted
     */
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

    /**
     * makes a request to the Model to get the logged users details
     * @return - the logged users details
     */
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

    /**
     * makes a request to the Model to get all the vacations for sale
     * @return - a collection of all the vacations for sale
     */
    public Collection<String[]> getFlightBoard() {
        return model.getFlightBoard();
    }

    /**
     * makes a request to the Model to purchase a specific flight
     * @param flightID - the flight id that the request is for
     */
    public void purchaseFlight(String flightID) throws Exception {
        model.purchaseFlight(flightID);
    }

    /**
     * makes a request to the Model from the seller to approve the transaction and notify
     * the buyer and get payment
     * @param transactionID - the transaction that the request regards
     */
    public void acceptPurchaseOffer(String transactionID){
        model.acceptPurchaseOffer(transactionID);
    }

    /**
     * makes a request to the Model from the seller to reject the transaction and notify
     * the buyer
     * @param transactionID
     */
    public void declinePurchaseOffer(String transactionID){
        model.declinePurchaseOffer(transactionID);
    }

    /**
     * * makes a request to the Model to retrieve the status of a specific transaction
     * @param transactionID - the transaction id
     * @return - the transaction status if exists, null if not
     */
    public String getTransactionStatus(String transactionID) {
        return model.getTransactionStatus(transactionID);
    }

    public String getSwapStatus(String swapID){
        return model.getSwapStatus(swapID);
    }

    /**
     * * makes a request to the Model from the buyer to accept the payment, save it and change the transaction status
     * @param transactionID - the transaction id
     */
    public void paymentAccepted(String transactionID) {
        model.confirmPayment(transactionID);
    }

    public void paymentReceived(String transactionID){
        model.transactionComplete(transactionID);
    }

    public Collection<String[]> getUsersFlights() {
        return model.getUsersFlights();
    }

    public void requestSwapTransaction(String sellersFlightID, String swapperFlightID) {
        try {
            model.swapFlight(sellersFlightID,swapperFlightID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptSwapOffer(String swapID) {
        model.acceptSwapOffer(swapID);
    }

    public void declineSwapOffer(String swapID) {
        model.declineSwapOffer(swapID);
    }
}

package Model;

import EntriesObject.IEntry;
import User.MailBox.Message;

import java.util.Collection;
import java.util.Map;

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

    boolean addPost(Map<String,String> fields);

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

    /**
     * getter for the logged user's messages in his mailbox
     * @return a collection of all the logged user's messages in mailbox, return null if not logged
     */
    Collection<Message> getUsersMessages();

    /**
     * getter for all the posted flights
     * @return - a collection of flight entries from db(flights)
     */
    Collection<String[]> getFlightBoard();

    void purchaseFlight(String flightID) throws Exception;

    void acceptPurchaseOffer(String transactionID);

    void declinePurchaseOffer(String transactionID);

    void confirmPayment(String transactionID, String cardNumber, String expYear,String expMonth, String csv, String payments,String ownerName);

    void createTables();

    String getTransactionStatus(String transactionID);
}

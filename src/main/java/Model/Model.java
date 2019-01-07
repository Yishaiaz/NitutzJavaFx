package Model;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;
import EntriesObject.IEntry;
import Flight.FlightEntry;
import Transaction.PaymentsEntry;
import Transaction.TransactionsEntry;
import User.MailBox.MailBox;
import User.MailBox.Message;
import User.User;

import java.sql.Date;
import java.util.Collection;
import java.util.Map;
import java.util.Observable;

public class Model extends Observable implements IModel {
    private IdbConnection db;
    private IEntry loggedUser;

    public Model(IdbConnection db) {
        this.db = db;
    }

    @Override
    public void createTables() {
        try {
            db.createNewTable(new TransactionsEntry());
            db.createNewTable(new PaymentsEntry());
            db.createNewTable(new FlightEntry(""));
            db.createNewTable(new User());
        } catch (Exception e) {
            e.printStackTrace();
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
    public String getTransactionStatus(String transactionID) {
        String status=null;
        try {
            status=db.getEntryById(transactionID,new TransactionsEntry())[4];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public void purchaseFlight(String flightID) throws Exception {
        try {
            String[] flightDetails=db.getEntryById(flightID,new FlightEntry(""));
            System.out.println(loggedUser.getAllData()[0] + "   mmmm   "+ flightDetails[1]);
            if(flightDetails[1].equals(loggedUser.getAllData()[0])) {
                throw new Exception("nope");
            }
            java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
            TransactionsEntry transactionsEntry=new TransactionsEntry(sqlDate,flightDetails[1],loggedUser.getAllData()[0],"New Transaction",flightID,flightDetails[6],flightDetails[10],db);
            transactionsEntry.setTransaction_status("Offer Received");
            db.insert(transactionsEntry);
        } catch (Exception e) {
            if(e.getMessage().equals("nope"))
                throw new Exception("nope");
        }
    }

    @Override
    public void acceptPurchaseOffer(String transactionID) {
        String[]transactionsEntry=null;
        try {
            transactionsEntry=db.getEntryById(transactionID,new TransactionsEntry());
            TransactionsEntry transaction=new TransactionsEntry(transactionsEntry[0],Date.valueOf(transactionsEntry[1]),transactionsEntry[2],transactionsEntry[3]
                    ,transactionsEntry[4],transactionsEntry[5],transactionsEntry[6],transactionsEntry[7],db);
            transaction.setTransaction_status("Offer Approved");
            db.updateEntry(transaction,transaction.getAllData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declinePurchaseOffer(String transactionID) {
        String[]transactionsEntry=null;
        try {
            transactionsEntry=db.getEntryById(transactionID,new TransactionsEntry());
            TransactionsEntry transaction=new TransactionsEntry(transactionsEntry[0],Date.valueOf(transactionsEntry[1]),transactionsEntry[2],transactionsEntry[3]
                    ,transactionsEntry[4],transactionsEntry[5],transactionsEntry[6],transactionsEntry[7],db);
            transaction.setTransaction_status("Rejected");
            db.updateEntry(transaction,transaction.getAllData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void confirmPayment(String transactionID, String cardNumber, String expYear,String expMonth, String csv, String payments,String ownerName) {
        String[]transactionsEntry=null;
        try {
            transactionsEntry=db.getEntryById(transactionID,new TransactionsEntry());
            TransactionsEntry transaction=new TransactionsEntry(transactionsEntry[0],Date.valueOf(transactionsEntry[1]),transactionsEntry[2],transactionsEntry[3]
                    ,transactionsEntry[4],transactionsEntry[5],transactionsEntry[6],transactionsEntry[7],db);
            transaction.setTransaction_status("Closed");
            db.updateEntry(transaction,transaction.getAllData());
            PaymentsEntry paymentsEntry=new PaymentsEntry(transactionID,cardNumber,expYear,expMonth,csv,Integer.valueOf(payments),ownerName);
            db.insert(paymentsEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean CreateAccount(String username, String password, String birthdate, String fName, String lName, String city) {
        try {
            db.createNewTable(new User());//creates user table if not exists
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        boolean ans = false;
        Date d = Date.valueOf(birthdate);
        IEntry user = new User(username, password, d, fName, lName, city);
        setLoggedUser(user);
        try {
            user.insertToDb(this.db);
            ans = true;
            ((User) user).createAllTables(db);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Hello " + ((User) user).getUser_firstname() + " " + ((User) user).getUser_lastname());
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
            return new User(entry[0], entry[1], Date.valueOf(entry[2]), entry[3], entry[4], entry[5]);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void UpdateAccount(String[] data) {
        setLoggedUser(new User(loggedUser.getIdentifierValue(), data[0], Date.valueOf(data[1]), data[2], data[3], data[4]));
        try {
            db.updateEntry(loggedUser, loggedUser.getAllData());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public IEntry LogIn(String username, String password) {
        try {
            User userFromDB = SearchUser(username);
            if (userFromDB != null && userFromDB.getUser_password().equals(password)) {
                setLoggedUser(userFromDB);
                return loggedUser;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean addPost(Map<String, String> fields) {
        if (fields == null)
            return false;

        try {
            boolean returnFlight = false;
            if (fields.get("returnFlight").equals("true"))
                returnFlight = true;
//            System.out.println(fields.get("airline")+"123");
//            Date d = Date.valueOf(fields.get("depDate"));


            AEntry post = new FlightEntry(loggedUser.getIdentifierValue(), fields.get("airline"), Date.valueOf(fields.get("depDate")), Date.valueOf(fields.get("arrDate")), fields.get("luggage"), (int) Integer.valueOf(fields.get("numOfTickets")), fields.get("from"), returnFlight, fields.get("ticketType"), (double) Double.valueOf(fields.get("price")), "For Sale", fields.get("to"), db);
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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

    @Override
    public Collection<String[]> getFlightBoard() {
        try {
            return db.getAllFromTable(new FlightEntry(""));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}

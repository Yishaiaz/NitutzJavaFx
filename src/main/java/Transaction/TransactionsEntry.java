package Transaction;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;
import Flight.FlightEntry;
import User.MailBox.Message;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * TransactionsEntry class extends AEntry class.
 * @Overide's the methods getTableName,getAllData,getIdentifierValue,deleteFromDb
 *
 *
 * available statuses:
 * For Sale, Offer Received, Offer Approved, Closed, Rejected
 *
 */
public class TransactionsEntry extends AEntry {
    private String transaction_number;
    private Date transaction_date;
    private String sellerUser_id;
    private String buyerUser_id;
    private String transaction_status;
    private String flight_id;
    private String number_of_tickets;
    private String amountPayed;

    private List<String> statuses;
    private IdbConnection idbConnection;
    /**
     * empty constructor mainly for testing
     */
    public TransactionsEntry(){}

    /**
     *
     * @param date - Date
     * @param sellerUser_id - String
     * @param buyerUser_id - String
     * @param flight_id - String
     * @param number_of_tickets - String
     * @param amountPayed - String
     * @throws Exception
     */
    public TransactionsEntry(Date date,String sellerUser_id,String buyerUser_id,String transaction_status,
                             String flight_id,String number_of_tickets,String amountPayed,IdbConnection idbConnection){
        String[] initStatuses={"Offer Received","Offer Approved","Closed","Rejected"};
        statuses= new ArrayList<>();
        for(String s:initStatuses){
            statuses.add(s);
        }
        int nextID=this.getMaximumId(getAllTransactions(idbConnection));
        this.transaction_number=String.valueOf(nextID);
        this.sellerUser_id=sellerUser_id;
        this.buyerUser_id=buyerUser_id;
        this.number_of_tickets=number_of_tickets;
        this.amountPayed=amountPayed;
        this.flight_id=flight_id;
        this.transaction_date =date;
        this.idbConnection=idbConnection;
        this.transaction_status=transaction_status;
    }

    public TransactionsEntry(String transaction_number, Date date,String sellerUser_id,String buyerUser_id,String transaction_status,
                             String flight_id,String number_of_tickets,String amountPayed,IdbConnection idbConnection){
        String[] initStatuses={"Offer Received","Offer Approved","Closed","Rejected"};
        statuses= new ArrayList<>();
        for(String s:initStatuses){
            statuses.add(s);
        }
        this.transaction_number=String.valueOf(transaction_number);
        this.sellerUser_id=sellerUser_id;
        this.buyerUser_id=buyerUser_id;
        this.number_of_tickets=number_of_tickets;
        this.amountPayed=amountPayed;
        this.flight_id=flight_id;
        this.transaction_date =date;
        this.idbConnection=idbConnection;
        this.transaction_status=transaction_status;
    }

    /**
     * enables to change the transaction status field
     * the function checks whether the status is legal @see class documentation.
     * @param transaction_status - String, the new transaction status
     * @throws Exception
     */
    public void setTransaction_status(String transaction_status) throws Exception {
        if(!statuses.contains(transaction_status)){
            throw new Exception("status not valid");
        }
        if(transaction_status.equals("Offer Received") && this.transaction_status.equals("New Transaction") ){
            this.transaction_status=transaction_status;
            sendOfferRecivedMsg();
        }
        if(transaction_status.equals("Offer Approved") && this.transaction_status.equals("Offer Received")){
            this.transaction_status=transaction_status;
            sendOfferApprovedMsg();
        }
        if(transaction_status.equals("Rejected") && this.transaction_status.equals("Offer Received")){
            this.transaction_status=transaction_status;
            sendOfferRejectedMsg();
        }
        if(transaction_status.equals("Closed") && this.transaction_status.equals("Offer Approved")){
            this.transaction_status=transaction_status;
            sendOfferFinishedMsg();
            deleteFlight();
        }
        if(transaction_status.equals("user has paid") && this.transaction_status.equals("Offer Approved")){
            this.transaction_status=transaction_status;
            sendOfUserPaidMsg();
            deleteFlight();
        }
    }

    private void deleteFlight() {
        try {
            String[]flightDetails=idbConnection.getEntryById(flight_id,new FlightEntry(""));
            idbConnection.deleteById(new FlightEntry(flightDetails[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendOfferRecivedMsg() {
        String flightDetails = getFlightDetails();
        String msgContent="You got an offer from the user: "+buyerUser_id+" about your flight post: "
                            +flightDetails+System.getProperty("line.separator")+"confirm or deny.";
        Date dateTest=new Date(System.currentTimeMillis());
        Message message=new Message("Vacation4U",sellerUser_id,"Offer Received",msgContent,new Date(System.currentTimeMillis()),"Vacation4U",transaction_number);
        sendMessage(sellerUser_id,message);
    }

    private void sendOfferApprovedMsg() {
        String flightDetails = getFlightDetails();
        String msgContent="user: "+sellerUser_id+" approved your offer regarding flight post: "+flightDetails
                                    +System.getProperty("line.separator")+"please make the payment VIA the seller."+System.getProperty("line.separator")
                + "after you have paid please press the \"I have paid\" button (seller will be asked to cofirm that the payment was recieved)";
        Message message=new Message("Vacation4U",buyerUser_id,"Offer Approved",msgContent,new Date(System.currentTimeMillis()),"Vacation4U",transaction_number);
        sendMessage(buyerUser_id,message);
    }

    private void sendOfferRejectedMsg() {
        String flightDetails = getFlightDetails();
        String msgContent="user: "+sellerUser_id+" rejected your offer about flight post: "+flightDetails
                +System.getProperty("line.separator")+"better luck next time (: .";
        Message message=new Message("Vacation4U",buyerUser_id,"Offer Rejected",msgContent,new Date(System.currentTimeMillis()),"Vacation4U",transaction_number);
        sendMessage(buyerUser_id,message);
    }

    private void sendOfferFinishedMsg() {
        String flightDetails = getFlightDetails();
        String msgContent="Transaction of flight post: "+flightDetails+" has been complete and the post has been removed"
                +System.getProperty("line.separator")+"thank you for using Vacation4U.";
        Message messageToSeller=new Message("Vacation4U",sellerUser_id,"Offer Closed",msgContent,new Date(System.currentTimeMillis()),"Vacation4U",transaction_number);
        Message messageToBuyer=new Message("Vacation4U",buyerUser_id,"Offer Closed",msgContent,new Date(System.currentTimeMillis()),"Vacation4U",transaction_number);
        sendMessage(sellerUser_id,messageToSeller);
        sendMessage(buyerUser_id,messageToBuyer);
    }

    private String getFlightDetails() {
        String[] flightEntry = null;

        try {
            flightEntry = idbConnection.getEntryById(flight_id, new FlightEntry(""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "departure at " + flightEntry[3] + " from " + flightEntry[7] + " to " + flightEntry[12] + " at the price of " + flightEntry[10];
    }

    private void updateMsgId(Message message,String receiverUserID){
        int max=0;
        try{
            LinkedList<String[]> all= idbConnection.getAllFromTable(new Message(receiverUserID));
            if(all==null){
                message.setMessage_id(String.valueOf(max+1));
                return;
            }
            for(String s[]: all){
                if(Integer.valueOf(s[0]) > max) max = Integer.valueOf(s[0]);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        message.setMessage_id(String.valueOf(max+1));
    }

    private void sendMessage(String receiverUserID, Message message){
        try{
            updateMsgId(message,receiverUserID);
            idbConnection.insertToDbByTableName(receiverUserID+"_mailbox", message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private int getMaximumId(LinkedList<String[]> all){
        int max=0;
        if (all==null || all.size()==0){
            return 1;
        }
        for(String[] s: all){
            if( Integer.valueOf(s[0]) > max) max =Integer.valueOf(s[0]);
        }
        return max+1;
    }

    //<editor-fold desc="DB">
    @Override
    public boolean validateEntry() {
        return true;
    }

    @Override
    public String getTableName() {
        return "transactions_table";
    }

    @Override
    public String[] getColumnsTitles() {
        String[]columnsTitles=new String[8];
        columnsTitles[0]="transaction_number";
        columnsTitles[1]="transaction_date";
        columnsTitles[2]="sellerUser_id";
        columnsTitles[3]="buyerUser_id";
        columnsTitles[4]="transaction_status";
        columnsTitles[5]="flight_id";
        columnsTitles[6]="number_of_tickets";
        columnsTitles[7]="amountPayed";
        return columnsTitles;
    }

    @Override
    public String[] getAllData() {
        String[] ans = new String[this.getColumnsTitles().length];
        ans[0]= transaction_number;
        ans[1]= transaction_date.toString();
        ans[2]= sellerUser_id;
        ans[3]= buyerUser_id;
        ans[4]= transaction_status;
        ans[5]= flight_id;
        ans[6]= number_of_tickets;
        ans[7]= amountPayed;
        return ans;
    }

    @Override
    public String getIdentifiers() {
        return "transaction_number";
    }

    @Override
    public String getIdentifierValue() {
        return transaction_number;
    }

    @Override
    public void deleteFromDb(IdbConnection idbConnection) throws Exception {
        idbConnection.deleteById(this);
    }

    /**
     * a method to get all the transactions on the db.
     * returns a linked list of arrays of strings. when each link in the list represents an entry from the database's
     * transaction table.
     * @param idbConnection
     * @return LinkedList<String[]>
     */
    public LinkedList<String[]> getAllTransactions(IdbConnection idbConnection) {
        try {
            return idbConnection.getAllFromTable(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    //</editor-fold>

    private void sendOfUserPaidMsg() {
        String flightDetails = getFlightDetails();
        String msgContent="seller: "+buyerUser_id+"has confirmed that he has paid for the flight : "+flightDetails+"."+System.getProperty("line.separator")
                +"if you have received the payment press \"confirm Received payment\".";
        Message messageToBuyer=new Message("Vacation4U",buyerUser_id,"buyer paid",msgContent,new Date(System.currentTimeMillis()),"Vacation4U",transaction_number);
        sendMessage(buyerUser_id,messageToBuyer);
    }
}

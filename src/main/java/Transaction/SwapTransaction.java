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
public class SwapTransaction extends AEntry {
    private String transaction_number;
    private Date transaction_date;
    private String sellerUser_id;
    private String buyerUser_id;
    private String transaction_status;
    private String firstflight_id;
    private String secondflight_id;
    private String number_of_ticketsfirst;
    private String number_of_ticketssecond;


    private List<String> statuses;
    private IdbConnection idbConnection;
    /**
     * empty constructor mainly for testing
     */
    public SwapTransaction(){}


    public SwapTransaction(Date date,String sellerUser_id,String buyerUser_id,String transaction_status,
                             String firstflight_id,String secondflight_id,String number_of_ticketsfirst,String number_of_ticketssecond,IdbConnection idbConnection){
        String[] initStatuses={"Offer Received","Offer Approved","Closed","Rejected"};
        statuses= new ArrayList<>();
        for(String s:initStatuses){
            statuses.add(s);
        }
        int nextID=this.getMaximumId(getAllTransactions(idbConnection));
        this.transaction_number=String.valueOf(nextID);
        this.sellerUser_id=sellerUser_id;
        this.buyerUser_id=buyerUser_id;
        this.number_of_ticketsfirst=number_of_ticketsfirst;
        this.number_of_ticketssecond=number_of_ticketssecond;
        this.firstflight_id=firstflight_id;
        this.secondflight_id=secondflight_id;
        this.transaction_date =date;
        this.idbConnection=idbConnection;
        this.transaction_status=transaction_status;
    }

    public SwapTransaction(String transaction_number, Date date,String sellerUser_id,String buyerUser_id,String transaction_status,
                             String firstflight_id,String secondflight_id,String firstnumber_of_tickets,String secondnumber_of_tickets,IdbConnection idbConnection){
        String[] initStatuses={"Offer Received","Offer Approved","Closed","Rejected"};
        statuses= new ArrayList<>();
        for(String s:initStatuses){
            statuses.add(s);
        }
        this.transaction_number=String.valueOf(transaction_number);
        this.sellerUser_id=sellerUser_id;
        this.buyerUser_id=buyerUser_id;
        this.number_of_ticketsfirst=firstnumber_of_tickets;
        this.number_of_ticketssecond=secondnumber_of_tickets;
        this.firstflight_id=firstflight_id;
        this.secondflight_id=secondflight_id;
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
            deleteFlight(firstflight_id);
            deleteFlight(secondflight_id);
        }
    }

    private void deleteFlight(String flightId) {
        try {
            String[]flightDetails=idbConnection.getEntryById(flightId,new FlightEntry(""));
            idbConnection.deleteById(new FlightEntry(flightDetails[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendOfferRecivedMsg() {
        String firstflightDetails = getFlightDetails(firstflight_id);
        String secondflightDetails = getFlightDetails(secondflight_id);
        String msgContent="You got a \"SWAP\" offer from the user: "+buyerUser_id+" to swap your flight: "
                +firstflightDetails+System.getProperty("line.separator")+"for his flight: "+secondflightDetails+System.getProperty("line.separator")+"confirm or deny.";
        Message message=new Message("Vacation4U",sellerUser_id,"Offer Received",msgContent,new Date(System.currentTimeMillis()),"Vacation4U",transaction_number);
        sendMessage(sellerUser_id,message);
    }

    private void sendOfferApprovedMsg() {
        String firstflightDetails = getFlightDetails(firstflight_id);
        String secondflightDetails = getFlightDetails(secondflight_id);
        String msgContent="user: "+sellerUser_id+" approved your offer to swap between his flight: "+firstflightDetails
                +System.getProperty("line.separator")+ "your flight: "+secondflightDetails+System.getProperty("line.separator")+" after you have paid the buyer (dont lie...) press the \"I have paid\" button";
        Message message=new Message("Vacation4U",buyerUser_id,"Offer Approved",msgContent,new Date(System.currentTimeMillis()),"Vacation4U",transaction_number);
        sendMessage(buyerUser_id,message);
    }

    private void sendOfferRejectedMsg() {
        String firstflightDetails = getFlightDetails(firstflight_id);
        String msgContent="user: "+sellerUser_id+" rejected your offer to make a swap flight on the flight: "+firstflightDetails
                +System.getProperty("line.separator")+"better luck next time (: .";
        Message message=new Message("Vacation4U",buyerUser_id,"Offer Rejected",msgContent,new Date(System.currentTimeMillis()),"Vacation4U",transaction_number);
        sendMessage(buyerUser_id,message);
    }

    private void sendOfferFinishedMsg() {
        String firstflightDetails = getFlightDetails(firstflight_id);
        String secondflightDetails = getFlightDetails(secondflight_id);
        String msgContent="Transaction of swapping the flights:"+System.getProperty("line.separator")+ "1. "+firstflightDetails+
                System.getProperty("line.separator")+"2. "+secondflightDetails+System.getProperty("line.separator")+"has been succesfully complete, thank you for using Vacation4U.";
        Message messageToSeller=new Message("Vacation4U",sellerUser_id,"Offer Closed",msgContent,new Date(System.currentTimeMillis()),"Vacation4U",transaction_number);
        Message messageToBuyer=new Message("Vacation4U",buyerUser_id,"Offer Closed",msgContent,new Date(System.currentTimeMillis()),"Vacation4U",transaction_number);
        sendMessage(sellerUser_id,messageToSeller);
        sendMessage(buyerUser_id,messageToBuyer);
    }

    private String getFlightDetails(String flightId) {
        String[] flightEntry = null;

        try {
            flightEntry = idbConnection.getEntryById(flightId, new FlightEntry(""));
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
        return "Swap_transactions_table";
    }

    @Override
    public String[] getColumnsTitles() {

        String[]columnsTitles=new String[8];
        columnsTitles[0]="transaction_number";
        columnsTitles[1]="transaction_date";
        columnsTitles[2]="sellerUser_id";
        columnsTitles[3]="buyerUser_id";
        columnsTitles[4]="transaction_status";
        columnsTitles[5]="firstflight_id";
        columnsTitles[6]="secondflight_id";
        columnsTitles[7]="number_of_ticketsfirst";
        columnsTitles[8]="number_of_ticketssecond";
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
        ans[5]= firstflight_id;
        ans[6]= secondflight_id;
        ans[7]= number_of_ticketsfirst;
        ans[8]= number_of_ticketssecond;
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
}

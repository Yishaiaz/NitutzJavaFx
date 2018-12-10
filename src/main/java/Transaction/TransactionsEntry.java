package Transaction;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;
import User.MailBox.Message;

import java.util.ArrayList;
import java.util.Date;

/**
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
    private IdbConnection idbConnection;
    /**
     * empty constructor mainly for testing
     */
    public TransactionsEntry(){}

    /**
     *
     * @param transaction_number
     * @param date
     * @param sellerUser_id
     * @param buyerUser_id
     * @param transaction_status
     * @param flight_id
     * @param number_of_tickets
     * @param amountPayed
     * @throws Exception
     */
    public TransactionsEntry(String transaction_number,Date date,String sellerUser_id,String buyerUser_id,String transaction_status,
                             String flight_id,String number_of_tickets,String amountPayed,IdbConnection idbConnection) throws Exception{
        String[] statuses={"For Sale","Offer Received","Offer Approved","Closed","Rejected"};
        ArrayList<String> possible_statuses= new ArrayList<String>();
        for(String s:statuses){
            possible_statuses.add(s);
        }
        if(!possible_statuses.contains(transaction_status)){
            throw new Exception("status not valid");
        }
        this.transaction_number=transaction_number;
        this.sellerUser_id=sellerUser_id;
        this.buyerUser_id=buyerUser_id;
        this.number_of_tickets=number_of_tickets;
        this.amountPayed=amountPayed;
        this.flight_id=flight_id;
        this.transaction_date =date;
        this.transaction_status=transaction_status;
        this.idbConnection=idbConnection;
        sendOfferRecivedMsg();
    }

    private void sendOfferRecivedMsg() {
        //String message_id,  String user_owner_id, String title,String buyerUser_id, Date message_date, String from_user_id, String transaction_id) {
        //need to change msg. to not have id.

        String msgContent="You got an offer from userName about your postDetails";
        Message message=new Message("-1",sellerUser_id,"Offer Recived",msgContent,new Date(),"-1",transaction_number);
        sendMessage(sellerUser_id,message);
    }

    private void sendOfferApprovedMsg() {
        //String message_id,  String user_owner_id, String title,String buyerUser_id, Date message_date, String from_user_id, String transaction_id) {
        //need to change msg.
        String msgContent="";
        Message message=new Message("-1",sellerUser_id,"Offer Recived",msgContent,new Date(),"-1",transaction_number);
        sendMessage(buyerUser_id,message);
    }

    private void sendMessage(String receiverUserID, Message message){
        try{
            idbConnection.insertToDbByTableName(receiverUserID+"_mailbox", message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
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
        String date = transaction_date.getDate()<10?"0"+transaction_date.getDate():transaction_date.getDate()+"";
        date += transaction_date.getMonth()<10?".0"+transaction_date.getMonth():"."+transaction_date.getMonth();
        date += ("."+transaction_date.getYear());
        ans[1]= date;
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

    public void getAllTransactionsByUserId(IdbConnection idbConnection,String user_id){
        try{
            idbConnection.getAllFromTable(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}

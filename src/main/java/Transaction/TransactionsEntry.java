package Transaction;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;
import User.MailBox.Message;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * available statuses:
 * For Sale, Offer Received, Offer Approved, Closed, Rejected
 *
 */
public class TransactionsEntry extends AEntry {
    private String transaction_number;
    private String user_id;
    private String flight_id;
    private String transaction_status;

    /**
     * empty constructor mainly for testing
     */
    public TransactionsEntry(){}

    /**
     * @see class documentation for acceptable transaction statuses.
     * this constructor is for creating a full Transaction entry object.
     *
     * @param transaction_number - String
     * @param user_id - String
     * @param flight_id - String
     * @param transaction_status - String
     * @throws Exception - thrown if the transaction type is not one of the valid types
     */
    public TransactionsEntry(String transaction_number, String user_id, String flight_id, String transaction_status) throws Exception{
        String[] statuses={"For Sale","Offer Received","Offer Approved","Closed","Rejected"};
        ArrayList<String> possible_statuses= new ArrayList<String>();
        for(String s:statuses){
            possible_statuses.add(s);
        }
        if(!possible_statuses.contains(transaction_status)){
            throw new Exception("status not valid");
        }
        this.transaction_number=transaction_number;
        this.user_id=user_id;
        this.flight_id=flight_id;
        this.transaction_status=transaction_status;
    }
    @Override
    public boolean validateEntry() {
        return true;
    }

    @Override
    public String getTableName() {
        return user_id+"_transactions_table";
    }

    @Override
    public String[] getAllData() {
        String[] ans = new String[this.getColumnsTitles().length];
        ans[0]= transaction_number;
        ans[1]= user_id;
        ans[2]= flight_id;
        ans[3]= transaction_status;
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

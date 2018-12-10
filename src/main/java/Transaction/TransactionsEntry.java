package Transaction;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;

import java.util.ArrayList;

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
    private ArrayList<String> possible_statuses= new ArrayList<String>();
    public TransactionsEntry(){}
    public TransactionsEntry(String transaction_number, String user_id, String flight_id, String transaction_status) throws Exception{
        String[] statuses={"For Sale","Offer Received","Offer Approved","Closed","Rejected"};
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
        return new String[0];
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

package EntriesObject;

import DataBaseConnection.IdbConnection;

public class TransactionsEntry extends AEntry{
    private String transaction_number;
    private String flight_id;
    private String transaction_status;

    @Override
    public boolean validateEntry() {
        return true;
    }

    @Override
    public String getTableName() {
        return "transactions_table";
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

    }
}

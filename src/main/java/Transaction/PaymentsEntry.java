package Transaction;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;

/**
 * PaymentEntry class extend the AEntry class.
 * @Override's the methods getTableName,getAllData,getIdentifierValue,deleteFromDb
 */
public class PaymentsEntry extends AEntry {
    protected String transactionNumber;
    protected String creditCardNumber;
    protected String creditCardExpirationYear;
    protected String creditCardExpirationMonth;
    protected String creditCardCVV;
    protected int numberOfPayments;
    protected String ownerName;

    /**
     * an empty constructor, mainly for debugging purposes.
     */
    public PaymentsEntry(){

    }

    /**
     *
     * @param transactionNumber - String
     * @param creditCardNumber - String
     * @param creditCardExpirationYear - String
     * @param creditCardExpirationMonth - String
     * @param creditCardCVV - String
     * @param numberOfPayments - String
     * @param ownerName - String
     */
    public PaymentsEntry(String transactionNumber,String creditCardNumber, String creditCardExpirationYear, String creditCardExpirationMonth, String creditCardCVV, int numberOfPayments, String ownerName){
        this.transactionNumber=transactionNumber;
        this.creditCardNumber= creditCardNumber;
        this.creditCardExpirationYear= creditCardExpirationYear;
        this.creditCardExpirationMonth = creditCardExpirationMonth;
        this.creditCardCVV = creditCardCVV;
        this.numberOfPayments = numberOfPayments;
        this.ownerName= ownerName;
    }

    @Override
    public boolean validateEntry() {
        return true;
    }

    @Override
    public String getTableName() {
        return "payments_table";
    }

    @Override
    public String[] getAllData() {
        String[] ans = new String[this.getColumnsTitles().length];
        ans[0]= transactionNumber;
        ans[1]= creditCardNumber;
        ans[2]= creditCardExpirationYear;
        ans[3]= creditCardExpirationMonth;
        ans[4] =creditCardCVV;
        ans[5]=String.valueOf(numberOfPayments);
        ans[6]= ownerName;
        return ans;
    }

    @Override
    public String getIdentifiers() {
        return "transactionNumber";
    }

    @Override
    public String getIdentifierValue() {
        return transactionNumber;
    }

    @Override
    public void deleteFromDb(IdbConnection idbConnection) throws Exception {
        idbConnection.deleteById(this);
    }
}

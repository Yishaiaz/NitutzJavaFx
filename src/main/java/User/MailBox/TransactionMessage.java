package User.MailBox;

import DataBaseConnection.IdbConnection;

import java.lang.reflect.Field;
import java.util.Date;

public class TransactionMessage extends AMessage {
    protected String transaction_id;
    protected boolean is_buyer;
    public TransactionMessage(String id){
        transaction_id="";
    }
    public TransactionMessage(String message_id,  String user_owner_id, String title, Date message_date, String from_user_id, String transaction_id, boolean is_buyer) {
        this.message_id=message_id;
        this.user_owner_id =user_owner_id;
        this.title=title;
        this.message_content =message_content ;
        this.message_date=message_date;
        this.from_user_id =from_user_id ;
        this.transaction_id = transaction_id;
        this.is_buyer= is_buyer;
        Class class1=this.getClass();
        Field[] fields = class1.getDeclaredFields();
        this.entryColumnNames=new String[this.getClass().getDeclaredFields().length];
        int i=0;
        for (Field field:this.getClass().getDeclaredFields()) {
            entryColumnNames[i]= field.getName();
            i++;
        }
    }

    public String getTransaction_id(){
        return transaction_id;
    }

    @Override
    public String getTableName() {
        return user_owner_id +"_transactions_message_table";
    }

    @Override
    public String getIdentifierValue() {
        return message_id;
    }

    @Override
    public void deleteFromDb(IdbConnection idbConnection) throws Exception {
        idbConnection.deleteById(this);
    }

    @Override
    public String[] getAllData() {
        String[] ans = new String[this.getColumnsTitles().length];
        ans[0]= message_id;
        ans[1]= user_owner_id;
        ans[2]= title;
        ans[3]= message_content;
        String date = message_date.getDate()<10?"0"+message_date.getDate():message_date.getDate()+"";
        date += message_date.getMonth()<10?".0"+message_date.getMonth():"."+message_date.getMonth();
        date += ("."+message_date.getYear());
        ans[4]= date;
        ans[5]= from_user_id;
        ans[6] = String.valueOf(is_buyer);
        ans[7]=transaction_id;
        return ans;
    }

}

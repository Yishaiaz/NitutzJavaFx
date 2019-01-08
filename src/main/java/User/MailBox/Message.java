package User.MailBox;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Message class extends AEntry abstract class.
 * represents a message in a user's mailbox db.
 * @Overide's the following functions getTableName,getAllData,getIdentifierValue,deleteFromDb
 *
 */
public class Message extends AEntry {
    protected String message_id;
    protected String user_owner_id;
    protected String title;
    protected String message_content;
    protected Date message_date;
    protected String from_user_id;
    protected String transaction_id;
    protected String swap_id;

    /**
     * a constructor with that only initializes the user owner field only.
     * @param user_owner_id
     */
    public Message(String user_owner_id){
        this.user_owner_id=user_owner_id;
    }

    /**
     * full constructor.
     * @param message_id - String
     * @param user_owner_id - String
     * @param title - String
     * @param message_content - String
     * @param message_date - Date( java.sql.Date)
     * @param from_user_id - String
     * @param transaction_id - String
     */
    public Message(String message_id,  String user_owner_id, String title, String message_content, Date message_date, String from_user_id, String transaction_id) {
        this.message_id=message_id;
        this.user_owner_id =user_owner_id;
        this.title=title;
        this.message_content =message_content ;
        this.message_date=message_date;
        this.from_user_id =from_user_id ;
        this.transaction_id = transaction_id;
        this.swap_id = null;
        Class class1=this.getClass();
        Field[] fields = class1.getDeclaredFields();
        this.entryColumnNames=new String[this.getClass().getDeclaredFields().length];
        int i=0;
        for (Field field:this.getClass().getDeclaredFields()) {
            entryColumnNames[i]= field.getName();
            i++;
        }
    }

    public Message(String message_id,  String user_owner_id, String title, String message_content, Date message_date, String from_user_id, String transaction_id, String swapTransactionId) {
        this.message_id=message_id;
        this.user_owner_id =user_owner_id;
        this.title=title;
        this.message_content =message_content ;
        this.message_date=message_date;
        this.from_user_id =from_user_id ;
        this.transaction_id = transaction_id==null?"":transaction_id;
        this.swap_id = swapTransactionId;
        Class class1=this.getClass();
        Field[] fields = class1.getDeclaredFields();
        this.entryColumnNames=new String[this.getClass().getDeclaredFields().length];
        int i=0;
        for (Field field:this.getClass().getDeclaredFields()) {
            entryColumnNames[i]= field.getName();
            i++;
        }
    }

    @Override
    public String getTableName() {
        return user_owner_id +"_mailbox";
    }

    @Override
    public boolean validateEntry() {
        return true;
    }

    @Override
    public String getIdentifiers() {
        return "message_id";
    }

    @Override
    public String[] getColumnsTitles() {
        return entryColumnNames;
    }
    @Override
    public String[] getAllData() {
        String[] ans = new String[this.getColumnsTitles().length];
        ans[0]= message_id;
        ans[1]= user_owner_id;
        ans[2]= title;
        ans[3]= message_content;
        ans[4]= message_date.toString();
        ans[5]= from_user_id;
        ans[6]=transaction_id;
        ans[7]=swap_id;
        return ans;
    }

    @Override
    public boolean equals(Object o){
        return ((o instanceof Message) && ((Message)o).message_id.equals(message_id));
    }

    /**
     * enables to set the message id field
     * @param message_id - String, the new message id.
     */
    public void setMessage_id(String message_id){
        this.message_id=message_id;
    }


    @Override
    public String getIdentifierValue() {
        return message_id;
    }

    @Override
    public void deleteFromDb(IdbConnection idbConnection) throws Exception {
        idbConnection.deleteById(this);
    }

    /**
     * returns a boolean representing whether this message is a transaction message ("system messages")
     * @return
     */
    public boolean isTransaction(){
        return !this.transaction_id.equals("");
    }

    /**
     * returns a boolean representing whether this message is a swap request message
     * @return
     */
    public boolean isSwap(){
        return !(this.swap_id == null);
    }
}

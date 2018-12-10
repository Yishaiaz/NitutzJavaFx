package User.MailBox;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;

import java.lang.reflect.Field;
import java.util.Date;

public class AMessage extends AEntry {
    protected String message_id="";
    protected String user_owner_id ="";
    protected String title="";
    protected String message_content ="";
    protected boolean is_transaction = false;
    protected Date message_date;
    protected String from_user_id ="";
    protected String flight_id ="";
    protected boolean is_buyer=false;


    public AMessage(){
        Field[] fields= AMessage.class.getDeclaredFields();
        this.entryColumnNames=new String[fields.length];
        int i=0;
        for (Field field:fields) {
            entryColumnNames[i]= field.getName();
            i++;
        }
    }
    public AMessage(String user_owner_id){
        this.user_owner_id = user_owner_id;
    }
    @Override
    public boolean validateEntry() {
        return true;
    }

    @Override
    public String getTableName() {
        return user_owner_id +"_mailbox";
    }

    @Override
    public String[] getAllData() {
        String[] ans = new String[this.getColumnsTitles().length];
        ans[0]= message_id;
        ans[1]= user_owner_id;
        ans[2]= title;
        ans[3]= message_content;
        ans[4] = String.valueOf(is_transaction);
        String date = message_date.getDate()<10?"0"+message_date.getDate():message_date.getDate()+"";
        date += message_date.getMonth()<10?".0"+message_date.getMonth():"."+message_date.getMonth();
        date += ("."+message_date.getYear());
        ans[5]= date;
        ans[6]= from_user_id;
        ans[7] = flight_id;
        ans[8] = String.valueOf(is_buyer);
        return ans;
    }

    @Override
    public String getIdentifiers() {
        return "message_id";
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
    public boolean equals(Object o){
        return ((o instanceof AMessage) && ((AMessage)o).message_id.equals(message_id));
    }

    public void setMessage_id(String message_id){
        this.message_id= message_id;
    }
}

package EntriesObject;

import DataBaseConnection.IdbConnection;
import java.lang.reflect.Field;
import java.util.Date;

public class AMessage extends AEntry{
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
        ans[5]= message_date.toString();
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
    public void setMessage_id(String message_id){
        this.message_id= message_id;
    }
}

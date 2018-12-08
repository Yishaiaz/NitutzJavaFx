package EntriesObject;

import DataBaseConnection.IdbConnection;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    public AMessage(){
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
        throw new NotImplementedException();
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
}

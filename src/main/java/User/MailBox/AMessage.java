package User.MailBox;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;

import java.lang.reflect.Field;
import java.util.Date;

public abstract class AMessage extends AEntry {
    protected String message_id;
    protected String user_owner_id;
    protected String title;
    protected String message_content;
    protected Date message_date;
    protected String from_user_id;

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
    public boolean equals(Object o){
        return ((o instanceof AMessage) && ((AMessage)o).message_id.equals(message_id));
    }

    public void setMessage_id(String message_id){
        this.message_id=message_id;
    }
}

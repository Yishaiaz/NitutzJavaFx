package EntriesObject;

import DataBaseConnection.IdbConnection;

import java.lang.reflect.Field;
import java.util.Date;

public class PersonalMessage extends AMessage{

    public PersonalMessage(String message_id, String user_owner_id, String title, String message_content, Date message_date, String from_user_id) {
        super();
        this.message_id=message_id;
        this.user_owner_id =user_owner_id;
        this.title=title;
        this.message_content =message_content ;
        this.message_date=message_date;
        this.from_user_id =from_user_id;
    }
}


package User.MailBox;

import java.util.Date;

public class TransactionMessage extends AMessage {
    public TransactionMessage(String message_id,  String user_owner_id, String title, String message_content, Date message_date, String from_user_id, String flight_id, boolean is_buyer) {
        super();
        this.message_id=message_id;
        this.user_owner_id =user_owner_id;
        this.title=title;
        this.message_content =message_content ;
        this.message_date=message_date;
        this.from_user_id =from_user_id ;
        this.is_transaction = true;
        this.flight_id =flight_id;
        this.is_buyer= is_buyer;
    }
}

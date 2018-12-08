package EntriesObject;

import java.util.Date;

public class TransactionMessage extends AMessage {
    public TransactionMessage(String message_id,  String user_owner_id, String title, String message_content, Date message_date, String from_user_id, String flight_id) {
        this.message_id=message_id;
        this.user_owner_id =user_owner_id;
        this.title=title;
        this.message_content =message_content ;
        this.message_date=message_date;
        this.from_user_id =from_user_id ;
        this.is_transaction = true;
        this.flight_id =flight_id;
    }

    @Override
    public String[] getAllData() {
        String[] ans = new String[getColumnsTitles().length];
        ans[0]= message_id;
        ans[1]= user_owner_id;
        ans[3]= title;
        ans[4]= message_content;
        ans[2]= message_date.toString();
        ans[5]= from_user_id;
        ans[6] = flight_id;
        return ans;
    }
}

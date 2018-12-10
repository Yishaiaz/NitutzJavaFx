package View.Displayers;

import User.MailBox.Message;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class MessageDisplayer extends VBox {
    private Message message;
    private Label lbl_fromUser;
    private Label lbl_title;
    private Label lbl_date;
    private ListView lv_content;
    private Label lbl_content;

    public MessageDisplayer(Message msg) {
        setMessage(msg);
    }

    private void setMessage(Message msg) {
        if(msg == null)
            return;
        message = msg;
        init();
        getChildren().addAll(lbl_fromUser,lbl_title, lbl_date, lv_content);
    }

    private void init(){
        String[] msgData = message.getAllData();
        lbl_fromUser = new Label("Sent By:\t\t"+msgData[5]);
        lbl_title = new Label("Subject:\t\t"+msgData[2]);
        lbl_date = new Label("    Date:\t\t"+msgData[4]);
        lbl_content = new Label(msgData[3]);
        lv_content = new ListView();
        lv_content.getItems().add(lbl_content);
    }

    public boolean isTransaction() {
        return message.isTransaction();
    }
}

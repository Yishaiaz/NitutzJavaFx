package View.Displayers;

import User.MailBox.Message;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MessageDisplayer extends VBox {
    private Message message;
    private Label lbl_fromUser;
    private Label lbl_title;
    private Label lbl_date;
    private javafx.scene.control.TextArea ta_content;

    public MessageDisplayer(Message msg) {
        setMessage(msg);
    }

    private void setMessage(Message msg) {
        if(msg == null)
            return;
        message = msg;
        init();
        getChildren().addAll(lbl_fromUser,lbl_title, lbl_date, ta_content);
    }

    private void init(){
        String[] msgData = message.getAllData();
        lbl_fromUser = new Label(msgData[1]);
        lbl_title = new Label(msgData[2]);
        lbl_date = new Label(msgData[5]);
        ta_content = new javafx.scene.control.TextArea(msgData[3]);
    }

    public boolean isTransaction() {
        return message.isTransaction();
    }
}

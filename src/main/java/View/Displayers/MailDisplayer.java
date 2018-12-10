package View.Displayers;

import User.MailBox.AMessage;
import User.MailBox.TransactionMessage;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class MailDisplayer extends GridPane {
    private AMessage msg; // the message
    private Label lbl_fromUser; //user that sent
    private Label lbl_title; //message's title
    private Label lbl_date; //message's date
    private CheckBox cb_isTransaction; //is transaction message

    /**
     * Constructor
     * @param msg - the message to display
     */
    public MailDisplayer(AMessage msg) {
        setMessage(msg);
        getColumnConstraints().add(new ColumnConstraints(75));
        getColumnConstraints().add(new ColumnConstraints(100));
        getColumnConstraints().add(new ColumnConstraints(100));
        getColumnConstraints().add(new ColumnConstraints(100));
    }

    /**
     * create displayer for the message headers
     * @return - MailDisplayer with the headers of the fields
     */
    public static MailDisplayer getHeaders(){
        MailDisplayer ans = new MailDisplayer(null);
        ans.lbl_fromUser = new Label("From");
        ans.lbl_title = new Label("Title");
        ans.lbl_date = new Label("Date");
        Label lbl_isTransaction = new Label("Is Transaction");
        ans.add(ans.lbl_fromUser, 0, 0);
        ans.add(ans.lbl_title, 1, 0);
        ans.add(ans.lbl_date, 2, 0);
        ans.add(lbl_isTransaction, 3, 0);
        return ans;
    }

    /**
     * sets the message to this displayer and initialize all fields
     * @param message - the message to display
     */
    public void setMessage(AMessage message){
        if(message == null)
            return;
        msg = message;
        init();
        add(lbl_fromUser, 0, 0);
        add(lbl_title, 1, 0);
        add(lbl_date, 2, 0);
        add(cb_isTransaction, 3, 0);
    }

    /**
     * initialize all the fields
     */
    private void init(){
        String[] msgData = msg.getAllData();
        lbl_fromUser = new Label(msgData[1]);
        lbl_title = new Label(msgData[2]);
        lbl_date = new Label(msgData[5]);
        cb_isTransaction = new CheckBox("");
        if(msg instanceof TransactionMessage){
            cb_isTransaction.setSelected(true);
        }
        else{
            cb_isTransaction.setSelected(false);
        }
        cb_isTransaction.setDisable(true);
    }
}
package View.Displayers;

import User.MailBox.Message;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MailBoxDisplayer extends ListView {
    private List<MailDisplayer> messages = new ArrayList<>();

    /**
     * Constructor
     * @param messageList - the list of messages to display
     */
    public MailBoxDisplayer(Collection<Message> messageList){
        setMessages(messageList);
    }

    /**
     * setter for the messages to display
     * @param messageList - a list of messages to display
     */
    public void setMessages(Collection<Message> messageList){
        if (messageList == null)
            return;
        MailDisplayer currrent;
        for (Message msg: messageList){
            currrent = new MailDisplayer(msg);
            currrent.setPrefWidth(450);
            messages.add(currrent);
        }
        init();
    }

    /**
     * initializing all the fields
     */
    private void init(){
        getItems().add(MailDisplayer.getHeaders());
        getItems().addAll(messages);
    }
}

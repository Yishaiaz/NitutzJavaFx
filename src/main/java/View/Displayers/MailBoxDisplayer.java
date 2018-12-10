package View.Displayers;

import User.MailBox.AMessage;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MailBoxDisplayer extends ListView {
    private List<MailDisplayer> messages = new ArrayList<>();

    /**
     * Constructor
     * @param messageCollection - the list of messages to display
     */
    public MailBoxDisplayer(Collection<AMessage> messageCollection){
        setMessages(messageCollection);
    }

    /**
     * setter for the messages to display
     * @param messageCollection - a list of messages to display
     */
    public void setMessages(Collection<AMessage> messageCollection){
        if (messageCollection == null)
            return;
        MailDisplayer currrent;
        for (AMessage msg: messageCollection){
            currrent = new MailDisplayer(msg);
            currrent.setPrefWidth(450);
            messages.add(currrent);
        }
        init();
    }

    public List<MailDisplayer> getMessages(){
        return messages;
    }

    /**
     * initializing all the fields
     */
    private void init(){
        getItems().add(MailDisplayer.getHeaders());
        getItems().addAll(messages);
    }
}

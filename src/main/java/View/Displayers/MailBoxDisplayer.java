package View.Displayers;

import EntriesObject.AMessage;
import EntriesObject.MailBox;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class MailBoxDisplayer extends ListView {
    private List<MailDisplayer> messages = new ArrayList<>();

    public MailBoxDisplayer(List<AMessage> messageList){
        setMessages(messageList);
    }

    public void setMessages(MailBox mb) {
        if (mb == null)
            return;
        MailDisplayer current;
        for (AMessage msg: mb.getAllMessages().values()){
            current = new MailDisplayer(msg);
            messages.add(current);
        }
        init();
    }

    public void setMessages(List<AMessage> messageList){
        if (messageList == null)
            return;
        MailDisplayer currrent;
        for (AMessage msg: messageList){
            currrent = new MailDisplayer(msg);
            currrent.setPrefWidth(450);
            messages.add(currrent);
        }
        init();
    }

    private void init(){
        getItems().add(MailDisplayer.getHeaders());
        getItems().addAll(messages);
    }
}

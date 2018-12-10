package User.MailBox;

import DataBaseConnection.IdbConnection;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MailBox {
    private Properties props;
    private String user_id;
    private IdbConnection idbConnection;
    private Map<String, Message> messagesList= new HashMap<>();

    public MailBox(String user_id, IdbConnection idbConnection) {
        //create the transaction and personal message table for the user, if they already exist, it will NOT overwrite
        this.user_id = user_id;
        this.idbConnection = idbConnection;
        this.createUserDataBases();
        try{
            for (String[] item: idbConnection.getAllFromTable(new Message(user_id))){
//                if(item[4].equals("false")){
//                    messagesList.put(item[0], new PersonalMessage(item[0],item[1],item[2],item[3], new Date(item[5]), item[6]));
//                }
//                else{
                    messagesList.put(item[0], new Message(item[0],item[1],item[3],new Date(item[4]),item[5], item[6]));
//                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        props = new Properties();
        String propFileName = "config.properties";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                props.load(inputStream);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }


    }
    private void createUserDataBases(){
        //creating the transaction message database for specific user
        try{
            this.idbConnection.createNewTable(user_id+"_mailbox", new Message("random").getColumnsTitles(), new Message(user_id).getIdentifiers());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void InsertEntry(Message message){
        messagesList.put(message.getIdentifierValue(),message);
        int nextid = Integer.valueOf(props.getProperty("nextMeassageId"));
        message.setMessage_id(String.valueOf(nextid));
        props.setProperty("nextMeassageId", String.valueOf(++nextid));
        try{
            props.store(new FileOutputStream("config.properties"), null);
            message.insertToDb(idbConnection);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void sendMessage(String receiverId, Message message){
        try{
            idbConnection.insertToDbByTableName(receiverId+"_mailbox", message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteEntry(Message message){
        messagesList.remove(message.getIdentifierValue());
        try{
            idbConnection.deleteById(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public boolean isEmpty(){
        return messagesList.isEmpty();
    }

    public Map<String, Message> getAllMessages(){
        return this.messagesList;
    }


}

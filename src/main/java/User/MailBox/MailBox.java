package User.MailBox;

import DataBaseConnection.IdbConnection;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

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
//                }String message_id,  String user_owner_id, String title, String message_content, Date message_date, String from_user_id, String transaction_id) {
//                else{
                    messagesList.put(item[0], new Message(item[0],item[1],item[3],item[4],new Date(item[5]),item[6], item[7]));
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

    private void updateEntryId(Message message){
        int max=0;
        for (String s:getAllMessages().keySet()){
            if(Integer.valueOf(s) > max) max = Integer.valueOf(s);
        }
        int nextid = max;
        message.setMessage_id(String.valueOf(nextid));
    }

    public void sendMessage(String receiverId, Message message){
        try{
            this.updateEntryId(message);
            messagesList.put(message.getIdentifierValue(),message);
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

    private int getMaximumId(LinkedList<String[]> all){
        int max=0;
        if (all==null || all.size()==0){
            return 1;
        }
        for(String[] s: all){
            if( Integer.valueOf(s[0]) > max) max =Integer.valueOf(s[0]);
        }
        return max+1;
    }


}

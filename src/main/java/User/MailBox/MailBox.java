package User.MailBox;

import DataBaseConnection.IdbConnection;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.*;

/**
 * MailBox class, represents the mailbox db table, dedicated to each user.
 * the mailbox object initializes with the user's existing messages (reads them from the db).
 *
 */
public class MailBox {
    private Properties props;
    private String user_id;
    private IdbConnection idbConnection;
    private Map<String, Message> messagesList= new HashMap<>();

    /**
     * read's all the user's messages from the db and creates a Message object, adding it to the HashMap
     * object (as a private field).
     * @param user_id
     * @param idbConnection
     */
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
                    messagesList.put(item[0], new Message(item[0],item[1],item[3],item[4],Date.valueOf(item[5]),item[6], item[7]));
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
        try{
            LinkedList<String[]> all= idbConnection.getAllFromTable(new Message(message.user_owner_id));
            if(all==null){
                message.setMessage_id(String.valueOf(max+1));
                return;
            }
            for(String s[]: all){
                if(Integer.valueOf(s[0]) > max) max = Integer.valueOf(s[0]);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        message.setMessage_id(String.valueOf(max+1));
    }

    /**
     * enables to insert to a different user mailbox db table a message, changes the message id
     * according to the target db table content.
     * @param receiverId - String
     * @param message = Message
     */
    public void sendMessage(String receiverId, Message message){
        try{
            this.updateEntryId(message);
            messagesList.put(message.getIdentifierValue(),message);
            idbConnection.insertToDbByTableName(receiverId+"_mailbox", message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes a message from the user's mailbox in the db.
     * @param message
     */
    public void deleteEntry(Message message){
        messagesList.remove(message.getIdentifierValue());
        try{
            idbConnection.deleteById(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * returns a boolean whether the mailbox has any messages in it.
     * @return
     */
    public boolean isEmpty(){
        return messagesList.isEmpty();
    }

    /**
     * returns the entire hashmap object, holding all the messages by <message_id:String, message:Message>
     * @return Map<String, Message> an interface wrapper for HashMap object.
     */
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

package EntriesObject;

import DataBaseConnection.IdbConnection;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MailBox {
    private String user_id;
    private IdbConnection idbConnection;
    private Map<String,AMessage> messagesList= new HashMap<>();

    public MailBox(String user_id, IdbConnection idbConnection) {
        this.user_id = user_id;
        this.idbConnection = idbConnection;
        try{
            for (String[] item: idbConnection.getAllFromTable(new AMessage(user_id))){
                if(item[4].equals("false")){
                    messagesList.put(item[0], new PersonalMessage(item[0],item[1],item[2],item[3], new Date(item[5]), item[6]));
                }
                else{
                    messagesList.put(item[0], new TransactionMessage(item[0],item[1],item[2],item[3], new Date(item[5]), item[6],item[7]));
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }


    }
    public void InsertEntry(AMessage message){
        messagesList.put(message.getIdentifierValue(),message);
        try{
            idbConnection.insert(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteEntry(AMessage message){
        messagesList.remove(message.getIdentifierValue());
        try{
            idbConnection.deleteById(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public boolean isEnpty(){
        return messagesList.isEmpty();
    }

    public String getTableName() {
        return user_id+"_MessageTable";
    }


}

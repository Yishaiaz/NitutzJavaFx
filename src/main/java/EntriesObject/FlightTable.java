package EntriesObject;

import DataBaseConnection.IdbConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FlightTable {
    private String user_id;
    private IdbConnection idbConnection;
    private Map<String,FlightEntry> flightsList= new HashMap<>();

    public FlightTable(String user_id, IdbConnection idbConnection) {
        this.user_id = user_id;
        this.idbConnection = idbConnection;
        try{
            for (String[] item: idbConnection.getAllFromTable(new AMessage(user_id))){
                if(item[4].equals("false")){
                    flightsList.put(item[0], new FlightEntry(item[0],item[1],item[2],new Date(item[3]),new Date(item[4]), item[5], Integer.valueOf(item[6]), item[7], item[8], item[9], item[10]));
                }
                else{
                    flightsList.put(item[0], new TransactionMessage(item[0],item[1],item[2],item[3], new Date(item[5]), item[6],item[7]));
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }


    }
    public void InsertEntry(AMessage message){
        flightsList.put(message.getIdentifierValue(),message);
        try{
            idbConnection.insert(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteEntry(AMessage message){
        flightsList.remove(message.getIdentifierValue());
        try{
            idbConnection.deleteById(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public boolean isEnpty(){
        return flightsList.isEmpty();
    }

    public Map<String, AMessage> getAllMessages(){
        return this.flightsList;
    }


}


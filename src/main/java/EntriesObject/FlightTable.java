package EntriesObject;

import DataBaseConnection.IdbConnection;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FlightTable {
    private Properties props;
    private String user_id;
    private IdbConnection idbConnection;
    private Map<String,FlightEntry> flightsList= new HashMap<>();

    public FlightTable(String user_id, IdbConnection idbConnection) {
        this.user_id = user_id;
        this.idbConnection = idbConnection;
        try{
            for (String[] item: idbConnection.getAllFromTable(new FlightEntry(user_id))){
                flightsList.put(item[0], new FlightEntry(item[0],item[1],item[2],new Date(item[3]),new Date(item[4]), item[5], Integer.valueOf(item[6]), item[7], Boolean.parseBoolean(item[8]), item[9], Double.parseDouble(item[10]), item[11], Boolean.valueOf(item[12]), item[13]));
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
    public void InsertEntry(FlightEntry flightEntry){
        flightsList.put(flightEntry.getIdentifierValue(),flightEntry);
        int nextid = Integer.valueOf(props.getProperty("nextFlightId"));
        flightEntry.setFlight_id(String.valueOf(nextid));
        props.setProperty("nextFlightId", String.valueOf(++nextid));
        try{
            props.store(new FileOutputStream("config.properties"), null);
            idbConnection.createNewTable("general_flights_table", flightEntry.getColumnsTitles(), flightEntry.getIdentifiers());
            idbConnection.insertToDbByTableName("general_flights_table", flightEntry);
            flightEntry.insertToDb(idbConnection);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteEntry(FlightEntry flightEntry){
        flightsList.remove(flightEntry.getIdentifierValue());
        try{
            idbConnection.createNewTable("general_flights_table",flightEntry.getColumnsTitles(), flightEntry.getIdentifiers());
            idbConnection.deleteById(flightEntry);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public boolean isEnpty(){
        return flightsList.isEmpty();
    }

    public Map<String, FlightEntry> getAllMessages(){
        return this.flightsList;
    }


}


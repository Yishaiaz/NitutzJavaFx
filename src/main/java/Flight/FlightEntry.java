package Flight;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

public class FlightEntry extends AEntry {
    private String flight_id="";
    private String publisher_user_id="";
    private String airline_name ="";
    private Date flight_start_date;
    private Date flight_end_date;
    private String flight_lagguage_type="";
    private int flight_number_of_tickets=0;
    private String flight_origin_country_code="IL";
    private boolean is_return_flight_included = false;
    private String flight_tickets_type="1 adult";
    private double flight_price=0;
    private String flight_status="";
    private String flight_destination;

    public FlightEntry(String publisher_user_id){
        this.publisher_user_id=publisher_user_id;
    }
    public FlightEntry(String publisher_user_id, String airline_name, Date flight_start_date, Date flight_end_date, String flight_lagguage_type, int flight_number_of_tickets, String flight_origin_country_code, boolean is_return_flight_included, String flight_tickets_type, double flight_price, String flight_status, String flight_destination) {
        Properties props = new Properties();
        String propFileName = "config.properties";
        try {
            FileInputStream inputStream = new FileInputStream(propFileName);
            //InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                props.load(inputStream);
                inputStream.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }
        int nextid = Integer.valueOf(props.getProperty("nextFlightId"));
        nextid++;
        props.setProperty("nextFlightId", String.valueOf(nextid+""));
        try{
            FileOutputStream outputStream=new FileOutputStream("config.properties");
            props.store(outputStream, null);
            outputStream.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        this.flight_id=String.valueOf(nextid);
        this.publisher_user_id = publisher_user_id;
        this.airline_name = airline_name;
        this.flight_start_date = flight_start_date;
        this.flight_end_date = flight_end_date;
        this.flight_lagguage_type = flight_lagguage_type;
        this.flight_number_of_tickets = flight_number_of_tickets;
        this.flight_origin_country_code = flight_origin_country_code;
        this.is_return_flight_included = is_return_flight_included;
        this.flight_tickets_type = flight_tickets_type;
        this.flight_price = flight_price;
        this.flight_status = flight_status;
        this.flight_destination = flight_destination;
    }

    @Override
    public boolean validateEntry() {
        return true;
    }

    @Override
    public String getTableName() {
        return "flights_table";
    }

    @Override
    public String[] getAllData() {
        String[] ans = new String[getColumnsTitles().length];
        ans[0] = flight_id;
        ans[1]= publisher_user_id ;
        ans[2]= airline_name;
        ans[3]= flight_start_date.toString();
        ans[4]= flight_end_date.toString();
        ans[5]= flight_lagguage_type;
        ans[6]= flight_number_of_tickets+"";
        ans[7] = flight_origin_country_code;
        ans[8] = String.valueOf(is_return_flight_included);
        ans[9] = flight_tickets_type;
        ans[10] = String.valueOf(flight_price);
        ans[11] = flight_status;
        ans[12] = flight_destination;
        return ans;
    }

    @Override
    public String getIdentifiers() {
        return "flight_id";
    }

    @Override
    public String getIdentifierValue() {
        return flight_id;
    }

    @Override
    public void deleteFromDb(IdbConnection idbConnection) throws Exception {
        try{
            idbConnection.deleteById(this);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

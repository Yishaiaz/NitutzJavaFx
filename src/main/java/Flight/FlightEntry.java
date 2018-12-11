package Flight;

import DataBaseConnection.IdbConnection;
import EntriesObject.AEntry;
import java.sql.Date;
import java.util.LinkedList;

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

    public FlightEntry(String publisher_user_id, String airline_name, Date flight_start_date, Date flight_end_date, String flight_lagguage_type, int flight_number_of_tickets, String flight_origin_country_code, boolean is_return_flight_included, String flight_tickets_type, double flight_price, String flight_status, String flight_destination, IdbConnection idbConnection) {
        int i=this.getMaximumId(getAllFlights(idbConnection));
        this.flight_id=String.valueOf(i);
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
    public String[] getColumnsTitles() {
        String[] ans = {"flight_id","publisher_user_id", "airline_name", "flight_start_date", "flight_end_date", "flight_lagguage_type", "flight_number_of_tickets", "flight_origin_country_code",
                "is_return_flight_included", "flight_tickets_type", "flight_price", "flight_status", "flight_destination"};
        return ans;
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
    public LinkedList<String[]> getAllFlights(IdbConnection idbConnection){
        try{
            return idbConnection.getAllFromTable(this);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isReturnIncluded(){
        return is_return_flight_included;
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

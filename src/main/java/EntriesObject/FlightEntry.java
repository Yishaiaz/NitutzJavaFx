package EntriesObject;

import DataBaseConnection.IdbConnection;

import java.util.Date;

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

    public FlightEntry(String publisher_user_id){
        this.publisher_user_id=publisher_user_id;
    }
    public FlightEntry(String flight_id, String publisher_user_id, String airline_name, Date flight_start_date, Date flight_end_date, String flight_lagguage_type, int flight_number_of_tickets, String flight_origin_country_code, boolean is_return_flight_included, String flight_tickets_type, double flight_price) {
        this.flight_id=flight_id;
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
    }

    public String getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(String flight_id) {
        this.flight_id = flight_id;
    }

    public String getPublisher_user_id() {
        return publisher_user_id;
    }

    public void setFlightId(String flight_id){
        this.flight_id = flight_id;
    }

    public void setPublisher_user_id(String publisher_user_id) {
        this.publisher_user_id = publisher_user_id;
    }

    public String getAirline_name() {
        return airline_name;
    }

    public void setAirline_name(String airline_name) {
        this.airline_name = airline_name;
    }

    public Date getFlight_start_date() {
        return flight_start_date;
    }

    public void setFlight_start_date(Date flight_start_date) {
        this.flight_start_date = flight_start_date;
    }

    public Date getFlight_end_date() {
        return flight_end_date;
    }

    public void setFlight_end_date(Date flight_end_date) {
        this.flight_end_date = flight_end_date;
    }

    public String getFlight_lagguage_type() {
        return flight_lagguage_type;
    }

    public void setFlight_lagguage_type(String flight_lagguage_type) {
        this.flight_lagguage_type = flight_lagguage_type;
    }

    public int getFlight_number_of_tickets() {
        return flight_number_of_tickets;
    }

    public void setFlight_number_of_tickets(int flight_number_of_tickets) {
        this.flight_number_of_tickets = flight_number_of_tickets;
    }

    public String getFlight_origin_country_code() {
        return flight_origin_country_code;
    }

    public void setFlight_origin_country_code(String flight_origin_country_code) {
        this.flight_origin_country_code = flight_origin_country_code;
    }

    public boolean isIs_return_flight_included() {
        return is_return_flight_included;
    }

    public void setIs_return_flight_included(boolean is_return_flight_included) {
        this.is_return_flight_included = is_return_flight_included;
    }

    public String getFlight_tickets_type() {
        return flight_tickets_type;
    }

    public void setFlight_tickets_type(String flight_tickets_type) {
        this.flight_tickets_type = flight_tickets_type;
    }

    public double getFlight_price() {
        return flight_price;
    }

    public void setFlight_price(double flight_price) {
        this.flight_price = flight_price;
    }

    @Override
    public boolean validateEntry() {
        return true;
    }

    @Override
    public String getTableName() {
        return this.publisher_user_id+"_flights_table";
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

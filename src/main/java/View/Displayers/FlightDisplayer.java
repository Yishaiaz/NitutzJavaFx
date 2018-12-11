package View.Displayers;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class FlightDisplayer extends GridPane {
    private String[] flight; //the flightDets to show
    private Label lbl_fid; //flightDets id
    private Label lbl_publisher; //who sells
    private Label lbl_dates; // fromDate - toDate
    private Label lbl_origin; // origin country
    private Label lbl_destination; // destination country
    private Label lbl_numOfTickets; // how many tickets
    private CheckBox cb_isReturn; // return tickets included
    private Label lbl_price; // cost

    /**
     * Constructor for the displayer
     * @param flightEntry - the flightDets entry to show
     */
    public FlightDisplayer(String[] flightEntry){
        setFlightDets(flightEntry);
        getColumnConstraints().add(new ColumnConstraints(50));
        getColumnConstraints().add(new ColumnConstraints(75));
        getColumnConstraints().add(new ColumnConstraints(100));
        getColumnConstraints().add(new ColumnConstraints(100));
        getColumnConstraints().add(new ColumnConstraints(50));
        getColumnConstraints().add(new ColumnConstraints(50));
        getColumnConstraints().add(new ColumnConstraints(75));
    }

    /**
     * create displayer for the flightDets headers
     * @return - FlightDisplayer with the headers of the fields.
     */
    public static FlightDisplayer getHeaders(){
        FlightDisplayer ans = new FlightDisplayer(null);
        ans.lbl_fid = new Label("Flight ID");
        ans.lbl_fid.setStyle("-fx-fill: RED;-fx-font-weight:bold;");
        ans.lbl_publisher = new Label("Publisher");
        ans.lbl_publisher.setStyle("-fx-fill: RED;-fx-font-weight:bold;");
        ans.lbl_dates = new Label("Dates");
        ans.lbl_dates.setStyle("-fx-fill: RED;-fx-font-weight:bold;");
        ans.lbl_origin = new Label("Depart From");
        ans.lbl_origin.setStyle("-fx-fill: RED;-fx-font-weight:bold;");
        ans.lbl_destination = new Label("Destination");
        ans.lbl_destination.setStyle("-fx-fill: RED;-fx-font-weight:bold;");
        ans.lbl_numOfTickets = new Label("Tickets");
        ans.lbl_numOfTickets.setStyle("-fx-fill: RED;-fx-font-weight:bold;");
        Label lbl_isReturn = new Label("Return Included");
        lbl_isReturn.setStyle("-fx-fill: RED;-fx-font-weight:bold;");
        ans.lbl_price = new Label("Price");
        ans.lbl_price.setStyle("-fx-fill: RED;-fx-font-weight:bold;");
        ans.addRow(0, ans.lbl_fid, ans.lbl_publisher, ans.lbl_dates, ans.lbl_origin, ans.lbl_destination, ans.lbl_numOfTickets, lbl_isReturn, ans.lbl_price);
        return ans;
    }

    /**
     * sets the flightDets to this displayer and initialize all fields
     * @param flightEntry - the flightDets entry to display
     */
    private void setFlightDets(String[] flightEntry) {
        if(flightEntry == null)
            return;
        flight = flightEntry;
        init();
        addRow(0, lbl_fid, lbl_publisher, lbl_dates, lbl_origin, lbl_destination, lbl_numOfTickets, cb_isReturn, lbl_price);
    }

    /**
     * initialize all the fields
     */
    private void init(){
        lbl_fid = new Label(flight[0]);
        lbl_publisher = new Label(flight[1]);
        lbl_dates = new Label(flight[3]+" - "+flight[4]);
        lbl_origin = new Label(flight[7]);
        lbl_destination = new Label(flight[12]);
        lbl_numOfTickets = new Label(flight[6]);
        cb_isReturn = new CheckBox();
        if (flight[8].equals("true")){
            cb_isReturn.setSelected(true);
        }
        else {
            cb_isReturn.setSelected(false);
        }
        cb_isReturn.setDisable(true);
        lbl_price = new Label(flight[10]);
    }

    public VacationDisplayer getVacationDisplayer(){return new VacationDisplayer(flight);}
}

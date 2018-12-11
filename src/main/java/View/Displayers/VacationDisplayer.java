package View.Displayers;

import Flight.FlightEntry;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VacationDisplayer extends VBox {
    private String[] flight; //the flight to show
    private VBox vb_systemDetails; //flight_id + publisher
    private HBox hb_flightDetails; //airline, startDate, endDate, origin, dest
    private HBox hb_additionalFlightDetails; //numOfTickets, type, isReturn, luggage type
    private HBox hb_price;

    /**
     * Constructor for VacationDisplayer
     * @param flightEntry - the flight to display
     */
    public VacationDisplayer(String[] flightEntry){
        setFlight(flightEntry);
    }

    /**
     * sets the flight to show
     * @param flightEntry - the flight to show
     */
    private void setFlight(String[] flightEntry) {
        if (flightEntry == null)
            return;
        flight = flightEntry;
        init();
        getChildren().addAll(vb_systemDetails, hb_flightDetails, hb_additionalFlightDetails, hb_price);
    }

    /**
     * initialize all the fields
     */
    private void init(){
        //-------system details--------
            //flight id part
        HBox hb_fid = getLine("Flight ID", flight[1]);
            //publisher
        HBox hb_publisher = getLine("Published by", flight[2]);

        vb_systemDetails = new VBox();
        vb_systemDetails.getChildren().addAll(hb_fid, hb_publisher);

        //-------flight details--------
        HBox hb_airline = getLine("Airline", flight[3]);
        HBox hb_startDate = getLine("Flight on", flight[4]);
        HBox hb_endDate = getLine("Back on", flight[5]);
        HBox hb_origin = getLine("From", flight[8]);
        HBox hb_destination = getLine("To", flight[13]);

        hb_flightDetails = new HBox();
        hb_flightDetails.getChildren().addAll(hb_airline, hb_startDate, hb_origin, hb_endDate, hb_destination);

        //-------flight details--------
        HBox hb_numOfTickets = getLine("Tickets", flight[7]);
        HBox hb_ticketType = getLine("Type", flight[10]);
        HBox hb_returnIncluded = getLine("Return Included", (flight[9].equals("true")?"Yes":"No"));
        HBox hb_luggage = getLine("Luggage", flight[6]);
        hb_additionalFlightDetails = new HBox();
        hb_additionalFlightDetails.getChildren().addAll(hb_numOfTickets, hb_ticketType, hb_returnIncluded, hb_luggage);
        hb_price = getLine("Price", flight[11]);
    }

    private HBox getLine(String title, String value){
        Label lbl_title = new Label(title+": ");
        lbl_title.setStyle("-fx-fill: BLACK;-fx-font-weight:bold;");
        Label lbl_value = new Label(value+"\t\t");
        HBox hb_ans = new HBox();
        hb_ans.getChildren().addAll(lbl_title, lbl_value);
        return hb_ans;
    }
}

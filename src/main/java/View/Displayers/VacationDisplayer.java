package View.Displayers;

import Flight.FlightEntry;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VacationDisplayer extends VBox {
    private String[] flight; //the flight to show
    private VBox vb_systemDetails; //flight_id + publisher
    private HBox hb_flightDetails; //airline, startDate, endDate, origin, dest
    private HBox hb_additionalFlightDetails; //numOfTickets, type, isReturn, luggage type
    private Label lbl_price;

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
        getChildren().addAll(vb_systemDetails, hb_flightDetails, hb_additionalFlightDetails, lbl_price);
    }

    /**
     * initialize all the fields
     */
    private void init(){
        Label fid = new Label("Flight ID:\t"+flight[0]);
        Label publisher = new Label("Published by:\t"+flight[1]);
        vb_systemDetails = new VBox();
        vb_systemDetails.getChildren().addAll(fid, publisher);
        Label airline = new Label("Airline:\t"+flight[2]);
        Label startDate = new Label("Flight on:\t"+flight[3]);
        Label endDate = new Label("Back on:\t"+flight[4]);
        Label origin = new Label("From:\t"+flight[7]);
        Label destination = new Label("To:\t"+flight[12]);
        hb_flightDetails = new HBox();
        hb_flightDetails.getChildren().addAll(airline, startDate, origin, endDate, destination);
        Label numOfTickets = new Label("Tickets:\t"+flight[6]);
        Label ticketType = new Label("Type:\t"+flight[9]);
        Label returnIncluded = new Label("Return Included:\t"+(flight[8].equals("true")?"Yes":"No"));
        Label luggageType = new Label("Luggage:\t"+flight[5]);
        hb_additionalFlightDetails = new HBox();
        hb_additionalFlightDetails.getChildren().addAll(numOfTickets, ticketType, returnIncluded, luggageType);
        lbl_price = new Label("Price:\t"+flight[10]);
    }
}

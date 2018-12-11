package View.Displayers;

import Flight.FlightEntry;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FlightBoard extends ListView {
    private List<FlightDisplayer> flights = new ArrayList<>();

    /**
     * Constructor for FlightBoard
     * @param flightEntries - a collection of the flights to show
     */
    public FlightBoard(Collection<String[]> flightEntries){
        setFlights(flightEntries);
    }

    /**
     * sets the flights to show
     * @param flightEntries - a collection of flights to show
     */
    private void setFlights(Collection<String[]> flightEntries) {
        if(flightEntries == null)
            return;
        FlightDisplayer current;
        for(String[] flight:flightEntries){
            current = new FlightDisplayer(flight);
            current.setPrefWidth(450);
            flights.add(current);
        }
        init();
    }

    /**
     * initialize all the fields
     */
    private void init(){
        getItems().add(FlightDisplayer.getHeaders());
        getItems().addAll(flights);
    }

    public List<FlightDisplayer> getFlights(){
        return flights;
    }
}

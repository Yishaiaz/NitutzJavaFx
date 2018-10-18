package View;

import Controller.Controller;
import javafx.scene.control.Label;

import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    public String loggedUser;
    private Controller m_controller;

    public void setController(Controller controller) {
        m_controller = controller;
        bindProperties(m_controller);
    }

    private void bindProperties(Controller controller) {
        loggedUser = controller.loggedUser.toString();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == m_controller)
            bindProperties(m_controller);

    }
}

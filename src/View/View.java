package View;

import Controller.Controller;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    public String loggedUser;
    private Controller m_controller;

    //fxml widgets
    public TextField textField_Search;
    public Button btn_StartSearch;
    public Button btn_cleanSearch;
    public Button btn_profile;
    public MenuItem menuItem_create;
    public MenuItem menuItem_update;
    public MenuItem menuItem_delete;

    public void setController(Controller controller) {
        m_controller = controller;
        bindProperties(m_controller);
        btn_StartSearch.setStyle("-fx-background-image: url('/x.png');");
    }

    private void bindProperties(Controller controller) {
        loggedUser = controller.loggedUser.toString();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == m_controller)
            bindProperties(m_controller);

    }

    //onClick functions

    public void onClickSearchUser(){

    }

    public void onClickLogin(){

    }

    public void onClickCreateProfile(){

    }

    public void onClickUpdateProfile(){

    }

    public void onClickDeleteProfile(){

    }

}

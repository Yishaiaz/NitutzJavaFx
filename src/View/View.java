package View;

import Controller.Controller;
import EntriesObject.User;
import View.CreateAcount.CreateAcountControlle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

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

    private void setController(Controller controller) {
        m_controller = controller;
        bindProperties(m_controller);
    }

    private void bindProperties(Controller controller) {
        loggedUser=controller.loggedUser.toString();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == m_controller)
            loggedUser=m_controller.getLoggedUser();

    }

    //onClick functions

    public void onClickSearchUser() {

    }

    public void onClickLogin() throws IOException {
        if(loggedUser==null)
            displayLoginDialog();
        else
            logOutProfileFromDB();
    }

    public void onClickCreateProfile() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CreateAcount/CreateAcount.fxml"));
        AnchorPane create = null;
        try {
            create = loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scene creatAcount = new Scene(create);
        CreateAcountControlle ca = loader.getController();
        ca.setController(m_controller);// sets the controller
        Stage popUp = new Stage();
        popUp.setScene(creatAcount);
        popUp.show();
    }

    public void onClickUpdateProfile() {

    }

    public void onClickDeleteProfile() {

    }

    private void displayLoginDialog() {

        // Custom dialog
        Dialog dialog = new Dialog();
        dialog.setHeaderText("Enter user name and password ");
        dialog.setResizable(true);

        // Widgets
        Label lbl_userName = new Label("User name: ");
        Label lbl_password = new Label("Password: ");
        Label lbl3 = new Label("dont have an account?");
        Label lbl_loginError = new Label("");
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        Button loginBtn = new Button("Login");
        Button btn_createAccount = new Button("Create Account");

        //on click handlers
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            }
        });

        // Create layout and add to dialog
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 35, 20, 35));
        grid.add(lbl_userName, 1, 1); // col=1, row=1
        grid.add(text1, 2, 1);
        grid.add(lbl_password, 1, 2); // col=1, row=2
        grid.add(text2, 2, 2);
        grid.add(loginBtn, 1, 3);
        grid.add(lbl_loginError, 2, 3);
        grid.add(lbl3, 1, 5);
        grid.add(btn_createAccount, 2, 5);
        dialog.getDialogPane().setContent(grid);

        // Add button to dialog
        ButtonType btn_cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(btn_cancel);

        //on click handlers
        /**
         * opens the "createAccount" popup
         */
        btn_createAccount.setOnAction(e -> onClickCreateProfile());

        loginBtn.setOnAction(e -> {
            String userName = text1.getText();
            String password = text2.getText();
            logInProfileToDB(userName, password);
            if (loggedUser != null) {
                btn_profile.setText("Log Out");
                dialog.close();
            } else {
                lbl_loginError.setText("Invalid password or user name");
                text1.clear();
                text2.clear();
            }

        });

        // Show dialog
        dialog.showAndWait();
    }

    /**
     * login the input user to the database by the username and password
     * @param username- the user name to login.
     * @param password- the user password to loin.
     */
    private void logInProfileToDB(String username, String password) {
        String userNameFromDb=m_controller.logIn(username, password);
        if(userNameFromDb!=null)
            loggedUser=userNameFromDb;

    }
    /**
     * logout the current user in the system loggedUser from the database.
     */
    private void logOutProfileFromDB() {
        loggedUser = null;
        btn_profile.setText("login/sign up");
        m_controller.logOut();
    }
}


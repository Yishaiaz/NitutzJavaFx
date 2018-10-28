package View;

import Controller.Controller;
import EntriesObject.IEntry;
import EntriesObject.User;
import View.CreateAcount.CreateAcountControlle;
import View.UpdateProfile.UpdateAccount;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class View implements Observer {
    public String loggedUser = null;
    private Controller m_controller;

    //fxml widgets
    public Label lbl_eror_searchUser;
    public TextField textField_Search;
    public Button btn_StartSearch;
    public Button btn_cleanSearch;
    public Button btn_profile;
    public MenuItem menuItem_create;
    public MenuItem menuItem_update;
    public MenuItem menuItem_delete;

    public void setController(Controller controller) {
        m_controller = controller;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == m_controller){
            loggedUser=m_controller.getLoggedUser();
            if(loggedUser==null)
                btn_profile.setText("login/sign up");
            else
                btn_profile.setText("Log Out");
        }

    }

    //onClick functions

    /**
     * when search is presses - sends the user name to the controller and displays the searched users data from the DB.
     * if the User doesnt exist, an error lable will be displayed.
     */
    public void onClickSearchUser() {
        if (textField_Search.getText().equals("")){
            lbl_eror_searchUser.setText("Must enter User name");
            lbl_eror_searchUser.setVisible(true);
            return;
        }
        String [] data =  m_controller.searchUser(textField_Search.getText());
        if (data==null){
            lbl_eror_searchUser.setText("User does not exist");
            lbl_eror_searchUser.setVisible(true);


        }

        else{
            lbl_eror_searchUser.setVisible(false);
            String message = "User name: "+data[0]+"\n"+"Birthdate: "+ data[2]+"\n"+"more information as needed...(to do)";
            Alert userFoundAlert = new Alert(Alert.AlertType.INFORMATION);
            userFoundAlert.setHeaderText(message);
            userFoundAlert.showAndWait();
        }

    }

    public void onClickLogin() throws IOException {
        if(loggedUser==null)
            displayLoginDialog();
        else
            m_controller.logOut();
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
        if(this.loggedUser!=null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("UpdateProfile/UpdateAccount.fxml"));
            AnchorPane create = null;
            try {
                create = loader.load();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Scene updateAcount = new Scene(create);
            UpdateAccount uA = loader.getController();
            uA.setController(m_controller);// sets the controller
            uA.setParameters();
            Stage popUp = new Stage();
            popUp.setScene(updateAcount);
            popUp.show();
        }
    }

    public void onClickDeleteProfile(){
        if (loggedUser != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Account Validation");
            alert.setContentText("Are you sure you want to delete your accoung?\n By pressing Yes your account will be deleted permanently");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type.getText() == "Yes") {
                    boolean res = m_controller.DeleteAccount();

                    if (!res)
                        DeletionFailed();
                    else{
                        DeletionSucceeded();
                    }
                }
            });
        }
    }

    private void DeletionFailed(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Deletion failed!");
        alert.setContentText("There was a problem while deleting. Please try again");
        alert.show();
    }

    private void DeletionSucceeded(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Deletion Succeeded!");
        alert.setHeaderText("Your account has been deleted successfully.");
        alert.setContentText("You will be crawling back!!!");
        alert.show();
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
        btn_createAccount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dialog.close();
                onClickCreateProfile();
            }
        });

        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String userName = text1.getText();
                String password = text2.getText();
                m_controller.logIn(userName, password);
                if (loggedUser != null) {
                    dialog.close();
                } else {
                    lbl_loginError.setText("Invalid password or user name");
                    text1.clear();
                    text2.clear();
                }

            }
        });

        // Show dialog
        dialog.showAndWait();
    }

    public void onSearchPressed(KeyEvent event){
        if(event!=null && event.getCode().getName().equals("Enter")){
            onClickSearchUser();
        }

//        btn_StartSearch.req

    }
}


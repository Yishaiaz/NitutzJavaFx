package View;

import Controller.Controller;
import Flight.FlightEntry;
import User.MailBox.Message;
import View.CreateAcount.CreateAcountControlle;
import View.Displayers.*;
import View.UpdateProfile.UpdateAccount;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class View implements Observer {
    public String loggedUser = null;
    private Controller m_controller;

    //fxml widgets
    public Label lbl_eror_searchUser;
    public TextField textField_Search;
    public Button btn_StartSearch;
    public Button btn_cleanSearch;
    public Button btn_profile;
    public Button btn_postFlight;
    public Button btn_mailbox;
    public Button btn_flightBoard;
    public MenuItem menuItem_create;
    public MenuItem menuItem_update;
    public MenuItem menuItem_delete;

    public void setController(Controller controller) {
        m_controller = controller;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == m_controller) {
            loggedUser = m_controller.getLoggedUser();
            if (loggedUser == null) {
                btn_profile.setText("login/sign up");
                btn_postFlight.setDisable(true);
                btn_mailbox.setDisable(true);
                btn_mailbox.setVisible(false);
            } else {
                btn_profile.setText("Log Out");
                btn_postFlight.setDisable(false);
                btn_mailbox.setDisable(false);
                btn_mailbox.setVisible(true);
            }
        }

    }

    //onClick functions

    /**
     * when search is presses - sends the user name to the controller and displays the searched users data from the DB.
     * if the User doesnt exist, an error lable will be displayed.
     */
    public void onClickSearchUser() {
        if (textField_Search.getText().equals("")) {
            lbl_eror_searchUser.setText("Must enter User name");
            lbl_eror_searchUser.setVisible(true);
            return;
        }
        String[] data = m_controller.searchUser(textField_Search.getText());
        if (data == null) {
            lbl_eror_searchUser.setText("User does not exist");
            lbl_eror_searchUser.setVisible(true);


        } else {
            lbl_eror_searchUser.setVisible(false);
            String message = "User name: " + data[0] + "\n" + "Birthdate: " + data[2] + "\n" + "more information as needed...(to do)";
            Alert userFoundAlert = new Alert(Alert.AlertType.INFORMATION);
            userFoundAlert.setHeaderText(message);
            userFoundAlert.showAndWait();
        }

    }

    /**
     * logged user clicked on mailbox
     */
    public void onClickMailBox() {
        //init mailbox and displayer
        Collection<Message> usersMessages = m_controller.getUsersMessages();
        MailBoxDisplayer mailBoxDisplayer = new MailBoxDisplayer(usersMessages);
//        VBox checkboxes = new VBox();
//        checkboxes.setPrefWidth(50);

        //set action on mailDisplayers
//        boolean header = true;
        for (MailDisplayer mailDisplayer : mailBoxDisplayer.getMessages()) {
            mailDisplayer.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        onClickMessage(mailDisplayer);
                    }
                }
            });
//            if(!header){
//                checkboxes.getChildren().add(new CheckBox());
//            }
//            else {
//                header = false;
//                CheckBox none = new CheckBox();
//                none.setSelected(false);
//                none.setDisable(true);
//                none.setVisible(false);
//                checkboxes.getChildren().add(none);
//            }
        }

        //opens popup
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        VBox dialogVbox = new VBox(20);
//        HBox dialogHbox = new HBox(20);
//        dialogHbox.getChildren().addAll(checkboxes, mailBoxDisplayer);
        dialogVbox.getChildren().addAll(mailBoxDisplayer);
        Scene dialogScene = new Scene(dialogVbox, 500, 500);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    /**
     * logged user clicked on
     *
     * @param mail
     */
    public void onClickMessage(MailDisplayer mail) {
        //init message displayer;
        MessageDisplayer md = mail.getMessageDisplayer();


        //opens popup
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(md);
        if (md.isTransaction()) {
            String transactionStatus = m_controller.getTransactionStatus(mail.getTransactionID());
            if(transactionStatus != null && transactionStatus.equals("Offer Received")) {
                ButtonBar bb = new ButtonBar();
                Button btn_accept = new Button("Accept");
                Button btn_decline = new Button("Decline");
                btn_accept.setOnAction(event -> {
                    onClickAcceptMessage(mail.getTransactionID());
                    dialog.close();
                });
                btn_decline.setOnAction(event -> {
                    onClickDeclineMessage(mail.getTransactionID());
                    dialog.close();
                });
                bb.getButtons().addAll(btn_accept, btn_decline);
                dialogVbox.getChildren().add(bb);
            }
            else if (transactionStatus != null && transactionStatus.equals("Offer Approved")){
                ButtonBar bb = new ButtonBar();
                Button btn_pay = new Button("Pay");
                Button btn_cancel = new Button("Cancel");
                btn_pay.setOnAction(event -> {
                    showPaymentWindow(mail.getTransactionID());
                    dialog.close();
                });
                btn_cancel.setOnAction(event -> {
                    dialog.close();
                });
                bb.getButtons().addAll(btn_pay, btn_cancel);
                dialogVbox.getChildren().add(bb);
            }
            else if(transactionStatus != null && (transactionStatus.equals("Closed") || transactionStatus.equals("Rejected"))){
                Button btn_ok = new Button("OK");
                btn_ok.setOnAction(event -> {
                    dialog.close();
                });
                dialogVbox.getChildren().add(btn_ok);
            }
        }
        Scene dialogScene = new Scene(dialogVbox, 500, 500);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public void onClickAcceptMessage(String transactionID) {
        m_controller.acceptPurchaseOffer(transactionID);
    }

    public void onClickDeclineMessage(String transactionID) {
        m_controller.declinePurchaseOffer(transactionID);
    }

    public void onClickFlightBoard() {
        //init
        Collection<String[]> flightEntries = m_controller.getFlightBoard();
        FlightBoard flightBoard = new FlightBoard(flightEntries);

        //set action on flightDisplayers;
        for (FlightDisplayer flightDisplayer : flightBoard.getFlights()) {
            flightDisplayer.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        onClickFlightDisplayer(flightDisplayer);
                    }
                }
            });
        }

        //opens popup
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(flightBoard);
        Scene dialogScene = new Scene(dialogVbox, 1000, 500);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public void onClickFlightDisplayer(FlightDisplayer flightDisplayer) {
        //init
        VacationDisplayer vd = flightDisplayer.getVacationDisplayer();

        //opens popup
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(vd);
        ButtonBar bb = new ButtonBar();
        Button btn_purchase = new Button("Purchase");
        Button btn_close = new Button("Close");
        btn_purchase.setOnAction(event -> {
            onClickPurchaseFlight(flightDisplayer.getFlightID());
            dialog.close();
        });
        btn_close.setOnAction(event -> {
            dialog.close();
        });
        if(loggedUser == null){
            btn_purchase.setDisable(true);
            btn_purchase.setText("Unsigned User");
        }
        bb.getButtons().addAll(btn_purchase, btn_close);
        dialogVbox.getChildren().add(bb);
        Scene dialogScene = new Scene(dialogVbox, 1000, 200);
        dialog.setScene(dialogScene);
        dialog.show();

    }

    public void onClickPurchaseFlight(String flightID) {
        m_controller.purchaseFlight(flightID);
    }

    public void onClickLogin() throws IOException {
        if (loggedUser == null)
            displayLoginDialog();
        else
            m_controller.logOut();
    }

    public void onClickCreateProfile() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/CreateAcount/CreateAcount.fxml"));
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
        if (this.loggedUser != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/UpdateProfile/UpdateAccount.fxml"));
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

    public void onClickDeleteProfile() {
        if (loggedUser != null) {
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
                    else {
                        DeletionSucceeded();
                    }
                }
            });
        }
    }

    private void DeletionFailed() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Deletion failed!");
        alert.setContentText("There was a problem while deleting. Please try again");
        alert.show();
    }

    private void DeletionSucceeded() {
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

    public void onSearchPressed(KeyEvent event) {
        if (event != null && event.getCode().getName().equals("Enter")) {
            onClickSearchUser();
        }
    }

    public void postFlightPressed() {

        Dialog dialog = new Dialog();
        dialog.setHeaderText("Pkost your flight");
        dialog.setResizable(true);

        // Widgets
        Label lbl_from = new Label("Departure from: ");
        Label lbl_to = new Label("Arrival at: ");
        Label lbl_depDate = new Label("Departure date: ");
        Label lbl_arrDate = new Label("Arrival date:");
        Label lbl_price = new Label("asked price($):");
        Label lbl_airline = new Label("Air line:");
        Label lbl_luagage = new Label("Luggage type");
        Label lbl_numOfTickets = new Label("number of tickets");
        Label lbl_returnFlight = new Label("return flight included");
        Label lbl_ticketType = new Label("ticket type");

        TextField txt_from = new TextField();
        TextField txt_to = new TextField();
        DatePicker dp_depDate = new DatePicker();
        DatePicker dp_arrDate = new DatePicker();
        setDateFormat(dp_depDate);
        setDateFormat(dp_arrDate);
        TextField txt_price = new TextField();
        TextField txt_airLine = new TextField();
        TextField txt_luagage = new TextField();
        TextField txt_numOfTickets = new TextField();
        CheckBox cb_returnFlight = new CheckBox();
        TextField txt_ticketType = new TextField();
        cb_returnFlight.setSelected(false);


        Button btn_postFlight = new Button("post flight");

        // Create layout and add to dialog
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 35, 20, 35));
        grid.add(lbl_from, 1, 1); // col=1, row=1
        grid.add(txt_from, 2, 1);
        grid.add(lbl_to, 1, 2); // col=1, row=2
        grid.add(txt_to, 2, 2);
        grid.add(lbl_depDate, 1, 3);
        grid.add(dp_depDate, 2, 3);
        grid.add(lbl_arrDate, 1, 4);
        grid.add(dp_arrDate, 2, 4);
        grid.add(lbl_numOfTickets, 1, 5);
        grid.add(txt_numOfTickets, 2, 5);
        grid.add(lbl_airline, 1, 6);
        grid.add(txt_airLine, 2, 6);
        grid.add(lbl_price, 1, 7);
        grid.add(txt_price, 2, 7);
        grid.add(lbl_luagage, 1, 8);
        grid.add(txt_luagage, 2, 8);
        grid.add(lbl_returnFlight, 1, 9);
        grid.add(cb_returnFlight, 2, 9);
        grid.add(lbl_ticketType, 1, 10);
        grid.add(txt_ticketType, 2, 10);

        grid.add(btn_postFlight, 2, 11);
        dialog.getDialogPane().setContent(grid);

        // Add button to dialog
        ButtonType btn_cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(btn_cancel);

        //on click handlers
        /**
         * opens the "createAccount" popup
         */
        btn_postFlight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (txt_from.getText().equals("") || txt_to.getText().equals("") || dp_depDate.getEditor().getText().equals("") || dp_arrDate.getEditor().getText().equals("") || txt_price.getText().equals("") || txt_airLine.getText().equals("") || txt_luagage.getText().equals("")
                        || txt_numOfTickets.getText().equals("") || txt_ticketType.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("error");
                    alert.setContentText("one or more of the fields was not filled correctly");
                    alert.showAndWait();
                    return;
                }
                try {
                    Integer.valueOf(txt_numOfTickets.getText());
                    Double.valueOf(txt_price.getText());

                } catch (Exception ee) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("number error");
                    alert.setContentText("number of tickets or price not compatible numbers");
                    alert.showAndWait();
                    return;
                }
                Map fields = new HashMap<String, String>();

                String returnFlightIncluded = "false";
                if (cb_returnFlight.isSelected())
                    returnFlightIncluded = "true";

                fields.put("from", txt_from.getText());
                fields.put("to", txt_to.getText());
                fields.put("depDate", changeDateFormat(dp_depDate.getEditor().getText()));
                fields.put("arrDate", changeDateFormat(dp_arrDate.getEditor().getText()));
                fields.put("price", txt_price.getText());
                fields.put("airline", txt_airLine.getText());
                fields.put("luggage", txt_luagage.getText());
                fields.put("numOfTickets", txt_numOfTickets.getText());
                fields.put("ticketType", txt_ticketType.getText());
                fields.put("returnFlight", returnFlightIncluded);//cb_returnFlight.isSelected());
                dialog.close();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                if (m_controller.postVacation(fields)) {

                    alert.setHeaderText("success");
                    alert.setContentText("your vacation has been posted");
                    dialog.close();
                    alert.showAndWait();
                } else {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("you post was not posted.. sorry");
                    alert.showAndWait();
                }

            }
        });
        dialog.showAndWait();
    }

    private String changeDateFormat(String text) {
        String[] s = text.split("/");
        return s[2] + "-" + s[1] + "-" + s[0];
    }

    /**
     * this function configures the DatePicker button to return the date in the following format:
     * "DD/MM/yyyy
     */
    private void setDateFormat(DatePicker dp) {
        dp.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
    }

    public void showPaymentWindow(String transactionID){
        Dialog dialog = new Dialog();
        dialog.setHeaderText("Payment");
        dialog.setResizable(true);

        // Widgets
        Label lbl_cardNumber = new Label("Card number: ");
        Label lbl_expDate = new Label("exp Date(mm/yyyy):");
        Label lbl_csv = new Label("csv:(3 dig)");
        Label lbl_ownerName = new Label("Owner Full Name: ");
        Label lbl_numOfPayments = new Label("number of payments:");

        TextField txt_cardNumber = new TextField();
        TextField txt_expDAte = new TextField();
        TextField txt_csv = new TextField();
        TextField txt_ownerName = new TextField();
        TextField txt_payments = new TextField();

        txt_cardNumber.setPromptText("(16 digits)");
        txt_expDAte.setPromptText("(MM/YYYY)");
        txt_csv.setPromptText(("(3 dig)"));
        txt_payments.setPromptText("(1-12");

        Button btn_pay = new Button("Confirm Payment");
        Button btn_yaniv = new Button("Auto fill");


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 35, 20, 35));
        grid.add(lbl_cardNumber, 1, 1);
        grid.add(txt_cardNumber,2,1);
        grid.add(lbl_expDate, 1, 2);
        grid.add(txt_expDAte,2,2);
        grid.add(lbl_csv, 1, 3);
        grid.add(txt_csv,2,3);
        grid.add(lbl_ownerName, 1, 4);
        grid.add(txt_ownerName, 2, 4);
        grid.add(lbl_numOfPayments, 1, 5);
        grid.add(txt_payments,2,5);

        grid.add(btn_pay, 2, 6);
        grid.add(btn_yaniv,1,6);
        dialog.getDialogPane().setContent(grid);

        // Add button to dialog
        ButtonType btn_cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(btn_cancel);


        btn_yaniv.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                txt_cardNumber.setText("1234567891234567");
                txt_expDAte.setText("12/2020");
                txt_csv.setText(("123"));
                txt_payments.setText("1");

            }
        });

        btn_pay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("INPUT ERROR");
                if (txt_cardNumber.getText().length() != 16 || !isNumber(txt_cardNumber.getText())){

                    alert.setContentText("card number must be 16 digits long");
                    alert.showAndWait();
                    return;
                }
                if(!legalExpDate(txt_expDAte.getText())){
                    alert.setContentText("illegal expiration date  (MM/YY)");
                    alert.showAndWait();
                    return;
                }
                if(txt_csv.getText().length() != 3 || !isNumber(txt_csv.getText())){
                    alert.setContentText("illegal csv  (123)");
                    alert.showAndWait();
                    return;
                }
                if(txt_ownerName.getText() == null || txt_ownerName.getText().length()==0 || txt_ownerName.getText().split(" ").length <2){
                    alert.setContentText("please type owner full name");
                }
                if(!validPayAmount(txt_payments.getText())){
                    alert.setContentText("illegal payments  (number: 1-12)");
                    alert.showAndWait();
                    return;
                }
                m_controller.paymentAccepted(transactionID, txt_cardNumber.getText(),txt_expDAte.getText().substring(3), txt_expDAte.getText().substring(0,2), txt_csv.getText(),txt_payments.getText(), txt_ownerName.getText() );
                dialog.close();

            }

            private boolean validPayAmount(String text) {
                try{
                    if(Integer.valueOf(text) <=12 && Integer.valueOf(text) >= 0)
                        return true;

                }catch (Exception e){
                    return false;
                }
                return false;
            }

            private boolean legalExpDate(String text) {
                if(text.length()!= 7 || text.charAt(2) != '/' || !isNumber(text.substring(0,2)) || !isNumber(text.substring(text.length()-4)))
                    return false;
                return true;
            }

            private boolean isNumber(String text) {
                for (int i = 0; i < text.length(); i++){
                    if(text.charAt(i) < '0' || text.charAt(i) > '9')
                        return false;
                }
                return true;
            }

        });

        dialog.showAndWait();
    }

}


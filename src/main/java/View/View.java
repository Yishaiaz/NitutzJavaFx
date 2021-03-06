package View;

import Controller.Controller;
import User.MailBox.Message;
import View.CreateAcount.CreateAcountControlle;
import View.Displayers.*;
import View.UpdateProfile.UpdateAccount;
import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    /**
     * setter for the controller
     *
     * @param controller - the controller
     */
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
     * logged user clicked on the message to open
     *
     * @param mail - the mail to open as a message
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
            if (transactionStatus != null && transactionStatus.equals("Offer Received")) {
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
            } else if (transactionStatus != null && transactionStatus.equals("Offer Approved")) {
                ButtonBar bb = new ButtonBar();
                Button btn_IPaid = new Button("I have Paid");
                Button btn_cancel = new Button("Cancel");
                btn_IPaid.setOnAction(event -> {
                    m_controller.paymentAccepted(mail.getTransactionID());
                    dialog.close();
                });
                btn_cancel.setOnAction(event -> {
                    dialog.close();
                });
                bb.getButtons().addAll(btn_IPaid, btn_cancel);
                dialogVbox.getChildren().add(bb);
            } else if (transactionStatus != null && transactionStatus.equals("user has paid")){
                ButtonBar bb = new ButtonBar();
                Button btn_paymentReceived = new Button("I Received Payment");
                Button btn_paymentNotReceived = new Button("I Have Not Received Payment");
                btn_paymentReceived.setOnAction(event -> {
                    m_controller.paymentReceived(mail.getTransactionID());
                    dialog.close();
                });
                btn_paymentNotReceived.setOnAction(event -> {
                    dialog.close();
                });
                bb.getButtons().addAll(btn_paymentReceived, btn_paymentNotReceived);
                dialogVbox.getChildren().add(bb);
            }
            else if (transactionStatus != null && (transactionStatus.equals("Closed") || transactionStatus.equals("Rejected"))) {
                Button btn_ok = new Button("OK");
                btn_ok.setOnAction(event -> {
                    dialog.close();
                });
                dialogVbox.getChildren().add(btn_ok);
            }
        } else if (md.isSwap()) {
            String swapStatus = m_controller.getSwapStatus(mail.getSwapID());
            ButtonBar bb = new ButtonBar();
            if (swapStatus != null && swapStatus.equals("Offer Received")) {
                Button btn_accept = new Button("Accept");
                Button btn_decline = new Button("Decline");
                btn_accept.setOnAction(event -> {
                    onClickAcceptSwap(mail.getSwapID());
                    dialog.close();
                });
                btn_decline.setOnAction(event -> {
                    onClickDeclineSwap(mail.getSwapID());
                    dialog.close();
                });
                bb.getButtons().addAll(btn_accept, btn_decline);
            } else {
                Button btn_ok = new Button("OK");
                btn_ok.setOnAction(event -> {
                    dialog.close();
                });
                bb.getButtons().addAll(btn_ok);
            }
                dialogVbox.getChildren().add(bb);
        }
        Scene dialogScene = new Scene(dialogVbox, 500, 500);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    /**
     * user clicked on accept message
     *
     * @param transactionID - the transactionID of the message
     */
    public void onClickAcceptMessage(String transactionID) {
        m_controller.acceptPurchaseOffer(transactionID);
    }

    /**
     * user clicked on decline message
     *
     * @param transactionID - the transactionID of the message
     */
    public void onClickDeclineMessage(String transactionID) {
        m_controller.declinePurchaseOffer(transactionID);
    }

    public void onClickAcceptSwap(String swapID) {
        m_controller.acceptSwapOffer(swapID);
    }

    public void onClickDeclineSwap(String swapID) {
        m_controller.declineSwapOffer(swapID);
    }

    /**
     * user clicked on flight board
     */
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

    /**
     * user clicked on flight displayer to open the flight
     *
     * @param flightDisplayer - the flight to open as vacationDisplayer
     */
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
        Button btn_swapVacations = new Button("Swap Vacations");
        Button btn_close = new Button("Close");

        btn_swapVacations.setOnAction(event -> {
            onClickedSwapVacations(flightDisplayer);
            dialog.close();

        });

        btn_purchase.setOnAction(event -> {
            onClickPurchaseFlight(flightDisplayer.getFlightID());
            dialog.close();
        });
        btn_close.setOnAction(event -> {
            dialog.close();
        });
        if (loggedUser == null) {
            btn_purchase.setDisable(true);
            btn_purchase.setText("Unsigned User");
            btn_swapVacations.setDisable(true);
            btn_swapVacations.setText("Unsigned user");
        }
        bb.getButtons().addAll(btn_purchase, btn_swapVacations, btn_close);
        dialogVbox.getChildren().add(bb);
        Scene dialogScene = new Scene(dialogVbox, 1000, 200);
        dialog.setScene(dialogScene);
        dialog.show();

    }

    private void onClickedSwapVacations(FlightDisplayer ogFlight) {
        if (ogFlight.getPublisher().equals(loggedUser)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Swaping vacations with yourself is useless.");
            alert.showAndWait();
            return;
        }

        //init
        Collection<String[]> flightEntries = m_controller.getUsersFlights();
        FlightBoard flightBoard = new FlightBoard(flightEntries);


        //opens popup
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.setTitle("Please choose a vacation to swap");

        //set action on flightDisplayers;
        for (FlightDisplayer flightDisplayer : flightBoard.getFlights()) {
            flightDisplayer.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        m_controller.requestSwapTransaction(ogFlight.getFlightID(), flightDisplayer.getFlightID());
                        dialog.close();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Swap request has been sent");
                        alert.setHeaderText("Your request has been sent to the seller");
                        alert.setContentText("A request message has been sent to the seller." +
                                System.getProperty("line.separator") + "please wait for his/her response");
                        alert.showAndWait();
                    }
                }
            });
        }

        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(flightBoard);
        Scene dialogScene = new Scene(dialogVbox, 1000, 500);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    /**
     * user clicked on purchase flight
     *
     * @param flightID
     */
    public void onClickPurchaseFlight(String flightID) {
        try {
            m_controller.purchaseFlight(flightID);
        } catch (Exception e) {
            System.out.println("you cant do that");
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("ILEGAL ACTION");
            a.setContentText("you have tried to purchase your posted vacation!\n" +
                    " this is not allowed and because of people like you, we lost 4 points on assignment 3!");
            a.showAndWait();
        }
    }

    /**
     * log in clicked
     *
     * @throws IOException
     */
    public void onClickLogin() throws IOException {
        if (loggedUser == null)
            displayLoginDialog();
        else
            m_controller.logOut();
    }

    /**
     * create profile clicked
     */
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

    /**
     * update the profile
     */
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

    /**
     * deleting the profile
     */
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

    /**
     * search pressed
     *
     * @param event
     */
    public void onSearchPressed(KeyEvent event) {
        if (event != null && event.getCode().getName().equals("Enter")) {
            onClickSearchUser();
        }
    }

    /**
     * post flight pressed
     */
    public void postFlightPressed() {
        Dialog dialog = new Dialog();
        dialog.setHeaderText("Post your flight");
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

                boolean validDates = false;
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date dateobj = new java.util.Date();
                String presentDate = df.format(dateobj);
                if (validateDates(dp_depDate.getEditor().getText(), dp_arrDate.getEditor().getText()) && validateDates(presentDate, dp_depDate.getEditor().getText()))
                    validDates = true;

                if (!validDates || txt_from.getText().equals("") || txt_to.getText().equals("") || dp_depDate.getEditor().getText().equals("") || dp_arrDate.getEditor().getText().equals("") || txt_price.getText().equals("") || txt_airLine.getText().equals("") || txt_luagage.getText().equals("")
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

            private boolean validateDates(String small, String big) {
                if (small == null || big == null || small.equals("") || big.equals(""))
                    return false;
                try {
                    if (Integer.valueOf(small.split("/")[2]) > Integer.valueOf(big.split("/")[2]))
                        return false;
                    if (Integer.valueOf(small.split("/")[1]) > Integer.valueOf(big.split("/")[1]))
                        return false;
                    if (Integer.valueOf(small.split("/")[0]) > Integer.valueOf(big.split("/")[0]))
                        return false;
                } catch (Exception e) {
                    return false;
                }
                return true;
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
        dp.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });
    }

}


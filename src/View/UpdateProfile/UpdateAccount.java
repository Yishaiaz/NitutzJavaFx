package View.UpdateProfile;

import Controller.Controller;
import EntriesObject.IEntry;
import EntriesObject.User;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;

public class UpdateAccount {

    private IEntry user;
    //the main controller
    private Controller m_controller;

    //buttons
    public Button btn_cancel;
    public Button btn_updateAccount;

    //fields
    public PasswordField fld_password;
    public PasswordField fld_verifyPassword;
    public TextField fld_firstName;
    public TextField fld_lastName;
    public TextField fld_city;
    public DatePicker fld_birthDate;

    //error labels
    public Label lbl_error_password;
    public Label lbl_error_verifyPassword;
    public Label lbl_error_firstName;
    public Label lbl_error_lastName;
    public Label lbl_error_city;
    public Label lbl_error_birthdate;


    /**
     * sets the controller
     * @param controller - the controler of the program
     */
    public void setController(Controller controller) {
        this.m_controller = controller;
    }
    public void setParameters(User user){
        this.user=user;
        this.fld_firstName.setText(user.getUser_firstname());
        this.fld_lastName.setText(user.getUser_lastname());
        this.fld_city.setText(user.getUser_city());
    }

    /**
     * Activated when cancel is pressed.
     * cancels the update account popup.
     */
    public void cancel(){

        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Activated when "creat acount is pressed
     * validates all the information from the user and notifys the user what the problem is.
     * will close the popup screen only if the user has been successfully added to the DB.
     */
    public void updateAccount(){
        if(checkPassword() && validatePassword() && checkFirstNAme() && checkLastName() && checkCity()&& checkDate()){
            // change the dat format
            String correctDateFormat = changeDateFormat(fld_birthDate.getEditor().getText());
            //adds user to DB
            try{
                User user=new User(this.user.getAllData()[0],
                        this.fld_password.getText(),
                        Date.valueOf(correctDateFormat),
                        this.fld_firstName.getText(),
                        this.fld_lastName.getText(),
                        this.fld_city.getText());
                this.user=user;
                m_controller.updateUser((User)this.user);
            }catch(Exception e){
                System.out.println("something went wrong");
            }
            //close popup
            Stage stage = (Stage) btn_cancel.getScene().getWindow();
            stage.close();
        }
    }

    //<editor-fold desc="User Input Validation">
    /**
     * checks that the date is not empty.
     * @return true if user has selected a date
     */
    private boolean checkDate() {
        if (fld_birthDate.getEditor().getText().equals("")) {
            lbl_error_birthdate.setText("must enter birth date");
            lbl_error_birthdate.setVisible(true);
            return false;
        }
        else {
            lbl_error_birthdate.setVisible(false);
        }
        return true;
    }

    /**
     * changes the date format from "10,11,2012" to "2012-11-10".
     * @param text - A string such as "10,11,2012"
     * @return a String sucj as "2012-11-10"
     */
    private String changeDateFormat(String text) {
        String [] s = text.split("/");
        return s[2]+"-"+s[0]+"-"+s[1];
    }

    /**
     * checks that the city is not empty
     * @return true if the user has inserted a city
     */
    private boolean checkCity() {
        if (fld_city.getText().equals("")) {
            lbl_error_city.setText("must enter city");
            lbl_error_city.setVisible(true);
            return false;
        }
        else lbl_error_city.setVisible(false);
        return true;
    }

    /**
     * checks that the First Name is not empty
     * @return true if the user has inserted a Last Name
     */
    private boolean checkLastName() {
        if (fld_lastName.getText().equals("")) {
            lbl_error_lastName.setText("must enter last name");
            lbl_error_lastName.setVisible(true);
            return false;
        }
        else lbl_error_lastName.setVisible(false);
        return true;
    }

    /**
     * checks that the First Name is not empty
     * @return true if the user has inserted a First Name
     */
    private boolean checkFirstNAme() {
        if (fld_firstName.getText().equals("")) {
            lbl_error_firstName.setText("must enter first name");
            lbl_error_firstName.setVisible(true);
            return false;
        }
        else lbl_error_firstName.setVisible(false);
        return true;

    }

    /**
     * checks that the password verification is correct
     * @return true if both field are identical
     */
    private boolean validatePassword() {
        if (!fld_password.getText().equals(fld_verifyPassword.getText())) {
            lbl_error_verifyPassword.setText("passwords dont match");
            lbl_error_verifyPassword.setVisible(true);
            return false;
        }
        else lbl_error_verifyPassword.setVisible(false);
        return true;
    }

    /**
     * checks that the password is not empty
     * @return - true if the user entered a password
     */
    private boolean checkPassword() {
        if (fld_password.getText().equals("")) {
            lbl_error_password.setText("must enter a password");
            lbl_error_password.setVisible(true);
            return false;
        }
        else  lbl_error_password.setVisible(false);
        return true;
    }


}

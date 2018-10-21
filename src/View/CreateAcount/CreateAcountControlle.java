package View.CreateAcount;

import Controller.Controller;
import Model.Model;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CreateAcountControlle{


        //the main controller
        private Controller m_controller;

        //buttons
        public Button btn_cancel;
        public Button btn_creatAcount;

        //fields
        public TextField fld_userName;
        public PasswordField fld_password;
        public PasswordField fld_verifyPassword;
        public TextField fld_firstName;
        public TextField fld_lastName;
        public TextField fld_city;
        public DatePicker fld_birthDate;

        //error labels
        public Label lbl_error_userName;
        public Label lbl_error_password;
        public Label lbl_error_verifyPassword;
        public Label lbl_error_firstName;
        public Label lbl_error_lastName;
        public Label lbl_error_city;
        public Label lbl_error_biirthdate;


        /**
         * Activated when cancel is pressed.
         * cancels the create account popup.
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
        public void createAcount(){

                boolean goodUserName = checkUserNAme();
                boolean goodPassword =checkPassword();
                boolean goodVerifyPassword=validatePassword();
                boolean goodfirstName = checkFirstNAme();
                boolean goodLastName =checkLastName();
                boolean goodCity =checkCity();
                boolean goodDate=checkDate();
                
                if(goodUserName && goodPassword && goodVerifyPassword && goodfirstName && goodLastName && goodCity && goodDate){
                        // change the dat format
                        String correctDateFormat = changeDateFormat(fld_birthDate.getEditor().getText());
                        //adds user to DB
                        m_controller.addUser(fld_userName.getText(),fld_password.getText(),correctDateFormat,fld_firstName.getText(),fld_lastName.getText(),fld_city.getText());

                        //close popup
                        Stage stage = (Stage) btn_cancel.getScene().getWindow();
                        stage.close();
                }
        }

        /**
         * checks that the date is not empty.
         * @return true if user has selected a date
         */
        private boolean checkDate() {
                if (fld_birthDate.getEditor().getText().equals("")) {
                        lbl_error_biirthdate.setText("must enter birth date");
                        lbl_error_biirthdate.setVisible(true);
                        return false;
                }
                else {
                        lbl_error_biirthdate.setVisible(false);
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
                return s[2]+"-"+s[1]+"-"+s[0];
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


        /**
         * checks that the user has entered a user name and that the userName does not exist in the DB
         * @return true if the user name is not empty nd the usr oes not exist in the DB.
         */
        private boolean checkUserNAme() {
                if (fld_userName.getText().equals("")) {
                        lbl_error_userName.setText("Enter User Name");
                        lbl_error_userName.setVisible(true);
                        return false;
                }
                else  lbl_error_userName.setVisible(false);
                if(m_controller.checkIfUserExists(fld_userName.getText())){
                        lbl_error_userName.setText("this user name allready exists");
                        lbl_error_userName.setVisible(true);
                        return false;
                }
                return true;

                ///check if user exists
        }

        /**
         * sets the controller
         * @param controller - the controler of the program
         */
        public void setController(Controller controller) {
                this.m_controller = controller;
        }
}

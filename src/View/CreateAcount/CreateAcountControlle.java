package View.CreateAcount;

import Controller.Controller;
import Model.Model;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CreateAcountControlle{


        private Controller m_controller;

        public Button btn_cancel;
        public Button btn_creatAcount;

        public TextField fld_userName;
        public PasswordField fld_password;
        public PasswordField fld_verifyPassword;
        public TextField fld_firstName;
        public TextField fld_lastName;
        public TextField fld_city;
        public DatePicker fld_birthDate;

        public Label lbl_error_userName;
        public Label lbl_error_password;
        public Label lbl_error_verifyPassword;
        public Label lbl_error_firstName;
        public Label lbl_error_lastName;
        public Label lbl_error_city;
        public Label lbl_error_biirthdate;



        public void cancel(){

                Stage stage = (Stage) btn_cancel.getScene().getWindow();


                stage.close();
        }

        public void createAcount(){
                checkUserNAme();
                checkPassword();
                validatePassword();
                checkFirstNAme();
                checkLastName();
                checkCity();
                checkDate();

                if(checkUserNAme()&&checkPassword()&&validatePassword()&&checkFirstNAme()&& checkLastName() && checkCity()&& checkDate()){
                        


                        fld_birthDate.getEditor().getText();
                        Stage stage = (Stage) btn_cancel.getScene().getWindow();
                        stage.close();
                }






        }

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

        private String changeDateFormat(String text) {
                String [] s = text.split("/");
                return s[2]+"-"+s[1]+"-"+s[0];
        }

        private boolean checkCity() {
                if (fld_city.getText().equals("")) {
                        lbl_error_city.setText("must enter city");
                        lbl_error_city.setVisible(true);
                        return false;
                }
                else lbl_error_city.setVisible(false);
                return true;
        }

        private boolean checkLastName() {
                if (fld_lastName.getText().equals("")) {
                        lbl_error_lastName.setText("must enter last name");
                        lbl_error_lastName.setVisible(true);
                        return false;
                }
                else lbl_error_lastName.setVisible(false);
                return true;
        }

        private boolean checkFirstNAme() {
                if (fld_firstName.getText().equals("")) {
                        lbl_error_firstName.setText("must enter first name");
                        lbl_error_firstName.setVisible(true);
                        return false;
                }
                else lbl_error_firstName.setVisible(false);
                return true;

        }

        private boolean validatePassword() {
                if (!fld_password.getText().equals(fld_verifyPassword.getText())) {
                        lbl_error_verifyPassword.setText("passwords dont match");
                        lbl_error_verifyPassword.setVisible(true);
                        return false;
                }
                else lbl_error_verifyPassword.setVisible(false);
                return true;
        }

        private boolean checkPassword() {
                if (fld_password.getText().equals("")) {
                        lbl_error_password.setText("must enter a password");
                        lbl_error_password.setVisible(true);
                        return false;
                }
                else  lbl_error_password.setVisible(false);
                return true;
        }


        private boolean checkUserNAme() {
                if (fld_userName.getText().equals("")) {
                        lbl_error_userName.setText("Enter User Name");
                        lbl_error_userName.setVisible(true);
                        return false;
                }
                else  lbl_error_userName.setVisible(false);
                return true;

                ///check if user exists
        }

        public void setController(Controller controller) {
                this.m_controller = controller;
        }
}

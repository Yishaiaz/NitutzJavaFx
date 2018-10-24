package View;

import Controller.Controller;
import DataBaseConnection.SqliteDbConnection;
import Model.Model;
import View.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Model model = new Model(new SqliteDbConnection(true));
            Controller controller = new Controller(model);
            model.addObserver(controller);

            //--------------
            primaryStage.setTitle("Vacation4U");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("View.fxml").openStream());
            Scene scene = new Scene(root, 800, 650);

            scene.getStylesheets().clear();
//            scene.getStylesheets().add(getClass().getResource("mainScreenStyle.css").toExternalForm());
            primaryStage.setScene(scene);
            //--------------
            View view = fxmlLoader.getController();
            view.setController(controller);
            controller.addObserver(view);
            //--------------
            primaryStage.show();
        }
        catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
            System.out.println("Zabary did it");
            return;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}

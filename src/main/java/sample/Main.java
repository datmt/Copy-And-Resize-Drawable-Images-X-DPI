package sample;

import com.aquafx_project.AquaFx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui.fxml"));
        primaryStage.setTitle("Copy and resize your images to x-dpi folders");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        AquaFx.style();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

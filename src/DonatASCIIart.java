import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DonatASCIIart extends Application{
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("gui/donatGui.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("ASCII Donat Art");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

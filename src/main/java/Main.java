import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * SpotyCheck queries the Spotify API for UPCs and other pieces of information.
 *
 * @author  lunax28
 * @version 1.0
 * @since   2017-09-07
 */

public class Main extends Application {

    Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("spotyCheckGui.fxml"));
        window.setTitle("Spoty Check");
        window.setScene(new Scene(root));
        window.show();
    }

    public Stage getWindow() {
        return window;
    }
}

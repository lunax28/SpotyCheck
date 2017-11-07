import com.equilibriummusicgroup.SpotyCheck.model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * SpotyCheck queries the Spotify API to check UPCs and retrieve Artists' information
 *
 * @author  lunax28
 * @version 1.0
 * @since   2017-09-07
 */

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("spotyCheckGui.fxml")) ;

            BorderPane root = (BorderPane)loader.load();
            SpotyCheckController controller = loader.getController();
            Model model = new Model();
            controller.setModel(model) ;

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}

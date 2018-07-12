/**
 * Sample Skeleton for 'SpotyCheckHome.fxml' Controller Class
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.equilibriummusicgroup.SpotyCheck.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SpotyCheckHomeController {

    @FXML
    private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="albumsButton"
    private Button albumsButton; // Value injected by FXMLLoader

    @FXML // fx:id="artistsButton"
    private Button artistsButton; // Value injected by FXMLLoader

    @FXML // fx:id="upcButton"
    private Button upcButton; // Value injected by FXMLLoader

    @FXML
    void changeAlbumsScene(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("spotyCheckAlbums.fxml"));

        Parent root = loader.load();
        SpotyCheckAlbumController controller = loader.getController();
        controller.setModel(this.model);

        Stage stage = (Stage) albumsButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void changeArtistsScene(ActionEvent event) {

    }

    @FXML
    void changeLookupScene(ActionEvent event) {

    }

    public void setModel(Model model) {
        this.model = model;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert albumsButton != null : "fx:id=\"albumsButton\" was not injected: check your FXML file 'SpotyCheckHome.fxml'.";
        assert artistsButton != null : "fx:id=\"artistsButton\" was not injected: check your FXML file 'SpotyCheckHome.fxml'.";
        assert upcButton != null : "fx:id=\"upcButton\" was not injected: check your FXML file 'SpotyCheckHome.fxml'.";

    }
}

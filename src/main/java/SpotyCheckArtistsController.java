/**
 * Sample Skeleton for 'spotyCheckArtists.fxml' Controller Class
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SpotyCheckArtistsController {

    @FXML
    private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="artistNameSearchTextField"
    private TextField artistNameSearchTextField; // Value injected by FXMLLoader

    @FXML // fx:id="checkArtistButton"
    private Button checkArtistButton; // Value injected by FXMLLoader

    @FXML // fx:id="idSearchTextField"
    private TextField idSearchTextField; // Value injected by FXMLLoader

    @FXML // fx:id="nameTextField"
    private TextField nameTextField; // Value injected by FXMLLoader

    @FXML // fx:id="followersTextField"
    private TextField followersTextField; // Value injected by FXMLLoader

    @FXML // fx:id="popularityTextField"
    private TextField popularityTextField; // Value injected by FXMLLoader

    @FXML // fx:id="uriTextField"
    private TextField uriTextField; // Value injected by FXMLLoader

    @FXML // fx:id="urlTextField"
    private TextField urlTextField; // Value injected by FXMLLoader

    @FXML // fx:id="backHomeButton"
    private Button backHomeButton; // Value injected by FXMLLoader

    @FXML
    void backHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spotyCheckHome.fxml"));

        Parent root = loader.load();
        SpotyCheckHomeController controller = loader.getController();
        controller.setModel(this.model);

        Stage stage = (Stage) backHomeButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void checkArtist(ActionEvent event) {

    }

    public void setModel(Model model) {
        this.model = model;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert artistNameSearchTextField != null : "fx:id=\"artistNameSearchTextField\" was not injected: check your FXML file 'spotyCheckArtists.fxml'.";
        assert checkArtistButton != null : "fx:id=\"checkArtistButton\" was not injected: check your FXML file 'spotyCheckArtists.fxml'.";
        assert idSearchTextField != null : "fx:id=\"idSearchTextField\" was not injected: check your FXML file 'spotyCheckArtists.fxml'.";
        assert nameTextField != null : "fx:id=\"nameTextField\" was not injected: check your FXML file 'spotyCheckArtists.fxml'.";
        assert followersTextField != null : "fx:id=\"followersTextField\" was not injected: check your FXML file 'spotyCheckArtists.fxml'.";
        assert popularityTextField != null : "fx:id=\"popularityTextField\" was not injected: check your FXML file 'spotyCheckArtists.fxml'.";
        assert uriTextField != null : "fx:id=\"uriTextField\" was not injected: check your FXML file 'spotyCheckArtists.fxml'.";
        assert urlTextField != null : "fx:id=\"urlTextField\" was not injected: check your FXML file 'spotyCheckArtists.fxml'.";
        assert backHomeButton != null : "fx:id=\"backHomeButton\" was not injected: check your FXML file 'spotyCheckArtists.fxml'.";

    }
}

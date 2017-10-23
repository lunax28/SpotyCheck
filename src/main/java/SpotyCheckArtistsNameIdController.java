/**
 * Sample Skeleton for 'spotyCheckArtistsNameId.fxml' Controller Class
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SpotyCheckArtistsNameIdController {

    @FXML
    private Button backButtonId;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="outputTextField"
    private TextField outputTextField; // Value injected by FXMLLoader

    @FXML // fx:id="nameTextArea"
    private TextArea nameTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="resultsTextArea"
    private TextArea resultsTextArea; // Value injected by FXMLLoader

    @FXML
    void checkNameButton(ActionEvent event) {

    }

    @FXML
    void clearButton(ActionEvent event) {

    }

    @FXML
    void backButton(ActionEvent event) throws IOException {

        backButtonId.getScene().setRoot(FXMLLoader.load(getClass().getResource("spotyCheckGui.fxml")));

    }



    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert outputTextField != null : "fx:id=\"outputTextField\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert nameTextArea != null : "fx:id=\"nameTextArea\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert resultsTextArea != null : "fx:id=\"resultsTextArea\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";

    }
}

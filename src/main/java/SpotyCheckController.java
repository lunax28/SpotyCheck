/**
 * Sample Skeleton for 'spotyCheckGui.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;

import com.equilibriummusicgroup.SpotyCheck.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SpotyCheckController {

    private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="outputTextField"
    private TextField outputTextField; // Value injected by FXMLLoader

    @FXML // fx:id="upcTextArea"
    private TextArea upcTextArea; // Value injected by FXMLLoader

    @FXML
    void checkButton(ActionEvent event) {
        if(this.outputTextField.getText().isEmpty()){
            displayErrorMessage("Make sure to add an output destination!");
        }
        if(this.upcTextArea.getText().isEmpty()){
            displayErrorMessage("Make sure to add a list of UPCs first!");
        }
    }

    @FXML
    void getInfoButton(ActionEvent event) {

    }

    @FXML
    void selectButton(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert outputTextField != null : "fx:id=\"outputTextField\" was not injected: check your FXML file 'spotyCheckGui.fxml'.";
        assert upcTextArea != null : "fx:id=\"upcTextArea\" was not injected: check your FXML file 'spotyCheckGui.fxml'.";

    }

    public void setModel(Model model){
        this.model = model;
    }

    public void displayErrorMessage(String textMessage){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(textMessage);
        alert.showAndWait();
        return;

    }
}

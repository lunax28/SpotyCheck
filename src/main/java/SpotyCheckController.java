/**
 * Sample Skeleton for 'spotyCheckGui.fxml' Controller Class
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.equilibriummusicgroup.SpotyCheck.model.ApiQueryUtil;
import com.equilibriummusicgroup.SpotyCheck.model.Model;
import com.equilibriummusicgroup.SpotyCheck.model.ModelSing;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class SpotyCheckController {
    @FXML
    private Model model;

    @FXML
    private Button nextButtonId;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="outputTextField"
    private TextField outputTextField; // Value injected by FXMLLoader

    @FXML // fx:id="upcTextArea"
    private TextArea upcTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="resultsTextArea"
    private TextArea resultsTextArea; // Value injected by FXMLLoader

    private Scanner scanner;



    @FXML
    void checkButton(ActionEvent event) {
/*        if(this.outputTextField.getText().isEmpty()){
            displayErrorMessage("Make sure to add an output destination!");
        }*/
        this.outputTextField.clear();
        this.resultsTextArea.clear();
        if(this.upcTextArea.getText().isEmpty()){
            displayErrorMessage("Make sure to add a list of UPCs first!");
        }
        String tmp = "";
        String link = "";
        scanner = new Scanner(upcTextArea.getText());

        while (scanner.hasNextLine()) {
            tmp = scanner.nextLine();

            System.out.println("TMP: " + tmp);

            link = ("https://api.spotify.com/v1/search?q=upc:" + tmp + "&type=album");

            System.out.println("LINK: " + link);

            int total = 0;

            //try {
                total = model.getTotal(link);
            //} catch (Exception e) {
                //showAlertErrorDialog(e);

            //}

            this.resultsTextArea.appendText(tmp + ", " + total + "\n");

        }

        this.outputTextField.setText("SUCCESS!");


    }

    private void showAlertErrorDialog(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Look, an Error Dialog");
        alert.setContentText("Ooops, there was an error!");

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();


    }

    @FXML
    void getInfoButton(ActionEvent event) {
        this.resultsTextArea.clear();
        this.outputTextField.clear();

        if(this.upcTextArea.getText().isEmpty()){
            displayErrorMessage("Make sure to add a list of UPCs first!");
        }
        String tmp = "";
        String link = "";
        scanner = new Scanner(upcTextArea.getText());

        while (scanner.hasNextLine()) {
            tmp = scanner.nextLine();

            System.out.println("TMP: " + tmp);

            link = ("https://api.spotify.com/v1/search?q=upc:" + tmp + "&type=album");

            System.out.println("LINK: " + link);

            String info = model.getInfo(link);

            this.resultsTextArea.appendText(tmp + "; " + info + "\n");

        }

        this.outputTextField.setText("SUCCESS!");


    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert outputTextField != null : "fx:id=\"outputTextField\" was not injected: check your FXML file 'spotyCheckGui.fxml'.";
        assert upcTextArea != null : "fx:id=\"upcTextArea\" was not injected: check your FXML file 'spotyCheckGui.fxml'.";
        assert resultsTextArea != null : "fx:id=\"resultsTextArea\" was not injected: check your FXML file 'spotyCheckGui.fxml'.";

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

    @FXML
    void clearButton(ActionEvent event) {
        this.outputTextField.clear();
        this.resultsTextArea.clear();
        this.upcTextArea.clear();

    }


    @FXML
    void nextButton(ActionEvent event) throws IOException {

        /*Parent root = FXMLLoader.load(getClass().getResource("spotyCheckArtistsNameId.fxml")) ;

        Stage stage = (Stage) nextButtonId.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/

        //nextButtonId.getScene().setRoot(FXMLLoader.load(getClass().getResource("spotyCheckArtistsNameId.fxml")));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("spotyCheckArtistsNameId.fxml")) ;

        Parent root = loader.load();
        SpotyCheckArtistsNameIdController controller = loader.getController() ;
        controller.setModel(this.model) ;

        Stage stage = (Stage) nextButtonId.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();



    }

}

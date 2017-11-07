/**
 * Sample Skeleton for 'spotyCheckArtistsNameId.fxml' Controller Class
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.equilibriummusicgroup.SpotyCheck.model.Model;
import com.equilibriummusicgroup.SpotyCheck.model.ModelSing;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SpotyCheckArtistsNameIdController {

    @FXML
    private Stage primaryStage;

    private Scanner scanner;

    @FXML
    private Model model;


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
        this.outputTextField.clear();

        this.outputTextField.clear();
        this.resultsTextArea.clear();
        if(this.nameTextArea.getText().isEmpty()){
            displayErrorMessage("Make sure to add a list of artists first!");
        }
        String tmp = "";
        String link = "";
        scanner = new Scanner(nameTextArea.getText());

        while (scanner.hasNextLine()) {
            tmp = scanner.nextLine();

            System.out.println("TMP: " + tmp);

            String urlEncodedString = urlEncode(tmp);

            if(urlEncodedString.isEmpty()){
                return;
            }

            String artistName = String.format("%s", urlEncodedString).replaceAll("\\s","%20");

            link = ("https://api.spotify.com/v1/search?q=" + artistName + "&type=artist");

            System.out.println("LINK: " + link);

            String artistInfoString = this.model.getArtistInfo(link, tmp);

            if(artistInfoString.isEmpty()){
                artistInfoString = "NOT FOUND";
            }

            this.resultsTextArea.appendText(tmp + "; " + artistInfoString + System.lineSeparator());

        }

        this.outputTextField.setText("SUCCESS!");


    }

    public String urlEncode(String source)
    {
        try {
            return URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @FXML
    void clearButton(ActionEvent event) {
        this.nameTextArea.clear();
        this.outputTextField.clear();
        this.resultsTextArea.clear();


    }

    @FXML
    void backButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("spotyCheckGui.fxml")) ;

        Parent root = loader.load();

        SpotyCheckController controller = loader.getController() ;
        controller.setModel(this.model) ;

        Stage stage = (Stage) backButtonId.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


        //backButtonId.getScene().setRoot(FXMLLoader.load(getClass().getResource("spotyCheckGui.fxml")));

/*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spotyCheckGui.fxml")) ;

        Parent root = loader.load();
        SpotyCheckController controller = loader.getController() ;
        controller.setModel(this.model) ;
        controller.setStage(this.primaryStage);
        //Stage stage = (Stage) nextButtonId.getScene().getWindow();
        Scene scene = new Scene(root);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
*/

        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("spotyCheckGui.fxml")) ;

        BorderPane root = (BorderPane)loader.load();
        SpotyCheckController controller = loader.getController() ;
        Model model = new Model() ;
        controller.setModel(model) ;
        Stage stage = (Stage) backButtonId.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/

    }

    public void displayErrorMessage(String textMessage){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(textMessage);
        alert.showAndWait();
        return;

    }

    public void setStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }



    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert outputTextField != null : "fx:id=\"outputTextField\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert nameTextArea != null : "fx:id=\"nameTextArea\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert resultsTextArea != null : "fx:id=\"resultsTextArea\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";

    }

    public void setModel(Model model){
        this.model = model;
    }
}

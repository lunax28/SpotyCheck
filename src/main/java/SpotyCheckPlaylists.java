/**
 * Sample Skeleton for 'spotyCheckPlaylists.fxml' Controller Class
 */

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.equilibriummusicgroup.SpotyCheck.model.CustomException;
import com.equilibriummusicgroup.SpotyCheck.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SpotyCheckPlaylists {

    private Scanner scanner;

    private List<String> playlistIdList;

    @FXML
    private Model model;

    @FXML // fx:id="userTextField"
    private TextField userTextField; // Value injected by FXMLLoader

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="progressBar"
    private ProgressBar progressBar; // Value injected by FXMLLoader

    @FXML // fx:id="nameTextArea"
    private TextArea inputTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="getFollowersButton"
    private Button getFollowersButton; // Value injected by FXMLLoader

    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader

    @FXML // fx:id="resultsTextArea"
    private TextArea resultsTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="backButtonId"
    private Button backButtonId; // Value injected by FXMLLoader

    @FXML
    void backButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("spotyCheckHome.fxml"));

        Parent root = loader.load();

        SpotyCheckHomeController controller = loader.getController();
        controller.setModel(this.model);

        Stage stage = (Stage) backButtonId.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void clearButton(ActionEvent event) {

    }

    @FXML
    void getFollowers(ActionEvent event) {

        this.resultsTextArea.clear();
        if (this.inputTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of UPCs first!");
        }

        scanner = new Scanner(inputTextArea.getText());
        playlistIdList = new ArrayList<>();

        while (scanner.hasNextLine()) {

            String playlistId = scanner.nextLine();
            //if (upc.matches("[0-9]{13}") || upc.matches("[0-9]{12}")) {
            playlistIdList.add(playlistId);

        }

        List<String> endpointList = new ArrayList<>();
        List<String> results = new ArrayList<>();

        Model model = new Model();

        for (String id : playlistIdList) {

            System.out.println(id);

            String link = ("https://api.spotify.com/v1/users/spotify/playlists/" + id);

            endpointList.add(link);

            System.out.println("Endpoint LINK: " + link);

            results.add(model.getPlaylistsFollowers(link, id));

        }


        for (String playlistResult : results) {

            resultsTextArea.appendText(playlistResult);

        }

    }

    @FXML
    void getUsersPlaylist(ActionEvent event) {

        this.resultsTextArea.clear();
        if (this.userTextField.getText().isEmpty()) {
            displayErrorMessage("Make sure to add USER ID!");
        }

        scanner = new Scanner(userTextField.getText());

        Model modelCopy = new Model();

        //while (scanner.hasNextLine()) {

            String user = scanner.nextLine();

            String link = ("https://api.spotify.com/v1/users/" + user + "/playlists");

            List<String> artistList = null;
            try {
                artistList = modelCopy.getUsersPlaylists(link);
            } catch (CustomException.ResponseCodeException e) {
                e.printStackTrace();
            } catch (CustomException e) {
                e.printStackTrace();
            }


            for (String listArtist : artistList) {
                this.resultsTextArea.appendText(listArtist);
            }
        //}



    }

    public void displayErrorMessage(String textMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(textMessage);
        alert.showAndWait();
        return;

    }

    public void setModel(Model model) {
        this.model = model;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'spotyCheckPlaylists.fxml'.";
        assert inputTextArea != null : "fx:id=\"nameTextArea\" was not injected: check your FXML file 'spotyCheckPlaylists.fxml'.";
        assert getFollowersButton != null : "fx:id=\"getFollowersButton\" was not injected: check your FXML file 'spotyCheckPlaylists.fxml'.";
        assert clearButton != null : "fx:id=\"clearButton\" was not injected: check your FXML file 'spotyCheckPlaylists.fxml'.";
        assert resultsTextArea != null : "fx:id=\"resultsTextArea\" was not injected: check your FXML file 'spotyCheckPlaylists.fxml'.";
        assert backButtonId != null : "fx:id=\"backButtonId\" was not injected: check your FXML file 'spotyCheckPlaylists.fxml'.";

    }
}

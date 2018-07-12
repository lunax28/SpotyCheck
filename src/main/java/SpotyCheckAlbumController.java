/**
 * Sample Skeleton for 'spotyCheckAlbums.fxml' Controller Class
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.equilibriummusicgroup.SpotyCheck.model.Model;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SpotyCheckAlbumController {

    @FXML
    private Model model;

    @FXML // fx:id="checkAlbumButton"
    private Button checkAlbumButton; // Value injected by FXMLLoader

    @FXML // fx:id="upcTextField"
    private TextField upcTextField; // Value injected by FXMLLoader

    @FXML // fx:id="backHomeButton"
    private Button backHomeButton; // Value injected by FXMLLoader

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="coverImageView"
    private ImageView coverImageView; // Value injected by FXMLLoader

    @FXML // fx:id="nameTextField"
    private TextField nameTextField; // Value injected by FXMLLoader

    @FXML // fx:id="artistsTextField"
    private TextField artistsTextField; // Value injected by FXMLLoader

    @FXML // fx:id="labelTextField"
    private TextField labelTextField; // Value injected by FXMLLoader

    @FXML // fx:id="releaseDateTextField"
    private TextField releaseDateTextField; // Value injected by FXMLLoader

    @FXML // fx:id="uriTextField"
    private TextField uriTextField; // Value injected by FXMLLoader

    @FXML // fx:id="popularityTextField"
    private TextField popularityTextField; // Value injected by FXMLLoader

    @FXML // fx:id="urlTextField"
    private TextField urlTextField; // Value injected by FXMLLoader


    @FXML
    void checkAlbum(ActionEvent event) {

        String upc = this.upcTextField.getText();

        if (!(upc.matches("[0-9]{13}")|| upc.matches("[0-9]{12}"))) {
            displayErrorMessage("UPC format error!");
            return;
        }

        Model modelCopy = new Model();

        String link = ("https://api.spotify.com/v1/search?q=upc:" + upc + "&type=album");
        System.out.println("UPC SEARCH LINK: " + link);

        JsonObject result = modelCopy.getAlbumInfo(link);

        System.out.println("jsonobject result line 84: " + result.toString());

        JsonObject albums = result.getAsJsonObject("albums");

        JsonArray items = albums.getAsJsonArray("items");

        JsonObject item = items.get(0).getAsJsonObject();

        String albumName = item.get("name").getAsString();

        this.nameTextField.setText(albumName);

        String releaseDate = item.get("release_date").getAsString();

        this.releaseDateTextField.setText(releaseDate);

        String uri = item.get("uri").getAsString();

        this.uriTextField.setText(uri);

        JsonArray artists = item.get("artists").getAsJsonArray();

        int artistsSize = artists.size();

        StringBuilder artistNames = new StringBuilder();

        for(int i = 0; i < artistsSize; i++){

            JsonObject artistsObj =  artists.get(i).getAsJsonObject();

            String artistName = artistsObj.get("name").getAsString();

            if( i == artistsSize -1){
                artistNames.append(artistName);
            } else {
                artistNames.append(artistName + ", ");
            }

        }
        this.artistsTextField.setText(artistNames.toString());


        String imageUrl = item.get("images").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();

        this.coverImageView.setImage(new Image(imageUrl));

        String url = item.get("external_urls").getAsJsonObject().get("spotify").getAsString();

        this.urlTextField.setText(url);

        String id = item.get("id").getAsString();

        link = "https://api.spotify.com/v1/albums/" + id;

        result = modelCopy.getAlbumInfo(link);

        System.out.println("jsonobject result line 141: " + result.toString());

        String label = result.get("label").getAsString();

        int popularity = result.get("popularity").getAsInt();

        this.labelTextField.setText(label);

        this.popularityTextField.setText(""+popularity);




    }

    public void displayErrorMessage(String textMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(textMessage);
        alert.showAndWait();
        return;

    }



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



    public void setModel(Model model) {
        this.model = model;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert coverImageView != null : "fx:id=\"coverImageView\" was not injected: check your FXML file 'spotyCheckAlbums.fxml'.";

    }
}

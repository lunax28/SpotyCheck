/**
 * Sample Skeleton for 'spotyCheckArtistsNameId.fxml' Controller Class
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.equilibriummusicgroup.SpotyCheck.model.Model;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SpotyCheckArtistsNameIdController {


    private Scanner scanner;

    @FXML
    private Model model;

    @FXML
    private Button backButtonId;


    @FXML // fx:id="nameTextArea"
    private TextArea nameTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="resultsTextArea"
    private TextArea resultsTextArea; // Value injected by FXMLLoader

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button checkNameButton;

    @FXML
    private Button clearButton;

    private List<String> nameList;

    @FXML
    void checkNameButton(ActionEvent event) {

        this.resultsTextArea.clear();
        if(this.nameTextArea.getText().isEmpty()){
            displayErrorMessage("Make sure to add a list of artists first!");
        }



        scanner = new Scanner(nameTextArea.getText());

        nameList = new ArrayList<>();

        while (scanner.hasNextLine()) {

            nameList.add(scanner.nextLine());

        }

        Task< List<String>> task = new Task< List<String>>() {

            @Override
            protected List<String> call() throws Exception {

                disableButtons();
                updateProgress(-1,-1);

                List<String>nameResult = new ArrayList<>();



                for (String st: nameList) {
                    System.out.println(st);

                    Model modelCopy = new Model();

                    String urlEncodedString = urlEncode(st);

                    if(urlEncodedString.isEmpty()){
                        return null;
                    }

                    String artistName = String.format("%s", urlEncodedString).replaceAll("\\s","%20");

                    String link = ("https://api.spotify.com/v1/search?q=" + artistName + "&type=artist");
                    System.out.println("LINK: " + link);

                    String result = modelCopy.getArtistInfo(link, st);

                    if(result.isEmpty()){
                        result = "NOT FOUND";
                    }

                    nameResult.add(st + ", " + result + System.lineSeparator());
                }
                updateProgress(1,1);
                return nameResult;
            }
        };

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                List<String> resultList = task.getValue();
                for (String st: resultList) {
                    resultsTextArea.appendText(st);
                }
                //resultsTextArea.appendText(resultList.toString());
                //outputTextField.setText("SUCCESS!");
                enableButtons();
            }
        });

        progressBar.progressProperty().bind(task.progressProperty());

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();


    }

    private void disableButtons() {
        checkNameButton.setDisable(true);
        clearButton.setDisable(true);
        backButtonId.setDisable(true);


    }

    private void enableButtons() {
        checkNameButton.setDisable(false);
        clearButton.setDisable(false);
        backButtonId.setDisable(false);

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


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert nameTextArea != null : "fx:id=\"nameTextArea\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert resultsTextArea != null : "fx:id=\"resultsTextArea\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";

    }

    public void setModel(Model model){
        this.model = model;
    }
}

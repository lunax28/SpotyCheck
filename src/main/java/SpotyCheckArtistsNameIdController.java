/**
 * Sample Skeleton for 'spotyCheckArtistsNameId.fxml' Controller Class
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.equilibriummusicgroup.SpotyCheck.model.CustomException;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
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

    private List<String> artistIdList;

    @FXML
    void checkNameButton(ActionEvent event) {

        this.resultsTextArea.clear();
        if (this.nameTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of artists first!");
        }


        scanner = new Scanner(nameTextArea.getText());

        nameList = new ArrayList<>();

        while (scanner.hasNextLine()) {

            nameList.add(scanner.nextLine());

        }

        Task<List<String>> task = new Task<List<String>>() {

            @Override
            protected List<String> call() throws Exception {

                disableButtons();
                updateProgress(-1, -1);

                List<String> nameResult = new ArrayList<>();


                for (String st : nameList) {
                    System.out.println(st);

                    Model modelCopy = new Model();

                    String urlEncodedString = urlEncode(st);

                    if (urlEncodedString.isEmpty()) {
                        return null;
                    }

                    String artistName = String.format("%s", urlEncodedString).replaceAll("\\s", "%20");

                    String link = ("https://api.spotify.com/v1/search?q=" + artistName + "&type=artist");
                    System.out.println("LINK: " + link);

                    String result = modelCopy.getArtistInfo(link, st);

                    if (result.isEmpty()) {
                        result = "NOT FOUND\n";
                    }

                    //nameResult.add(st + ", " + result);
                    nameResult.add(result);
                }
                updateProgress(1, 1);
                return nameResult;
            }
        };

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                List<String> resultList = task.getValue();
                for (String st : resultList) {
                    resultsTextArea.appendText(st);
                }
                //resultsTextArea.appendText(resultList.toString());
                //outputTextField.setText("SUCCESS!");
                enableButtons();
            }
        });

        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Throwable ex = task.getException();
                ex.printStackTrace();
                String message = ex.getMessage();
                System.out.println("LINE 142 MESSAGE: " + message);
                displayExceptionDialog(ex, "Response code error!");
                enableButtons();
            }
        });

        progressBar.progressProperty().bind(task.progressProperty());

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();


    }

    @FXML
    void getSuggestions(ActionEvent event) {
        this.resultsTextArea.clear();
        if (this.nameTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of artists first!");
        }

        scanner = new Scanner(nameTextArea.getText());

        Model modelCopy = new Model();

        while (scanner.hasNextLine()) {

            String artist = scanner.nextLine();

            String artistUrlEncoded = urlEncode(artist);

            String artistNameWhitespaceEscaped = String.format("%s", artistUrlEncoded).replaceAll("\\s", "%20");

            String link = ("https://api.spotify.com/v1/search?q=" + artistNameWhitespaceEscaped + "&type=artist&limit=50");

            List<String> artistList = null;
            try {
                artistList = modelCopy.getSuggestions(link);
            } catch (CustomException.ResponseCodeException e) {
                e.printStackTrace();
            } catch (CustomException e) {
                e.printStackTrace();
            }


            for (String listArtist : artistList) {
                this.resultsTextArea.appendText(listArtist);
            }
        }
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

    public String urlEncode(String source) {
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("spotyCheckGui.fxml"));

        Parent root = loader.load();

        SpotyCheckController controller = loader.getController();
        controller.setModel(this.model);

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

    public void displayErrorMessage(String textMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(textMessage);
        alert.showAndWait();
        return;

    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert nameTextArea != null : "fx:id=\"nameTextArea\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert resultsTextArea != null : "fx:id=\"resultsTextArea\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";

    }

    public void setModel(Model model) {
        this.model = model;
    }

    private void displayExceptionDialog(Throwable ex, String exceptionMessage) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception");
        alert.setContentText(exceptionMessage);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
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
    void getArtistsFollowers() {

        System.out.println("getArtistsFollowers() called");
        this.resultsTextArea.clear();

        //check also whether there are UPCs!!! whitespaces may still be present
        if (this.nameTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of Artists' IDs first!");
            return;
        }

        scanner = new Scanner(nameTextArea.getText());

        //a list containing several endpoints in case the user has inserted more than 50 IDs
        List<StringBuilder> linkList = new ArrayList<>();

        //a list containing only artists' IDs
        artistIdList = new ArrayList<>();


        while (scanner.hasNextLine()) {

            artistIdList.add(scanner.nextLine());

        }

        StringBuilder artistsIdBuilder = new StringBuilder("https://api.spotify.com/v1/artists?ids=");

        for (int i = 1; i <= artistIdList.size(); i++) {

            if (i == artistIdList.size()) {
                artistsIdBuilder.append(artistIdList.get(i-1));
                linkList.add(artistsIdBuilder);
                break;
            }


            if(i % 50 == 0){
                artistsIdBuilder.append(artistIdList.get(i-1));
                linkList.add(artistsIdBuilder);
                artistsIdBuilder = new StringBuilder("https://api.spotify.com/v1/artists?ids=");
                continue;


            }

            artistsIdBuilder.append(artistIdList.get(i-1) + ",");

        }

        System.out.println("Printing linkList: ");
        for(StringBuilder sb : linkList){
            System.out.println(sb.toString());

            List<String> result = new ArrayList<>();
            try {
                result = model.getArtistsFollowers(sb.toString());
            } catch (CustomException.ResponseCodeException e) {
                e.printStackTrace();
            } catch (CustomException e) {
                e.printStackTrace();
            }

            for(String artist : result){

                resultsTextArea.appendText(artist);

            }

        }
    }
}

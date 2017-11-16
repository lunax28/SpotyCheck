/**
 * Sample Skeleton for 'spotyCheckGui.fxml' Controller Class
 */

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class SpotyCheckController {

    @FXML
    private Model model;

    @FXML
    private Button nextButtonId;

    @FXML
    private Button checkButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button getInfoButton;

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

    private List<String> upcList;

    @FXML
    private ProgressBar progressBar;

    private String tmpUPC;

    private boolean stopProgram = false;

    private StringBuilder tracksResult = new StringBuilder();


    @FXML
    void checkButton(ActionEvent event) throws IOException {
        //this.outputTextField.clear();
        this.resultsTextArea.clear();
        if (this.upcTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of UPCs first!");
        }

        scanner = new Scanner(upcTextArea.getText());

        upcList = new ArrayList<>();

        while (scanner.hasNextLine()) {

            upcList.add(scanner.nextLine());

        }
        /*for (String st: upcList) {
            System.out.println(st);

        }*/

        Task<List<String>> task = new Task<List<String>>() {

            @Override
            protected List<String> call() throws Exception {

                disableButtons();
                updateProgress(-1, -1);

                List<String> upcResult = new ArrayList<>();


                for (String st : upcList) {
                    System.out.println(st);

                    Model modelCopy = new Model();

                    String link = ("https://api.spotify.com/v1/search?q=upc:" + st + "&type=album");
                    System.out.println("LINK: " + link);

                    int result = modelCopy.getTotal(link);

                    upcResult.add(st + ", " + result + System.lineSeparator());
                }
                updateProgress(1, 1);
                return upcResult;
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

        progressBar.progressProperty().bind(task.progressProperty());

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    private void disableButtons() {
        checkButton.setDisable(true);
        clearButton.setDisable(true);
        getInfoButton.setDisable(true);
        nextButtonId.setDisable(true);


    }

    private void enableButtons() {
        checkButton.setDisable(false);
        clearButton.setDisable(false);
        getInfoButton.setDisable(false);
        nextButtonId.setDisable(false);


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

        if (this.upcTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of UPCs first!");
        }

        scanner = new Scanner(upcTextArea.getText());

        upcList = new ArrayList<>();


        while (scanner.hasNextLine()) {

            upcList.add(scanner.nextLine());

        }


        Task<List<String>> task = new Task<List<String>>() {

            @Override
            protected List<String> call() throws Exception {

                disableButtons();
                updateProgress(-1, -1);

                List<String> upcResult = new ArrayList<>();


                for (String st : upcList) {
                    System.out.println(st);

                    Model modelCopy = new Model();

                    String link = ("https://api.spotify.com/v1/search?q=upc:" + st + "&type=album");
                    System.out.println("LINK: " + link);

                    String result = model.getInfo(link);

                    upcResult.add(st + ", " + result + System.lineSeparator());
                }
                updateProgress(1, 1);
                return upcResult;
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

        progressBar.progressProperty().bind(task.progressProperty());

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();


    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert outputTextField != null : "fx:id=\"outputTextField\" was not injected: check your FXML file 'spotyCheckGui.fxml'.";
        assert upcTextArea != null : "fx:id=\"upcTextArea\" was not injected: check your FXML file 'spotyCheckGui.fxml'.";
        assert resultsTextArea != null : "fx:id=\"resultsTextArea\" was not injected: check your FXML file 'spotyCheckGui.fxml'.";

    }

    public void setModel(Model model) {
        this.model = model;
    }


    public void displayErrorMessage(String textMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(textMessage);
        alert.showAndWait();
        return;

    }

    @FXML
    void clearButton(ActionEvent event) {
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("spotyCheckArtistsNameId.fxml"));

        Parent root = loader.load();
        SpotyCheckArtistsNameIdController controller = loader.getController();
        controller.setModel(this.model);

        Stage stage = (Stage) nextButtonId.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void getInfoTracks() {

        System.out.println("getInfoTracks() called");

        this.resultsTextArea.clear();

        this.tracksResult = new StringBuilder();


        //check also whether there are UPCs!!! whitespaces may still be present
        if (this.upcTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of UPCs first!");
            return;
        }

        scanner = new Scanner(upcTextArea.getText());

        upcList = new ArrayList<>();

        while (scanner.hasNextLine()) {

            upcList.add(scanner.nextLine());

        }

        /*int count = 0;

        while (true) {

            if(!scanner.hasNextLine()){
                stopProgram = true;
                break;
            }

            if(count >= 20){
                tmpUPC = scanner.nextLine();;
                break;
            }


            String upc = scanner.nextLine();
            System.out.println("UPC line 354: " + upc);
            if(upc.isEmpty()){
                continue;
            }
            upcList.add(upc);
            this.upcTextArea.setText(this.upcTextArea.getText().replace(upc,""));
            count++;

        }*/

        Task<List<String>> task = new Task<List<String>>() {

            @Override
            protected List<String> call() throws Exception {

                disableButtons();
                updateProgress(-1, -1);

                List<String> albumId = new ArrayList<>();

                //for loop to extract album IDs for each UPC
                for (String upc : upcList) {
                    System.out.println(upc);

                    String link = ("https://api.spotify.com/v1/search?q=upc:" + upc + "&type=album");
                    System.out.println("LINK: " + link);

                    String resultId = model.getInfoTracks(link);

                    if (resultId != null) {
                        albumId.add(resultId);
                    }


                }

                List<StringBuilder> IDsLinkList = new ArrayList<>();
                StringBuilder linkId = new StringBuilder();
                linkId.append("https://api.spotify.com/v1/albums/?ids=");

                for (int i = 0; i < albumId.size(); i++) {
                    System.out.println("albumId: " + albumId.get(i));


                    if (i < 20 && i == albumId.size() - 1) {
                        linkId.append(albumId.get(i));
                        System.out.println("LINE 410 linkId = " + linkId.toString());
                        IDsLinkList.add(linkId);
                        //linkId = new StringBuilder(); //or should I clear() it?
                        continue;
                    } else if ( i > 0 && i % 20 == 19){
                        linkId.append(albumId.get(i));
                        System.out.println("LINE 416 linkId = " + linkId.toString());
                        IDsLinkList.add(linkId);
                        linkId = new StringBuilder("https://api.spotify.com/v1/albums/?ids="); //or should I clear() it?
                        continue;
                    }else if (i == albumId.size() - 1){
                        linkId.append(albumId.get(i));
                        System.out.println("LINE 416 linkId = " + linkId.toString());
                        IDsLinkList.add(linkId);
                        continue;
                    }

                    linkId.append(albumId.get(i) + ",");

                }

                List<String> trackNamesList = new ArrayList<>();

                for (StringBuilder sb : IDsLinkList) {

                    List<String> trackList = new ArrayList<>();
                    trackList = model.getTrackList(sb.toString());

                    for (String track : trackList) {

                        trackNamesList.add(track);

                    }
                }
                //array that collects every track in those albums

                updateProgress(1, 1);
                return trackNamesList;
            }
        };

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                //StringBuilder tracksResult = new StringBuilder();
                List<String> resultList = task.getValue();
                for (String st : resultList) {
                    //resultsTextArea.appendText(st);
                    resultsTextArea.appendText(st);
                    System.out.println("LINE 455 st: " + st);
                }
                System.out.println("*#*#TEST*#*#");
                //resultsTextArea.appendText(resultList.toString());
                //outputTextField.setText("SUCCESS!");
                enableButtons();

                System.out.println("END OF TASK LINE 463");

            }
        });

        progressBar.progressProperty().bind(task.progressProperty());

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}
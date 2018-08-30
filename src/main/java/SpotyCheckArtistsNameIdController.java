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

    @FXML // fx:id="relatedArtistsButton"
    private Button relatedArtistsButton; // Value injected by FXMLLoader


    @FXML // fx:id="inputTextArea"
    private TextArea inputTextArea; // Value injected by FXMLLoader

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

    private List<String> upcList;

    @FXML
    void checkNameButton(ActionEvent event) {

        this.resultsTextArea.clear();
        if (this.inputTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of artists first!");
        }


        scanner = new Scanner(inputTextArea.getText());

        nameList = new ArrayList<>();

        while (scanner.hasNextLine()) {

            nameList.add(scanner.nextLine());

        }

        getArtistsResults();
    }



    private void getArtistsResults() {
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

                        String link = ("https://api.spotify.com/v1/search?q=" + artistName + "&type=artist&limit=50");
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
        if (this.inputTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of artists first!");
        }

        scanner = new Scanner(inputTextArea.getText());

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
        this.inputTextArea.clear();
        this.resultsTextArea.clear();

    }

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

    @FXML
    void getRelatedArtists(ActionEvent event) {

        this.resultsTextArea.clear();
        if (this.inputTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of artists first!");
        }

        scanner = new Scanner(inputTextArea.getText());

        Model modelCopy = new Model();

        while (scanner.hasNextLine()) {

            String artistId = scanner.nextLine();

            if(artistId.length() != 22){

                displayErrorMessage("Make sure to add a list of artists IDs!");
                return;
            }

            String link = ("https://api.spotify.com/v1/artists/"+ artistId + "/related-artists");

            List<String> artistList = null;
            try {
                artistList = modelCopy.getRelatedArtists(link);
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

    @FXML
    void getFollowers(ActionEvent event) {

        this.resultsTextArea.clear();
        if (this.inputTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of artists first!");
        }

        scanner = new Scanner(inputTextArea.getText());

        Model modelCopy = new Model();

        while (scanner.hasNextLine()) {

            String artistId = scanner.nextLine();

            if(artistId.length() != 22){

                displayErrorMessage("Make sure to add a list of artists IDs!");
                return;
            }

            String link = ("https://api.spotify.com/v1/artists/"+ artistId);

            List<String> artistList = null;
            try {
                artistList = modelCopy.getArtistsFollowers(link);
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

    @FXML
    void getIsrcLabels(ActionEvent event) {

        this.resultsTextArea.clear();
        if (this.inputTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of ISRC!");
        }

        scanner = new Scanner(inputTextArea.getText());

        Model modelCopy = new Model();

        while (scanner.hasNextLine()) {

            String isrc = scanner.nextLine();

            /*if(artistId.length() != 22){

                displayErrorMessage("Make sure to add a list of artists IDs!");
                return;
            }*/

            System.out.println("ISRC LINE 378: " + isrc);

            String link = ("https://api.spotify.com/v1/search?q=isrc%3A"+ isrc + "&type=track&limit=50");

            System.out.println("LINK LINE 382: " + link);

            List<String> artistList = null;
            try {
                artistList = modelCopy.getIsrcLabels(link);
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




    @FXML
    void getArtistsFollowers() {

        System.out.println("getArtistsFollowers() called");
        this.resultsTextArea.clear();

        //check also whether there are UPCs!!! whitespaces may still be present
        if (this.inputTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of Artists' IDs first!");
            return;
        }

        scanner = new Scanner(inputTextArea.getText());

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

    @FXML
    void getCategoriesPlaylists(ActionEvent event) {

        this.resultsTextArea.clear();

        List<String> categoriesList = new ArrayList<>();

        Model modelCopy = new Model();

        String link = ("https://api.spotify.com/v1/browse/categories");

        try {
            categoriesList = modelCopy.getCategories(link);
        } catch (CustomException.ResponseCodeException e) {
            e.printStackTrace();
        } catch (CustomException e) {
            e.printStackTrace();
        }


        List<String> categoriesPlaylists = null;

            try {
                categoriesPlaylists = modelCopy.getCategoriesPlaylists(categoriesList);
            } catch (CustomException.ResponseCodeException e) {
                e.printStackTrace();
            } catch (CustomException e) {
                e.printStackTrace();
            }



        for (String categoriesPlaylistsList : categoriesPlaylists) {
            this.resultsTextArea.appendText(categoriesPlaylistsList);
        }




    }

    @FXML
    void getFeaturedPlaylists(ActionEvent event) {

        this.resultsTextArea.clear();

        String link = ("https://api.spotify.com/v1/browse/featured-playlists");

        Model modelCopy = new Model();

        List<String> featuredPlaylists = null;

        System.out.println("LINK LINE 495: " + link);

        try {
            featuredPlaylists = modelCopy.getFeaturedPlaylists(link);
        } catch (CustomException.ResponseCodeException e) {
            e.printStackTrace();
        } catch (CustomException e) {
            e.printStackTrace();
        }


        for (String listArtist : featuredPlaylists) {
            this.resultsTextArea.appendText(listArtist);
        }
    }


    @FXML
    void getUsersPlaylist(ActionEvent event) {

        this.resultsTextArea.clear();
        if (this.inputTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of ISRC!");
        }

        scanner = new Scanner(inputTextArea.getText());

        Model modelCopy = new Model();

        while (scanner.hasNextLine()) {

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
        }



    }

    @FXML
    void checkAlbum(ActionEvent event) throws IOException {

        this.resultsTextArea.clear();
        if (this.inputTextArea.getText().isEmpty()) {
            displayErrorMessage("Make sure to add a list of UPCs first!");
        }

        scanner = new Scanner(inputTextArea.getText());
        upcList = new ArrayList<>();

        while (scanner.hasNextLine()) {

            String upc = scanner.nextLine();
            if (upc.matches("[0-9]{13}") || upc.matches("[0-9]{12}")) {
                upcList.add(upc);
            } else {
                displayErrorMessage("UPC format error!");
                return;
            }
        }

        Task<List<String>> task = new Task<List<String>>() {

            @Override
            protected List<String> call() throws Exception {

                disableButtons();
                updateProgress(-1, -1);

                List<String> upcResult = new ArrayList<>();
                int result = 0;

                for (String st : upcList) {
                    System.out.println(st);

                    Model modelCopy = new Model();

                    String link = ("https://api.spotify.com/v1/search?q=upc:" + st + "&type=album");
                    System.out.println("LINK: " + link);

//                    upcResult.add(81 + ", " + result + System.lineSeparator());

                    result = modelCopy.getTotal(link);

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



    public void displayErrorMessage(String textMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(textMessage);
        alert.showAndWait();
        return;

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert inputTextArea != null : "fx:id=\"inputTextArea\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert checkNameButton != null : "fx:id=\"checkNameButton\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert relatedArtistsButton != null : "fx:id=\"relatedArtistsButton\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert clearButton != null : "fx:id=\"clearButton\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert resultsTextArea != null : "fx:id=\"resultsTextArea\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";
        assert backButtonId != null : "fx:id=\"backButtonId\" was not injected: check your FXML file 'spotyCheckArtistsNameId.fxml'.";

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
}

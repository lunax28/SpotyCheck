package com.equilibriummusicgroup.SpotyCheck.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;


public class Model {

    private ApiQueryUtil apiQuery;
    private int failCount = 0;
    private int secondsToWait = 0;


    public Model(){
        this.apiQuery  = new ApiQueryUtil();
    }

    /**
     * Checks whether an Album exists on Spotify
     * @param link the constructed link to query the web API
     * @return <p>int:</p>
     *          <p> 0 - The album associated with this UPC does not exist</p>
     *          <p> 1 - The album associated with this UPC exists</p>
     */
    public int getTotal(String link) throws InterruptedException, CustomException, CustomException.ResponseCodeException {
        if(secondsToWait>0){
            Thread.sleep(secondsToWait * 3000);
        }

        if(failCount>5){
            throw new CustomException("Too many attempts");
        }

        JsonObject jsonResponse = null;

        try {
            jsonResponse = apiQuery.getJson(link);
            System.out.println("Model line 36 jsonResponse: " + jsonResponse.toString());
        } catch (CustomException.ResponseCodeException e) {
            secondsToWait = Integer.parseInt(e.getMessage());
            failCount++;
            System.out.println("FAILCOUNT: " + failCount);
            this.getTotal(link);
        } catch(CustomException e){
            System.out.println("LINE 43 CUSTOMEXCEPTION CAUGHT");
            throw new CustomException(e.getMessage());
        }
        System.out.println("Model line 47 jsonResponse: " + jsonResponse.toString());

        boolean checkAlbum = this.isAlbum(jsonResponse);

        if(checkAlbum){
            return 1;
        }

        return 0;
    }

    /**
     * Retrieves Album Info
     * @param link the constructed link to query the web API
     * @return <p>String:</p>
     *          <p>Album name and Artists</p>
     */
    public String getInfo(String link) throws CustomException, CustomException.ResponseCodeException {
        StringBuilder artistsString = new StringBuilder();

        JsonObject jsonResponse = apiQuery.getJson(link);
        System.out.println("JSON RESPONSE: " + jsonResponse.toString());

        boolean checkAlbum = this.isAlbum(jsonResponse);

        if(!checkAlbum){
            return "NULL";
        }

        JsonObject jsonAlbum = jsonResponse.get("albums").getAsJsonObject();

        JsonArray itemsArray = jsonAlbum.get("items").getAsJsonArray();

        JsonObject firstItemsObj = itemsArray.get(0).getAsJsonObject();

        String albumName = firstItemsObj.get("name").getAsString();

        artistsString.append(albumName + "; ");

        JsonArray artistsArray = firstItemsObj.get("artists").getAsJsonArray();

        for(int i = 0; i<artistsArray.size();i++){

            JsonObject artistsObj =  artistsArray.get(i).getAsJsonObject();

            String artistName = artistsObj.get("name").getAsString();

            if( i == artistsArray.size() -1){
                artistsString.append(artistName);
            } else {
                artistsString.append(artistName + ", ");
            }

        }

        return artistsString.toString();

    }

    /**
     * Convenience method for Album validity for <b>getInfo()</b> method
     * @param jsonResponse the JsonObject returned by <b>getJson()</b>
     * @return <p>boolean</p>
     */
    public boolean isAlbum(JsonObject jsonResponse){

        JsonObject jsonAlbum = jsonResponse.get("albums").getAsJsonObject();

        int total = jsonAlbum.get("total").getAsInt();

        if(total == 1){
            return true;
        }

        return false;
    }

    /**
     * Retrieves Artist info: <b>popularity</b>, <b>followers</b>, <b>id</b>
     * @param link the constructed link to query the web API
     * @param tmp the current artist in the TextArea
     * @return <p>String</p>
     */
    public String getArtistInfo(String link, String tmp) throws CustomException, CustomException.ResponseCodeException {

        StringBuilder artistInfoString = new StringBuilder();

        JsonObject jsonResponse = apiQuery.getJson(link);

        System.out.println("JSON RESPONSE: " + jsonResponse.toString());

        JsonObject artistsObj = jsonResponse.get("artists").getAsJsonObject();

        JsonArray artistsArray = artistsObj.get("items").getAsJsonArray();


        for (int i = 0; i < artistsArray.size(); i++) {

            JsonObject jsonObjArr = artistsArray.get(i).getAsJsonObject();

            String lowerCaseArtist = jsonObjArr.get("name").getAsString().toLowerCase();

            if (tmp.toLowerCase().equals(lowerCaseArtist)) {

                int popularity = jsonObjArr.get("popularity").getAsInt();

                JsonObject followersObject = jsonObjArr.get("followers").getAsJsonObject();

                int followers = followersObject.get("total").getAsInt();

                String id = jsonObjArr.get("id").getAsString();

                artistInfoString.append(lowerCaseArtist + "; " + popularity + "; " + followers + "; " + id + System.lineSeparator());

            }

        }
        return artistInfoString.toString();
    }

    /**
     * Retrieves the track names for all the albums associated with the UPC specified in the text area
     * @param link the constructed link to query the web API
     * @return <p>String</p>
     */
    public String getInfoTracks(String link) throws CustomException, CustomException.ResponseCodeException {
        StringBuilder artistsString = new StringBuilder();

        List<String> idArray = new ArrayList<>();

        JsonObject jsonResponse = apiQuery.getJson(link);
        System.out.println("JSON RESPONSE: " + jsonResponse.toString());

        boolean checkAlbum = this.isAlbum(jsonResponse);

        if(!checkAlbum){
            System.out.println("NOT AN ALBUM!");
            return null;
        }

        JsonObject jsonAlbum = jsonResponse.get("albums").getAsJsonObject();

        JsonArray itemsArray = jsonAlbum.get("items").getAsJsonArray();

        JsonObject firstItemsObj = itemsArray.get(0).getAsJsonObject();

        String id = firstItemsObj.get("id").getAsString();

        return id;



    }

    public List<String> getTrackList(String finalLink) throws CustomException, CustomException.ResponseCodeException {
        System.out.println("finalLink: " + finalLink);

        List<String> tracksList = new ArrayList<>();

        JsonObject jsonResponse = apiQuery.getJson(finalLink);
        System.out.println("JSON RESPONSE: " + jsonResponse.toString());


        JsonArray albumArray = jsonResponse.get("albums").getAsJsonArray();

        for (int i = 0; i < albumArray.size(); i++) {

            JsonObject nextAlbum = albumArray.get(i).getAsJsonObject();

            JsonObject tracksObject = nextAlbum.get("tracks").getAsJsonObject();

            JsonArray itemsTracks = tracksObject.get("items").getAsJsonArray();

            for (int z = 0; z < itemsTracks.size(); z++) {

                JsonObject nextTrack = itemsTracks.get(z).getAsJsonObject();

                String name = nextTrack.get("name").getAsString();

                tracksList.add(name + System.lineSeparator());

            }

        }

        return tracksList;
    }

    /**
     * Retrieves the search suggestions for the artists specified in the text area
     * @param finalLink the constructed link to query the web API
     * @return <p>List&lt;String&gt;</p>
     */
    public List<String> getSuggestions(String finalLink) throws CustomException.ResponseCodeException, CustomException {

        System.out.println("getSuggestionsLink: " + finalLink);

        List<String> artistList = new ArrayList<>();

        JsonObject jsonResponse = apiQuery.getJson(finalLink);

        JsonObject artistObject = jsonResponse.get("artists").getAsJsonObject();
        System.out.println("JSON RESPONSE: " + jsonResponse.toString());

        JsonArray artistArray = artistObject.get("items").getAsJsonArray();

        for (int i = 0; i < artistArray.size(); i++) {

            JsonObject nextArtist = artistArray.get(i).getAsJsonObject();

            String artistName = nextArtist.get("name").getAsString();

            String uri = nextArtist.get("uri").getAsString();

            JsonObject followersObj = nextArtist.get("followers").getAsJsonObject();
            int followers = followersObj.get("total").getAsInt();

            artistList.add(artistName + "; " + uri + "; " + followers + System.lineSeparator());

        }

            return artistList;

    }

    public List<String> getArtistsFollowers(String link) throws CustomException.ResponseCodeException, CustomException {
        List<String> artistList = new ArrayList<>();
        JsonObject jsonResponse = apiQuery.getJson(link);

        JsonArray artistsArray = jsonResponse.get("artists").getAsJsonArray();

        for (int i = 0; i < artistsArray.size(); i++) {

            JsonObject nextArtist = artistsArray.get(i).getAsJsonObject();

            String artistName = nextArtist.get("name").getAsString();

            int popularity = nextArtist.get("popularity").getAsInt();

            JsonObject followersObj = nextArtist.get("followers").getAsJsonObject();
            int followers = followersObj.get("total").getAsInt();

            artistList.add(artistName + "; " + followers + "; " + popularity + System.lineSeparator());

        }

        return artistList;



    }
}

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

    public JsonObject getAlbumInfo ( String link){

        JsonObject jsonResponse = null;
        try {
            jsonResponse = apiQuery.getJson(link);
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (CustomException.ResponseCodeException e) {
            e.printStackTrace();
        }

        return jsonResponse;



    }


    public String getPlaylistsFollowers(String link, String id) {

        StringBuilder artistsString = new StringBuilder();

        JsonObject jsonResponse = null;
        try {
            jsonResponse = apiQuery.getJson(link);
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (CustomException.ResponseCodeException e) {
            e.printStackTrace();
        }
        System.out.println("PLAYLISTS JSON RESPONSE: " + jsonResponse.toString());

        JsonObject followersObject = jsonResponse.getAsJsonObject("followers");

        int followers = followersObject.get("total").getAsInt();



        return id + ", " + followers + "\n";


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

        /*int total = artistsObj.get("total").getAsInt();

        if(total>50){
            System.out.println("TOTAL > 50!!");
            throw new CustomException("TOTAL > 50");

        }*/

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

        String nextLink = "";
        System.out.println("getSuggestionsLink: " + finalLink);
        JsonObject jsonResponse = null;

        List<String> artistList = new ArrayList<>();

        //for loop to retrieve multiple pages. Look at the AM API doc
        for (int x = 0; x < 2; x++) {

            if (x<1) {
                jsonResponse = apiQuery.getJson(finalLink);
                System.out.println("x<1 First Pass");

            } else if(!nextLink.isEmpty()) {
                System.out.println("LINE 260 nextLink: " + nextLink);
                jsonResponse = apiQuery.getJson(nextLink);
            }

            JsonObject artistObject = jsonResponse.get("artists").getAsJsonObject();

            if (!artistObject.get("next").toString().equals("null") && !artistObject.get("next").isJsonNull()){
                nextLink = artistObject.get("next").getAsString();
            }

            JsonArray artistArray = artistObject.get("items").getAsJsonArray();

            for (int i = 0; i < artistArray.size(); i++) {

                JsonObject nextArtist = artistArray.get(i).getAsJsonObject();
                String artistName = nextArtist.get("name").getAsString();
                String uri = nextArtist.get("uri").getAsString();
                JsonObject followersObj = nextArtist.get("followers").getAsJsonObject();
                int followers = followersObj.get("total").getAsInt();

                artistList.add(artistName + "; " + uri + "; " + followers + System.lineSeparator());
            }
        }
        return artistList;
    }

    /**
     * Helper method to check whether the json retrieved has the field passed as a parameter the <code>jsonResponse</code> from the <code>com.equilibriummusicgroup.AMCharts.model.JsonQueryUtils</code>class.
     *
     * @param gson the json object retrieved
     * @param key  the key against which the check is made
     * @return Boolean
     */
    private Boolean checkNode(JsonObject gson, String key) {
        return gson.has(key);
    }

    public List<String> getArtistsFollowers(String link) throws CustomException.ResponseCodeException, CustomException {
        List<String> artistList = new ArrayList<>();
        JsonObject jsonResponse = apiQuery.getJson(link);


        String artistName = jsonResponse.get("name").getAsString();

        int popularity = jsonResponse.get("popularity").getAsInt();

        JsonObject followersObj = jsonResponse.get("followers").getAsJsonObject();
        int followers = followersObj.get("total").getAsInt();

        artistList.add(artistName + "; " + followers + "; " + popularity + System.lineSeparator());

        return artistList;

    }


    public List<String> getRelatedArtists(String link) throws CustomException.ResponseCodeException, CustomException {

        List<String> artistList = new ArrayList<>();
        JsonObject jsonResponse = apiQuery.getJson(link);

        JsonArray artistsArray = jsonResponse.get("artists").getAsJsonArray();

        for (int i = 0; i < artistsArray.size(); i++) {

            JsonObject nextArtist = artistsArray.get(i).getAsJsonObject();

            String artistName = nextArtist.get("name").getAsString();

            int popularity = nextArtist.get("popularity").getAsInt();

            String id = nextArtist.get("id").getAsString();

            artistList.add(artistName + "; " + id + "; " + popularity + System.lineSeparator());

        }

        return artistList;



    }

    public List<String> getIsrcLabels(String link) throws CustomException.ResponseCodeException, CustomException {

        List<String> albumLabelList = new ArrayList<>();
        JsonObject jsonResponse = apiQuery.getJson(link);

        JsonObject tracksObj = jsonResponse.get("tracks").getAsJsonObject();

        JsonArray tracksArray = tracksObj.get("items").getAsJsonArray();

        //String nextLink = tracksObj.get("next").getAsString();


        for (int i = 0; i < tracksArray.size(); i++) {

            JsonObject nextTrack = tracksArray.get(i).getAsJsonObject();

            JsonObject albumTrackObj = nextTrack.get("album").getAsJsonObject();

            String idAlbum = albumTrackObj.get("id").getAsString();


            String linkAlbum = ("https://api.spotify.com/v1/albums/"+ idAlbum);
            JsonObject jsonResponseAlbum = apiQuery.getJson(linkAlbum);

            String albumLabel = jsonResponseAlbum.get("label").getAsString();


            albumLabelList.add(albumLabel + ";" + idAlbum + System.lineSeparator());

        }


        /*JsonObject jsonResponseNext = apiQuery.getJson(nextLink);

        tracksObj = jsonResponse.get("tracks").getAsJsonObject();

        tracksArray = tracksObj.get("items").getAsJsonArray();

        for (int i = 0; i < tracksArray.size(); i++) {

            JsonObject nextTrack = tracksArray.get(i).getAsJsonObject();

            JsonObject albumTrackObj = nextTrack.get("album").getAsJsonObject();

            String idAlbum = albumTrackObj.get("id").getAsString();


            String linkAlbum = ("https://api.spotify.com/v1/albums/"+ idAlbum);
            JsonObject jsonResponseAlbum = apiQuery.getJson(linkAlbum);

            String albumLabel = jsonResponseAlbum.get("label").getAsString();


            albumLabelList.add(albumLabel + ";" + idAlbum + System.lineSeparator());

        }*/



        return albumLabelList;


    }



    public List<String> getFeaturedPlaylists(String link) throws CustomException.ResponseCodeException, CustomException {


        List<String> featuredPlaylistsList = new ArrayList<>();

        JsonObject jsonResponse = apiQuery.getJson(link);

        JsonObject playlistsObj = jsonResponse.get("playlists").getAsJsonObject();

        JsonArray playlistsArray = playlistsObj.get("items").getAsJsonArray();

        //String nextLink = tracksObj.get("next").getAsString();


        for (int i = 0; i < playlistsArray.size(); i++) {

            JsonObject nextPlaylist = playlistsArray.get(i).getAsJsonObject();

            String uri = nextPlaylist.get("uri").getAsString();

            String name = nextPlaylist.get("name").getAsString();

            JsonObject ownerObj = nextPlaylist.get("owner").getAsJsonObject();

            String ownerUri = ownerObj.get("uri").getAsString();


            featuredPlaylistsList.add(name + ";" + uri + ownerUri + System.lineSeparator());

        }

        return featuredPlaylistsList;




    }

    public List<String> getCategories(String link) throws CustomException.ResponseCodeException, CustomException {

        List<String> categoriesList = new ArrayList<>();

        JsonObject jsonResponse = apiQuery.getJson(link);

        JsonObject categoriesObj = jsonResponse.get("categories").getAsJsonObject();

        JsonArray categoriesArray = categoriesObj.get("items").getAsJsonArray();

        //String nextLink = tracksObj.get("next").getAsString();


        for (int i = 0; i < categoriesArray.size(); i++) {

            JsonObject nextCategory = categoriesArray.get(i).getAsJsonObject();

            String id = nextCategory.get("id").getAsString();


            categoriesList.add(id);

        }

        return categoriesList;

    }

    public List<String> getCategoriesPlaylists(List<String> categoriesList) throws CustomException.ResponseCodeException, CustomException {


        List<String> categoriesPlaylistsList = new ArrayList<>();


        for (String categoryId : categoriesList) {

            if(categoryId.equals("popindo")){
                System.out.println("POPINDO SPOTTED!!");
                continue;
            }


            System.out.println("category ID: "+ categoryId);

            String link = ("https://api.spotify.com/v1/browse/categories/"+ categoryId + "/playlists?limit=50");
                          //https://api.spotify.com/v1/browse/categories/toplists/playlists

            System.out.println("LINK LINE 531: " + link);

            JsonObject jsonResponse = apiQuery.getJson(link);

            JsonObject playlistsObj = jsonResponse.get("playlists").getAsJsonObject();

            JsonArray categoriesArray = playlistsObj.get("items").getAsJsonArray();

            //String nextLink = tracksObj.get("next").getAsString();


            for (int i = 0; i < categoriesArray.size(); i++) {

                JsonObject nextCategoryPlaylist = categoriesArray.get(i).getAsJsonObject();

                String name = nextCategoryPlaylist.get("name").getAsString();

                String uri = nextCategoryPlaylist.get("uri").getAsString();


                categoriesPlaylistsList.add(name + ";" + uri + System.lineSeparator());

            }




        }



        return categoriesPlaylistsList;



    }

    public List<String> getUsersPlaylists(String link) throws CustomException.ResponseCodeException, CustomException {

        List<String> usersPlaylistList = new ArrayList<>();


        int count = 0;

        String nextLink = link;



        /*while(count < 80){

            JsonObject jsonResponse = apiQuery.getJson(nextLink);


            if(checkNode(jsonResponse, "next")){
                if(jsonResponse.get("next").isJsonNull()){
                    System.out.println("NEXTLINK IS NULL OR EMPTY!!!");
                    break;
                } else{
                    nextLink = jsonResponse.get("next").getAsString();
                }
            } else {
                nextLink = null;
                System.out.println("NO MORE NEXTLINK!!!!");
            }

            if(nextLink == null || nextLink.isEmpty()){
                System.out.println("611 NEXT LINK IS NULL OR EMPTY!!!");
                break;
            }



            System.out.println("nextLink: " + nextLink);

            JsonArray userPlaylistsArray = jsonResponse.get("items").getAsJsonArray();

            for (int i = 0; i < userPlaylistsArray.size(); i++) {

                JsonObject nextPlaylist = userPlaylistsArray.get(i).getAsJsonObject();

                String playlistName = nextPlaylist.get("name").getAsString();
                String uri = nextPlaylist.get("uri").getAsString();

                usersPlaylistList.add(playlistName + "; " + uri  + System.lineSeparator());

            }

            count++;

        }*/

        JsonObject jsonResponse = null;



        do {

            jsonResponse = apiQuery.getJson(nextLink);


            if(checkNode(jsonResponse, "next")){
                if(jsonResponse.get("next").isJsonNull()){
                    System.out.println("NEXTLINK IS NULL OR EMPTY!!!");
                    break;
                } else{
                    nextLink = jsonResponse.get("next").getAsString();
                }
            } else {
                nextLink = null;
                System.out.println("NO MORE NEXTLINK!!!!");
            }

            if(nextLink == null || nextLink.isEmpty()){
                System.out.println("611 NEXT LINK IS NULL OR EMPTY!!!");
                break;
            }



            System.out.println("nextLink: " + nextLink);

            JsonArray userPlaylistsArray = jsonResponse.get("items").getAsJsonArray();

            for (int i = 0; i < userPlaylistsArray.size(); i++) {

                JsonObject nextPlaylist = userPlaylistsArray.get(i).getAsJsonObject();

                String playlistName = nextPlaylist.get("name").getAsString();
                String uri = nextPlaylist.get("uri").getAsString();

                usersPlaylistList.add(playlistName + "; " + uri  + System.lineSeparator());

            }

            count++;


        } while (!jsonResponse.get("next").isJsonNull());


        System.out.println("COUNT: " + count);

        return usersPlaylistList;


    }
}

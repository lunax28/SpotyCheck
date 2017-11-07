package com.equilibriummusicgroup.SpotyCheck.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class Model {

    private ApiQueryUtil apiQuery;


    public Model(){
        this.apiQuery  = new ApiQueryUtil();
    }

    /**
     * Checks whether an Album exists
     * @param link the constructed link to query the web API
     * @return <p>int:</p>
     *          <p> 0 - The album associated with this UPC does not exist</p>
     *          <p> 1 - The album associated with this UPC exists</p>
     */
    public int getTotal(String link){

        JsonObject jsonResponse = apiQuery.getJson(link);
        System.out.println("JSON RESPONSE: " + jsonResponse.toString());

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
    public String getInfo(String link) {
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
     * Checks for Album validity for <b>getInfo()</b> method
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
     * Retrieves Artist info
     * @param link the constructed link to query the web API
     * @param tmp the current artist in the TextArea
     * @return <p>String</p>
     */
    public String getArtistInfo(String link, String tmp) {

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
}

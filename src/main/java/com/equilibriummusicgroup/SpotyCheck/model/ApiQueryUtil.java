package com.equilibriummusicgroup.SpotyCheck.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.prefs.Preferences;

public class ApiQueryUtil {

    private String link;
    private JsonObject jsonObject;
    private String responseTrimmed;
    private String albumsJson;
    private String tokenString;
    private int responseCode;
    //a Preferences object to store the token, avoiding repetitive calls
    private Preferences preferences = Preferences.userNodeForPackage(ApiQueryUtil.class);

    public ApiQueryUtil() {
        this.link = "";
        this.responseTrimmed = "";
        this.jsonObject = null;
        this.tokenString = "";
        this.responseCode = 0;
    }

    public String getToken() {
        String json_response = "";

        try {
            URL url = new URL("https://accounts.spotify.com/api/token");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            String basicAuth = "Basic NTM0NzYyN2JkYzQ0NGEwYzg3ZWI4NGFkZTkwMTc0YzI6ZTZiMmVhNzIzYTY5NDc4MjhiNTQyMDQzM2E1MTdjYzg=";
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            httpCon.setRequestProperty("Authorization", basicAuth);

            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write("grant_type=client_credentials");
            out.flush();
            out.close();

            InputStreamReader in = new InputStreamReader(httpCon.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String text = "";
            while ((text = br.readLine()) != null) {
                json_response += text;
            }

        } catch (MalformedURLException ex) {
            System.out.println("MalformedURLException!!");
        } catch (ProtocolException ex) {
            System.out.println("ProtocolException!!");
        } catch (IOException ex) {
            System.out.println("IOException!!");
        }

        JsonObject token = new JsonParser().parse(json_response).getAsJsonObject();

        long expiry = System.currentTimeMillis() + (token.get("expires_in").getAsLong() * 1000);

        preferences.get("token", "");
        preferences.put("token", token.get("access_token").getAsString());

        preferences.getLong("expiry", 0);
        preferences.putLong("expiry", expiry);

        System.out.println("TOKEN: " + token.get("access_token").getAsString());
        return token.get("access_token").getAsString();

    }

    public String getLink() {
        return this.link;
    }

    public JsonObject getJson(String link) {
        String response = "";
        try {

            URL url = new URL(link);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

            String basicAuth = "";

            if (preferences.get("token", "").isEmpty() || preferences.getLong("expiry", 0) < System.currentTimeMillis()) {

                System.out.println("###\nREQUESTED NEW TOKEN!!!\n###");
                basicAuth = "Bearer " + getToken();
            } else {
                basicAuth = "Bearer " + preferences.get("token", "");
            }

            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("Authorization", basicAuth);

            this.responseCode = httpCon.getResponseCode();
            if (this.responseCode != 200) {
                System.out.println("RESPONSE CODE: " + this.responseCode);
                return null;
            }

            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpCon.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response += inputLine;
            }
            in.close();
        } catch (MalformedURLException ex) {
            System.out.println("MalformedURLException!!");
        } catch (ProtocolException ex) {
            System.out.println("ProtocolException!!");
        } catch (IOException ex) {
            System.out.println("IOException!!");
        }

        this.responseTrimmed = response.trim();

        this.jsonObject = new JsonParser().parse(responseTrimmed).getAsJsonObject();
        System.out.println("jsonobj: " + this.jsonObject.toString());

        return this.jsonObject;

    }


    public String isAlbum(String link) {
        this.link = link;

        JsonObject jsonIsAlbum = getJson(link);

        if (jsonIsAlbum == null) {
            return "RATE LIMIT";

        }

        JsonObject jsonId = new JsonParser().parse(this.responseTrimmed).getAsJsonObject();
        this.albumsJson = jsonId.get("albums").toString();
        jsonId = new JsonParser().parse(this.albumsJson).getAsJsonObject();

        String total = jsonId.get("total").toString();
        System.out.println("TOTAL: " + total);
        this.responseTrimmed = "";
        if (total.equals("1")) {
            return "1";

        } else {
            return "0";
        }
    }

    public int getResponseCode() {
        return responseCode;
    }


}

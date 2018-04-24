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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;

public class ApiQueryUtil {

    private String retryAfterSeconds;

    private JsonObject jsonObject;
    private String responseTrimmed;
    private String albumsJson;
    private int responseCode;

    //a Preferences object to store the token, avoiding repetitive calls
    private static final Preferences preferences = Preferences.userNodeForPackage(ApiQueryUtil.class);

    public ApiQueryUtil() {
        this.responseTrimmed = "";
        this.jsonObject = null;
        this.responseCode = 0;
    }

    public JsonObject getJson(String link) throws CustomException, CustomException.ResponseCodeException {
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

            Map<String, List<String>> headers = httpCon.getHeaderFields();

            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String key = entry.getKey();
                if (key != null) {
                    if (key.equals("Retry-After")) {
                        List<String> value = entry.getValue();
                        this.retryAfterSeconds = value.get(0);

                        System.out.println("RETRY AFTER: " + this.retryAfterSeconds + " seconds!");
//                        throw new CustomException(seconds);
                    } else {
                        //System.out.println("NO RETRY AFTER!");
                    }
                }
                System.out.println("Header Name: " + key);
                List<String> value = entry.getValue();
                System.out.println("Header Value: " + value.get(0));

            }
            this.responseCode = httpCon.getResponseCode();

//            this.responseCode = 502;
            if (this.responseCode == 429) {
                System.out.println("ApiQueryUtil LINE 81 Response Code : " + this.responseCode);
                throw new CustomException.ResponseCodeException(this.retryAfterSeconds);
            } else if (this.responseCode != 200){
                System.out.println("ApiQueryUtil LINE 84 Response Code : " + this.responseCode);
                throw new CustomException("Response Code : " + this.responseCode);

            }

            System.out.println("\nApiQueryUtil Sending 'GET' request to URL : " + url);
            System.out.println("ApiQueryUtil Response Code : " + responseCode);

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

    private static String getToken() {
        String json_response = "";

        try {
            URL url = new URL("https://accounts.spotify.com/api/token");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            String basicAuth = "SECRET KEY";
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

}

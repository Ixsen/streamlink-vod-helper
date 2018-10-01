package de.ixsen.streamlinkvodhelper.main.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HtmlCalls implements HasLogger {

    public static void getVodInfo(int vodId) {

    }

    public static JsonArray getVodsByUserId(int userId) {
        try {
            URL url = new URL("https://api.twitch.tv/helix/videos?user_id=" + userId);
            HttpURLConnection connection = doHtmlCall(url);
            connection.connect();
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new InputStreamReader((connection.getInputStream()), StandardCharsets.UTF_8));
            return ((JsonObject) jsonElement).getAsJsonArray("data");
        } catch (IOException e) {
            throw new RuntimeException("Getting VODs failed", e);
        }
    }

    public static int getUserIdByLogin(String loginName) {
        try {
            URL url = new URL("https://api.twitch.tv/helix/users?login=" + loginName);
            HttpURLConnection connection = doHtmlCall(url);
            connection.connect();
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new InputStreamReader((connection.getInputStream()), StandardCharsets.UTF_8));
            return ((JsonObject) jsonElement).getAsJsonArray("data").get(0).getAsJsonObject().get("id").getAsInt();
        } catch (IOException e) {
            throw new RuntimeException("Getting ID failed", e);
        }
    }

    public static HttpURLConnection doHtmlCall(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Client-ID", "SETOWN");
            return connection;
        } catch (IOException e) {
            throw new RuntimeException("Could not create Connection", e);
        }
    }

    public static JsonArray getVodsByLogin(String login) {
        return getVodsByUserId(getUserIdByLogin(login));
    }
}

package de.ixsen.streamlinkvodhelper.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.ixsen.streamlinkvodhelper.data.SearchType;
import de.ixsen.streamlinkvodhelper.data.settings.Settings;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HtmlCallUtils {

    public static JsonArray getVodsByUserId(int userId, SearchType type) {
        try {
            String userIdHeader = "&user_id=" + userId;
            String amount = "&first=100";
            String typeHeader = "&type=" + type;
            String urlPath = "https://api.twitch.tv/helix/videos?" + userIdHeader + amount + typeHeader;
            URL url = new URL(urlPath);
            LoggerHelper.getLogger().info("Doing Get on: " + urlPath);
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
            if (Settings.getSettings().getClientId().isEmpty()) {
                DialogUtils.warning("Please set your Client ID in the settings.");
                LoggerHelper.getLogger().severe("No client ID set!");
            }
            connection.setRequestProperty("Client-ID", Settings.getSettings().getClientId());
            return connection;
        } catch (IOException e) {
            throw new RuntimeException("Could not create Connection", e);
        }
    }
}

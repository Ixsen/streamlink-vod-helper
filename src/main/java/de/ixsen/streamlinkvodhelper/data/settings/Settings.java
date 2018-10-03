package de.ixsen.streamlinkvodhelper.data.settings;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.ixsen.streamlinkvodhelper.utils.DialogUtils;
import de.ixsen.streamlinkvodhelper.utils.LoggerHelper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings implements LoggerHelper {
    private static final String FILE_NAME = "settings.cfg";
    private String clientId;
    private String pathStreamlink;
    private String player;

    public String getPathStreamlink() {
        return this.pathStreamlink;
    }

    public void setPathStreamlink(String pathStreamlink) {
        this.pathStreamlink = pathStreamlink;
    }

    public String getPlayer() {
        return this.player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Settings() {
        this.clientId = "";
        this.pathStreamlink = "";
        this.player = "";
    }

    public static Settings getSettings() {
        File settingsFile = new File(FILE_NAME);
        if (!settingsFile.exists()) {
            initializeSettings(settingsFile);
        }
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(settingsFile)) {
            Settings settings = gson.fromJson(reader, Settings.class);
            if (settings == null) {
                initializeSettings(settingsFile);
                settings = gson.fromJson(reader, Settings.class);
            }
            return settings;
        } catch (IOException e) {
            throw new RuntimeException("Loading settings failed", e);
        } catch (JsonSyntaxException e) {
            DialogUtils.error("Could not process config file!", "Please delete the config.cfg or message the creator of this tool");
            throw new RuntimeException("", e);
        }
    }

    private static void initializeSettings(File settingsFile) {
        try (FileWriter writer = new FileWriter(settingsFile)) {
            Settings settings = new Settings();
            Gson gson = new Gson();
            gson.toJson(settings, writer);
        } catch (IOException e) {
            LoggerHelper.getLogger().severe("Creating settings file failed");
        }
    }

    public static void saveSettings(Settings settings) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            new Gson().toJson(settings, writer);
        } catch (IOException e) {
            LoggerHelper.getLogger().severe("Writing settings file failed");
        }
    }

    public void makeChanges(String pathStreamlink, String clientIdText, String player) {
        this.pathStreamlink = pathStreamlink;
        this.clientId = clientIdText;
        this.player = player;
    }
}

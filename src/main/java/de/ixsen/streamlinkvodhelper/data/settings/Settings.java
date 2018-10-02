package de.ixsen.streamlinkvodhelper.data.settings;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.ixsen.streamlinkvodhelper.utils.Dialogs;
import de.ixsen.streamlinkvodhelper.utils.HasLogger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class Settings {
    private static final String FILE_NAME = "settings.cfg";
    private String clientId;
    private String pathStreamlink;
    private String freeText;

    public String getPathStreamlink() {
        return this.pathStreamlink;
    }

    public void setPathStreamlink(String pathStreamlink) {
        this.pathStreamlink = pathStreamlink;
    }

    public String getFreeText() {
        return this.freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
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
        this.freeText = "";
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
            Dialogs.error("Could not process config file!", "Please delete the config.cfg or message the creator of this tool");
            throw new RuntimeException("", e);
        }
    }

    private static void initializeSettings(File settingsFile) {
        try (FileWriter writer = new FileWriter(settingsFile)) {
            Settings settings = new Settings();
            Gson gson = new Gson();
            gson.toJson(settings, writer);
        } catch (IOException e) {
            HasLogger.getLogger().log(Level.SEVERE, "Creating settings file failed", e);
        }
    }

    public static void saveSettings(Settings settings) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            new Gson().toJson(settings, writer);
        } catch (IOException e) {
            HasLogger.getLogger().log(Level.SEVERE, "Writing settings file failed", e);
        }
    }

    public void makeChanges(String pathStreamlink, String clientIdText, String freeSlotText) {
        this.pathStreamlink = pathStreamlink;
        this.clientId = clientIdText;
        this.freeText = freeSlotText;
    }
}

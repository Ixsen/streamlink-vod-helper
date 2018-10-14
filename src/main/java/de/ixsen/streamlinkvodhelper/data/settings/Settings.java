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

    private String pathStreamlink;
    private String player;
    private String videoAmount;
    private String defaultQuality;

    public Settings() {
        this.pathStreamlink = "";
        this.player = "";
        this.videoAmount = "50";
        this.defaultQuality = "";
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
            LoggerHelper.logger().severe("Creating settings file failed");
        }
    }

    public static void saveSettings(Settings settings) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            new Gson().toJson(settings, writer);
        } catch (IOException e) {
            LoggerHelper.logger().severe("Writing settings file failed");
        }
        LoggerHelper.logger().info("Saved new settings");
    }

    public String getPathStreamlink() {
        return this.pathStreamlink;
    }

    public String getPlayer() {
        return this.player;
    }

    public String getVideoAmount() {
        return this.videoAmount;
    }

    public String getDefaultQuality() {
        return this.defaultQuality;
    }

    public void makeChanges(String pathStreamlink, String videoAmount, String player, String defaultQuality) {
        this.pathStreamlink = pathStreamlink;
        this.player = player;
        this.videoAmount = videoAmount;
        this.defaultQuality = defaultQuality;
    }

    public String getClientId() {
        return "6qxg5pw6ecl25072ehzzpiboa49nxe";
    }
}

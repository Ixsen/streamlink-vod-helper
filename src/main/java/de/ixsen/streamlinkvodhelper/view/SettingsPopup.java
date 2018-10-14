package de.ixsen.streamlinkvodhelper.view;

import de.ixsen.streamlinkvodhelper.custom.components.NumericTextField;
import de.ixsen.streamlinkvodhelper.data.settings.Settings;
import de.ixsen.streamlinkvodhelper.utils.LoggerHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class SettingsPopup extends BorderPane implements LoggerHelper {

    private final Settings settings;
    @FXML
    private TextField pathStreamlink;
    @FXML
    private NumericTextField videoAmount;
    @FXML
    private TextField freeSlot;
    @FXML
    private TextField defaultQuality;

    public SettingsPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("SettingsPopup.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            this.getLogger().severe("Could not load popup");
        }
        this.settings = Settings.getSettings();

        this.pathStreamlink.setText(this.settings.getPathStreamlink());
        this.videoAmount.setRange(1, 100);
        this.videoAmount.setText(this.settings.getVideoAmount());
        this.freeSlot.setText(this.settings.getPlayer());
        this.defaultQuality.setText(this.settings.getDefaultQuality());
    }

    public static void show(StackPane parent) {
        SettingsPopup settingsPopup = new SettingsPopup();
        ObservableList<Node> children = parent.getChildren();
        children.forEach(n -> n.setEffect(new BoxBlur()));
        children.add(settingsPopup);
    }


    @FXML
    private void saveClicked() {
        this.settings.makeChanges(this.pathStreamlink.getText(), this.videoAmount.getText(), this.freeSlot.getText(), this.defaultQuality.getText());
        de.ixsen.streamlinkvodhelper.data.settings.Settings.saveSettings(this.settings);
        this.cancel();
    }

    @FXML
    private void cancel() {
        StackPane parent = (StackPane) this.getParent();
        parent.getChildren().remove(this);
        parent.getChildren().forEach(n -> n.setEffect(null));
    }
}

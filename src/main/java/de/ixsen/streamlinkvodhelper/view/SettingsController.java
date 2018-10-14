package de.ixsen.streamlinkvodhelper.view;

import de.ixsen.streamlinkvodhelper.custom.components.NumericTextField;
import de.ixsen.streamlinkvodhelper.data.settings.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private TextField pathStreamlink;
    @FXML
    private NumericTextField videoAmount;
    @FXML
    private TextField freeSlot;
    @FXML
    private TextField defaultQuality;

    private final Settings settings;

    public SettingsController() {
        this.settings = Settings.getSettings();
    }

    @FXML
    private void initialize() {
        this.pathStreamlink.setText(this.settings.getPathStreamlink());
        this.videoAmount.setRange(1, 100);
        this.videoAmount.setText(this.settings.getVideoAmount());
        this.freeSlot.setText(this.settings.getPlayer());
        this.defaultQuality.setText(this.settings.getDefaultQuality());
    }

    @FXML
    private void saveClicked() {
        this.settings.makeChanges(this.pathStreamlink.getText(), this.videoAmount.getText(), this.freeSlot.getText(), this.defaultQuality.getText());
        Settings.saveSettings(this.settings);
        this.cancel();
    }

    @FXML
    private void cancel() {
        ((Stage) this.pathStreamlink.getScene().getWindow()).close();
    }
}

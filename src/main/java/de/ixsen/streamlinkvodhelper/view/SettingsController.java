package de.ixsen.streamlinkvodhelper.view;

import de.ixsen.streamlinkvodhelper.data.settings.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private TextField pathStreamlink;
    @FXML
    private TextField clientId;
    @FXML
    private TextField freeSlot;

    private final Settings settings;

    public SettingsController() {
        this.settings = Settings.getSettings();
    }

    @FXML
    private void initialize() {
        this.pathStreamlink.setText(this.settings.getPathStreamlink());
        this.clientId.setText(this.settings.getClientId());
        this.freeSlot.setText(this.settings.getPlayer());
    }

    @FXML
    private void saveClicked() {
        this.settings.makeChanges(this.pathStreamlink.getText(), this.clientId.getText(), this.freeSlot.getText());
        Settings.saveSettings(this.settings);
        this.cancel();
    }

    @FXML
    private void cancel() {
        ((Stage) this.pathStreamlink.getScene().getWindow()).close();
    }
}

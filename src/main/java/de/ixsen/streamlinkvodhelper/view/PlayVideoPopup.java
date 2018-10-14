package de.ixsen.streamlinkvodhelper.view;

import de.ixsen.streamlinkvodhelper.data.QualityOptions;
import de.ixsen.streamlinkvodhelper.data.VideoDTO;
import de.ixsen.streamlinkvodhelper.data.settings.Settings;
import de.ixsen.streamlinkvodhelper.utils.LoggerHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PlayVideoPopup extends BorderPane implements LoggerHelper {

    private VideoDTO videoDTO;
    private LoadVideoCallback loadVideoCallback;
    @FXML
    private ComboBox<String> quality;
    @FXML
    private Button playButton;

    public PlayVideoPopup(VideoDTO videoDTO, LoadVideoCallback loadVideoCallback) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("PlayVideoPopup.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            this.getLogger().severe("Could not load popup");
        }
        this.loadVideoCallback = loadVideoCallback;
        this.videoDTO = videoDTO;

        this.quality.setPadding(new Insets(5.0d));

        this.quality.getItems().addAll(Arrays.stream(QualityOptions.values()).map(QualityOptions::getValue).collect(Collectors.toList()));
        this.quality.setOnAction(this::selectionChanged);
    }

    public static void show(StackPane parent, VideoDTO videoDTO, LoadVideoCallback loadVideo) {
        String defaultQuality = Settings.getSettings().getDefaultQuality();
        if (!defaultQuality.isEmpty()) {
            loadVideo.loadVideo(videoDTO.getTitle(), videoDTO.getVideoUrl(), videoDTO.getCreationDate(), defaultQuality);
        } else {
            PlayVideoPopup playVideoPopup = new PlayVideoPopup(videoDTO, loadVideo);
            ObservableList<Node> children = parent.getChildren();
            children.forEach(n -> n.setEffect(new BoxBlur()));
            children.add(playVideoPopup);
        }
    }

    private void selectionChanged(ActionEvent actionEvent) {
        this.playButton.setDisable(false);
    }

    @FXML
    private void onCancel() {
        StackPane parent = (StackPane) this.getParent();
        parent.getChildren().remove(this);
        parent.getChildren().forEach(n -> n.setEffect(null));
    }

    @FXML
    private void playClicked() {
        this.loadVideoCallback.loadVideo(this.videoDTO.getTitle(), this.videoDTO.getVideoUrl(), this.videoDTO.getCreationDate(), this.quality.getSelectionModel().getSelectedItem().toString());
        this.onCancel();
    }

    public void setStuff(VideoDTO videoDTO, LoadVideoCallback loadVideoCallback) {
        this.loadVideoCallback = loadVideoCallback;
        this.videoDTO = videoDTO;
    }

    @FunctionalInterface
    public interface LoadVideoCallback {
        void loadVideo(String title, String url, String date, String quality);
    }
}


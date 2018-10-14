package de.ixsen.streamlinkvodhelper.view;

import de.ixsen.streamlinkvodhelper.data.QualityOptions;
import de.ixsen.streamlinkvodhelper.data.VideoDTO;
import de.ixsen.streamlinkvodhelper.utils.LoggerHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PlayVideoPopup extends AnchorPane {

    private final VideoDTO videoDTO;
    private LoadVideoCallback loadVideoCallback;

    @FXML
    private ComboBox<String> quality;
    @FXML
    private Button cancelButton;
    @FXML
    private Button playButton;


    public PlayVideoPopup(VideoDTO videoDTO, LoadVideoCallback loadVideoCallback) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("PlayVideoPopup.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException exc) {
            LoggerHelper.logger().severe("Creating Search Result failed");
        }
        this.loadVideoCallback = loadVideoCallback;
        this.videoDTO = videoDTO;

        this.quality.setPadding(new Insets(5.0d));

        this.quality.getItems().addAll(Arrays.stream(QualityOptions.values()).map(QualityOptions::getValue).collect(Collectors.toList()));
        this.quality.setOnAction(this::selectionChanged);

        this.playButton.setOnMouseClicked(this::playClicked);
        this.cancelButton.setOnMouseClicked(this::onCancel);
    }

    private void selectionChanged(ActionEvent actionEvent) {
        this.playButton.setDisable(false);
    }

    @FXML
    private void onCancel(MouseEvent mouseEvent) {
        ((Stage) this.getScene().getWindow()).close();
    }

    @FXML
    private void playClicked(MouseEvent mouseEvent) {
        this.loadVideoCallback.loadVideo(this.videoDTO.getTitle(), this.videoDTO.getVideoUrl(), this.videoDTO.getCreationDate(), this.quality.getSelectionModel().getSelectedItem().toString());
        this.onCancel(mouseEvent);
    }


    public static void show(VideoDTO videoDTO, LoadVideoCallback loadVideo) {
        PlayVideoPopup popup = new PlayVideoPopup(videoDTO, loadVideo);
        Scene scene = new Scene(popup);
        Stage stage = new Stage();
        stage.setTitle("Quality Options");
        stage.setScene(scene);
        stage.show();
    }

    @FunctionalInterface
    public interface LoadVideoCallback {
        void loadVideo(String title, String url, String date, String quality);
    }
}


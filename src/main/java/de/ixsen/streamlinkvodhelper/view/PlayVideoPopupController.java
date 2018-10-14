package de.ixsen.streamlinkvodhelper.view;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class PlayVideoPopupController extends AnchorPane {

    private ComboBox<String> quality;
    private Button playButton;
    private Button cancelButton;
    private LoadVideoCallback loadVideoCallback;

    public PlayVideoPopupController(List<String> options, LoadVideoCallback loadVideoCallback) {
        this.loadVideoCallback = loadVideoCallback;

        this.quality = new ComboBox<>();
        this.quality.getItems().addAll(options);
        this.quality.setStyle("margin: 5px 5px 5px 0");

        this.playButton = new Button("Play");
        this.playButton.setStyle("margin: 5px 5px 0 5px");
        this.playButton.setOnMouseClicked(this::playClicked);

        this.cancelButton = new Button("Cancel");
        this.cancelButton.setStyle("margin: 5px");

        HBox buttonBar = new HBox();
        buttonBar.getChildren().addAll(this.playButton, this.cancelButton);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(this.quality, buttonBar);


    }

    private void playClicked(MouseEvent mouseEvent) {
        this.loadVideoCallback.loadVideo(null, null, null);
    }


    @FunctionalInterface
    interface LoadVideoCallback {
        void loadVideo(String title, String url, String date);
    }
}


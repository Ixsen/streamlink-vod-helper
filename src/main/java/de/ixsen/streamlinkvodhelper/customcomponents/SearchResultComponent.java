package de.ixsen.streamlinkvodhelper.customcomponents;

import de.ixsen.streamlinkvodhelper.utils.LoggerHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.logging.Logger;

public class SearchResultComponent extends AnchorPane {

    @FXML
    private ImageView thumbnail;
    @FXML
    private Label duration;
    @FXML
    private Label title;
    @FXML
    private Label pubDate;

    public SearchResultComponent(String imageUrl, String duration, String title, String pubDate) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("SearchResult.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException exc) {
            LoggerHelper.getLogger().severe("Creating Search Result failed");
        }
        this.thumbnail.setImage(new Image(imageUrl));
        this.duration.setText(duration);
        this.title.setText(title);
        this.pubDate.setText(pubDate);
    }

    public Label getDuration() {
        return this.duration;
    }

    public void setDuration(Label duration) {
        this.duration = duration;
    }

    public Label getTitle() {
        return this.title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    public Label getPubDate() {
        return this.pubDate;
    }

    public void setPubDate(Label pubDate) {
        this.pubDate = pubDate;
    }

    public ImageView getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(ImageView thumbnail) {
        this.thumbnail = thumbnail;
    }
}

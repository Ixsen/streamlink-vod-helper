package de.ixsen.streamlinkvodhelper;

import de.ixsen.streamlinkvodhelper.utils.CacheUtils;
import de.ixsen.streamlinkvodhelper.utils.DatabaseUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
        CacheUtils.initialize();
        DatabaseUtils.loadDatabase();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void start(Stage window) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("view/MainView.fxml"));
        Scene scene = new Scene(root);

        window.setScene(scene);
        window.setTitle("Streamlink Vod Helper");
        window.show();
    }

}

package de.ixsen.streamlinkvodhelper.main;

import de.ixsen.streamlinkvodhelper.main.persistance.DatabaseActions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    public static final String LINK = "http://www.twitch.tv";

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        DatabaseActions.loadDatabase();
    }

    @Override
    public void stop() throws Exception {
        this.persistCookies();
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

    private void persistCookies() throws IOException, URISyntaxException {
        File file = new File("cookies.sav");
        FileWriter fileWriter = new FileWriter(file);
        List<HttpCookie> httpCookies = new ArrayList<>(((CookieManager) CookieHandler.getDefault()).getCookieStore().getCookies());
        for (HttpCookie httpCookie : httpCookies) {
            String domain = httpCookie.getDomain();
            if (domain.contains("twitch.tv")) {
                String name = httpCookie.getName();
                String value = httpCookie.getValue();
                String line = domain + "\\\\" + name + "\\\\" + value;
                fileWriter.write(line);
                fileWriter.write(System.lineSeparator());
            }
        }
        fileWriter.close();
    }
}

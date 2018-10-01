package de.ixsen.streamlinkvodhelper.main.view;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import static de.ixsen.streamlinkvodhelper.main.Main.LINK;

public class MainViewController {

    @FXML
    private WebView webview;

    public MainViewController() {
    }

    @FXML
    public void initialize() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        this.webview.getEngine().load(LINK);
        this.webview.getEngine().getLoadWorker().stateProperty().addListener(this::test);
    }

    private void test(ObservableValue ov, Worker.State oldState, Worker.State newState) {
        if (newState == Worker.State.SCHEDULED) {
            System.out.println("LOADED");
        }
    }

    @FXML
    private void openInPlayerClicked() throws IOException {
        String link = this.webview.getEngine().getLocation();
        Runtime.getRuntime().exec(String.format("\"C:\\Program Files (x86)\\Streamlink\\bin\\streamlink.exe\" --player-passthrough hls %s best", link));
    }
}

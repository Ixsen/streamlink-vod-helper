package de.ixsen.streamlinkvodhelper.main.view;

import com.google.gson.JsonArray;
import de.ixsen.streamlinkvodhelper.main.customcomponents.LinkButton;
import de.ixsen.streamlinkvodhelper.main.data.HistoryDTO;
import de.ixsen.streamlinkvodhelper.main.data.LinkDTO;
import de.ixsen.streamlinkvodhelper.main.data.SearchType;
import de.ixsen.streamlinkvodhelper.main.utils.DatabaseActions;
import de.ixsen.streamlinkvodhelper.main.utils.Dialogs;
import de.ixsen.streamlinkvodhelper.main.utils.HtmlCalls;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Collections;
import java.util.List;

import static de.ixsen.streamlinkvodhelper.main.Main.LINK;

public class MainViewController {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<SearchType> searchTypeCombobox;
    @FXML
    private Tab browseTab;
    @FXML
    private Tab historyTab;
    @FXML
    private Pane hSpacer;
    @FXML
    private ProgressIndicator calcIndicator;
    @FXML
    private TableView<HistoryDTO> historyTable;
    @FXML
    private VBox links;
    @FXML
    private WebView webview;

    private WebEngine webviewEngine;

    public MainViewController() {
    }

    @FXML
    public void initialize() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        HBox.setHgrow(this.hSpacer, Priority.SOMETIMES);

        this.webviewEngine = this.webview.getEngine();
        this.webviewEngine.load(LINK);
        this.reloadLinks();
        this.initializeTable();
        this.reloadHistory();
        this.initializeSearchTypes();
//        this.webviewEngine.getLoadWorker().stateProperty().addListener(this::websiteLoaded);
    }

    private void initializeTable() {
        this.historyTable.getVisibleLeafColumn(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        this.historyTable.getVisibleLeafColumn(0).prefWidthProperty().bind(this.historyTable.widthProperty().divide(2.5));
        this.historyTable.getVisibleLeafColumn(1).setCellValueFactory(new PropertyValueFactory<>("url"));
        this.historyTable.getVisibleLeafColumn(1).prefWidthProperty().bind(this.historyTable.widthProperty().divide(2.5));
        this.historyTable.getVisibleLeafColumn(2).setCellValueFactory(new PropertyValueFactory<>("date"));
        this.historyTable.getVisibleLeafColumn(2).prefWidthProperty().bind(this.historyTable.widthProperty().divide(5));
    }

    private void initializeSearchTypes() {
        for (SearchType value : SearchType.values()) {
            this.searchTypeCombobox.getItems().add(value);
        }
    }

//    private void websiteLoaded(ObservableValue ov, Worker.State oldState, Worker.State newState) {
//        if (newState == Worker.State.SCHEDULED) {
//            System.out.println("LOADED");
//        }
//    }

    @FXML
    private void openInPlayerClicked() throws IOException, InterruptedException {
        String link = this.webviewEngine.getLocation();
        String title = this.webviewEngine.getTitle();

        ProcessBuilder processBuilder = new ProcessBuilder("streamlink", "--player-passthrough", "hls", link, "best").inheritIO();
        ProcessRunner processRunner = new ProcessRunner(processBuilder, this.calcIndicator);
        processRunner.start();

        DatabaseActions.addToHistory(title, link, "SOMEDAY");
    }

    @FXML
    private void addLink() {
        String title = this.webviewEngine.getTitle();
        String url = this.webviewEngine.getLocation();
        DatabaseActions.addToLinks(title, url);
        this.reloadLinks();
    }

    @FXML
    private void settingsClicked() {

    }

    @FXML
    private void startSearch() {
        SearchType selectedItem = this.searchTypeCombobox.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            switch (selectedItem) {
                case Streamer:
                    JsonArray vodsByLogin = HtmlCalls.getVodsByLogin(this.searchField.getText());
                    Dialogs.info("VODS FOUND!", "There are many: " + vodsByLogin.size());
                    break;
                case Game:
                    Dialogs.warning("Not implemented yet");
                    break;
            }
        } else {
            Dialogs.info("Please select a search type");
        }
    }

    private void reloadLinks() {
        this.links.getChildren().clear();
        List<LinkDTO> links = DatabaseActions.getLinks();
        for (LinkDTO link : links) {
            LinkButton button = new LinkButton(link.getId(), link.getName(), link.getUrl());
            button.addDeleteCallback(this::reloadLinks);
            button.setOnMouseClicked(e -> this.onLinkClicked(e, button));
            this.links.getChildren().add(button);
        }
    }

    private void reloadHistory() {
        this.historyTable.getItems().clear();
        List<HistoryDTO> history = DatabaseActions.getHistory();
        Collections.reverse(history);
        for (HistoryDTO historyDTO : history) {
            this.historyTable.getItems().add(historyDTO);
        }
    }

    private void onLinkClicked(MouseEvent e, LinkButton button) {
        if (e.getButton() == MouseButton.PRIMARY) {
            this.webviewEngine.load(button.getUrl());
        }
    }


    public void tableRowDoubleClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            HistoryDTO selectedItem = this.historyTable.getSelectionModel().getSelectedItem();
            this.webviewEngine.load(selectedItem.getUrl());
            this.browseTab.getTabPane().getSelectionModel().select(this.browseTab);
        }
    }

    @FXML
    private void searchFieldKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            this.startSearch();
        }
    }
}

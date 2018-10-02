package de.ixsen.streamlinkvodhelper.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.ixsen.streamlinkvodhelper.customcomponents.LinkButton;
import de.ixsen.streamlinkvodhelper.customcomponents.ProcessRunner;
import de.ixsen.streamlinkvodhelper.customcomponents.SearchResultComponent;
import de.ixsen.streamlinkvodhelper.data.HistoryDTO;
import de.ixsen.streamlinkvodhelper.data.LinkDTO;
import de.ixsen.streamlinkvodhelper.data.SearchType;
import de.ixsen.streamlinkvodhelper.data.settings.Settings;
import de.ixsen.streamlinkvodhelper.utils.DatabaseActions;
import de.ixsen.streamlinkvodhelper.utils.Dialogs;
import de.ixsen.streamlinkvodhelper.utils.HtmlCalls;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainViewController {

    @FXML
    private ComboBox<SearchType> searchType;
    @FXML
    private FlowPane searchResults;
    @FXML
    private TextField searchField;
    @FXML
    private Tab browseTab;
    @FXML
    private Pane hSpacer;
    @FXML
    private ProgressIndicator calcIndicator;
    @FXML
    private TableView<HistoryDTO> historyTable;
    @FXML
    private VBox links;

    private Integer currentId;


    public MainViewController() {
    }

    @FXML
    public void initialize() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        HBox.setHgrow(this.hSpacer, Priority.SOMETIMES);
        this.searchType.getItems().addAll(Arrays.asList(SearchType.values()));
        this.reloadLinks();
        this.initializeTable();
        this.reloadHistory();
    }

    private void initializeTable() {
        this.historyTable.getVisibleLeafColumn(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        this.historyTable.getVisibleLeafColumn(0).prefWidthProperty().bind(this.historyTable.widthProperty().divide(2.5));
        this.historyTable.getVisibleLeafColumn(1).setCellValueFactory(new PropertyValueFactory<>("url"));
        this.historyTable.getVisibleLeafColumn(1).prefWidthProperty().bind(this.historyTable.widthProperty().divide(2.5));
        this.historyTable.getVisibleLeafColumn(2).setCellValueFactory(new PropertyValueFactory<>("date"));
        this.historyTable.getVisibleLeafColumn(2).prefWidthProperty().bind(this.historyTable.widthProperty().divide(5));
    }

    private void loadVideo(String title, String url, String date) {
        String pathStreamlink = Settings.getSettings().getPathStreamlink();
        pathStreamlink = pathStreamlink.isEmpty()
                ? "streamlink"
                : pathStreamlink;

        ProcessBuilder processBuilder = new ProcessBuilder(pathStreamlink, "--player-passthrough", "hls", url, "best").inheritIO();
        ProcessRunner processRunner = new ProcessRunner(processBuilder, this.calcIndicator);
        processRunner.start();

        DatabaseActions.addToHistory(title, url, date);
        this.reloadHistory();
    }

    @FXML
    private void addLink() {
        if (this.currentId != null) {
            DatabaseActions.addToLinks(this.searchField.getText(), this.currentId);
            this.reloadLinks();
        } else {
            Dialogs.info("Please search for runSearch streamer to save runSearch search link");
        }
    }

    @FXML
    private void settingsClicked() throws IOException {
        Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("Settings.fxml")));
        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void startSearch() {
        this.search(this.searchField.getText());
    }

    private void search(String userName) {
        int userIdByLogin = HtmlCalls.getUserIdByLogin(userName);
        if (!Objects.equals(this.currentId, userIdByLogin)) {
            this.currentId = userIdByLogin;
        }
        this.searchResults.getChildren().clear();
        JsonArray vodsByLogin = HtmlCalls.getVodsByUserId(userIdByLogin, this.searchType.getValue());
        for (JsonElement elem : vodsByLogin) {
            JsonObject jsonObject = elem.getAsJsonObject();

            String thumbnail_url = jsonObject.get("thumbnail_url").getAsString().replace("%{width}", "320").replace("%{height}", "180");
            String duration = jsonObject.get("duration").getAsString();
            String title = jsonObject.get("title").getAsString();
            String created_at = jsonObject.get("created_at").getAsString();
            String url = jsonObject.get("url").getAsString();

            SearchResultComponent searchResultComponent = new SearchResultComponent(thumbnail_url, duration, title, created_at);
            searchResultComponent.setOnMouseClicked(e -> this.loadVideo(title, url, created_at));
            this.searchResults.getChildren().add(searchResultComponent);
        }
    }

    private void reloadLinks() {
        this.links.getChildren().clear();
        List<LinkDTO> links = DatabaseActions.getLinks();
        for (LinkDTO link : links) {
            LinkButton button = new LinkButton(link.getId(), link.getName(), link.getUserId());
            button.addDeleteCallback(this::reloadLinks);
            button.setOnMouseClicked(e -> this.onLinkClicked(e, button));
            this.links.getChildren().add(button);
        }
    }

    private void onLinkClicked(MouseEvent e, LinkButton button) {
        if (e.getButton() == MouseButton.PRIMARY) {
            String userName = button.getText();
            this.searchField.setText(button.getText());
            this.search(userName);
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


    public void tableRowDoubleClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            HistoryDTO selectedItem = this.historyTable.getSelectionModel().getSelectedItem();
            this.loadVideo(selectedItem.getName(), selectedItem.getUrl(), selectedItem.getDate());
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

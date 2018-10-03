package de.ixsen.streamlinkvodhelper.view;

import de.ixsen.streamlinkvodhelper.custom.PlayVideoCalculation;
import de.ixsen.streamlinkvodhelper.custom.SearchCalculation;
import de.ixsen.streamlinkvodhelper.custom.components.LinkButton;
import de.ixsen.streamlinkvodhelper.custom.components.SearchResult;
import de.ixsen.streamlinkvodhelper.data.HistoryDTO;
import de.ixsen.streamlinkvodhelper.data.LinkDTO;
import de.ixsen.streamlinkvodhelper.data.SearchType;
import de.ixsen.streamlinkvodhelper.data.settings.Settings;
import de.ixsen.streamlinkvodhelper.utils.DatabaseUtils;
import de.ixsen.streamlinkvodhelper.utils.DialogUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public void loadVideo(String title, String url, String date) {
        String pathStreamlink = Settings.getSettings().getPathStreamlink();
        pathStreamlink = pathStreamlink.isEmpty()
                ? "streamlink"
                : pathStreamlink;

        ProcessBuilder processBuilder = new ProcessBuilder(pathStreamlink, "--player-passthrough", "hls", url, "best", "--player", Settings.getSettings().getPlayer()).inheritIO();
        PlayVideoCalculation playVideoCalculation = new PlayVideoCalculation(processBuilder, this.calcIndicator);
        playVideoCalculation.start();

        DatabaseUtils.addToHistory(title, url, date);
        this.reloadHistory();
    }

    @FXML
    private void addLink() {
        if (this.currentId != null) {
            DatabaseUtils.addToLinks(this.searchField.getText(), this.currentId);
            this.reloadLinks();
        } else {
            DialogUtils.info("Please search for streamer to save a shortcut");
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

    private void search(String loginName) {
        this.searchResults.getChildren().clear();

        SearchCalculation searchCalculation = new SearchCalculation(this.calcIndicator, loginName, this.searchType.getValue(), this);
        searchCalculation.start();
    }

    public void fillSearchResults(List<SearchResult> searchResults) {
        if (searchResults.isEmpty()) {
            DialogUtils.info("No videos found");
        } else {
            this.searchResults.getChildren().addAll(searchResults);
        }
    }

    private void reloadLinks() {
        this.links.getChildren().clear();
        List<LinkDTO> links = DatabaseUtils.getLinks();
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
        List<HistoryDTO> history = DatabaseUtils.getHistory();
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

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }
}

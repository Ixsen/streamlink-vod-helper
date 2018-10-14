package de.ixsen.streamlinkvodhelper.view;

import de.ixsen.streamlinkvodhelper.custom.PlayVideoCalculation;
import de.ixsen.streamlinkvodhelper.custom.SearchCalculation;
import de.ixsen.streamlinkvodhelper.custom.components.CalcIndicator;
import de.ixsen.streamlinkvodhelper.custom.components.LinkButton;
import de.ixsen.streamlinkvodhelper.custom.components.SearchResult;
import de.ixsen.streamlinkvodhelper.data.HistoryDTO;
import de.ixsen.streamlinkvodhelper.data.LinkDTO;
import de.ixsen.streamlinkvodhelper.data.SearchType;
import de.ixsen.streamlinkvodhelper.data.VideoDTO;
import de.ixsen.streamlinkvodhelper.utils.DatabaseUtils;
import de.ixsen.streamlinkvodhelper.utils.DialogUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainViewController {

    @FXML
    private TabPane tabPane;
    @FXML
    private StackPane root;
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
    private CalcIndicator calcIndicator;
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

    public void loadVideo(String title, String url, String date, String quality) {
        String pathStreamlink = de.ixsen.streamlinkvodhelper.data.settings.Settings.getSettings().getPathStreamlink();
        pathStreamlink = pathStreamlink.isEmpty()
                ? "streamlink"
                : pathStreamlink;

        PlayVideoCalculation playVideoCalculation = new PlayVideoCalculation(this.calcIndicator,
                pathStreamlink,
                "--player-passthrough", "hls",
                url,
                quality,
                "--player", de.ixsen.streamlinkvodhelper.data.settings.Settings.getSettings().getPlayer(),
                "--player-http");
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
    private void settingsClicked() {
        SettingsPopup.show(this.root);
    }

    @FXML
    private void startSearch() {
        this.search(this.searchField.getText());
    }

    private void search(String loginName) {
        this.searchResults.getChildren().clear();

        SearchType value = this.searchType.getValue();
        if (value == null) {
            value = SearchType.all;
            this.searchType.setValue(value);
        }

        SearchCalculation searchCalculation = new SearchCalculation(this.root, this.calcIndicator, loginName, value, this);
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
            PlayVideoPopup.show(this.root, new VideoDTO(selectedItem.getName(), selectedItem.getUrl(), selectedItem.getDate()), this::loadVideo);
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

package de.ixsen.streamlinkvodhelper.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.ixsen.streamlinkvodhelper.custom.components.SearchResult;
import de.ixsen.streamlinkvodhelper.data.SearchType;
import de.ixsen.streamlinkvodhelper.utils.CacheUtils;
import de.ixsen.streamlinkvodhelper.utils.HtmlCallUtils;
import de.ixsen.streamlinkvodhelper.utils.LoggerHelper;
import de.ixsen.streamlinkvodhelper.view.MainViewController;
import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class SearchCalculation extends AbstractCalculation implements LoggerHelper {

    private final SearchType searchType;
    private final String loginName;
    private int userId;
    private final MainViewController controller;
    private List<SearchResult> searchResults;

    public SearchCalculation(ProgressIndicator calcIndicator, String loginName, SearchType searchType, MainViewController controller) {
        super(calcIndicator);
        this.searchResults = new ArrayList<>();
        this.loginName = loginName;
        this.searchType = searchType;
        this.controller = controller;
    }

    @Override
    void init() {
        // do nothing
    }

    @Override
    void calculate() {
        this.userId = HtmlCallUtils.getUserIdByLogin(this.loginName);

        JsonArray vodsByLogin = HtmlCallUtils.getVodsByUserId(this.userId, this.searchType);
        for (JsonElement elem : vodsByLogin) {
            JsonObject jsonObject = elem.getAsJsonObject();

            String thumbnailUrl = jsonObject.get("thumbnail_url").getAsString().replace("%{width}", "320").replace("%{height}", "180");
            String duration = jsonObject.get("duration").getAsString();
            String title = jsonObject.get("title").getAsString();
            String creationDate = jsonObject.get("created_at").getAsString();
            String videoUrl = jsonObject.get("url").getAsString();

            String cachedThumbnail = CacheUtils.cacheImage(thumbnailUrl);

            SearchResult searchResult = new SearchResult(cachedThumbnail, duration, title, creationDate);

            searchResult.setOnMouseClicked(e -> this.controller.loadVideo(title, videoUrl, creationDate));
            this.searchResults.add(searchResult);
        }
    }

    @Override
    void closure() {
        Platform.runLater(() -> {
            this.controller.fillSearchResults(this.searchResults);
            this.controller.setCurrentId(this.userId);
        });
    }

}

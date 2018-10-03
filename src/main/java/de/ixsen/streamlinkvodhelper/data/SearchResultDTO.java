package de.ixsen.streamlinkvodhelper.data;

public class SearchResultDTO {

    private final String thumbnailUrl;
    private final String duration;
    private final String title;
    private final String creationDate;
    private final String videoUrl;

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public SearchResultDTO(String thumbnailUrl, String duration, String title, String creationDate, String videoUrl) {
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        this.title = title;
        this.creationDate = creationDate;
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCreationDate() {
        return this.creationDate;
    }
}

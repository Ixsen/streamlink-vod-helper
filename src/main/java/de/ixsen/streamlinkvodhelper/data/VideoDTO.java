package de.ixsen.streamlinkvodhelper.data;

public class VideoDTO {
    private String title;
    private String videoUrl;
    private String creationDate;

    public VideoDTO(String title, String videoUrl, String creationDate) {
        this.title = title;
        this.videoUrl = videoUrl;
        this.creationDate = creationDate;
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

}


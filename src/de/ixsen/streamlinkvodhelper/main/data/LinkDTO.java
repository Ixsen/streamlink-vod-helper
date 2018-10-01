package de.ixsen.streamlinkvodhelper.main.data;

public class LinkDTO {
    private final int id;
    private String name;
    private String url;

    public LinkDTO(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public int getId() {
        return this.id;
    }
}

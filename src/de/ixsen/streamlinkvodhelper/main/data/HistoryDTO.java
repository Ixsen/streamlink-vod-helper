package de.ixsen.streamlinkvodhelper.main.data;

public class HistoryDTO {
    private final int id;
    private String name;
    private String url;
    private String date;

    public int getId() {
        return this.id;
    }

    public HistoryDTO(int id, String name, String url, String date) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.date = date;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getDate() {
        return this.date;
    }

}

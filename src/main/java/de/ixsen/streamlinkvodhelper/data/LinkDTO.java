package de.ixsen.streamlinkvodhelper.data;

public class LinkDTO {
    private final int id;
    private final String name;
    private final int userId;

    public LinkDTO(int id, String name, int userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getId() {
        return this.id;
    }
}

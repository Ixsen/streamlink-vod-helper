package de.ixsen.streamlinkvodhelper.utils;

public interface SQLCommands {
    String CREATE_LINK_TABLE = "CREATE TABLE IF NOT EXISTS links(\n" +
            "\tid integer PRIMARY KEY,\n" +
            "\tname text NOT NULL UNIQUE,\n" +
            "\tuser_id integer NOT NULL\n" +
            ");";
    String CREATE_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS history (\n" +
            "\tid integer PRIMARY KEY,\n" +
            "\tname text NOT NULL,\n" +
            "\turl text NOT NULL,\n" +
            "\tdate text NOT NULL\n" +
            ");";

    String LOAD_LINKS = "SELECT * FROM links";
    String LOAD_HISTORY = "SELECT * FROM history";

    String INSERT_LINK = "INSERT INTO links(name,user_id) VALUES(?,?)";
    String INSERT_HISTORY = "INSERT INTO history(name,url,date) VALUES(?,?,?)";

    String DELETE_LINK = "DELETE FROM links WHERE id = ?";
    String DELETE_HISTORY = "DELETE FROM history WHERE id = ?";
}

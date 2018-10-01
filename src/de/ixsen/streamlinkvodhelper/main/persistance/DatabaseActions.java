package de.ixsen.streamlinkvodhelper.main.persistance;

import de.ixsen.streamlinkvodhelper.main.data.HistoryDTO;
import de.ixsen.streamlinkvodhelper.main.data.LinkDTO;
import de.ixsen.streamlinkvodhelper.main.log.HasLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static de.ixsen.streamlinkvodhelper.main.persistance.SQLCommands.CREATE_HISTORY_DB;
import static de.ixsen.streamlinkvodhelper.main.persistance.SQLCommands.CREATE_LINK_DB;
import static de.ixsen.streamlinkvodhelper.main.persistance.SQLCommands.DELETE_HISTORY;
import static de.ixsen.streamlinkvodhelper.main.persistance.SQLCommands.DELETE_LINK;
import static de.ixsen.streamlinkvodhelper.main.persistance.SQLCommands.INSERT_HISTORY;
import static de.ixsen.streamlinkvodhelper.main.persistance.SQLCommands.INSERT_LINK;
import static de.ixsen.streamlinkvodhelper.main.persistance.SQLCommands.LOAD_HISTORY;
import static de.ixsen.streamlinkvodhelper.main.persistance.SQLCommands.LOAD_LINKS;

public class DatabaseActions implements HasLogger {


    public static void loadDatabase() {
        executeSql(CREATE_LINK_DB);
        executeSql(CREATE_HISTORY_DB);
        HasLogger.getLogger().log(Level.INFO, "Database connected");
    }

    public static void addToHistory(String name, String url, String date) {
        try (PreparedStatement preparedStatement = getPreparedStatement(INSERT_HISTORY)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, url);
            preparedStatement.setString(3, date);
            preparedStatement.executeUpdate();
            HasLogger.getLogger().log(Level.INFO, String.format("History added: %s, %s, %s", name, url, date));
        } catch (SQLException e) {
            HasLogger.getLogger().log(Level.SEVERE, "Could not insert history into database", e);
        }
    }


    public static void deleteHistoryEntry(int id) {
        try (PreparedStatement preparedStatement = getPreparedStatement(DELETE_HISTORY)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            HasLogger.getLogger().log(Level.INFO, String.format("Deleted history entry with id: %d", id));
        } catch (SQLException e) {
            HasLogger.getLogger().log(Level.SEVERE, "Could not insert history into database", e);
        }
    }

    public static List<HistoryDTO> getHistory() {
        ResultSet resultSet = executeQuery(LOAD_HISTORY);
        List<HistoryDTO> historyDTOS = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String url = resultSet.getString("url");
                String date = resultSet.getString("date");
                historyDTOS.add(new HistoryDTO(id, name, url, date));
                HasLogger.getLogger().log(Level.INFO, String.format("History loaded: %s, %s, %s", name, url, date));
            }
            return historyDTOS;
        } catch (Exception e) {
            throw new RuntimeException("Loading history has failed", e);
        }
    }

    public static void addToLinks(String name, String url) {
        try (PreparedStatement preparedStatement = getPreparedStatement(INSERT_LINK)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, url);
            preparedStatement.executeUpdate();
            HasLogger.getLogger().log(Level.INFO, String.format("Link added: %s, %s", name, url));
        } catch (SQLException e) {
            HasLogger.getLogger().log(Level.SEVERE, "Could not insert link into database", e);
        }
    }

    public static void deleteLink(int id) {
        try (PreparedStatement preparedStatement = getPreparedStatement(DELETE_LINK)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            HasLogger.getLogger().log(Level.INFO, String.format("Deleted link with id: %d", id));
        } catch (SQLException e) {
            HasLogger.getLogger().log(Level.SEVERE, "Could not insert history into database", e);
        }
    }


    public static List<LinkDTO> getLinks() {
        ResultSet resultSet = executeQuery(LOAD_LINKS);
        List<LinkDTO> linkDTOS = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String url = resultSet.getString("url");
                linkDTOS.add(new LinkDTO(id, name, url));
                HasLogger.getLogger().log(Level.INFO, String.format("Link loaded: %s, %s", name, url));
            }
            return linkDTOS;
        } catch (Exception e) {
            throw new RuntimeException("Getting links has failed", e);
        }
    }

    private static void executeSql(String query) {
        try {
            getStatement().execute(query);
        } catch (SQLException e) {
            HasLogger.getLogger().log(Level.SEVERE, "Executing SQL failed", e);
        }
    }

    private static ResultSet executeQuery(String query) {
        try {
            return getStatement().executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException("Executing Query failed", e);
        }
    }

    private static Statement getStatement() throws SQLException {
        Connection connect = connect();
        return connect.createStatement();
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:database.db");
    }

    private static PreparedStatement getPreparedStatement(String queryStatement) throws SQLException {
        return connect().prepareStatement(queryStatement);
    }

}

package de.ixsen.streamlinkvodhelper.utils;

import javafx.scene.control.Alert;

public class Dialogs {

    public static void info(String message) {
        info(null, message);
    }

    public static void info(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    public static void warning(String message) {
        warning(null, message);
    }

    public static void warning(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(message);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    public static void error(String message) {
        error(null, message);
    }

    public static void error(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}

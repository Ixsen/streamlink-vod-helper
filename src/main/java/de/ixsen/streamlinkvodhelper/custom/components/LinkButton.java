package de.ixsen.streamlinkvodhelper.custom.components;

import de.ixsen.streamlinkvodhelper.utils.DatabaseUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class LinkButton extends Button {

    private final int userId;
    private final MenuItem menuItem;
    private final int id;

    public LinkButton(int id, String name, int userId) {
        super(name);
        this.userId = userId;
        this.id = id;

        ContextMenu contextMenu = new ContextMenu();
        this.menuItem = new MenuItem("Delete link");
        contextMenu.getItems().add(this.menuItem);
        this.setContextMenu(contextMenu);
    }

    public int getUserId() {
        return this.userId;
    }

    public void addDeleteCallback(Runnable reloadLinks) {
        this.menuItem.setOnAction(e -> {
            DatabaseUtils.deleteLink(this.id);
            reloadLinks.run();
        });
    }
}

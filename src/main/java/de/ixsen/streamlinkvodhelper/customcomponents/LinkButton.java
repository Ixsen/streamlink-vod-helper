package de.ixsen.streamlinkvodhelper.customcomponents;

import de.ixsen.streamlinkvodhelper.utils.DatabaseActions;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class LinkButton extends Button {

    private final String url;
    private final MenuItem menuItem;
    private final int id;

    public LinkButton(int id, String name, String url) {
        super(name);
        this.url = url;
        this.id = id;

        ContextMenu contextMenu = new ContextMenu();
        this.menuItem = new MenuItem("Delete link");
        contextMenu.getItems().add(this.menuItem);
        this.setContextMenu(contextMenu);
    }

    public String getUrl() {
        return this.url;
    }

    public void addDeleteCallback(Runnable reloadLinks) {
        this.menuItem.setOnAction(e -> {
            DatabaseActions.deleteLink(this.id);
            reloadLinks.run();
        });
    }
}

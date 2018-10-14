package de.ixsen.streamlinkvodhelper.custom.components;

import javafx.scene.control.ProgressIndicator;

public class CalcIndicator extends ProgressIndicator {

    private int visibilityDepth = 0;

    public void setVisible() {
        this.visibilityDepth++;
        this.changeVisibility();
    }

    public void setInvisible() {
        this.visibilityDepth--;
        this.changeVisibility();
    }

    public void changeVisibility() {
        boolean shouldBeVisible = this.visibilityDepth > 0;
        this.setVisible(shouldBeVisible);
    }
}

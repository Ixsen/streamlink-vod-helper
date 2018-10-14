package de.ixsen.streamlinkvodhelper.custom.components;

import javafx.scene.control.ProgressIndicator;

public class CalcIndicator extends ProgressIndicator {

    private int visibilityDepth = 0;


    public void changeVisibility(boolean visible) {
        if (visible) {
            this.visibilityDepth++;
        } else {
            this.visibilityDepth--;
        }

        boolean shouldBeVisible = this.visibilityDepth > 0;
        this.setVisible(shouldBeVisible);
    }
}

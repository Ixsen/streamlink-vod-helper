package de.ixsen.streamlinkvodhelper.custom.components;

import javafx.scene.control.ProgressIndicator;

public class CalcIndicator extends ProgressIndicator {

    private int visiblilityDepth = 0;


    public void changeVisibility(boolean visible) {
        if (visible) {
            this.visiblilityDepth++;
        } else {
            this.visiblilityDepth--;
        }

        boolean shouldBeVisible = this.visiblilityDepth > 0;
        this.setVisible(shouldBeVisible);
    }
}

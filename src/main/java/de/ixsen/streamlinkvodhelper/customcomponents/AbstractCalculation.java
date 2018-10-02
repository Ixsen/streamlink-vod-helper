package de.ixsen.streamlinkvodhelper.customcomponents;

import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;

abstract class AbstractCalculation extends Thread {
    private final ProgressIndicator calcIndicator;

    AbstractCalculation(ProgressIndicator calcIndicator) {
        this.calcIndicator = calcIndicator;
    }

    @Override
    public void run() {
        if (!this.calcIndicator.isVisible()) {
            this.calcIndicator.setVisible(true);
            Platform.runLater(() -> {
                this.calculate();
                this.calcIndicator.setVisible(false);
            });
        } else {
            this.calculate();
        }
    }

    abstract void calculate();
}

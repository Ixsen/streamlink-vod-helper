package de.ixsen.streamlinkvodhelper.custom;

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
            this.init();
            this.calculate();
            this.closure();
            this.calcIndicator.setVisible(false);
        } else {
            this.calculate();
        }
    }

    abstract void init();

    abstract void calculate();

    abstract void closure();
}

package de.ixsen.streamlinkvodhelper.custom;

import de.ixsen.streamlinkvodhelper.custom.components.CalcIndicator;

abstract class AbstractCalculation extends Thread {
    private final CalcIndicator calcIndicator;

    AbstractCalculation(CalcIndicator calcIndicator) {
        this.calcIndicator = calcIndicator;
    }

    @Override
    public void run() {
        try {
            this.calcIndicator.setVisible();
            this.init();
            this.calculate();
            this.closure();
        } finally {
            this.calcIndicator.setInvisible();
        }
    }

    abstract void init();

    abstract void calculate();

    abstract void closure();
}

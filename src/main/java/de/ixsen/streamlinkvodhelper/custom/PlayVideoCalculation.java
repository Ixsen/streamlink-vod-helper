package de.ixsen.streamlinkvodhelper.custom;

import javafx.scene.control.ProgressIndicator;

import java.io.IOException;

public class PlayVideoCalculation extends AbstractCalculation {
    private final ProcessBuilder processBuilder;

    public PlayVideoCalculation(ProcessBuilder processBuilder, ProgressIndicator calcIndicator) {
        super(calcIndicator);
        this.processBuilder = processBuilder;
    }

    @Override
    void init() {
        // do nothing
    }

    @Override
    void calculate() {
        try {
            this.processBuilder.start().waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Thread was destroyed too early", e);
        }
    }

    @Override
    void closure() {
        // do nothing
    }
}

package de.ixsen.streamlinkvodhelper.main.view;

import javafx.scene.control.ProgressIndicator;

import java.io.IOException;

public class ProcessRunner extends Thread {
    private final ProgressIndicator calcIndicator;
    private ProcessBuilder processBuilder;

    public ProcessRunner(ProcessBuilder processBuilder, ProgressIndicator calcIndicator) {
        this.processBuilder = processBuilder;
        this.calcIndicator = calcIndicator;
    }

    @Override
    public void run() {
        try {
            this.calcIndicator.setVisible(true);
            this.processBuilder.start().waitFor();
            this.calcIndicator.setVisible(false);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Indicator failed hard", e);
        }
    }
}

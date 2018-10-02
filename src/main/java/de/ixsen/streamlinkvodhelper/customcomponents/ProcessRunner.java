package de.ixsen.streamlinkvodhelper.customcomponents;

import javafx.scene.control.ProgressIndicator;

import java.io.IOException;

public class ProcessRunner extends AbstractCalculation {
    private final ProcessBuilder processBuilder;

    public ProcessRunner(ProcessBuilder processBuilder, ProgressIndicator calcIndicator) {
        super(calcIndicator);
        this.processBuilder = processBuilder;
    }

    @Override
    void calculate() {
        try {
            this.processBuilder.start().waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Indicator failed hard", e);
        }
    }
}

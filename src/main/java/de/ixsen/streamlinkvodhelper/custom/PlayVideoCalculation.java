package de.ixsen.streamlinkvodhelper.custom;

import de.ixsen.streamlinkvodhelper.utils.LoggerHelper;
import javafx.scene.control.ProgressIndicator;
import org.sqlite.util.StringUtils;

import java.io.IOException;

public class PlayVideoCalculation extends AbstractCalculation implements LoggerHelper {
    private final ProcessBuilder processBuilder;

    public PlayVideoCalculation(ProgressIndicator calcIndicator, String... commands) {
        super(calcIndicator);
        this.processBuilder = new ProcessBuilder(commands).inheritIO();
    }

    @Override
    void init() {
        // do nothing
    }

    @Override
    void calculate() {
        try {
            Process start = this.processBuilder.start();
            this.getLogger().info("Starting player with the folloing command: " + StringUtils.join(this.processBuilder.command(), " "));
            start.waitFor();
        } catch (IOException | InterruptedException e) {
            this.getLogger().warning("The player thread was closed unexpectedly");
        }
    }

    @Override
    void closure() {
        // do nothing
    }
}

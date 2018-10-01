package de.ixsen.streamlinkvodhelper.main.log;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

public interface HasLogger {

    static Logger getLogger() {
        return Logger.getLogger(MethodHandles.lookup().getClass().getName());
    }
}

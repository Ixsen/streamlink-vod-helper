package de.ixsen.streamlinkvodhelper.utils;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

public interface LoggerHelper {

    static Logger logger() {
        return Logger.getLogger(MethodHandles.lookup().getClass().getName());
    }

    default Logger getLogger() {
        return Logger.getLogger(MethodHandles.lookup().getClass().getName());
    }
}

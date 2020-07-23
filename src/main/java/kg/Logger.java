package kg;

import java.util.logging.Level;

public class Logger {
    static void setLogLevel(Level level) {
        java.util.logging.Logger rootLog = java.util.logging.Logger.getLogger("");
        rootLog.setLevel(level);
        rootLog.getHandlers()[0].setLevel(level);
    }
}

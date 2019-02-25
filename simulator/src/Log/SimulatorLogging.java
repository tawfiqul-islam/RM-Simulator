
package Log;

import java.io.IOException;
import java.util.logging.*;

public class SimulatorLogging {
    static Logger logger;
    public Handler fileHandler;
    Formatter plainText;

    private SimulatorLogging() throws IOException {

        logger = Logger.getLogger(SimulatorLogging.class.getName());
        fileHandler = new FileHandler(Settings.Settings.simulatorHome+"/logs/simulatorLog.txt", true);
        plainText = new SimpleFormatter();
        fileHandler.setFormatter(plainText);
        logger.addHandler(fileHandler);
    }

    private static Logger getLogger(){
        if(logger == null){
            try {
                new SimulatorLogging();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

    public static void log(Level level, String msg){
        getLogger().log(level, msg);
    }
}
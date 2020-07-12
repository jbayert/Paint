/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * This is used with the logger. This sets ups the functions for the Logger to call
 * @author jonb1
 */
interface getCallback {

    /**
     * This is function is run on the clock cycles.
     *
     * @return returns a string of what event is called
     */
    String OnEvent();
}

/**
 * Creates a timer for the auto save. implements a task, and a new process
 *
 * @author jonb1
 */
public class LoggerPaint extends Task {

    /**
     * The total time per interval ((seconds
     */
    private float timerInterval;

    /**
     * stores what function to call every clock cycle
     */
    private getCallback callbackCheckTool = null;

    /**
     *
     * @param interval The starting time of the timer
     */
    /**
     * The logger to handle data
     */
    private Logger logger;

    /**
     * Handle the file output
     */
    private FileHandler fh;

    /**
     * The current tool used
     */
    private String currentTool;
    /**
     * The time on the tool
     */
    private int timeOnTool;

    /**
     * starts the logger
     *
     * @param interval how often logger checks
     * @param LogFile The log file to open
     */
    public LoggerPaint(float interval, String LogFile) {
        setInterval(interval);
        logger = Logger.getLogger("MyLog");

        currentTool = "startLogSkip";
        timeOnTool = 0;

        try {

            // This block configure the logger with handler and formatter  
            fh = new FileHandler(LogFile);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "The Log File has started");
    }

    /**
     * Sets the max time
     *
     * @param newTimerDuration the new maximum timer
     */
    public void setInterval(float newTimerDuration) {
        timerInterval = newTimerDuration;
    }

    /**
     * Sets the event that is too be called every time that the clock changes
     *
     * @param newEvent extent the OnTick interface and pass an instance of that
     * class here.
     */
    public void setCallbackCheckTool(getCallback newEvent) {
        callbackCheckTool = newEvent;
    }

    /**
     * Loops every second to change the clock
     *
     * @return returns if the loop exited correctly
     * @throws Exception if there is an error
     */
    @Override
    protected Integer call() throws Exception {
        while (true) {
            if (isCancelled()) {
                break;
            }
            Thread.sleep((long) (1000 * timerInterval));
            timeOnTool += timerInterval;
            if (callbackCheckTool != null) {
                Platform.runLater(() -> {
                    String newTool = callbackCheckTool.OnEvent();
                    if (newTool == null ? currentTool != null : !newTool.equals(currentTool)) {
                        String timeStr = Integer.toString(timeOnTool);
                        if ("startLogSkip".equals(currentTool)) {
                        } else if ("null".equals(currentTool)) {
                            logger.log(Level.INFO, "There was no tool selected for {0} seconds.", timeStr);
                        } else {
                            logger.log(Level.INFO, "{0} was selected for {1} seconds.", new Object[]{currentTool, timeStr});
                        }
                        currentTool = newTool;
                        timeOnTool = 0;
                    }
                });
            }
        }
        return 1;
    }
}

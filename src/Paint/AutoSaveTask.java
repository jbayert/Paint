/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Creates a timer for the auto save. implements a task, and a new process
 *
 * @author jonb1
 */
public class AutoSaveTask extends Task<Integer> {

    /**
     * The total time per interval ((seconds
     */
    private int timerDuration;
    /**
     * The time left (seconds)
     */
    private int timeLeft;
    /**
     * stores if the program is paused
     */
    private boolean isPaused;
    /**
     * stores what function to call every clock cycle
     */
    private OnTick callbackTickfunc = null;
    /**
     * stores what function to call every time the clock ends
     */
    private OnTick callbackEndfunc = null;

    /**
     *
     * @param newTimerDuration The starting time of the timer
     */
    public AutoSaveTask(int newTimerDuration) {
        setTotalTime(newTimerDuration);
        isPaused = false;
        reset();
    }

    /**
     * Resets the timer
     */
    public void reset() {
        timeLeft = timerDuration;
    }

    /**
     * Pause the timer
     */
    public void pause() {
        isPaused = true;
    }

    /**
     * Resumes the timer
     */
    public void resume() {
        isPaused = false;
    }

    /**
     * Start/stops the timer
     */
    public void togglePause() {
        isPaused = !isPaused;
    }

    /**
     * Gets if the timer is paused
     *
     * @return (boolean) is the timer paused
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Gets the time left on the clock
     *
     * @return returns the time left(int)
     */
    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * Sets the max time
     *
     * @param newTimerDuration the new maximum timer
     */
    public void setTotalTime(int newTimerDuration) {
        timerDuration = newTimerDuration;
    }

    /**
     * Sets the event that is too be called every time that the clock changes
     *
     * @param newEvent extent the OnTick interface and pass an instance of that
     * class here.
     */
    public void setOnTick(OnTick newEvent) {
        callbackTickfunc = newEvent;
    }

    /**
     * Sets the event that is too be called every time that the clock reaches
     * zero
     *
     * @param newEvent extent the OnTick interface and pass an instance of that
     * class here.
     */
    public void setOnEnd(OnTick newEvent) {
        callbackEndfunc = newEvent;
    }

    /**
     * Loops every second to change the clock
     *
     * @return returns if the loop exited correctly
     * @throws Exception if there is an error
     */
    @Override
    protected Integer call() throws Exception {
             // pseudo-code:
        //   query the database
        //   read the values

        // Now update the customer
        while (true) {
            if (isCancelled()) {
                break;
            }
            Thread.sleep(1000);
            if (!isPaused) {
                timeLeft--;
                Platform.runLater(() -> {
                    callbackTickfunc.OnEvent(timeLeft);
                });
            }
            if (timeLeft == 0) {
                try {
                    if (callbackEndfunc != null) {
                        Platform.runLater(() -> {
                            callbackEndfunc.OnEvent(timeLeft);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                reset();
                //call the ending evet
                try {
                    if (callbackTickfunc != null) {
                        Platform.runLater(() -> {
                            callbackTickfunc.OnEvent(timeLeft);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }
}

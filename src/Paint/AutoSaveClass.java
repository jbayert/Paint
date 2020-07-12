/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import java.io.File;

/**
 * This class manages the autosave prompt. It will trigger a backup at chooser
 * intervals
 *
 * @author jonb1
 */
public class AutoSaveClass {

    /**
     * This is how often the autosave will update
     */
    public static final int AUTO_SAVE_TIMER = 20;
    /**
     * The location where the temp files will be stored
     */
    public static final String TEMP_FOLDER = "src\\Temp\\";

    /**
     * AutoSave this sets up the autosave setting and implements a Task
     */
    private AutoSaveTask autoSaveTimer;

    /**
     * The main controller to call save and other features
     */
    private PrimaryController mainFrame;

    /**
     * The location of the temp file
     */
    private File autoSaveFile;

    /**
     * This sets up the timer
     *
     * @param mainFrame the main controller
     */
    AutoSaveClass(PrimaryController mainFrame) {
        this.mainFrame = mainFrame;

        //create the timer
        autoSaveTimer = new AutoSaveTask(AUTO_SAVE_TIMER);
        autoSaveTimer.setOnTick((int timeSeconds) -> {
            setAutoSaveTime(timeSeconds);
        });
        autoSaveTimer.setOnEnd((int timeSeconds) -> {
            autosave();
        });

        //start the thread
        Thread th = new Thread(autoSaveTimer);
        th.setDaemon(true);
        th.start();

        //Turn off prompt
        setAutoSavePromptManaged(false);
    }

    /**
     * Updates if the prompt asking to open the temp file
     *
     * @param isShown show the prompt be shown
     */
    private void setAutoSavePromptManaged(boolean isShown) {
        mainFrame.autoSavePrompt.setManaged(isShown);
        mainFrame.autoSavePrompt.setVisible(isShown);
    }

    /**
     * If the no button is pressed on the prompt
     */
    public void NoAction() {
        autoSaveFile.delete();
        setAutoSavePromptManaged(false);
    }

    /**
     * If yes if pressed on the prompt
     */
    public void YesAction() {
        mainFrame.openFile(autoSaveFile);
        setAutoSavePromptManaged(false);
    }

    /**
     * Launch the prompt window
     *
     * @param file What file is recovered
     */
    public void prompt(File file) {
        setAutoSavePromptManaged(true);
        autoSaveFile = file;
    }

    /**
     * Updates the time left show.
     *
     * @param secondsLeft How many seconds are left
     */
    private void setAutoSaveTime(int secondsLeft) {
        String minutes = Integer.toString(secondsLeft / 60);
        String seconds = Integer.toString(secondsLeft % 60);
        if ((secondsLeft % 60) < 10) {
            seconds = "0" + seconds;
        }
        mainFrame.autoSaveTime.setText(minutes + ":" + seconds);
    }

    /**
     * The auto save is paused/unpaused
     */
    public void autoSaveAction() {
        autoSaveTimer.togglePause();
        if (autoSaveTimer.isPaused()) {
            mainFrame.autoSaveTime.setText("Paused");
        } else {
            setAutoSaveTime(autoSaveTimer.getTimeLeft());
        }

    }

    /**
     * resets the timer
     */
    private void reset() {
        autoSaveTimer.reset();
    }

    /**
     * Get the time left on the clock
     */
    public void getTimeLeft() {
        autoSaveTimer.getTimeLeft();
    }

    /**
     * Auto save all images
     */
    private void autosave() {
        mainFrame.tabPaneObject.getTabs().forEach((tab) -> {
            ImageEditor tabEditor = (ImageEditor) tab;
            tabEditor.autoSave();
        });
    }
}

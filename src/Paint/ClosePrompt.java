/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This shows the prompt for if the file should be saved
 *
 * @author jonb1
 */
//Launches a new window to ask if it closed.
//You can set the event handlers to result with the actions
public class ClosePrompt {

    /**
     * The prompt to displae
     */
    private String textToDisplay;
    /**
     * The save Event Action
     */
    private EventHandler<ActionEvent> saveEvent;
    /**
     * The do not save Event Action
     */
    private EventHandler<ActionEvent> doNotSaveEvent;
    /**
     * The cancel save Event Action
     */
    private EventHandler<ActionEvent> cancelEvent;

    /**
     * What the result is
     */
    private int result;
    /**
     * The prompt is still running
     */
    public static final int STILL_RUNNING = -1;
    /**
     * There has been an error
     */
    public static final int ERROR = 0;
    /**
     * The file was saved
     */
    public static final int SAVE = 1;
    /**
     * The file was not saved
     */
    public static final int DO_NOT_SAVE = 2;
    /**
     * The close was canceled
     */
    public static final int CANCEL = 3;

    /**
     * The internal even handler
     */
    private EventHandler<ActionEvent> saveEventInternal;
    /**
     * The do not save event handler
     */
    private EventHandler<ActionEvent> doNotSaveEventInternal;
    /**
     * The cancel save event handler
     */
    private EventHandler<ActionEvent> cancelEventInternal;

    /**
     * The stage to show the prompt
     */
    private Stage stage;

    /**
     * Launches the save menu
     */
    public ClosePrompt() {
        textToDisplay = "Save?";
        initEvents();
    }

    /**
     * Launches the save menu
     *
     * @param toDisplay what text to display in the prompt
     */
    public ClosePrompt(String toDisplay) {
        textToDisplay = toDisplay;
        initEvents();
    }

    /**
     * create the call backs
     */
    private void initEvents() {
        stage = new Stage();//Create a new stage for the prompt
        saveEvent = (ActionEvent e) -> {
        };
        doNotSaveEvent = (ActionEvent e) -> {
        };
        cancelEvent = (ActionEvent e) -> {
        };

        saveEventInternal = (ActionEvent e) -> {
            saveEvent.handle(e);
            stage.hide();
            result = SAVE;
        };
        doNotSaveEventInternal = (ActionEvent e) -> {
            doNotSaveEvent.handle(e);
            stage.hide();
            result = DO_NOT_SAVE;
        };
        cancelEventInternal = (ActionEvent e) -> {
            cancelEvent.handle(e);
            stage.hide();
            result = CANCEL;
        };

    }

    /**
     * set call back for when save is pressed
     *
     * @param e The event to call
     */
    public void setSaveEvent(EventHandler<ActionEvent> e) {
        saveEvent = e;
    }

    /**
     * set call back for when do not save is pressed
     *
     * @param e The event to call
     */
    public void setDoNotSaveEvent(EventHandler<ActionEvent> e) {
        doNotSaveEvent = e;
    }

    /**
     * set call back for when cancel is pressed
     *
     * @param e The event to call
     */
    public void setCancelEvent(EventHandler<ActionEvent> e) {
        cancelEvent = e;
    }

    /**
     * update text
     *
     * @param toDisplay the text to display
     */
    public void setText(String toDisplay) {
        textToDisplay = toDisplay;
    }

    /**
     * create the layout does not use fxml
     *
     * @return 0 is there is an errorr
     */
    public int launch() {
        try {
            result = STILL_RUNNING;
            Label prompt = new Label(textToDisplay);

            //The save button
            Button SaveBtn = new Button("Save");
            SaveBtn.setOnAction(saveEventInternal);

            //Do not save button
            Button DoNotSaveBtn = new Button("Do not Save");
            DoNotSaveBtn.setOnAction(doNotSaveEventInternal);

            Button CancelBtn = new Button("Cancel");
            CancelBtn.setOnAction(cancelEventInternal);

            HBox button_layout = new HBox();
            button_layout.getChildren().addAll(SaveBtn, DoNotSaveBtn, CancelBtn);

            VBox main_layout = new VBox();
            main_layout.getChildren().addAll(prompt, button_layout);

            stage.initModality(Modality.APPLICATION_MODAL);//Prevent from editing the main window
            setText(textToDisplay);
            stage.setScene(new Scene(main_layout));

            stage.show();

            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
}

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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class manages the prompt to resize the files
 * @author jonb1
 */
public class ResizeClass {

    /**
     * the prompt stage
     */
    private final Stage stage;
    /**
     * the current size
     */
    private final Label currentSize;
    /**
     * the new height
     */
    private final TextField heightSize;
    /**
     * the new width
     */
    private final TextField widthSize;
    /**
     * the current tab
     */
    private ImageEditor currentTab;

    /**
     * starts the class
     */
    ResizeClass() {

        stage = new Stage();

        currentSize = new Label("Current size: Null by Null");
        Label prompt = new Label("Resize to:");

        Label heightLabel = new Label("New Height: ");
        heightSize = new TextField();
        HBox row3 = new HBox();
        row3.getChildren().addAll(heightLabel, heightSize);

        Label widthLabel = new Label("New Width: ");
        widthSize = new TextField();
        HBox row4 = new HBox();
        row4.getChildren().addAll(widthLabel, widthSize);

        //Callbacks
        EventHandler<ActionEvent> cancelEvent = (ActionEvent e) -> {
            cancelCallback();
        };
        EventHandler<ActionEvent> resizeEvent = (ActionEvent e) -> {
            resizeCallback();
        };

        //The resize button
        Button SetBtn = new Button("Resize");
        SetBtn.setOnAction(resizeEvent);
        Button CancelBtn = new Button("Cancel");
        CancelBtn.setOnAction(cancelEvent);
        HBox row5 = new HBox();
        row5.getChildren().addAll(SetBtn, CancelBtn);

        VBox main_layout = new VBox();
        main_layout.getChildren().addAll(currentSize, prompt, row3, row4, row5);

        stage.initModality(Modality.APPLICATION_MODAL);//Prevent from editing the main window
        stage.setScene(new Scene(main_layout));

        currentTab = null;

    }

    /**
     * cancel the resize prompt
     */
    private void cancelCallback() {
        stage.hide();
    }

    /**
     * resize the image
     */
    private void resizeCallback() {
        if (currentTab != null) {
            try {
                double new_height = Double.parseDouble(heightSize.getText());
                double new_width = Double.parseDouble(widthSize.getText());
                if ((new_width > 0) && (new_height > 0)) {
                    currentTab.resize(new_width, new_height);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        stage.hide();
    }

    /**
     * launches the prompt
     *
     * @param newCurrentTab the current tab to resize
     * @return returns 0 if the prompt runs correctly
     */
    public int prompt(ImageEditor newCurrentTab) {

        try {
            currentTab = newCurrentTab;
            if (currentTab == null) {
                return 1;
            }
            String width = String.valueOf(currentTab.getImageWidth());
            String height = String.valueOf(currentTab.getImageHeight());
            currentSize.setText("Current size: " + height + " by " + width);
            stage.show();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

}

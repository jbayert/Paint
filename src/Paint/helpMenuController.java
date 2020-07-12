/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * adds the text to the help menu
 *
 * @author jonb1
 */
public class helpMenuController {

    /**
     * the text to display for how to open the help
     */
    private static final String OpenText = "You can open png files with the open buttons. \n Multiple files can be opened seperately and worked on.\n\n"
            + "Save\\Save All: Saves the currently opened file(s) with all edits. \n\n Save As opens up a prompt to create a new file.";
    /**
     * the edit text
     */
    private static final String EditText = "Draw a Line: Select our color and width and \n then click and drag the mouse to draw the line\n the line is finalized after you release it.\n"+
            "Other features avalible:\n"+
            "Line: Click and drag\n"+
            "Arc: Click the center point, click an outside point and drag the arc open\n"+
            "Eraser: This erases areas\n"+
            "Text: add text based on size and the imput box. Click to place the text.\n"+
            "Rectange:Click and drage\n"
            + "Ellipse: Click and drag\n"
            + "Circle: Click and drag\n"
            + "Free Draw: Click and draw a line\n"
            + "Eye Dropper: select a color with the right or left click\n"
            + "Select: selects a region\n"
            + "Move:Move the selected region\n"
            + "Copy: Copy the selected region\n"
            + "Rotate:Rotate the selected region\n";
    /**
     * The text under the other text file
     */
    private static final String OtherText = "To zoom in press control and the scroll wheel\n\n"+
            "click resize to change the size of the picture";

    /**
     * The openSave drop down
     */
    @FXML
    private Label OpenSave;

    /**
     * The label for how to edit stuff
     */
    @FXML
    private Label Edit;

    /**
     * The label for other information
     */
    @FXML
    private Label Other;

    /**
     * launches the help menu
     */
    @FXML
    public void initialize() {
        Edit.setText(EditText);
        OpenSave.setText(OpenText);
        Other.setText(OtherText);
    }

}

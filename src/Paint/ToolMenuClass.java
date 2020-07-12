package Paint;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.beans.value.ChangeListener;

/**
 * The tool menu so that only one is pressed
 *
 * @author jonb1
 */
public class ToolMenuClass extends ToolBar {

    /**
     * This is a group to hold the buttons. This ensures only one button is
     * pressed.
     */
    private final ToggleGroup group;

    /**
     * the location of all selected
     */
    private ToolMenuInputsRow ToolMenuInputs;

    /**
     * This is a string of tools. The order of each subset loop goes as follows:
     * NameOfTool, TextToDisplay, ToolTip
     */
    public String tools[][] = {{"Line", "Line", "Draws a Line"},
    {"Arc", "Arc", "Draws an Arc"},
    {"Eraser", "Eraser", "Erases a region"},
    {"Polygon", "Polygon", "Create a polygon of n sides"},
    {"Text", "Text", "Add text"},
    {"Rectangle", "Rectangle", "Draw a rectangle"},
    {"Ellipse", "Ellipse", "Draw an Ellipse"},
    {"Circle", "Circle", "Draw a circle"},
    {"Free Draw", "Free Draw", "Draw any line"},
    {"Eye Dropper", "Eye Dropper", "Pick a color"},
    {"Select", "Select", "Select a region"},
    {"Move", "Move", "Move selected region"},
    {"Copy", "Copy", "Copy a Region"},
    {"Rotate", "Rotate", "Rotates a Region"}
    };

    /**
     * Creates a menu to choose Tools
     *
     * @param ToolMenuInputs the tool to be added to the group
     */
    public ToolMenuClass(ToolMenuInputsRow ToolMenuInputs) {
        this.ToolMenuInputs = ToolMenuInputs;

        group = new ToggleGroup();

        for (String[] tool : tools) {
            addTool(tool[0], tool[1], tool[2]);
        }

        //get when an event switches
        group.selectedToggleProperty().addListener(testToggleChange);

    }

    /**
     * Adds a tool into the menu
     *
     * @param name the name of the tool
     * @param Text the text to be displays
     * @param TooltipText The text displayed in the tool tip
     * @return returns the button that was created
     */
    private ToolBarButton addTool(String name, String Text, String TooltipText) {
        ToolBarButton tb = new ToolBarButton(name, Text, TooltipText);
        getItems().add(tb);
        tb.setToggleGroup(group);
        return tb;
    }

    /**
     * Gets the current tool in use
     *
     * @return the name of the current tool in use.
     */
    public String getSelectedTool() {
        Toggle t1 = group.getSelectedToggle();
        if (t1 != null) {
            ToolBarButton t2 = (ToolBarButton) t1;
            return (String) t2.getName();
        } else {
            return "null";
        }
    }

    /**
     * Updates the settings shown when the TypeToDraw is updated
     */
    private final ChangeListener<Toggle> testToggleChange = (ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {

        ToolMenuInputs.updateShownMenu(getSelectedTool());

    };

}

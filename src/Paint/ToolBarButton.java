package Paint;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;

/**
 * creates a button for the tooltip
 *
 * @author jonb1
 */
public class ToolBarButton extends ToggleButton {

    /**
     * The name of a the button
     */
    private final String name;

    /**
     * The text of that is displayed
     */
    private final String text;

    /**
     * The text of the tooltip
     */
    private String ToolTipText;

    /**
     * The Tooltip that is set
     */
    Tooltip TooltipObject;

    /**
     * creates the toolbar button
     *
     * @param newName the name of the button
     * @param newText the text to show on the button
     * @param newToolTipText the tooltip for this button
     */
    public ToolBarButton(String newName, String newText, String newToolTipText) {
        super(newText);//create the ToggleButton
        name = newName;
        text = newText;
        setToolTip(newToolTipText);
    }

    /**
     * gets the name of the button
     *
     * @return the name of the button
     */
    public String getName() {
        return name;
    }

    /**
     * sets the tool tip
     *
     * @param newTooltipText the text of the tooltip
     */
    public void setToolTip(String newTooltipText) {
        TooltipObject = new Tooltip(newTooltipText);
        ToolTipText = newTooltipText;
        Tooltip.install(this, TooltipObject);
    }

}

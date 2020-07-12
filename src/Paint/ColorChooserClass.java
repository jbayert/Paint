/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;

/**
 * manages the color/ width chooser has getters and setters for the color
 * pickers
 *
 * @author jonb1
 */
public class ColorChooserClass {

    /**
     * the current primary color
     */
    private final ColorPicker primaryColorPicker;
    /**
     * the current secondary color
     */
    private final ColorPicker secondaryColorPicker;
    /**
     * the width selector
     */
    private final Spinner<Integer> spinnerObject;

    /**
     * The minimum width from the tools
     */
    public static final int MIN_WIDTH = 1;

    /**
     * The maximum width for the tools
     */
    public static final int MAX_WIDTH = 50;

    /**
     * The starting width for the tool
     */
    public static final int START_WIDTH = 5;

    /**
     * The starting color
     */
    public static final Color START_COLOR = Color.BLUE;

    /**
     *
     * @param primaryColorPicker the primary color picker
     * @param secondaryColorPicker the secondary color picker
     * @param spinnerObject the spinner object
     */
    ColorChooserClass(ColorPicker primaryColorPicker,
            ColorPicker secondaryColorPicker,
            Spinner<Integer> spinnerObject) {
        this.primaryColorPicker = primaryColorPicker;
        this.secondaryColorPicker = secondaryColorPicker;
        this.spinnerObject = spinnerObject;

        /**
         * Add custom colors
         */
        primaryColorPicker.getCustomColors().addAll(Color.RED, Color.BLUE,
                Color.GRAY, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.PINK,
                Color.YELLOW, Color.ORANGE);
        secondaryColorPicker.getCustomColors().addAll(Color.RED, Color.BLUE,
                Color.GRAY, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.PINK,
                Color.YELLOW, Color.ORANGE);

        //Set the initial Color
        setPrimaryColor(START_COLOR);
        setSecondaryColor(START_COLOR);

        // Value factory
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH, START_WIDTH);
        spinnerObject.setValueFactory(valueFactory);
    }

    /**
     * gets the width of the tool
     *
     * @return the width of the tool
     */
    public int getWidth() {
        return spinnerObject.getValue();
    }

    /**
     * set a new primary color
     *
     * @param newColor the color to set
     */
    public void setPrimaryColor(Color newColor) {
        primaryColorPicker.setValue(newColor);
    }

    /**
     * get the primary color
     *
     * @return The current color being used
     */
    public Color getPrimaryColor() {
        return primaryColorPicker.getValue();
    }

    /**
     * sets the secondary color
     *
     * @param newColor the color to be used
     */
    public void setSecondaryColor(Color newColor) {
        secondaryColorPicker.setValue(newColor);
    }

    /**
     * gets the current secondary color that is being used
     *
     * @return the current color being used
     */
    public Color getSecondaryColor() {
        return secondaryColorPicker.getValue();
    }
}

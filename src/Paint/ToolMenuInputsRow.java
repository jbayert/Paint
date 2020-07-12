/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * prompts the input for selected row. Allows the user to enter how much to
 * rotate and certain text and the number of sides on a polygon
 *
 * @author jonb1
 */
public class ToolMenuInputsRow extends HBox {

    /**
     * The maximum size for text font
     */
    private static final int MAX_FONT_SIZE = 70;
    /**
     * The minimum size for text font
     */
    private static final int MIN_FONT_SIZE = 3;
    /**
     * The starting size for text font
     */
    private static final int START_FONT_SIZE = 30;

    /**
     * The maximum number of sides for a polygon
     */
    private static final int MAX_POLYGON_SIDES = 15;
    /**
     * The minimum number of sides for a polygon
     */
    private static final int MIN_POLYGON_SIDES = 3;
    /**
     * The starting number of sides for a polygon
     */
    private static final int START_POLYGON_SIDES = 5;

    /**
     * The minimum number of degrees that the tool can rotate
     */
    private static final int MIN_ROTATE_DEGREES = 0;
    /**
     * The maximum number of degrees that the tool can rotate
     */
    private static final int MAX_ROTATE_DEGREES = 360;
    /**
     * The starting number of degrees that the tool can rotate
     */
    private static final int START_ROTATE_DEGREES = 90;
    /**
     * The step size of degrees that the tool can rotate
     */
    private static final int STEP_ROTATE_DEGREES = 15;

    /**
     * the spinner to choose the number of sides
     */
    private final Spinner<Integer> numberOfSidesObject;

    /**
     * Turns off/on Rotate settings
     */
    private HBox RotateObject;

    /**
     * Turns off/on Polygon settings
     */
    private HBox PolygonSettingsObject;

    /**
     * Turns off/on Rotate settings
     */
    private HBox TextSettingsObject;

    /**
     * The text size spinner
     */
    private final Spinner<Integer> TextSizeObject;

    /**
     * The rotate tool spinner
     */
    private final Spinner<Integer> rotateDegrees;

    /**
     * The text input field
     */
    private final TextField TextObject;

    /**
     * The tool menu row to help with prompts
     *
     * @throws java.io.IOException if there is an error
     */
    public ToolMenuInputsRow() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ToolMenuInputsRowFXML.fxml"));
        Parent root;
        root = loader.load();
        getChildren().addAll(root);

        TextSettingsObject = (HBox) recursizeLookup(root, "TextSettingsObject");
        RotateObject = (HBox) recursizeLookup(root, "RotateObject");
        PolygonSettingsObject = (HBox) recursizeLookup(root, "PolygonSettingsObject");

        numberOfSidesObject = (Spinner) recursizeLookup(root, "numberOfSidesObject");
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_POLYGON_SIDES, MAX_POLYGON_SIDES, START_POLYGON_SIDES);
        numberOfSidesObject.setValueFactory(valueFactory);

        TextSizeObject = (Spinner) recursizeLookup(root, "TextSizeObject");
        SpinnerValueFactory<Integer> valueFactory2 = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_FONT_SIZE, MAX_FONT_SIZE, START_FONT_SIZE);
        TextSizeObject.setValueFactory(valueFactory2);

        rotateDegrees = (Spinner) recursizeLookup(root, "rotateDegrees");
        SpinnerValueFactory<Integer> valueFactory3 = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_ROTATE_DEGREES, MAX_ROTATE_DEGREES, START_ROTATE_DEGREES, STEP_ROTATE_DEGREES);
        rotateDegrees.setValueFactory(valueFactory3);

        TextObject = (TextField) recursizeLookup(root, "TextObject");

        setTextSettingManaged(false);
        setPolygonSettingManaged(false);
        setRotateManaged(false);
    }

    /**
     * Turns on/off Text Settings
     *
     * @param isShown should the textmenu be shown
     */
    private void setTextSettingManaged(boolean isShown) {
        TextSettingsObject.setManaged(isShown);
        TextSettingsObject.setVisible(isShown);
    }

    /**
     * set if the polygon setting is shown.
     *
     * @param isShown boolean if the polygon is shown.
     */
    private void setPolygonSettingManaged(boolean isShown) {
        PolygonSettingsObject.setManaged(isShown);
        PolygonSettingsObject.setVisible(isShown);
    }

    /**
     * set if the rotate method is shown.
     *
     * @param isShown boolean if the rotate is shown.
     */
    private void setRotateManaged(boolean isShown) {
        RotateObject.setManaged(isShown);
        RotateObject.setVisible(isShown);
    }

    /**
     * gets the number of sides on the polygon
     *
     * @return the number of sides for the polygon
     */
    public int getPolygonSides() {
        return numberOfSidesObject.getValue();
    }

    /**
     * get the font size for the text
     *
     * @return the font size of the text
     */
    public int getFontSize() {
        return TextSizeObject.getValue();
    }

    /**
     * gets the number of degrees that should be rotated
     *
     * @return the number of degreed rotated
     */
    public int getRotateDegrees() {
        return rotateDegrees.getValue();
    }

    /**
     * gets the text to be drawn
     *
     * @return the text that should be drawn
     */
    public String getTextField() {
        return TextObject.getText();
    }

    /**
     * Shows a menu. Only one at a time
     *
     * @param ItemToShow The item that is to be shown. This can be
     * "Text","Polygon","Rotate"
     */
    public void updateShownMenu(String ItemToShow) {
        setTextSettingManaged("Text".equals(ItemToShow));
        setPolygonSettingManaged("Polygon".equals(ItemToShow));
        setRotateManaged("Rotate".equals(ItemToShow));

    }

    /**
     * look up a node by id.
     *
     * Returns null if no node is found
     *
     * @param parentNode the node to look under
     * @param id the id to lookup
     * @return the node that was found
     */
    private static Node recursizeLookup(Node parentNode, String id) {
        try {
            if (parentNode.getId() == null ? id != null : !parentNode.getId().equals(id)) {
                try {
                    for (Node node : ((Pane) parentNode).getChildren()) {
                        Node test2 = recursizeLookup(node, id);
                        if (test2 != null) {
                            return test2;
                        }
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    return null;
                }
            } else {
                return parentNode;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        return null;
    }
}

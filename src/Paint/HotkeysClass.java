package Paint;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 * Manages when the hotkeys are pressed
 *
 * @author jonb1
 */
public class HotkeysClass {

    /**
     * is the control key currently pressed
     */
    private static boolean isCtrlPressedBool = false;

    /**
     * creates the class
     *
     * @param ctrl the controller
     * @param scene the current scene
     * @param root the parent of the scene
     */
    HotkeysClass(PrimaryController ctrl, Scene scene, Parent root) {
        KeyCombination kc = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        Runnable rn = () -> {
            ctrl.UndoBtnAction();
        };
        scene.getAccelerators().put(kc, rn);

        KeyCombination kc2 = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        Runnable rn2 = () -> {
            ctrl.RedoBtnAction();
        };
        scene.getAccelerators().put(kc2, rn2);

        KeyCombination kc3 = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        Runnable rn3 = () -> {
            ctrl.SaveBtnAction();
        };
        scene.getAccelerators().put(kc3, rn3);

        KeyCombination kc4 = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
        Runnable rn4 = () -> {
            ctrl.SaveAsAction();
        };
        scene.getAccelerators().put(kc4, rn4);

        KeyCombination kc5 = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
        Runnable rn5 = () -> {
            try {
                ctrl.OpenBtnAction();
            } catch (IOException ex) {
                Logger.getLogger(PaintMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        scene.getAccelerators().put(kc5, rn5);

        root.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.CONTROL) {
                isCtrlPressedBool = true;
                ctrl.setPannable(true);
            }
        });

        root.setOnKeyReleased((KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.CONTROL) {
                isCtrlPressedBool = false;
                ctrl.setPannable(false);
            }
        });
    }

    /**
     * checks if the control key is pressed
     *
     * @return returns is the control key is pressed
     */
    public static boolean IsCtrlPressed() {
        return isCtrlPressedBool;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * This loads the fxml file AboutPaint and displays the version number and
 * release date
 *
 * @author jonb1
 */
public class AboutPaintController {

    /**
     * There are no parameters in the constructor
     */
    public AboutPaintController(){
        
    }
    
    /**
     * The label that shows the version number
     */
    @FXML
    public Label version;
    
    /**
     * The label that shows the release date
     */
    @FXML
    public Label releaseDate;

    /**
     * This initializes the About paint and updates the version number from
     * PainMain
     */
    @FXML
    public void initialize() {
        version.setText(PaintMain.VERSION);
        releaseDate.setText(PaintMain.RELEASE_DATE);
    }

    /**
     * launches the about menu
     *
     * @return returns zero if it launched without errors
     */
    public int launch_window() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("AboutPaint.fxml"));
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root));
            stage.show();
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }
}

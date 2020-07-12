package Paint;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The main application
 *
 * @author jonb1
 */
public class PaintMain extends Application {

    /**
     * The main stage that the program runs off
     */
    private static Stage main_stage;
    /**
     * The location of the release Notes
     */
    public static final File RELEASE_NOTES = new File("src\\Paint\\Pain(t) Release Notes.txt");
    /**
     * The string of the version number
     */
    public static final String VERSION = "7.0";
    /**
     * The date of this release notes
     */
    public static final String RELEASE_DATE = "November 9, 2019";

    /**
     * The location of the Log files
     */
    public static final String LOG_FILE = "src\\Paint\\Events.log";

    /**
     * manages the hotkeys that are pressed
     */
    private HotkeysClass Hotkeys;

    /**
     * The main program to start
     *
     * @param stage the main stage that the program runs off of
     * @throws IOException if there is an error
     */
    @Override
    public void start(Stage stage) throws IOException {

        //Load the main fxml controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = (Parent) loader.load();
        PrimaryController ctrl = loader.getController();

        main_stage = stage;

        //Prompt before closing
        stage.setOnCloseRequest((WindowEvent we) -> {
            try {
                we.consume();//Prevent From closing
                ctrl.CloseAll(true);
            } catch (Exception e) {
                e.printStackTrace();
                CloseWindow();
            }
        });

        Scene scene = new Scene(root);

        stage.setScene(scene);
        setTitle("Pain(t)");

        Hotkeys = new HotkeysClass(ctrl, scene, root);

        stage.show();
    }

    /**
     * Setter for the title
     *
     * @param newTitle the title to set for the applications
     */
    public static void setTitle(String newTitle) {
        main_stage.setTitle(newTitle);
    }

    /**
     * Gets the title of the application
     *
     * @return The title of the application
     */
    public static String getTitle() {
        return main_stage.getTitle();
    }

    /**
     * Getter for the main stage
     *
     * @return the stage that is open
     */
    public static Stage getMainStage() {
        return main_stage;
    }

    /**
     * Closes down the main program. Closes down all the processes
     */
    public static void CloseWindow() {
        main_stage.close();
        Platform.exit();
        System.exit(0);
    }

    /**
     * launches the program
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

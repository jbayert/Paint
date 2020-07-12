package Paint;

import java.awt.Desktop;
import java.io.IOException;
import javafx.fxml.FXML;
import java.io.File;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * The main fxml controller handles user inputs nd the choosing of buttons
 *
 * @author jonb1
 */
public class PrimaryController {

    /**
     * the pane for all the tabs
     */
    @FXML
    public TabPane tabPaneObject;

    /**
     * the width spinner
     */
    @FXML
    private Spinner<Integer> spinnerObject;

    /**
     * the button to open the release notes
     */
    @FXML
    private MenuItem releaseNotesButton;

    /**
     * the primary color chooser
     */
    @FXML
    private ColorPicker primaryColorPicker;

    /**
     * The secondary color picker
     */
    @FXML
    private ColorPicker secondaryColorPicker;

    /**
     * where the tools are to be chosen from
     */
    @FXML
    private AnchorPane ToolChooserPane;

    /**
     * the tool menu inputs
     */
    @FXML
    private HBox ToolMenuInputsHBox;

    /**
     * The menu for the Tools
     */
    public ToolMenuClass ToolMenu;

    /**
     * where the user enters the inputs for certain tools there are several
     * setters and getter to get number of sides, text sides, etc.
     */
    public ToolMenuInputsRow ToolMenuInputs;

    /**
     * launches the file explorer prompt
     */
    public FileExplorerClass FileExplorer;

    /**
     * manages the autosave timer
     */
    public AutoSaveClass AutoSave;

    /**
     * allows the user to set the color chooser
     */
    public ColorChooserClass ColorChooser;

    /**
     * the prompt to save the resize
     */
    private ResizeClass resize;

    /**
     * The constructor
     */
    public PrimaryController() {

    }

    /**
     * launch on start
     *
     * @throws java.io.IOException if there is an error
     */
    @FXML
    public void initialize() throws IOException {
        releaseNotesButton.setText(PaintMain.RELEASE_NOTES.getName());

        ToolMenuInputs = new ToolMenuInputsRow();
        ToolMenuInputsHBox.getChildren().add(ToolMenuInputs);

        ToolMenu = new ToolMenuClass(ToolMenuInputs);
        ToolChooserPane.getChildren().add(ToolMenu);

        FileExplorer = new FileExplorerClass();

        AutoSave = new AutoSaveClass(this);

        resize = new ResizeClass();

        setUndoDisable(true);
        setRedoDisable(true);

        ColorChooser = new ColorChooserClass(primaryColorPicker, secondaryColorPicker, spinnerObject);

        //updates the tabs when switched
        tabPaneObject.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (tabPaneObject.getTabs().size() != 0) {
                Tab test = tabPaneObject.getTabs().get((int) newValue);
                if (test != null) {
                    ImageEditor test2 = (ImageEditor) test;
                    test2.reopenedCall();
                }
            }
        });

        //Start the logger
        LoggerPaint log2 = new LoggerPaint(1, PaintMain.LOG_FILE);
        log2.setCallbackCheckTool(() -> {
            return ToolMenu.getSelectedTool(); //To change body of generated methods, choose Tools | Templates.
        });
        Thread th2 = new Thread(log2);
        th2.setDaemon(true);
        th2.start();
    }

    /**
     * opens a text editor to read the release notes
     *
     * @return returns a 0 if it opens correctly
     */
    @FXML
    private int OpenReleaseNotes() {
        try {
            Desktop.getDesktop().open(PaintMain.RELEASE_NOTES);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Saves the image shown when the save button is pressed
     *
     * @return returns if it was a success
     */
    @FXML
    public int SaveBtnAction() {
        try {
            ImageEditor currentItem = (ImageEditor) tabPaneObject.getSelectionModel().getSelectedItem();
            if (currentItem != null) {
                currentItem.save();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }

    /**
     * Opens the SaveAs prompt when the button is pressed
     *
     * @return 0 if exits properly
     */
    @FXML
    public int SaveAsAction() {
        try {
            Alert a = new Alert(AlertType.NONE);
            File fileTest = FileExplorer.saveAsExplorer();//ask for a file
            ImageEditor currentItem = (ImageEditor) tabPaneObject.getSelectionModel().getSelectedItem();
            if (currentItem != null) {
                if (!FileExplorer.getFileExtensionOfFile(fileTest).equals(currentItem.getFileExtension())) {
                    // set alert type 
                    a.setAlertType(AlertType.WARNING);

                    // set content text 
                    a.setContentText("You are changing File Types. This could cause errors.");

                    // show the dialog 
                    a.show();
                }
                currentItem.saveAsImage(fileTest);

            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Save all the open images
     *
     * @return 0 if exits properly
     */
    @FXML
    private int SaveAllBtnAction() {
        try {
            tabPaneObject.getTabs().forEach((tab) -> {
                ImageEditor tabEditor = (ImageEditor) tab;
                tabEditor.save();
            });
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Saves the image shown when the save button is pressed
     * @return returns 0 if a success
     */
    @FXML
    private int CloseBtnAction() {
        try {
            ImageEditor currentItem = (ImageEditor) tabPaneObject.getSelectionModel().getSelectedItem();
            if (currentItem != null) {
                currentItem.close(true, false);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * the redo button is pressed
     *
     * @return returns 0 if there was no error
     */
    @FXML
    public int RedoBtnAction() {
        try {
            ImageEditor currentItem = (ImageEditor) tabPaneObject.getSelectionModel().getSelectedItem();
            if (currentItem != null) {
                currentItem.redo();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * the undo button call back
     *
     * @return returns 0 if there was no error
     */
    @FXML
    public int UndoBtnAction() {
        try {
            ImageEditor currentItem = (ImageEditor) tabPaneObject.getSelectionModel().getSelectedItem();
            if (currentItem != null) {
                currentItem.undo();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * opens an image of a duck
     *
     * @return returns 0 if there was no error
     * @throws IOException if there is an error
     */
    @FXML
    private int OpenDuck() throws IOException {
        File file_test = new File("src\\Paint\\windowspaintcs250.jpg");
        ImageEditor test = new ImageEditor(this, file_test);
        tabPaneObject.getTabs().add(test);
        tabPaneObject.getSelectionModel().select(test);
        return 0;
    }

    /**
     * Closes all files and asks if you want to save
     *
     * @return returns 0 if there was no error
     * @throws IOException if there is an error
     */
    @FXML
    private int CloseAllBtnAction() throws IOException {
        try {
            CloseAll(false);

            return 0;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Closes the whole programs
     *
     * @return returns 0 if there was no error
     * @throws IOException if there is an error
     */
    @FXML
    private int ExitBtnAction() throws IOException {
        try {
            closeProgram();
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * opens a file
     *
     * @param file the file to open
     */
    public void openFile(File file) {
        ImageEditor test = new ImageEditor(this, file);
        tabPaneObject.getTabs().add(test);
        tabPaneObject.getSelectionModel().select(test);
    }

    /**
     * Opens a file when clicked
     *
     * @return returns 0 if there was no error
     * @throws IOException if there is an error
     */
    @FXML
    public int OpenBtnAction() throws IOException {
        try {
            File file_test = FileExplorer.pickFileExplorer();
            if (file_test != null) {
                openFile(file_test);
            }
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Opens the help menu
     *
     * @return returns 0 if there was no error
     * @throws IOException if there is an error
     */
    @FXML
    private int HelpBtnAction() throws IOException {
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("helpMenu.fxml"));
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();

            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * prompts the resize menu
     *
     * @throws IOException if there is an error
     */
    @FXML
    private void ResizeBtnAction() throws IOException {

        resize.prompt(getCurrentTab());
    }

    /**
     * Launches the about paint menu
     *
     * @return returns 0 if there was no error
     * @throws IOException if there is an error
     */
    @FXML
    private int AboutPaintBtnAction() throws IOException {
        return (new AboutPaintController()).launch_window();
        
    }

    /**
     * sets the all the tabs to pannable when the control button is
     * pressed/released
     *
     * @param isPannable should the image be able to be panned
     */
    public void setPannable(boolean isPannable) {
        tabPaneObject.getTabs().forEach((tab) -> {
            ImageEditor tabEditor = (ImageEditor) tab;
            tabEditor.setPannable(isPannable);
        });
    }

    /**
     * Gets the current tab open
     *
     * @return the current tab opened
     */
    public ImageEditor getCurrentTab() {
        return (ImageEditor) tabPaneObject.getSelectionModel().getSelectedItem();
    }



    /**
     * The undo button
     */
    @FXML
    MenuItem UndoBtnObject;

    /**
     * enables/disables the undo button
     *
     * @param Disable should the undo button be disabled
     */
    public void setUndoDisable(boolean Disable) {
        UndoBtnObject.setDisable(Disable);
    }

    /**
     * The redo button
     */
    @FXML
    MenuItem RedoBtnObject;

    /**
     * enables/pannable the redo button
     *
     * @param Disable should the redo button be disabled
     */
    public void setRedoDisable(boolean Disable) {
        RedoBtnObject.setDisable(Disable);
    }

    /**
     * the time left till the next autosave
     */
    @FXML
    public Label autoSaveTime;

    /**
     * The auto save prompt
     */
    @FXML
    public HBox autoSavePrompt;

    /**
     * Close the program and end all processes
     */
    private void closeProgram() {
        PaintMain.CloseWindow();
        Platform.exit();
        System.exit(0);
    }

    /**
     * should the whole program be closed during CloseAll
     */
    private boolean closeMain = false;

    /**
     * prompts the close for all the tabes
     *
     * @param newCloseMain the start of a close
     * @return result is 0 if the user has not chosen, 1 if error, 2 is Save, 3
     * if do not Save, 4 if cancel launches the prompt to ask if the file should
     * be saved called by the main function on close
     */
    public int CloseAll(boolean newCloseMain) {
        try {
            closeMain = newCloseMain;
            closeRecursive();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * calls one tab at a time
     */
    public void closeRecursive() {
        try {
            if (tabPaneObject.getTabs().get(0) != null) {
                ImageEditor currentItem = (ImageEditor) tabPaneObject.getTabs().get(0);
                if (currentItem != null) {
                    currentItem.close(true, true);
                } else if (closeMain) {
                    closeProgram();
                }
            } else {
                closeProgram();
            }
        } catch (Exception e) {
            closeProgram();
            e.printStackTrace();
        }
    }

    /**
     * sets the auto save action
     */
    @FXML
    private void autoSaveNoAction() {
        AutoSave.NoAction();
    }

    /**
     * sets the auto save action
     */
    @FXML
    private void autoSaveYesAction() {
        AutoSave.YesAction();
    }

    /**
     * sets the auto save action
     */
    @FXML
    private void autoSaveAction() {
        AutoSave.autoSaveAction();
    }

}

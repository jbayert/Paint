/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * Adds a tab to the image editor
 *
 * @author jonb1
 */
public class ImageEditor extends Tab {

    /**
     * The current file that is open
     */
    private File currentFile;
    /**
     * Is the image saved
     */
    private boolean isSaved;
    /**
     * The zoomable pane to scroll
     */
    private ZoomableScrollPane scrollPaneObject;
    /**
     * The shape pane to store the shapes
     */
    private ShapePane ShapePaneObject;

    /**
     * The main frame
     */
    private PrimaryController mainFrame;
    /**
     * The maximum height that is used
     */
    private static double MAX_HEIGHT = 2000.0;

    /**
     * The file that holds auto save
     */
    private File tempFile = null;

    /**
     * opens a new tab
     *
     * @param newMainFrame pass the main controller where the save buttons are
     * located
     */
    public ImageEditor(PrimaryController newMainFrame) {
        super();

        mainFrame = newMainFrame;

        setup_draw();
    }

    /**
     * opens a file on start
     *
     * @param newMainFrame pass the main controller where the save buttons are
     * located
     * @param new_File the file to open
     */
    public ImageEditor(PrimaryController newMainFrame, File new_File) {
        super();

        try {
            mainFrame = newMainFrame;
            setup_draw();
            setFile(new_File);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads necessary thing for the canvas
     */
    private void setup_draw() {
        isSaved = true;

        ShapePaneObject = new ShapePane(mainFrame, this);

        scrollPaneObject = new ZoomableScrollPane(ShapePaneObject);
        scrollPaneObject.setPrefViewportHeight(MAX_HEIGHT);//set very large

        //  scroll bar is only displayed when needed
        scrollPaneObject.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        //  scroll bar is only displayed when needed
        scrollPaneObject.setHbarPolicy(ScrollBarPolicy.ALWAYS);

        setContent(scrollPaneObject);

        setClosable(true);
        setOnCloseRequest(onCloseEvent);

    }

    /**
     * closes the current tab
     */
    private void closeTab() {
        try {
            //delete temp file if saved properly
            if (isSaved) {
                tempFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            EventHandler<Event> handler = getOnClosed();
            if (null != handler) {
                handler.handle(null);
            }
            if (getTabPane() != null) {
                getTabPane().getTabs().remove(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates is called when it is open
     */
    public void reopenedCall() {
        ShapePaneObject.updateUndoBtn();
    }

    /**
     * undo the most recent change
     */
    public void undo() {
        //if something was undone then set that it is not saves
        if (0 == ShapePaneObject.undo()) {
            setNotSaved();
        }
    }

    /**
     * redo something that was undone
     *
     */
    public void redo() {
        //if something changed then set that it is not saves
        if (0 == ShapePaneObject.redo()) {
            setNotSaved();
        }
    }

    /**
     * what happends when the close button is pressed Launches an option to
     * save/cancel
     */
    private EventHandler<Event> onCloseEvent = (Event event) -> {
        close(true, false);
        event.consume();
    };

    /**
     * Asks if the user actually wants to save it
     *
     * @param askOnClose should there be a prompt to ask
     * @param recursiveClose should this call other tabs to close
     * @return returns 0 if exits properly
     */
    public int close(boolean askOnClose, boolean recursiveClose) {
        if (isSaved) {
            closeTab();
            if (recursiveClose) {
                mainFrame.closeRecursive();
            }
            return 0;
        } else if (askOnClose) {
            //lainch a new window
            ClosePrompt closePrompt = new ClosePrompt("Do you want to save " + getFileName() + "?");

            //callbacks on result
            closePrompt.setSaveEvent((ActionEvent e) -> {
                save();
                closeTab();
                if (recursiveClose) {
                    mainFrame.closeRecursive();
                }
            });
            closePrompt.setCancelEvent((ActionEvent e) -> {
            });
            closePrompt.setDoNotSaveEvent((ActionEvent e) -> {
                closeTab();
                if (recursiveClose) {
                    mainFrame.closeRecursive();
                }
            });

            return closePrompt.launch();
        } else {
            closeTab();
            if (recursiveClose) {
                mainFrame.closeRecursive();
            }
            return 0;
        }
    }

    /**
     * returns the name of the file opened
     *
     * @return the current string that is open
     */
    public String getFileName() {
        return currentFile.getName();
    }

    /**
     * loads the currnt file
     *
     * @return returns if it opened correctly
     */
    private int loadImage() {
        try {
            ShapePaneObject.loadImage(currentFile);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            sourceChannel.close();
            destChannel.close();
        }
    }

    /**
     * load a new file into the editor
     *
     * @param new_File the file to open
     * @return returns if it is a success.
     */
    private int setFile(File new_File) {
        try {
            currentFile = new_File;
            loadImage();
            setSaved();
            //create the temp file
            tempFile = new File(AutoSaveClass.TEMP_FOLDER + "TEMP_" + getFileName());
            if (tempFile.exists()) {
                //create a recovered file
                File recoveredFile = new File(AutoSaveClass.TEMP_FOLDER + "Recovered_" + getFileName());
                copyFileUsingChannel(tempFile, recoveredFile);
                mainFrame.AutoSave.prompt(recoveredFile);
            }

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Set the flag that the file is not saved
     */
    public void setNotSaved() {
        isSaved = false;
        setText("*" + getFileName() + "*");
    }

    /**
     * Set the flag that the file is saved
     */
    private void setSaved() {
        isSaved = true;
        setText(getFileName());
    }

    /**
     * save the image
     * @return returns an integer 0 if the file saves, else errors.
     */
    public int save() {
        return saveImage(currentFile);
    }

    /**
     * Gets the current file type of the opened file
     *
     * @return the file type, such as "png", "jpg", etc.
     */
    public String getFileExtension() {
        return mainFrame.FileExplorer.getFileExtensionOfFile(currentFile);
    }

    /**
     * auto save to a temp file
     */
    public void autoSave() {
        saveImage(tempFile, true);
    }

    /**
     * Saves the image
     *
     * @param file The file to saves
     * @return returns 0 if the program exits correctly
     */
    private int saveImage(File file) {
        return saveImage(file, false);
    }

    /**
     * saves the file.
     *
     * @param file The place where the where the file is saved
     * @param autoSave Is this being used for autosave. If it is not it resets
     * the flags that it has been saved
     * @return returns 0 if a success
     */
    private int saveImage(File file, boolean autoSave) {
        if (file != null) {
            try {
                WritableImage writableImage = ShapePaneObject.getWritableImage();

                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, mainFrame.FileExplorer.getFileExtensionOfFile(file), file);

                if (!autoSave) {
                    //do not reset if autosaved
                    setSaved();
                    ShapePaneObject.flatten();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 2;
            }
        }
        return 0;
    }

    /**
     * save as callback
     *
     * @param file the new file to save to
     * @return returns if the file saved correctly
     */
    public int saveAsImage(File file) {
        try {
            currentFile = file;
            int result = saveImage(file);
            setSaved();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * resizes the image
     *
     * @param new_width the new width
     * @param new_height the new height
     */
    public void resize(double new_width, double new_height) {
        ShapePaneObject.resizeImage(new_width, new_height);
        setNotSaved();
    }

    /**
     * gets the image height
     *
     * @return the image height
     */
    public double getImageHeight() {
        return ShapePaneObject.getImageHeight();
    }

    /**
     * gets the image width
     *
     * @return the current image width
     */
    public double getImageWidth() {
        return ShapePaneObject.getImageWidth();
    }

    /**
     * set if the image can be panned
     *
     * @param isPannable should the image be able to pan
     */
    public void setPannable(boolean isPannable) {
        scrollPaneObject.setPannable(isPannable);
    }

}

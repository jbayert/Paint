/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import java.io.File;
import javafx.stage.FileChooser;

/**
 * Creates a prompt to allow the user to save/saveAs/open a file
 *
 * @author jonb1
 */
public class FileExplorerClass {

    /**
     * The file chooser being used
     */
    private final FileChooser fileChooser;

    /**
     * creates a file explorer
     */
    FileExplorerClass() {

        //filter images
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(fileExtensions);

        fileExtensions = new FileChooser.ExtensionFilter("JPG (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(fileExtensions);

        fileExtensions = new FileChooser.ExtensionFilter("GIF (*.gif)", "*.gif");
        fileChooser.getExtensionFilters().add(fileExtensions);

        fileExtensions = new FileChooser.ExtensionFilter("HEIC (*heic)", "*.heic");
        fileChooser.getExtensionFilters().add(fileExtensions);

        //Load an Images category
        FileChooser.ExtensionFilter ImageExtensions = new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif", "*.mpo", "*.jfif");
        fileChooser.getExtensionFilters().add(ImageExtensions);
    }

    /**
     * launches the Explorer to save an image
     *
     * @return what file the user choose to save to
     */
    public File saveAsExplorer() {
        fileChooser.setTitle("Save As...");
        File test = fileChooser.showSaveDialog(PaintMain.getMainStage());
        return test;//return the file found    
    }

    /**
     * launches the Explorer to choose the the file
     *
     * @return what file the user choose to save to
     */
    public File pickFileExplorer() {
        fileChooser.setTitle("Open File");
        return fileChooser.showOpenDialog(PaintMain.getMainStage());//return the file found 
    }

    /**
     * Gets the file type of an inputed file
     *
     * @param file The
     * @return the file type
     */
    public static String getFileExtensionOfFile(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
}

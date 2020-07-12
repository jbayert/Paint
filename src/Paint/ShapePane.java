/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paint;

import java.io.File;
import java.util.Stack;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * manages the open image and the drawing of the shape
 *
 * @author jonb1
 */
public class ShapePane extends AnchorPane {

    /**
     * The main Frame
     */
    private final PrimaryController mainFrame;

    /**
     * The image editor for this shape pane
     */
    private final ImageEditor ImageEditorObject;

    /**
     * The size to start at
     */
    private static final int START_SIZE = 300;

    /**
     * The anchor pane with the image
     */
    private final AnchorPane anchorPaneObject;
    /**
     * The selection rectangle
     */
    private final Rectangle recSelect;
    /**
     * The image that is opened
     */
    private final ImageView mainImage;

    /**
     * The current shape being drawn
     */
    private Node currentShape;
    /**
     * The current toll selected
     */
    private String currentTool = null;

    /**
     * The arc is started
     */
    private static final int ARC_START = 0;
    /**
     * The arc is clicked
     */
    private static final int ARC_CLICKED = 1;
    /**
     * The arc is being dragged
     */
    private static final int ARC_DRAG = 2;
    /**
     * the current arc shape
     */
    private int arcClick = ARC_START;

    /**
     * The current image shown
     */
    private Image imageShown;

    /**
     * The undo stack that stores added Nodes
     */
    private final Stack<Node> UndoStack;
    /**
     * The redo stack that stores added Nodes
     */
    private final Stack<Node> RedoStack;

    /**
     * manages the drawing of the shape
     *
     * @param newMainFrame the main frame
     * @param newImageEditorObject the image editor for this shape pane
     */
    ShapePane(PrimaryController newMainFrame, ImageEditor newImageEditorObject) {
        super();

        mainFrame = newMainFrame;
        ImageEditorObject = newImageEditorObject;

        mainImage = new ImageView();

        //Set up the layout
        anchorPaneObject = new AnchorPane();
        anchorPaneObject.getChildren().addAll(mainImage);

        setOnMousePressed(mouseDownHandler);
        setOnMouseDragged(mouseDraggedHandler);

        recSelect = new Rectangle();
        recSelect.setStrokeWidth(3);
        recSelect.setStroke(Color.BLACK);
        recSelect.setFill(Color.TRANSPARENT);
        recSelect.setVisible(false);
        getChildren().addAll(anchorPaneObject, recSelect);

        UndoStack = new Stack<>();
        RedoStack = new Stack<>();

    }

    /**
     * save the start of the line To be removed
     */
    private double xPressedLocation, yPressedLocation;
    /**
     * Handle a mouse clicked
     */
    EventHandler<MouseEvent> mouseDownHandler = (MouseEvent mouseEvent) -> {
        xPressedLocation = mouseEvent.getX();
        yPressedLocation = mouseEvent.getY();
        boolean isPrimaryClicked = (mouseEvent.getButton() == MouseButton.PRIMARY);
        drawStart(xPressedLocation, yPressedLocation, isPrimaryClicked);
    };

    /**
     * temp line to show where it might go
     */
    EventHandler<MouseEvent> mouseDraggedHandler = (MouseEvent mouseEvent) -> {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x > getWidth()) {
            x = getWidth();
        }
        if (y > getHeight()) {
            y = getHeight();
        }
        drawdrag(x, y);
    };

    /**
     * This draws the shapes and saves it to currentShape
     *
     * @param x1 the starting x
     * @param y1 the starting y
     * @param isPrimaryClicked is the left or right key pressed
     */
    private void drawStart(double x1, double y1, boolean isPrimaryClicked) {
        try {
            if (HotkeysClass.IsCtrlPressed()) {
                return;
            }
            currentTool = mainFrame.ToolMenu.getSelectedTool();
            if (currentTool == null) {
                return;
            }
            if (null != currentTool) {
                switch (currentTool) {
                    case "Line":
                        currentShape = new Line();
                        Line currentLine = (Line) currentShape;
                        currentLine.setStrokeWidth(mainFrame.ColorChooser.getWidth());
                        if (isPrimaryClicked) {
                            currentLine.setStroke(mainFrame.ColorChooser.getPrimaryColor());
                        } else {
                            currentLine.setStroke(mainFrame.ColorChooser.getSecondaryColor());
                        }
                        currentLine.setStartX(x1);
                        currentLine.setStartY(y1);
                        currentLine.setEndX(x1);
                        currentLine.setEndY(y1);
                        break;
                    case "Rectangle": {
                        currentShape = new Rectangle();
                        Rectangle r = (Rectangle) currentShape;
                        r.setX(x1);
                        r.setY(y1);
                        r.setWidth(0);
                        r.setHeight(0);
                        r.setStrokeWidth(mainFrame.ColorChooser.getWidth());
                        r.setStroke(mainFrame.ColorChooser.getPrimaryColor());
                        r.setFill(mainFrame.ColorChooser.getSecondaryColor());
                        break;
                    }
                    case "Free Draw": {
                        currentShape = new Path();
                        Path p = (Path) currentShape;
                        MoveTo moveTo = new MoveTo();
                        moveTo.setX(x1);
                        moveTo.setY(y1);
                        p.setStrokeWidth(mainFrame.ColorChooser.getWidth());
                        if (isPrimaryClicked) {
                            p.setStroke(mainFrame.ColorChooser.getPrimaryColor());
                        } else {
                            p.setStroke(mainFrame.ColorChooser.getSecondaryColor());
                        }
                        p.getElements().add(moveTo);
                        break;
                    }
                    case "Eraser": {
                        currentShape = new Path();
                        Path p = (Path) currentShape;
                        MoveTo moveTo = new MoveTo();
                        moveTo.setX(xPressedLocation);
                        moveTo.setY(yPressedLocation);
                        p.setStrokeWidth(mainFrame.ColorChooser.getWidth());
                        p.setStroke(Color.WHITE);
                        p.getElements().add(moveTo);
                        break;
                    }
                    case "Circle":
                        currentShape = new Circle();
                        Circle c = (Circle) currentShape;
                        c.setCenterX(x1);
                        c.setCenterY(y1);
                        c.setRadius(0.0f);
                        c.setFill(mainFrame.ColorChooser.getSecondaryColor());
                        c.setStrokeWidth(mainFrame.ColorChooser.getWidth());
                        c.setStroke(mainFrame.ColorChooser.getPrimaryColor());
                        break;
                    case "Ellipse":
                        currentShape = new Ellipse();
                        Ellipse e = (Ellipse) currentShape;
                        e.setCenterX(x1);
                        e.setCenterY(y1);
                        e.setRadiusX(0.0f);
                        e.setRadiusY(0.0f);
                        e.setFill(mainFrame.ColorChooser.getSecondaryColor());
                        e.setStrokeWidth(mainFrame.ColorChooser.getWidth());
                        e.setStroke(mainFrame.ColorChooser.getPrimaryColor());
                        break;
                    case "Eye Dropper": {
                        WritableImage writableImage;
                        writableImage = new WritableImage((int) getWidth(), (int) getHeight());
                        anchorPaneObject.snapshot(null, writableImage);
                        PixelReader r = writableImage.getPixelReader();
                        Color argb = r.getColor((int) x1, (int) y1);
                        if (isPrimaryClicked) {
                            mainFrame.ColorChooser.setPrimaryColor(argb);
                        } else {
                            mainFrame.ColorChooser.setSecondaryColor(argb);
                        }
                        break;
                    }
                    case "Polygon": {
                        currentShape = new Polygon();
                        Polygon p = (Polygon) currentShape;
                        p.setFill(mainFrame.ColorChooser.getSecondaryColor());
                        p.setStrokeWidth(mainFrame.ColorChooser.getWidth());
                        p.setStroke(mainFrame.ColorChooser.getPrimaryColor());
                        break;
                    }
                    case "Text":
                        currentShape = new Text(x1, y1, mainFrame.ToolMenuInputs.getTextField());
                        Text t = (Text) currentShape;
                        //Polygon p = (Polygon) currentShape;
                        t.setFont(Font.font("Verdana", mainFrame.ToolMenuInputs.getFontSize()));
                        if (isPrimaryClicked) {
                            t.setStroke(mainFrame.ColorChooser.getPrimaryColor());
                            t.setFill(mainFrame.ColorChooser.getSecondaryColor());
                        } else {
                            t.setStroke(mainFrame.ColorChooser.getSecondaryColor());
                            t.setFill(mainFrame.ColorChooser.getPrimaryColor());
                        }
                        break;
                }
            }

            if (null != currentTool) {
                switch (currentTool) {
                    case "Select":
                        recSelect.setX(x1);
                        recSelect.setY(y1);
                        recSelect.setHeight(0);
                        recSelect.setWidth(0);
                        recSelect.setVisible(true);
                        currentShape = null;
                        break;
                    case "Copy":
                        if (recSelect.isVisible()) {
                            SnapshotParameters newVeiw = new SnapshotParameters();
                            newVeiw.setViewport(new Rectangle2D(recSelect.getX(), recSelect.getY(), recSelect.getWidth(), recSelect.getHeight()));
                            WritableImage writableImage;
                            writableImage = new WritableImage((int) recSelect.getWidth(), (int) recSelect.getHeight());
                            anchorPaneObject.snapshot(newVeiw, writableImage);

                            //Creates a new Node to add
                            currentShape = new AnchorPane();
                            AnchorPane a = (AnchorPane) currentShape;

                            //Move the pixels
                            ImageView iv2 = new ImageView();
                            iv2.setImage(writableImage);
                            iv2.setX(x1);
                            iv2.setY(y1);

                            //Clear the current Image
                            //Rectangle r = new Rectangle(recSelect.getX(), recSelect.getY(), recSelect.getWidth(), recSelect.getHeight());
                            //r.setStrokeWidth(1);
                            //r.setStroke(Color.WHITE);
                            //r.setFill(Color.WHITE);
                            a.getChildren().addAll(iv2);

                            //recSelect.setVisible(false);
                        }
                        break;
                    case "Move":
                        if (recSelect.isVisible()) {
                            SnapshotParameters newVeiw = new SnapshotParameters();
                            newVeiw.setViewport(new Rectangle2D(recSelect.getX(), recSelect.getY(), recSelect.getWidth(), recSelect.getHeight()));
                            WritableImage writableImage;
                            writableImage = new WritableImage((int) recSelect.getWidth(), (int) recSelect.getHeight());
                            anchorPaneObject.snapshot(newVeiw, writableImage);

                            //Creates a new Node to add
                            currentShape = new AnchorPane();
                            AnchorPane a = (AnchorPane) currentShape;

                            //Move the pixels                            
                            ImageView iv2 = new ImageView();
                            iv2.setImage(writableImage);
                            iv2.setX(x1);
                            iv2.setY(y1);

                            //Clear the current Image
                            Rectangle r = new Rectangle(recSelect.getX(), recSelect.getY(), recSelect.getWidth(), recSelect.getHeight());
                            r.setStrokeWidth(1);
                            r.setStroke(Color.WHITE);
                            r.setFill(Color.WHITE);

                            a.getChildren().addAll(iv2, r);

                            recSelect.setVisible(false);
                        }
                        break;
                    case "Rotate":
                        if (recSelect.isVisible()) {
                            SnapshotParameters newVeiw = new SnapshotParameters();
                            newVeiw.setViewport(new Rectangle2D(recSelect.getX(), recSelect.getY(), recSelect.getWidth(), recSelect.getHeight()));
                            WritableImage writableImage;
                            writableImage = new WritableImage((int) recSelect.getWidth(), (int) recSelect.getHeight());
                            anchorPaneObject.snapshot(newVeiw, writableImage);

                            //Creates a new Node to add
                            currentShape = new AnchorPane();
                            AnchorPane a = (AnchorPane) currentShape;

                            //Move the pixels
                            ImageView iv2 = new ImageView();
                            iv2.setImage(writableImage);
                            iv2.setX(x1);
                            iv2.setY(y1);
                            iv2.setRotate((double) mainFrame.ToolMenuInputs.getRotateDegrees());

                            //Clear the current Image
                            Rectangle r = new Rectangle(recSelect.getX(), recSelect.getY(), recSelect.getWidth(), recSelect.getHeight());
                            r.setStrokeWidth(1);
                            r.setStroke(Color.WHITE);
                            r.setFill(Color.WHITE);

                            a.getChildren().addAll(iv2, r);

                            recSelect.setVisible(false);
                        }
                        break;
                    default:
                        recSelect.setVisible(false);
                        break;
                }
            }

            if ("Arc".equals(currentTool)) {
                if (arcClick == ARC_START) {
                    currentShape = new Arc();
                    Arc a = (Arc) currentShape;
                    a.setFill(mainFrame.ColorChooser.getSecondaryColor());
                    a.setStrokeWidth(mainFrame.ColorChooser.getWidth());
                    a.setStroke(mainFrame.ColorChooser.getPrimaryColor());
                    Arc arc = (Arc) currentShape;
                    arc.setCenterX(x1);
                    arc.setCenterY(y1);
                    arcClick = ARC_CLICKED;
                } else {
                    Arc arc = (Arc) currentShape;
                    double radius = Math.sqrt(Math.pow(x1 - arc.getCenterX(), 2) + Math.pow(y1 - arc.getCenterY(), 2));
                    arc.setRadiusX(radius);
                    arc.setRadiusY(radius);
                    arc.setStartAngle(java.lang.Math.toDegrees(Math.atan2(arc.getCenterY() - y1, x1 - arc.getCenterX())));

                    arcClick = ARC_DRAG;
                    arc.setType(ArcType.ROUND);
                }
            } else {
                arcClick = ARC_START;
            }

            if (currentShape != null) {
                if (!anchorPaneObject.getChildren().contains(currentShape)) {
                    anchorPaneObject.getChildren().add(currentShape);
                    pushUndo(currentShape);
                    ImageEditorObject.setNotSaved();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * temporary draws. Shows the nodes while the draw is pressed
     *
     * @param x2 the x of the mouse
     * @param y2 the y of the mouse
     */
    private void drawdrag(double x2, double y2) {
        if (HotkeysClass.IsCtrlPressed()) {
            //pass
        } else if (currentTool == null) {
            //pass
        } else if ("Line".equals(currentTool)) {
            Line currentLine = (Line) currentShape;
            currentLine.setEndX(x2);
            currentLine.setEndY(y2);
        } else if ("Rectangle".equals(currentTool)) {
            double width = x2 - xPressedLocation;
            double height = y2 - yPressedLocation;
            Rectangle r = (Rectangle) currentShape;
            if (width < 0) {
                r.setX(x2);
                r.setWidth(-1.0 * width);
            } else {
                r.setWidth(width);
            }

            if (height < 0) {
                r.setY(y2);
                r.setHeight(-1.0 * height);
            } else {
                r.setHeight(height);
            }
        } else if ("Free Draw".equals(currentTool)) {
            Path p = (Path) currentShape;
            LineTo linto = new LineTo();
            linto.setX(x2);
            linto.setY(y2);
            p.getElements().add(linto);
        } else if ("Eraser".equals(currentTool)) {
            Path p = (Path) currentShape;
            LineTo linto = new LineTo();
            linto.setX(x2);
            linto.setY(y2);
            p.getElements().add(linto);
        } else if ("Circle".equals(currentTool)) {
            double radius = Math.sqrt(Math.pow(xPressedLocation - x2, 2) + Math.pow(yPressedLocation - y2, 2));
            Circle c = (Circle) currentShape;
            c.setRadius(radius);
        } else if ("Ellipse".equals(currentTool)) {
            double RadiusX = Math.abs((xPressedLocation - x2) / 2.0f);
            double RadiusY = Math.abs((yPressedLocation - y2) / 2.0f);
            double centerX = (xPressedLocation + x2) / 2.0f;
            double centerY = (yPressedLocation + y2) / 2.0f;
            Ellipse e = (Ellipse) currentShape;
            e.setCenterX(centerX);
            e.setCenterY(centerY);
            e.setRadiusX(RadiusX);
            e.setRadiusY(RadiusY);
        } else if ("Arc".equals(currentTool)) {
            if (arcClick != ARC_CLICKED) {//The first click does not drag
                Arc arc = (Arc) currentShape;
                double radius = Math.sqrt(Math.pow(x2 - arc.getCenterX(), 2) + Math.pow(y2 - arc.getCenterY(), 2));
                arc.setLength(java.lang.Math.toDegrees(Math.atan2(arc.getCenterY() - y2, x2 - arc.getCenterX())) - arc.getStartAngle());
                arcClick = ARC_START;
            }
        } else if ("Polygon".equals(currentTool)) {
            Polygon polygon = (Polygon) currentShape;
            int sides = mainFrame.ToolMenuInputs.getPolygonSides();
            polygon.getPoints().clear();
            double radius = Math.sqrt(Math.pow(xPressedLocation - x2, 2) + Math.pow(yPressedLocation - y2, 2));
            final double angleStep = Math.PI * 2 / sides;
            double angle = Math.atan2(yPressedLocation - y2, x2 - xPressedLocation);
            for (int i = 0; i < sides; i++, angle += angleStep) {
                polygon.getPoints().addAll(
                        Math.sin(angle) * radius + xPressedLocation, // x coordinate of the corner
                        Math.cos(angle) * radius + yPressedLocation // y coordinate of the corner
                );
            }
        } else if ("Text".equals(currentTool)) {
            Text t = (Text) currentShape;
            t.setX(x2);
            t.setY(y2);
        } else if ("Select".equals(currentTool)) {
            double width = x2 - xPressedLocation;
            double height = y2 - yPressedLocation;
            if (width < 0) {
                recSelect.setX(x2);
                recSelect.setWidth(-1.0 * width);
            } else {
                recSelect.setWidth(width);
            }

            if (height < 0) {
                recSelect.setY(y2);
                recSelect.setHeight(-1.0 * height);
            } else {
                recSelect.setHeight(height);
            }
        } else if ("Move".equals(currentTool)) {
            if (!recSelect.isVisible()) {

            }
        }

    }

    /**
     * updates the polygon location
     *
     * @param polygon the pointer to the polygon node
     * @param centerX the center x of the polygon
     * @param centerY the center y of the polygon
     * @param radius the radius of the polygon
     * @param sides the number of sides of the polygon
     */
    private static void setPolygonSides(Polygon polygon, double centerX, double centerY, double radius, int sides) {
        polygon.getPoints().clear();
        final double angleStep = Math.PI * 2 / sides;
        double angle = 0; // assumes one point is located directly beneat the center point
        for (int i = 0; i < sides; i++, angle += angleStep) {
            polygon.getPoints().addAll(
                    Math.sin(angle) * radius + centerX, // x coordinate of the corner
                    Math.cos(angle) * radius + centerY // y coordinate of the corner
            );
        }
    }

    /**
     * draws a line on the canvas
     */
    private void drawFinal() {
        currentShape = null;
    }

    /**
     * enables undo btns
     */
    public void updateUndoBtn() {
        mainFrame.setUndoDisable(UndoStack.size() == 0);
        mainFrame.setRedoDisable(RedoStack.size() == 0);
    }

    /**
     * Clears the undo Stack
     */
    public void clearUndo() {
        UndoStack.clear();
        updateUndoBtn();
    }

    /**
     * Push to the undo stack
     *
     * @param n the node to be pushed
     */
    public void pushUndo(Node n) {
        try {
            if (UndoStack.search(n) == -1) {
                UndoStack.push(n);
                RedoStack.clear();
                updateUndoBtn();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * gets if there is anything to undo
     * @return returns if the Undo is empty
     */
    private boolean isUndoEmpty() {
        return (UndoStack.size() == 0);
    }
    /**
     * gets if there is anything to redo
     * @return returns if the redo is empty
     */
    private boolean isRedoEmpty() {
        return (RedoStack.size() == 0);
    }

    /**
     * remove the last drawn shape
     *
     * @return returns 0 if exited without an error
     */
    public int undo() {
        try {
            if (UndoStack.size() != 0) {
                Node shapeToRemove = UndoStack.pop();
                if (shapeToRemove != null) {
                    anchorPaneObject.getChildren().remove(shapeToRemove);
                    RedoStack.push(shapeToRemove);

                    updateUndoBtn();
                    return 0;
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }

    /**
     * undo the last redo
     *
     * @return returns 0 if something is undone
     */
    public int redo() {
        try {
            if (RedoStack.size() != 0) {
                Node shapeToAdd = RedoStack.pop();
                if (shapeToAdd != null) {
                    anchorPaneObject.getChildren().add(shapeToAdd);
                    UndoStack.push(shapeToAdd);
                    updateUndoBtn();
                    return 0;
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }

    /**
     * load a new image
     *
     * @param file the file to load
     */
    public void loadImage(File file) {
        imageShown = new Image(file.toURI().toString());
        mainImage.setImage(imageShown);
        mainImage.setFitHeight(imageShown.getHeight());
        mainImage.setFitWidth(imageShown.getWidth());
    }

    /**
     * resize the canvas
     *
     * @param new_width the new width
     * @param new_height the new height
     */
    public void resizeImage(double new_width, double new_height) {
        flatten();
        mainImage.setFitHeight((int) new_height);
        mainImage.setFitWidth((int) new_width);
    }

    /**
     * gets the writable image of the item shown
     *
     * @return returns the writable image
     */
    public WritableImage getWritableImage() {
        SnapshotParameters newVeiw = new SnapshotParameters();
        newVeiw.setViewport(new Rectangle2D(mainImage.getX(), mainImage.getY(), mainImage.getFitWidth(), mainImage.getFitHeight()));
        WritableImage writableImage;
        writableImage = new WritableImage((int) imageShown.getWidth(), (int) imageShown.getHeight());
        anchorPaneObject.snapshot(newVeiw, writableImage);

        return writableImage;
    }

    /**
     * Flattens the current image. Removes all items in the undo and redo stack.
     *
     * @return returns the flattened Image
     */
    public WritableImage flatten() {
        WritableImage writableImage = getWritableImage();
        anchorPaneObject.getChildren().clear();
        imageShown = (Image) writableImage;
        mainImage.setImage(imageShown);
        anchorPaneObject.getChildren().add(mainImage);

        clearUndo();

        return writableImage;
    }

    /**
     * gets the current height
     *
     * @return the current height
     */
    public double getImageHeight() {
        return imageShown.getHeight();
    }

    /**
     * gets the current width
     *
     * @return the current width
     */
    public double getImageWidth() {
        return imageShown.getWidth();
    }

}

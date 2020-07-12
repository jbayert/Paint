package Paint;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * allows a pane to be scrolled with the zoom wheel
 *
 * @author jonb1
 */
public class ZoomableScrollPane extends ScrollPane {

    /**
     * how initial scale value
     */
    private double scaleValue = 0.7;
    /**
     * how fast the scale happens
     */
    private double zoomIntensity = 0.02;
    /**
     * the node to scale
     */
    private Node target;
    /**
     * the node that is actually zooming
     */
    private Node zoomNode;
    /**
     * the group node
     */
    private Node test;

    /**
     * zooms a pane
     *
     * @param target the pane to zoom
     */
    public ZoomableScrollPane(Node target) {
        super();
        this.target = target;
        test = new Group(this.target);
        this.zoomNode = new Group(test);
        setContent(outerNode(zoomNode));

        //setPannable(true);
        setFitToHeight(true);//center
        setFitToWidth(true);//center

        updateScale();
    }

    /**
     *
     * sets the center of the outer node
     *
     * @param node the node to center
     * @return the outerNode
     */
    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();

            //check if the control is pressed before scrolling
            if (HotkeysClass.IsCtrlPressed()) {
                onScroll(e.getTextDeltaY(), new Point2D(e.getX(), e.getY()));
            }
        });
        return outerNode;
    }

    /**
     * center the node
     *
     * @param node within what node
     * @return node created
     */
    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    /**
     * updates the scale of the node
     */
    private void updateScale() {
        test.setScaleX(scaleValue);
        test.setScaleY(scaleValue);
    }

    /**
     * when a scroll is detected
     *
     * @param wheelDelta how much was scrolled
     * @param mousePoint where the mouse is
     */
    private void onScroll(double wheelDelta, Point2D mousePoint) {
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

        Bounds innerBounds = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        // calculate pixel offsets from [0, 1] range
        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        scaleValue = scaleValue * zoomFactor;
        updateScale();
        this.layout(); // refresh ScrollPane scroll positions & target bounds

        // convert target coordinates to zoomTarget coordinates
        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));

        // calculate adjustment of scroll position (pixels)
        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

        // convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane)
        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
        this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }
}

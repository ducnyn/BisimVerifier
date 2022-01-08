package me.ducanh.thesis;

import com.brunomnsilva.smartgraph.graphview.SmartArrow;
import com.brunomnsilva.smartgraph.graphview.UtilitiesBindings;
import com.brunomnsilva.smartgraph.graphview.UtilitiesPoint2D;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import me.ducanh.thesis.model.CustomEdge;

import java.awt.*;
import java.util.Set;
import java.util.concurrent.Callable;

public class VisEdge {

    private final DoubleProperty distance = new SimpleDoubleProperty();
    @FXML
//  public Path path;
    public CubicCurve cubicCurve;
    @FXML
    public Text visEdgeLabel;

    public SmartArrow arrow;
    @FXML
    private AnchorPane anchorPane = new AnchorPane();
    private CustomEdge edge;
    private VisVertex source;
    private VisVertex target;

    public void init(String label, VisVertex source, VisVertex target) {
        this.source = source;
        this.target = target;
        this.visEdgeLabel.setText(label);

        visEdgeLabel.toFront();
        cubicCurve.setFill(Color.TRANSPARENT);
        cubicCurve.setStroke(Color.DARKGREY);

        draw();
        source.getCenterXProperty().addListener((oldV,newV,value)->draw());
        source.getCenterYProperty().addListener((oldV,newV,value)->draw());
        target.getCenterXProperty().addListener((oldV,newV,value)->draw());
        target.getCenterYProperty().addListener((oldV,newV,value)->draw());

        bindLabelPosition();
        bindArrow(new SmartArrow(8));
        anchorPane.getChildren().add(arrow);
//        anchorPane.setPickOnBounds(false);
    }

    private void draw() {

        double controlX1;
        double controlY1;
        cubicCurve.startXProperty().set(source.getCenterX());
        cubicCurve.startYProperty().set(source.getCenterY());
        cubicCurve.endXProperty().set(target.getCenterX());
        cubicCurve.endYProperty().set(target.getCenterY());
        if (this.source == this.target) {

            controlX1 = this.target.getCenterX() - this.target.getRadius() * 2.0D;
            controlY1 = this.target.getCenterY() - this.target.getRadius() * 2.0D;
            double controlX2 = this.target.getCenterX() + this.target.getRadius() * 2.0D;
            double controlY2 = this.target.getCenterY() - this.target.getRadius() * 2.0D;


            int startDegree = 120;
            int endDegree = 60;
            cubicCurve.setStartX(source.getCenterX()+source.getRadius()*Math.cos(Math.toRadians(startDegree)));
            cubicCurve.setStartY(source.getCenterY()-source.getRadius()*Math.sin(Math.toRadians(startDegree)));
            cubicCurve.setEndX(target.getCenterX()+target.getRadius()*Math.cos(Math.toRadians(endDegree)));
            cubicCurve.setEndY(target.getCenterY()-target.getRadius()*Math.sin(Math.toRadians(endDegree)));

            cubicCurve.setControlX1(controlX1);
            cubicCurve.setControlY1(controlY1);
            cubicCurve.setControlX2(controlX2);
            cubicCurve.setControlY2(controlY2);
        } else {
            controlX1 = (this.target.getCenterX() + this.source.getCenterX()) / 2.0D;
            controlY1 = (this.target.getCenterY() + this.source.getCenterY()) / 2.0D;
            Point2D midpoint = new Point2D(controlX1, controlY1);
            Point2D startpoint = new Point2D(this.source.getCenterX(), this.source.getCenterY());
            Point2D endpoint = new Point2D(this.target.getCenterX(), this.target.getCenterY());
            double angle = 20.0D;
            double distance = startpoint.distance(endpoint);
            angle -= distance / 1500.0D * angle;
            midpoint = UtilitiesPoint2D.rotate(midpoint, startpoint, -angle + 1 * (angle + angle));
            cubicCurve.setControlX1(midpoint.getX());
            cubicCurve.setControlY1(midpoint.getY());
            cubicCurve.setControlX2(midpoint.getX());
            cubicCurve.setControlY2(midpoint.getY());
        }
    }

    private void bindLabelPosition() {
//    visEdgeLabel.xProperty().bind(sourceXProperty.add(targetXProperty).divide(2).subtract(visEdgeLabel.getLayoutBounds().getWidth() / 2.0D));
//    visEdgeLabel.yProperty().bind(sourceYProperty.add(targetYProperty).divide(2).add(visEdgeLabel.getLayoutBounds().getHeight() / 1.5D));
//    this.attachedLabel = label;
        visEdgeLabel.xProperty().bind(cubicCurve.controlX1Property().add(cubicCurve.controlX2Property()).divide(2).subtract(visEdgeLabel.getLayoutBounds().getWidth() / 2.0D));
        visEdgeLabel.yProperty().bind(cubicCurve.controlY1Property().add(cubicCurve.controlY2Property()).divide(2).add(visEdgeLabel.getLayoutBounds().getHeight() / 2.0D));

    }

    public void bindArrow(SmartArrow arrow) {
        this.arrow = arrow;
        arrow.setStroke(Color.DARKGREY);


        arrow.translateXProperty().bind(cubicCurve.endXProperty());
        arrow.translateYProperty().bind(cubicCurve.endYProperty());
        Rotate rotation = new Rotate();
//    rotation.pivotXProperty().bind(cubicCurve.translateXProperty());
//    rotation.pivotYProperty().bind(cubicCurve.translateYProperty());
        rotation.angleProperty().bind(UtilitiesBindings.toDegrees(UtilitiesBindings.atan2(cubicCurve.endYProperty().subtract(cubicCurve.controlY2Property()), cubicCurve.endXProperty().subtract(cubicCurve.controlX2Property()))));
//      rotation.angleProperty().bind(UtilitiesBindings.toDegrees(UtilitiesBindings.atan2()))
//    rotation.angleProperty().add(4.0D);
        Translate translation = new Translate(-this.target.getRadius(), 0.0D);
        FlowLayout
        arrow.getTransforms().add(rotation);
        arrow.getTransforms().add(translation);
    }

    public Node getRoot() {
        return anchorPane;
    }
//  public DoubleProperty getStartXProperty(){
//    return line.startXProperty();
//  }
//
//  public DoubleProperty getStartYProperty(){
//    return line.startYProperty();
//  }
//
//  public DoubleProperty getEndXProperty(){
//    return line.endXProperty();
//  }

    //  public DoubleProperty getEndYProperty(){
//    return line.endYProperty();
//  }
    @Override
    public String toString() {
        return edge.toString();
    }

    public void toBack() {
        anchorPane.toBack();
    }
}

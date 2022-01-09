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
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import me.ducanh.thesis.model.CustomEdge;

import java.util.Set;
import java.util.concurrent.Callable;

public class VisEdge extends AnchorPane{


    public CubicCurve cubicCurve = new CubicCurve();
    public Text visEdgeLabel = new Text();
    public SmartArrow arrow = new SmartArrow(8);

    private final VisVertex source;
    private final VisVertex target;
    Double endAngle;
    Double startAngle;

    public VisEdge(String label, VisVertex source, VisVertex target) {
        this.getChildren().add(cubicCurve);
        this.getChildren().add(visEdgeLabel);
        this.getChildren().add(arrow);

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
        bindArrow();
    }

    private void draw() {

        double controlX1;
        double controlY1;

        if (this.source == this.target) {

//            controlX1 = this.target.getCenterX() - this.target.getRadius() * 2.0D;
//            controlY1 = this.target.getCenterY() - this.target.getRadius() * 2.0D;
//            double controlX2 = this.target.getCenterX() + this.target.getRadius() * 2.0D;
//            double controlY2 = this.target.getCenterY() - this.target.getRadius() * 2.0D;


            int startDegree = 130;
            int endDegree = 50;
            double selfPointerSize = 3.5;
            //for some reason, you hae to subtract r*sin(angle) from the Y coordinate, as if the axis was mirrored.
            //positioning start and end points on the edge of the circle using angle properties to get the coordinates
            //you could also add a point at the center of the vertex, rotate its coordinate system to the preferred angle and translate the X value to its radius
            cubicCurve.setStartX(source.getCenterX()+source.getRadius()*Math.cos(Math.toRadians(startDegree)));
            cubicCurve.setStartY(source.getCenterY()-source.getRadius()*Math.sin(Math.toRadians(startDegree)));
            cubicCurve.setEndX(target.getCenterX()+(target.getRadius()+cubicCurve.getStrokeWidth())*Math.cos(Math.toRadians(endDegree)));
            cubicCurve.setEndY(target.getCenterY()-(target.getRadius()+cubicCurve.getStrokeWidth())*Math.sin(Math.toRadians(endDegree)));



            controlX1 = this.target.getCenterX() + selfPointerSize*(cubicCurve.getStartX()-target.getCenterX());
            controlY1 = this.target.getCenterY() + selfPointerSize*(cubicCurve.getStartY()-target.getCenterY());
            double controlX2 = this.target.getCenterX() + selfPointerSize*(cubicCurve.getEndX()-target.getCenterX());
            double controlY2 = this.target.getCenterY() + selfPointerSize*(cubicCurve.getEndY()-target.getCenterY());

            cubicCurve.setControlX1(controlX1);
            cubicCurve.setControlY1(controlY1);
            cubicCurve.setControlX2(controlX2);
            cubicCurve.setControlY2(controlY2);
        } else {

            double customRotation = Math.toRadians(20);

            //here you don't have to compensate for the y-coordinate bug, because atan already includes the y-coordinate
            double sourceTargetAngle = Math.atan2(target.getCenterY()-source.getCenterY(),target.getCenterX()-source.getCenterX());
            double targetSourceAngle = Math.atan2(source.getCenterY()-target.getCenterY(),source.getCenterX()-target.getCenterX());
            cubicCurve.setStartX(source.getCenterX()+source.getRadius()*Math.cos(sourceTargetAngle+customRotation));
            cubicCurve.setStartY(source.getCenterY()+source.getRadius()*Math.sin(sourceTargetAngle+customRotation));
            cubicCurve.setEndX(target.getCenterX()+target.getRadius()*Math.cos(targetSourceAngle-customRotation));
            cubicCurve.setEndY(target.getCenterY()+target.getRadius()*Math.sin(targetSourceAngle-customRotation));

            double midX = (cubicCurve.getEndX() + cubicCurve.getStartX()) / 2.0;
            double midY = (cubicCurve.getEndY() + cubicCurve.getStartY()) / 2.0;
            Point2D mid = new Point2D(midX, midY);
            mid = UtilitiesPoint2D.rotate(mid, new Point2D(source.getCenterX(),source.getCenterY()), -4*customRotation);
            cubicCurve.setControlX1(mid.getX());
            cubicCurve.setControlY1(mid.getY());
            cubicCurve.setControlX2(mid.getX());
            cubicCurve.setControlY2(mid.getY());
        }
    }

    private void bindLabelPosition() {
//    visEdgeLabel.xProperty().bind(sourceXProperty.add(targetXProperty).divide(2).subtract(visEdgeLabel.getLayoutBounds().getWidth() / 2.0D));
//    visEdgeLabel.yProperty().bind(sourceYProperty.add(targetYProperty).divide(2).add(visEdgeLabel.getLayoutBounds().getHeight() / 1.5D));
//    this.attachedLabel = label;
        visEdgeLabel.xProperty().bind(cubicCurve.controlX1Property().add(cubicCurve.controlX2Property()).divide(2).subtract(visEdgeLabel.getLayoutBounds().getWidth() / 2.0D));
        visEdgeLabel.yProperty().bind(cubicCurve.controlY1Property().add(cubicCurve.controlY2Property()).divide(2).add(visEdgeLabel.getLayoutBounds().getHeight() / 2.0D));

    }

    public void bindArrow() {
        arrow.setStroke(Color.RED);


        arrow.translateXProperty().bind(cubicCurve.endXProperty());
        arrow.translateYProperty().bind(cubicCurve.endYProperty());
        Rotate rotation = new Rotate();
        Translate translate = new Translate();
        translate.setX(arrow.getStrokeWidth());
        rotation.angleProperty().bind(UtilitiesBindings.toDegrees(UtilitiesBindings.atan2(cubicCurve.endYProperty().subtract(cubicCurve.controlY2Property()), cubicCurve.endXProperty().subtract(cubicCurve.controlX2Property()))));
        arrow.getTransforms().add(rotation);
        arrow.getTransforms().add(translate);
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

}

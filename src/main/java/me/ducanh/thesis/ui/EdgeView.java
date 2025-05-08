package me.ducanh.thesis.ui;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class EdgeView extends AnchorPane {


    public CubicCurve cubicCurve = new CubicCurve();
    public final Text visEdgeLabel = new Text();
    Arrow arrow = new Arrow(6);

    private final DoubleProperty dfltDegree = new SimpleDoubleProperty(10);
    private final VertexView source;
    private final VertexView target;
    private final String label;


    public EdgeView(String label, VertexView source, VertexView target) {
        this.source = source;
        this.target = target;
        this.label = label;

        setFocusTraversable(true);
        getChildren().add(cubicCurve);
        getChildren().add(visEdgeLabel);
        getChildren().add(arrow);
//        setPickOnBounds(false);


        visEdgeLabel.setText(label);
        visEdgeLabel.toFront();
        visEdgeLabel.setFill(Color.CORAL);
        visEdgeLabel.setStyle("-fx-font-weight: bold");
        visEdgeLabel.setStyle("-fx-font-size: 16");
        cubicCurve.setFill(Color.TRANSPARENT);
        cubicCurve.setStroke(Color.DARKGREY);


        draw(dfltDegree.get());
        source.getCenterXProperty().addListener((oldV, newV, value) -> draw(dfltDegree.get()));
        source.getCenterYProperty().addListener((oldV, newV, value) -> draw(dfltDegree.get()));
        target.getCenterXProperty().addListener((oldV, newV, value) -> draw(dfltDegree.get()));
        target.getCenterYProperty().addListener((oldV, newV, value) -> draw(dfltDegree.get()));


        bindLabelPosition();
        bindArrow();
    }

    private void draw(double rotationDeg) {

        double controlX1;
        double controlY1;

        if (this.source == this.target) {

            int startAngle = 130;
            int endAngle = 50;
            double selfPointerSize = 3.5;
            //for some reason, you hae to subtract r*sin(angle) from the Y coordinate, as if the axis was mirrored.
            //positioning start and end points on the edgeView of the circle using angle properties to get the coordinates
            //you could also add a point at the center of the vertex, rotate its coordinate system to the preferred angle and translate the X value to its radius
            cubicCurve.setStartX(source.getCenterX() + source.getRadius() * Math.cos(Math.toRadians(startAngle)));
            cubicCurve.setStartY(source.getCenterY() - source.getRadius() * Math.sin(Math.toRadians(startAngle)));
            cubicCurve.setEndX(target.getCenterX() + (target.getRadius() + cubicCurve.getStrokeWidth()) * Math.cos(Math.toRadians(endAngle)));
            cubicCurve.setEndY(target.getCenterY() - (target.getRadius() + cubicCurve.getStrokeWidth()) * Math.sin(Math.toRadians(endAngle)));


            controlX1 = this.target.getCenterX() + selfPointerSize * (cubicCurve.getStartX() - target.getCenterX());
            controlY1 = this.target.getCenterY() + selfPointerSize * (cubicCurve.getStartY() - target.getCenterY());
            double controlX2 = this.target.getCenterX() + selfPointerSize * (cubicCurve.getEndX() - target.getCenterX());
            double controlY2 = this.target.getCenterY() + selfPointerSize * (cubicCurve.getEndY() - target.getCenterY());

            cubicCurve.setControlX1(controlX1);
            cubicCurve.setControlY1(controlY1);
            cubicCurve.setControlX2(controlX2);
            cubicCurve.setControlY2(controlY2);

        } else {

            double rotationRadians = Math.toRadians(rotationDeg);

            //here you don't have to compensate for the y-coordinate bug, because atan already includes the y-coordinate
            double sourceTargetAngle = Math.atan2(target.getCenterY() - source.getCenterY(), target.getCenterX() - source.getCenterX());
            double targetSourceAngle = Math.atan2(source.getCenterY() - target.getCenterY(), source.getCenterX() - target.getCenterX());
//   TODO test         Assert.assertEquals( (Math.toDegrees(sourceTargetAngle)+180)% 360 , Math.toDegrees(targetSourceAngle)% 360);
            //imagine an arrow pointing from source center to its radius. Rotate towards target and add preferred rotation.
            cubicCurve.setStartX(source.getCenterX() + source.getRadius() * Math.cos(sourceTargetAngle + rotationRadians));
            cubicCurve.setStartY(source.getCenterY() + source.getRadius() * Math.sin(sourceTargetAngle + rotationRadians));
            //imagine an arrow pointing from target center to its radius. Rotate towards source and add preferred rotation.
            cubicCurve.setEndX(target.getCenterX() + (target.getRadius() + cubicCurve.getStrokeWidth()) * Math.cos(targetSourceAngle - rotationRadians));
            cubicCurve.setEndY(target.getCenterY() + (target.getRadius() + cubicCurve.getStrokeWidth()) * Math.sin(targetSourceAngle - rotationRadians));

//            TODO increase amplitude depending on rotation
            Point2D control1 = new Point2D(getMidX() + ((cubicCurve.getStartX() - getMidX()) / 4), getMidY() + ((cubicCurve.getStartY() - getMidY()) / 4));
            Point2D control2 = new Point2D(getMidX() + ((cubicCurve.getEndX() - getMidX()) / 4), getMidY() + ((cubicCurve.getEndY() - getMidY()) / 4));

            control1 = rotateAroundPivot(control1, new Point2D(source.getCenterX(), source.getCenterY()), 4);
            control2 = rotateAroundPivot(control2, new Point2D(source.getCenterX(), source.getCenterY()), 4);

            cubicCurve.setControlX1(control1.getX());
            cubicCurve.setControlY1(control1.getY());
            cubicCurve.setControlX2(control2.getX());
            cubicCurve.setControlY2(control2.getY());
        }

    }

    public VertexView getTarget() {
        return target;
    }

    public VertexView getSource() {
        return source;
    }

    public void changeDegree(double degree) {
        dfltDegree.set(degree);
    }


    private void bindLabelPosition() {

        if (this.source == this.target) {
            visEdgeLabel.xProperty().bind(cubicCurve.controlX1Property().add(cubicCurve.controlX2Property()).divide(2).subtract(visEdgeLabel.getLayoutBounds().getWidth() / 2.0D));
            visEdgeLabel.yProperty().bind(cubicCurve.controlY1Property().add(cubicCurve.controlY2Property()).divide(2).add(visEdgeLabel.getLayoutBounds().getHeight() / 2.0D));
        } else {

            visEdgeLabel.xProperty().bind(Bindings.createDoubleBinding(
                    () -> getControlMidX() + ((getControlMidX() - getMidX())),
                    cubicCurve.endXProperty(),
                    cubicCurve.controlX2Property(),
                    cubicCurve.controlX1Property()
            ));
            visEdgeLabel.yProperty().bind(Bindings.createDoubleBinding(
                    () -> getControlMidY() + ((getControlMidY() - getMidY())),
                    cubicCurve.endYProperty(),
                    cubicCurve.controlY2Property(),
                    cubicCurve.controlY1Property()
            ));
        }

    }

    public void bindArrow() {
        arrow.setStroke(Color.DARKGREY);
        arrow.setFill(Color.DARKGREY);

        arrow.translateXProperty().bind(cubicCurve.endXProperty());
        arrow.translateYProperty().bind(cubicCurve.endYProperty());
        Rotate rotation = new Rotate();
        Translate translate = new Translate();
        DoubleBinding y = cubicCurve.endYProperty().subtract(cubicCurve.controlY2Property());
        DoubleBinding x = cubicCurve.endXProperty().subtract(cubicCurve.controlX2Property());
        DoubleBinding atan2Binding = Bindings.createDoubleBinding(() -> Math.atan2(y.get(), x.get()), y, x);
        DoubleBinding toDegreeBinding = Bindings.createDoubleBinding(() -> Math.toDegrees(atan2Binding.get()), atan2Binding);
        rotation.angleProperty().bind(toDegreeBinding);
        arrow.getTransforms().add(rotation);
        arrow.getTransforms().add(translate);
    }

    private double getMidX() {
        return (cubicCurve.getEndX() + cubicCurve.getStartX()) / 2.0;

    }

    private double getMidY() {
        return (cubicCurve.getEndY() + cubicCurve.getStartY()) / 2.0;

    }

    private double getControlMidX() {
        return (cubicCurve.getControlX1() + cubicCurve.getControlX2()) / 2.0;
    }

    private double getControlMidY() {
        return (cubicCurve.getControlY1() + cubicCurve.getControlY2()) / 2.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeView edgeView = (EdgeView) o;
        return label.equals(edgeView.label) && source.equals(edgeView.source) && target.equals(edgeView.target);
    }


    @Override
    public String toString() {
        return source.getLabel() + " -" + label + "> " + target.getLabel();
    }


    private static Point2D rotateAroundPivot(final Point2D point, final Point2D pivot, double angle_degrees) {
        double angle = Math.toRadians(angle_degrees);

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        //translate to origin
        Point2D result = point.subtract(pivot);

        // rotate point
        Point2D rotatedOrigin = new Point2D(
                result.getX() * cos - result.getY() * sin,
                result.getX() * sin + result.getY() * cos);

        // translate point back
        result = rotatedOrigin.add(pivot);

        return result;

    }
}



class Arrow extends Polygon {

    public Arrow(double size) {
        super(0.0,0.0,-size,size,-size,-size,-0.0,0.0);
    }
}

package me.ducanh.thesis;

import com.brunomnsilva.smartgraph.graphview.SmartArrow;
import com.brunomnsilva.smartgraph.graphview.UtilitiesBindings;
import com.brunomnsilva.smartgraph.graphview.UtilitiesPoint2D;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class EdgeView extends AnchorPane{


    public CubicCurve cubicCurve = new CubicCurve();
    public Text visEdgeLabel = new Text();
    public SmartArrow arrow = new SmartArrow(6);

    private final VertexView source;
    private final VertexView target;



    public EdgeView(String label, VertexView source, VertexView target) {
        getChildren().add(cubicCurve);
        getChildren().add(visEdgeLabel);
        getChildren().add(arrow);
        setPickOnBounds(false);

        this.source = source;
        this.target = target;
        visEdgeLabel.setText(label);
        visEdgeLabel.toFront();
        visEdgeLabel.setFill(Color.CORAL);
        visEdgeLabel.setStyle("-fx-font-weight: bold");
        visEdgeLabel.setStyle("-fx-font-size: 16");
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


            int startAngle = 130;
            int endAngle = 50;
            double selfPointerSize = 3.5;
            //for some reason, you hae to subtract r*sin(angle) from the Y coordinate, as if the axis was mirrored.
            //positioning start and end points on the edge of the circle using angle properties to get the coordinates
            //you could also add a point at the center of the vertex, rotate its coordinate system to the preferred angle and translate the X value to its radius
            cubicCurve.setStartX(source.getCenterX()+source.getRadius()*Math.cos(Math.toRadians(startAngle)));
            cubicCurve.setStartY(source.getCenterY()-source.getRadius()*Math.sin(Math.toRadians(startAngle)));
            cubicCurve.setEndX(target.getCenterX()+(target.getRadius()+cubicCurve.getStrokeWidth())*Math.cos(Math.toRadians(endAngle)));
            cubicCurve.setEndY(target.getCenterY()-(target.getRadius()+cubicCurve.getStrokeWidth())*Math.sin(Math.toRadians(endAngle)));



            controlX1 = this.target.getCenterX() + selfPointerSize*(cubicCurve.getStartX()-target.getCenterX());
            controlY1 = this.target.getCenterY() + selfPointerSize*(cubicCurve.getStartY()-target.getCenterY());
            double controlX2 = this.target.getCenterX() + selfPointerSize*(cubicCurve.getEndX()-target.getCenterX());
            double controlY2 = this.target.getCenterY() + selfPointerSize*(cubicCurve.getEndY()-target.getCenterY());

            cubicCurve.setControlX1(controlX1);
            cubicCurve.setControlY1(controlY1);
            cubicCurve.setControlX2(controlX2);
            cubicCurve.setControlY2(controlY2);
        } else {

            double customRotation = Math.toRadians(10);

            //here you don't have to compensate for the y-coordinate bug, because atan already includes the y-coordinate
            double sourceTargetAngle = Math.atan2(target.getCenterY()-source.getCenterY(),target.getCenterX()-source.getCenterX());
            double targetSourceAngle = Math.atan2(source.getCenterY()-target.getCenterY(),source.getCenterX()-target.getCenterX());
            cubicCurve.setStartX(source.getCenterX()+source.getRadius()*Math.cos(sourceTargetAngle+customRotation));
            cubicCurve.setStartY(source.getCenterY()+source.getRadius()*Math.sin(sourceTargetAngle+customRotation));
            cubicCurve.setEndX(target.getCenterX()+target.getRadius()*Math.cos(targetSourceAngle-customRotation));
            cubicCurve.setEndY(target.getCenterY()+target.getRadius()*Math.sin(targetSourceAngle-customRotation));



            Point2D control1 = new Point2D(getMidX()+((cubicCurve.getStartX()-getMidX())/4),getMidY()+((cubicCurve.getStartY()-getMidY())/4));
            Point2D control2 = new Point2D(getMidX()+((cubicCurve.getEndX()-getMidX())/4),getMidY()+((cubicCurve.getEndY()-getMidY())/4));

            control1 = UtilitiesPoint2D.rotate(control1, new Point2D(source.getCenterX(),source.getCenterY()), 4);
            control2 = UtilitiesPoint2D.rotate(control2, new Point2D(source.getCenterX(),source.getCenterY()), 4);

            cubicCurve.setControlX1(control1.getX());
            cubicCurve.setControlY1(control1.getY());
            cubicCurve.setControlX2(control2.getX());
            cubicCurve.setControlY2(control2.getY());
        }
    }

    private void bindLabelPosition() {

        if(this.source == this.target){
            visEdgeLabel.xProperty().bind(cubicCurve.controlX1Property().add(cubicCurve.controlX2Property()).divide(2).subtract(visEdgeLabel.getLayoutBounds().getWidth() / 2.0D));
            visEdgeLabel.yProperty().bind(cubicCurve.controlY1Property().add(cubicCurve.controlY2Property()).divide(2).add(visEdgeLabel.getLayoutBounds().getHeight() / 2.0D));
        } else {

            visEdgeLabel.xProperty().bind(Bindings.createDoubleBinding(
                    ()->getControlMidX() + ((getControlMidX() - getMidX())),
                    cubicCurve.endXProperty(),
                    cubicCurve.controlX2Property(),
                    cubicCurve.controlX1Property()
            ));
            visEdgeLabel.yProperty().bind(Bindings.createDoubleBinding(
                    ()->getControlMidY()+ ((getControlMidY() - getMidY())) ,
                    cubicCurve.endYProperty(),
                    cubicCurve.controlY2Property(),
                    cubicCurve.controlY1Property()
            ));
        }

    }

    public void bindArrow() {
        arrow.setStroke(Color.DARKGREY);


        arrow.translateXProperty().bind(cubicCurve.endXProperty());
        arrow.translateYProperty().bind(cubicCurve.endYProperty());
        Rotate rotation = new Rotate();
        Translate translate = new Translate();
        translate.setX(arrow.getStrokeWidth());
        rotation.angleProperty().bind(UtilitiesBindings.toDegrees(UtilitiesBindings.atan2(cubicCurve.endYProperty().subtract(cubicCurve.controlY2Property()), cubicCurve.endXProperty().subtract(cubicCurve.controlX2Property()))));
        arrow.getTransforms().add(rotation);
        arrow.getTransforms().add(translate);
    }

    private double getMidX(){
        return (cubicCurve.getEndX()+cubicCurve.getStartX()) / 2.0;

    }
    private double getMidY(){
        return (cubicCurve.getEndY()+cubicCurve.getStartY()) / 2.0;

    }
    private double getControlMidX(){
        return (cubicCurve.getControlX1() + cubicCurve.getControlX2()) / 2.0;
    }
    private double getControlMidY(){
        return (cubicCurve.getControlY1() + cubicCurve.getControlY2()) / 2.0;
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

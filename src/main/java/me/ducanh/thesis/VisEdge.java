package me.ducanh.thesis;

import com.brunomnsilva.smartgraph.graphview.SmartArrow;
import com.brunomnsilva.smartgraph.graphview.UtilitiesBindings;
import com.brunomnsilva.smartgraph.graphview.UtilitiesPoint2D;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

public class VisEdge {

  @FXML
  private AnchorPane anchorPane = new AnchorPane();
  @FXML
//  public Path path;
  public CubicCurve cubicCurve;
  @FXML
  public Text visEdgeLabel;

  public SmartArrow attachedArrow;
  private CustomEdge edge;
  private VisVertex source;
  private VisVertex target;
  public DoubleProperty sourceXProperty = new SimpleDoubleProperty();
  public DoubleProperty sourceYProperty = new SimpleDoubleProperty();
  public DoubleProperty targetXProperty = new SimpleDoubleProperty();
  public DoubleProperty targetYProperty = new SimpleDoubleProperty();
  private DoubleProperty distance = new SimpleDoubleProperty();

  public void init(CustomEdge edge, VisVertex source, VisVertex target){
    this.edge = edge;
    this.source = source;
    this.target = target;


    this.visEdgeLabel.setText(edge.getLabel());
    visEdgeLabel.toFront();
    bindNodePositions();
    cubicCurve.setFill(Color.TRANSPARENT);
    cubicCurve.setStroke(Color.DARKGREY);
    distance.bind(Bindings.createDoubleBinding(
            ()-> Math.sqrt(Math.pow((targetXProperty.get()- sourceXProperty.get()),2) + Math.pow((targetYProperty.get()- sourceYProperty.get()),2)),
            targetXProperty,
            targetYProperty,
            sourceXProperty,
            sourceYProperty));
    distance.addListener((oldV,newV,event)->{
      System.out.println("Distance of " + this.edge + " is : "+ newV);
    });
    drawEdge();
    bindLabelPosition();
    bindArrow(new SmartArrow(8));
    anchorPane.getChildren().add(attachedArrow);
    anchorPane.setPickOnBounds(false);

  }

  private void update() {
    double midpointX1;
    double midpointY1;
    if (this.source == this.target) {
      midpointX1 = this.target.getCenterX() - this.source.getRadius() * 5.0D;
      midpointY1 = this.target.getCenterY() - this.source.getRadius() * 2.0D;
      double midpointX2 = this.target.getCenterX() + this.source.getRadius() * 5.0D;
      double midpointY2 = this.target.getCenterY() - this.source.getRadius() * 2.0D;
      cubicCurve.setControlX1(midpointX1);
      cubicCurve.setControlY1(midpointY1);
      cubicCurve.setControlX2(midpointX2);
      cubicCurve.setControlY2(midpointY2);
    } else {
      midpointX1 = (this.target.getCenterX() + this.source.getCenterX()) / 2.0D;
      midpointY1 = (this.target.getCenterY() + this.source.getCenterY()) / 2.0D;
      Point2D midpoint = new Point2D(midpointX1, midpointY1);
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
  private void drawEdge(){
    cubicCurve.startXProperty().bind(source.getCenterXProperty());
    cubicCurve.startYProperty().bind(source.getCenterYProperty());
    cubicCurve.endXProperty().bind(target.getCenterXProperty());
    cubicCurve.endYProperty().bind(target.getCenterYProperty());
    update();


      cubicCurve.startXProperty().addListener((oldV,newV,change)-> update());
      cubicCurve.startYProperty().addListener((oldV,newV,change)-> update());
      cubicCurve.endXProperty().addListener((oldV,newV,change)-> update());
      cubicCurve.endYProperty().addListener((oldV,newV,change)-> update());


//    path.getElements().clear();
//    MoveTo moveTo = new MoveTo();
//    moveTo.xProperty().bind(sourceXProperty);
//    moveTo.yProperty().bind(sourceYProperty);
//
//    ArcTo arcTo = new ArcTo();
//    arcTo.xProperty().bind(targetXProperty);
//    arcTo.yProperty().bind(targetYProperty);
//
//
//
//    arcTo.radiusXProperty().bind(Bindings.createDoubleBinding(
//            ()-> distance.get()*distance.get()/200,
//            distance
//    ));
//    arcTo.radiusYProperty().bind(Bindings.createDoubleBinding(
//            ()-> distance.get()*distance.get()/200,
//            distance
//    ));
//
//    path.getElements().add(moveTo);
//    path.getElements().add(arcTo);



//    double height = 20;
//    arcTo.radiusYProperty().bind(arcTo.radiusXProperty());
//    arcTo.radiusXProperty().bind(Bindings.createDoubleBinding(
//                ()-> (height/2) + Math.pow(distance.get(),2)/(8*height),
////            ()-> distance.get()*distance.get()/200,
//            distance
//    ));
//
//    path.getElements().add(moveTo);
//    path.getElements().add(arcTo);


  }


  private void bindLabelPosition() {
//    visEdgeLabel.xProperty().bind(sourceXProperty.add(targetXProperty).divide(2).subtract(visEdgeLabel.getLayoutBounds().getWidth() / 2.0D));
//    visEdgeLabel.yProperty().bind(sourceYProperty.add(targetYProperty).divide(2).add(visEdgeLabel.getLayoutBounds().getHeight() / 1.5D));
//    this.attachedLabel = label;
    visEdgeLabel.xProperty().bind(cubicCurve.controlX1Property().add(cubicCurve.controlX2Property()).divide(2).subtract(visEdgeLabel.getLayoutBounds().getWidth() / 2.0D));
    visEdgeLabel.yProperty().bind(cubicCurve.controlY1Property().add(cubicCurve.controlY2Property()).divide(2).add(visEdgeLabel.getLayoutBounds().getHeight() / 2.0D));

  }

  private void bindNodePositions() {
    sourceXProperty.bind(source.getCenterXProperty());
    sourceYProperty.bind(source.getCenterYProperty());
    targetXProperty.bind(target.getCenterXProperty());
    targetYProperty.bind(target.getCenterYProperty());
  }


  public void bindArrow(SmartArrow arrow) {
    this.attachedArrow = arrow;
    arrow.setStroke(Color.DARKGREY);

//    arrow.translateXProperty().bind(this.endXProp);
//    arrow.translateYProperty().bind(this.endYProp);
//    Rotate rotation = new Rotate();
//    rotation.pivotXProperty().bind(path.translateXProperty());
//    rotation.pivotYProperty().bind(path.translateYProperty());
//    rotation.angleProperty().bind(UtilitiesBindings.toDegrees(UtilitiesBindings.atan2(path.endYProperty().subtract(this.controlY2Property()), this.endXProperty().subtract(this.controlX2Property()))));
//    arrow.getTransforms().add(rotation);
//    Translate t = new Translate(-this.target.getRadius(), 0.0D);
//    arrow.getTransforms().add(t);

//    /* attach arrow to line's endpoint */
//    arrow.translateXProperty().bind(targetXProperty);
//    arrow.translateYProperty().bind(targetYProperty);
//
//    /* rotate arrow around itself based on this line's angle */
//    Rotate rotation = new Rotate();
//    rotation.pivotXProperty().bind(cubicCurve.translateXProperty());
//    rotation.pivotYProperty().bind(cubicCurve.translateYProperty());
//    rotation.angleProperty().bind(UtilitiesBindings.toDegrees(
//            UtilitiesBindings.atan2(targetYProperty.subtract(sourceYProperty),
//                    targetXProperty.subtract(sourceXProperty))
//    ));
//
//    arrow.getTransforms().add(rotation);
//
//    /* add translation transform to put the arrow touching the circle's bounds */
//    Translate t = new Translate(-target.getRadius(), 0);
//    arrow.getTransforms().add(t);

    arrow.translateXProperty().bind(cubicCurve.endXProperty());
    arrow.translateYProperty().bind(cubicCurve.endYProperty());
    Rotate rotation = new Rotate();
    rotation.pivotXProperty().bind(cubicCurve.translateXProperty());
    rotation.pivotYProperty().bind(cubicCurve.translateYProperty());
    rotation.angleProperty().bind(UtilitiesBindings.toDegrees(UtilitiesBindings.atan2(cubicCurve.endYProperty().subtract(cubicCurve.controlY2Property()), cubicCurve.endXProperty().subtract(cubicCurve.controlX2Property()))));
    arrow.getTransforms().add(rotation);
    Translate t = new Translate(-this.target.getRadius(), 0.0D);
    arrow.getTransforms().add(t);
  }
  public Node getRoot(){
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
  public String toString(){
    return edge.toString();
  }

  public void toBack() {
    anchorPane.toBack();
  }
}

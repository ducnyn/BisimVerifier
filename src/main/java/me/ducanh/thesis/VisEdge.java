package me.ducanh.thesis;

import com.brunomnsilva.smartgraph.graphview.SmartArrow;
import com.brunomnsilva.smartgraph.graphview.UtilitiesBindings;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import me.ducanh.thesis.model.CustomEdge;

public class VisEdge {

  @FXML
  private AnchorPane anchorPane = new AnchorPane();
  @FXML
//  public Line line = new Line();
  public Path path;
  @FXML
  public Text visEdgeLabel;

  public SmartArrow attachedArrow;
  private CustomEdge edge;
  private VisVertex source;
  private VisVertex target;
  public DoubleProperty startXProp = new SimpleDoubleProperty();
  public DoubleProperty startYProp= new SimpleDoubleProperty();
  public DoubleProperty endXProp= new SimpleDoubleProperty();
  public DoubleProperty endYProp= new SimpleDoubleProperty();
  private DoubleProperty distance = new SimpleDoubleProperty();

  public void init(CustomEdge edge, VisVertex source, VisVertex target){
    this.edge = edge;
    this.source = source;
    this.target = target;


    this.visEdgeLabel.setText(edge.getLabel());
    visEdgeLabel.toFront();
    bindNodePositions();

    distance.bind(Bindings.createDoubleBinding(
            ()-> Math.sqrt(Math.pow((endXProp.get()-startXProp.get()),2) + Math.pow((endYProp.get()-startYProp.get()),2)),
            endXProp,
            endYProp,
            startXProp,
            startYProp));
    distance.addListener((oldV,newV,event)->{
      System.out.println("Distance of " + this.edge + " is : "+ newV);
    });
    drawEdge();
    bindLabelPosition();
    bindArrow(new SmartArrow(8));
    anchorPane.getChildren().add(attachedArrow);
    anchorPane.setPickOnBounds(false);

  }


  private void drawEdge(){
    path.getElements().clear();
    MoveTo moveTo = new MoveTo();
    moveTo.xProperty().bind(startXProp);
    moveTo.yProperty().bind(startYProp);

    ArcTo arcTo = new ArcTo();
    arcTo.xProperty().bind(endXProp);
    arcTo.yProperty().bind(endYProp);



    arcTo.radiusXProperty().bind(Bindings.createDoubleBinding(
            ()-> distance.get()*distance.get()/200,
            distance
    ));
    arcTo.radiusYProperty().bind(Bindings.createDoubleBinding(
            ()-> distance.get()*distance.get()/200,
            distance
    ));

    path.getElements().add(moveTo);
    path.getElements().add(arcTo);


  }


  private void bindLabelPosition() {
    visEdgeLabel.xProperty().bind(startXProp.add(endXProp).divide(2).subtract(visEdgeLabel.getLayoutBounds().getWidth() / 2.0D));
    visEdgeLabel.yProperty().bind(startYProp.add(endYProp).divide(2).add(visEdgeLabel.getLayoutBounds().getHeight() / 1.5D));
  }

  private void bindNodePositions() {
    startXProp.bind(source.getCenterXProperty());
    startYProp.bind(source.getCenterYProperty());
    endXProp.bind(target.getCenterXProperty());
    endYProp.bind(target.getCenterYProperty());
  }


  public void bindArrow(SmartArrow arrow) {
    this.attachedArrow = arrow;

//    arrow.translateXProperty().bind(this.endXProp);
//    arrow.translateYProperty().bind(this.endYProp);
//    Rotate rotation = new Rotate();
//    rotation.pivotXProperty().bind(path.translateXProperty());
//    rotation.pivotYProperty().bind(path.translateYProperty());
//    rotation.angleProperty().bind(UtilitiesBindings.toDegrees(UtilitiesBindings.atan2(path.endYProperty().subtract(this.controlY2Property()), this.endXProperty().subtract(this.controlX2Property()))));
//    arrow.getTransforms().add(rotation);
//    Translate t = new Translate(-this.outbound.getRadius(), 0.0D);
//    arrow.getTransforms().add(t);

    /* attach arrow to line's endpoint */
    arrow.translateXProperty().bind(endXProp);
    arrow.translateYProperty().bind(endYProp);

    /* rotate arrow around itself based on this line's angle */
    Rotate rotation = new Rotate();
    rotation.pivotXProperty().bind(path.translateXProperty());
    rotation.pivotYProperty().bind(path.translateYProperty());
    rotation.angleProperty().bind(UtilitiesBindings.toDegrees(
            UtilitiesBindings.atan2(endYProp.subtract(startYProp),
                    endXProp.subtract(startXProp))
    ));

    arrow.getTransforms().add(rotation);

    /* add translation transform to put the arrow touching the circle's bounds */
    Translate t = new Translate(-target.getRadius(), 0);
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

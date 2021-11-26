package me.ducanh.thesis;

import com.brunomnsilva.smartgraph.graphview.SmartArrow;
import com.brunomnsilva.smartgraph.graphview.UtilitiesBindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import me.ducanh.thesis.model.CustomEdge;

public class VisEdge {

  @FXML
  private AnchorPane anchorPane = new AnchorPane();
  @FXML
  public Line line = new Line();
  @FXML
  public Text visEdgeLabel;

  public SmartArrow attachedArrow;
  private CustomEdge edge;
  private VisVertex source;
  private VisVertex target;

  public void init(CustomEdge edge, VisVertex source, VisVertex target){
    this.edge = edge;
    this.source = source;
    this.target = target;
    this.visEdgeLabel.setText(edge.getLabel());
    visEdgeLabel.toFront();
    bindLabelPosition();
    bindNodePositions();
    attachArrow(new SmartArrow(8));
    anchorPane.getChildren().add(attachedArrow);
    anchorPane.setPickOnBounds(false);

  }
  private void bindLabelPosition() {
    visEdgeLabel.xProperty().bind(this.startXProperty().add(this.endXProperty()).divide(2).subtract(this.visEdgeLabel.getLayoutBounds().getWidth() / 2.0D));
    visEdgeLabel.yProperty().bind(this.startYProperty().add(this.endYProperty()).divide(2).add(this.visEdgeLabel.getLayoutBounds().getHeight() / 1.5D));
  }

  private void bindNodePositions() {
    source.bindToCenter(startXProperty(),startYProperty());
    target.bindToCenter(endXProperty(),endYProperty());
  }
  public void attachArrow(SmartArrow arrow) {
    this.attachedArrow = arrow;

    /* attach arrow to line's endpoint */
    arrow.translateXProperty().bind(endXProperty());
    arrow.translateYProperty().bind(endYProperty());

    /* rotate arrow around itself based on this line's angle */
    Rotate rotation = new Rotate();
    rotation.pivotXProperty().bind(line.translateXProperty());
    rotation.pivotYProperty().bind(line.translateYProperty());
    rotation.angleProperty().bind(UtilitiesBindings.toDegrees(
            UtilitiesBindings.atan2(endYProperty().subtract(startYProperty()),
                    endXProperty().subtract(startXProperty()))
    ));

    arrow.getTransforms().add(rotation);

    /* add translation transform to put the arrow touching the circle's bounds */
    Translate t = new Translate(-target.getRadius(), 0);
    arrow.getTransforms().add(t);
  }
  public Node getRoot(){
    return anchorPane;
  }
  public DoubleProperty startXProperty(){
    return line.startXProperty();
  }

  public DoubleProperty startYProperty(){
    return line.startYProperty();
  }

  public DoubleProperty endXProperty(){
    return line.endXProperty();
  }

  public DoubleProperty endYProperty(){
    return line.endYProperty();
  }
  @Override
  public String toString(){
    return edge.toString();
  }

  public void toBack() {
    anchorPane.toBack();
  }
}

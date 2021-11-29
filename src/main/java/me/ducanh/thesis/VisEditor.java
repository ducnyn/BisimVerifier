package me.ducanh.thesis;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.application.Platform;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import me.ducanh.thesis.model.*;
import me.ducanh.thesis.util.Colors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class VisEditor {
  private final HashMap<Integer, VisVertex> drawnVertices = new HashMap<>();
  private final HashMap<CustomEdge,VisEdge> drawnEdges = new HashMap<>();
  @FXML
  ScrollPane scrollPane ;
  @FXML
  AnchorPane anchorPane;
  @FXML
  StackPane centerBox;
  @FXML
  Group group;
  @FXML
  Label bisimToggleLabel;

  private final int radius = 40;
  private int colorID = 0;
  double spawnPosX = 1;
  double spawnPosY = 1;


//TODO scroll pane when item dragged past boundaries
//TODO When you add/remove an edge, the edgeVis doesn't have to be drawn again, just add / remove a label

  public void initialize(Model model) {
    anchorPane.setMinHeight(600);
    anchorPane.setMinWidth(900);

    anchorPane.setOnMousePressed(pressed -> {
      if (pressed.getButton().equals(MouseButton.PRIMARY) && (pressed.isControlDown())) {
         spawnPosX = pressed.getX() - radius;
         spawnPosY = pressed.getY() - radius - 20;
         model.addNextIDVertex();
        pressed.consume();
      }

    });
    model.getBisimToggle().addListener((observable, changeToFalse, changeToTrue) ->{
      if (changeToTrue){
        bisimToggleLabel.setText("Bisimulation Indicators: On");
      } else {
        bisimToggleLabel.setText("Bisimulation Indicators: Off");
      }
    });

    model.getEdges().addListener((SetChangeListener<? super CustomEdge>) change->{

      if(change.wasAdded()){
        System.out.println("Edge was added: "+change.getElementAdded() + "try to create visualization");
        try {
          CustomEdge edge = change.getElementAdded();
          FXMLLoader visEdgeLoader = new FXMLLoader(getClass().getResource(FXMLPATH.VISEDGE.getFileName()));
          Node visEdgeRoot = visEdgeLoader.load();
          VisEdge visEdge = visEdgeLoader.getController();
          visEdge.init(edge, drawnVertices.get(edge.getSource().getID()),drawnVertices.get(edge.getTarget().getID()));
//          drawnVertices.get(edge.getSource().getID()).bindToCenter(visEdge.startXProperty(),visEdge.startYProperty());
//          drawnVertices.get(edge.getTarget().getID()).bindToCenter(visEdge.endXProperty(),visEdge.endYProperty());
          drawnEdges.put(edge,visEdge);

          Platform.runLater(()->{
            anchorPane.getChildren().add(visEdgeRoot);
            visEdge.getRoot().toBack();
          });

        } catch (IOException ioException) {
          ioException.printStackTrace();
        }

      } else {
        anchorPane.getChildren().remove(drawnEdges.get(change.getElementRemoved()).getRoot());
        drawnEdges.remove(change.getElementRemoved());
      }
      if(model.getBisimToggle().get()){
        model.updatePartition(Algorithms.bisim(model.getVertices()).getValue());
      }


    });


    model.addGraphListener(vertex -> {

      int id = vertex.getKey();
      if (vertex.wasAdded()) {
        if (!drawnVertices.containsKey(id)){
            drawnVertices.put(id, drawVertex(model,id, spawnPosX, spawnPosY));
            this.spawnPosX = 1;
            this.spawnPosY = 1;
        }
      } else {
        VisVertex deletedVisVertex = drawnVertices.remove(id);
        Platform.runLater(()-> {
          anchorPane.getChildren().remove(deletedVisVertex.getRootPane());

        });
      }
      if(model.getBisimToggle().get()){
        model.updatePartition(Algorithms.bisim(model.getVertices()).getValue());
      }

    });

    model.getBisimToggle().addListener((observable, changeToFalse, changeToTrue) ->{
      if (changeToTrue){
        Platform.runLater(()->{
          Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Set<Block> partition = Algorithms.bisim(model.getVertices()).getValue();
                model.setOutputString("\n Equivalence classes (Bisimulation): \n" + partition.toString());
                model.updatePartition(partition);
                System.out.println("Vertices: " + model.getVertices());
                System.out.println("Edges: " + model.getEdges());
                System.out.println(Thread.currentThread() + " should be backGroundThread");
            }
        });
        taskThread.setDaemon(true);
        taskThread.start();
        });

      } else {
        Platform.runLater(()->{
          Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
              for (VisVertex visVertex : drawnVertices.values()) {
                visVertex.setCircleFill(visVertex.getDefaultFill());
              }
            }
          });
          taskThread.setDaemon(true);
          taskThread.start();
        });
      }
    });

    //        Thread taskThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Set<Block> partition = Algorithms.bisim(model.getVertices()).getValue();
//                model.setOutputString("\n Equivalence classes (Bisimulation): \n" + partition.toString());
//                model.updatePartition(partition);
//                System.out.println("Vertices: " + model.getVertices());
//                System.out.println("Edges: " + model.getEdges());
//                System.out.println(Thread.currentThread() + " should be backGroundThread");
//            }
//        });
//        taskThread.setDaemon(true);
//        taskThread.start();

    model.getPartition().addListener((SetChangeListener<Block>) change -> {
      if (change.wasAdded()) {
        if (change.getElementAdded().getVertices().size() > 1) {
          Paint color = Colors.array[colorID++ % 120];
          System.out.println("block added (colorListener" + change.getElementAdded().getVertices());

          for (CustomVertex vertex : change.getElementAdded()) {
            drawnVertices.get(vertex.getID()).setCircleFill(color);
          }
        } else if (change.getElementAdded().getVertices().size() == 1){
          drawnVertices.get(change.getElementAdded().iterator().next().getID()).setCircleFill(Color.LIGHTGRAY);
        }
      } else {
        if (change.getElementRemoved().getVertices().size() > 1) {
          System.out.println("block removed (colorListener" + change.getElementRemoved().getVertices());

          colorID--;
        }
      } //TODO doesn't remove color when clearing the partition list
    });

    centerBox.setOnScrollStarted(scrollStart->{

    });
    centerBox.setOnScroll(scroll->{

        double zoom;

        if (scroll.getDeltaY()==0) zoom = 1;
        else if (scroll.getDeltaY()>0) zoom = 1.05;
        else zoom = 0.95;
      System.out.println("delta " + scroll.getDeltaY());


      //calculate pixel offsets from [0,1] range
        double offsetX = scrollPane.getHvalue() * (group.getLayoutBounds().getWidth() - scrollPane.getViewportBounds().getWidth());
        double offsetY = scrollPane.getVvalue() * (group.getLayoutBounds().getHeight() - scrollPane.getViewportBounds().getHeight());

        anchorPane.setScaleX(anchorPane.getScaleX()*zoom); // might cause problems because you didnt use scaleValue = scaleValue(0.7)*zoomFactor
        anchorPane.setScaleY(anchorPane.getScaleY()*zoom);
        scrollPane.layout(); //refresh ScrollPane scroll positions @ target bounds

        //convert absMousePos to relMousePos(anchorPane)
        Point2D anchorMousePos = anchorPane.sceneToLocal(new Point2D(scroll.getSceneX(),scroll.getSceneY()));
        //zoom towards mouse position
        Point2D delta = anchorPane.getLocalToParentTransform().deltaTransform(anchorMousePos.multiply(zoom-1));

        scrollPane.setHvalue((offsetX + delta.getX()) / (group.getBoundsInLocal().getWidth() - scrollPane.getViewportBounds().getWidth()));
        scrollPane.setVvalue((offsetY + delta.getY()) / (group.getBoundsInLocal().getHeight() - scrollPane.getViewportBounds().getHeight()));
        //inspired by this over-engineered version with no practical difference : https://stackoverflow.com/questions/39827911/javafx-8-scaling-zooming-scrollpane-relative-to-mouse-position
    });
  }


  private VisVertex drawVertex(Model model, int id, double mouseX, double mouseY) {

    FXMLLoader visVertexLoader = new FXMLLoader(getClass().getResource(FXMLPATH.VISVERTEX.getFileName()));
    try {
      StackPane rootPane = visVertexLoader.load();
      Platform.runLater(()-> anchorPane.getChildren().add(rootPane));
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    VisVertex visVertex = visVertexLoader.getController();
    visVertex.init(model, id, mouseX, mouseY);

    return visVertex;

  }

}

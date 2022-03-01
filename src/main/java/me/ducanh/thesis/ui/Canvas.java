package me.ducanh.thesis.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import me.ducanh.thesis.Block;
import me.ducanh.thesis.Edge;
import me.ducanh.thesis.Model;
import me.ducanh.thesis.Vertex;
import me.ducanh.thesis.algorithms.*;
import me.ducanh.thesis.util.Colors;

import java.util.HashSet;
import java.util.Set;


public class Canvas {
    private final HashSet<Vertex> drawnVertices = new HashSet<>();
    private final ObservableSet<Edge> drawnEdges = FXCollections.observableSet();
    private final int radius = 40;
    @FXML
    ScrollPane scrollPane;
    @FXML
    AnchorPane anchorPane;
    @FXML
    StackPane centerBox;
    @FXML
    Group group;
    @FXML
    Label bisimToggleLabel;
    double spawnPosX = Vertex.DEFAULT_RADIUS;
    double spawnPosY = Vertex.DEFAULT_RADIUS;
    private int colorID = 0;


//TODO scroll pane when item dragged past boundaries

    public void initialize(Model model) {
        anchorPane.setMinHeight(600);
        anchorPane.setMinWidth(900);

        anchorPane.setOnMousePressed(pressed -> {
            if (pressed.getButton().equals(MouseButton.PRIMARY) && (pressed.isControlDown())) {
                spawnPosX = pressed.getX();
                spawnPosY = pressed.getY();
                model.addNextIDVertex(pressed.getX(),pressed.getY());
                pressed.consume();
            }

        });
        model.getBisimToggle().addListener((observable, changeToFalse, changeToTrue) -> {
            if (changeToTrue) {
                bisimToggleLabel.setText("Bisimulation Indicators: On");
            } else {
                bisimToggleLabel.setText("Bisimulation Indicators: Off");
            }
        });

//        model.getEdges().addListener((SetChangeListener<? super Edge>) change -> {
//
//            if (change.wasAdded()) {
//                Edge edge = change.getElementAdded();
//                Edge visEdge = new Edge(drawnVertices.get(edge.getSource().getID()), edge.getLabel(), drawnVertices.get(edge.getTarget().getID()));
//                drawnEdges.put(edge, visEdge);
//
//                Platform.runLater(() -> {
//                    anchorPane.getChildren().add(visEdge);
//                    visEdge.toBack();
//                });
//
//            } else {
//                anchorPane.getChildren().remove(drawnEdges.get(change.getElementRemoved()));
//                drawnEdges.remove(change.getElementRemoved());
//            }
//            if (model.getBisimToggle().get()) {
//                model.setPartition(Algorithms.bisim(model.getVertices()).getValue());
//            }
//
//
//        });

        model.addVertexListener(vListChange -> {

            int id = vListChange.getKey();

            if (vListChange.wasAdded()) {
                Vertex vertex = vListChange.getValueAdded();

//                if (!drawnVertices.contains(id)) {
//                    drawnVertices.add(vertex);
//                    this.spawnPosX = Vertex.DEFAULT_RADIUS;
//                    this.spawnPosY = Vertex.DEFAULT_RADIUS;
//                }
                drawnVertices.add(vertex);
                Platform.runLater(() -> {
                    anchorPane.getChildren().add(vListChange.getValueAdded());
                    if(vertex.getCenterX()==Vertex.DEFAULT_RADIUS){
                        vertex.initView(this.spawnPosX,this.spawnPosY);
                        resetSpawnPos();

                    } else {
                        vertex.initView();
                    }
                    //TODO always spawns at 1,1 when it comes from model.
                });

                ObservableSet<Edge> edgeSet = vListChange.getValueAdded().getEdges();
                edgeSet.addListener((SetChangeListener<? super Edge>) eListChange -> {

                    if (eListChange.wasAdded()) {
                        Edge edgeAdded = eListChange.getElementAdded();
//TODO: check if parallel edge already exists and modify that edge if it doesn't include new edge yet
//TODO why are random edges not following this rule
                        if(!drawnEdges.contains(edgeAdded)){
                            long parallelEdgeCount =
                                    drawnEdges  .stream()
                                                .filter(modeledE-> modeledE.getTarget().equals(edgeAdded.getTarget())
                                                        &&  modeledE.getSource().equals(edgeAdded.getSource())
                                                ).count();
//TODO                            JUnit System.out.println("there are already " + parallelEdgeCount + "parallel Edges");
//                            for(int i =0;i<parallelEdgeCount;i++){
                                edgeAdded.changeDegree(10 + parallelEdgeCount*10);
//                            }
                            Platform.runLater(() -> {
                                anchorPane.getChildren().add(edgeAdded);
                                drawnEdges.add(edgeAdded);
                                edgeAdded.toBack();
                                edgeAdded.initView();

                            });
                        }

//                            HashMap<Integer,HashMap<Integer,Edge>> adjMatrix = new HashMap<>(new HashMap<>());
//                            if(adjMatrix.containsKey(1) && adjMatrix.get(1).containsKey(2)){
//                                adjMatrix.get(1).get(2).
//                            }


                    } else {
                        Edge edgeRemoved = eListChange.getElementRemoved();
                        drawnEdges.remove(edgeRemoved);
                        anchorPane.getChildren().remove(edgeRemoved);
                    }
                    if (model.getBisimToggle().get()) {
                        model.setPartition(Algorithms.bisim(model.getVertices()).getValue());
                    }
                });
            } else {
                Vertex vertex = vListChange.getValueRemoved();
                Platform.runLater(() -> {
                    anchorPane.getChildren().remove(vertex);
                });
            }
            if (model.getBisimToggle().get()) {
                model.setPartition(Algorithms.bisim(model.getVertices()).getValue());
            }

        });

        model.getBisimToggle().addListener((observable, changeToFalse, changeToTrue) -> {
            if (changeToTrue) {
                Platform.runLater(() -> {
                    Thread taskThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Set<Block> partition = Algorithms.bisim(model.getVertices()).getValue();
                            model.requestPrint("\n Equivalence classes (Bisimulation): \n" + partition.toString());
                            model.setPartition(partition);
                            System.out.println("Vertices: " + model.getVertices());
                            System.out.println("Edges: " + model.getEdges());
                            System.out.println(Thread.currentThread() + " should be backGroundThread");
                        }
                    });
                    taskThread.setDaemon(true);
                    taskThread.start();
                });

            } else {
                Platform.runLater(() -> {
                    Thread taskThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (Vertex vertex : drawnVertices) {
                                vertex.setFill(vertex.getDefaultFill());
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
//                Set<BlockNode> partition = Algorithms.bisim(model.getVertices()).getValue();
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

                    for (Vertex vertex : change.getElementAdded()) {
                        vertex.setFill(color);
                    }
                } else if (change.getElementAdded().getVertices().size() == 1) {
                    change.getElementAdded().iterator().next().resetFill();
                }
            } else {
                if (change.getElementRemoved().getVertices().size() > 1) {
                    System.out.println("block removed (colorListener" + change.getElementRemoved().getVertices());

                    colorID--;
                }
            } //TODO doesn't remove color when clearing the partition list
        });

        centerBox.setOnScrollStarted(scrollStart -> {

        });
        centerBox.setOnScroll(scroll -> {

            double zoom;

            if (scroll.getDeltaY() == 0) zoom = 1;
            else if (scroll.getDeltaY() > 0) zoom = 1.05;
            else zoom = 0.95;
            System.out.println("delta " + scroll.getDeltaY());


            //calculate pixel offsets from [0,1] range
            double offsetX = scrollPane.getHvalue() * (group.getLayoutBounds().getWidth() - scrollPane.getViewportBounds().getWidth());
            double offsetY = scrollPane.getVvalue() * (group.getLayoutBounds().getHeight() - scrollPane.getViewportBounds().getHeight());

            anchorPane.setScaleX(anchorPane.getScaleX() * zoom); // might cause problems because you didnt use scaleValue = scaleValue(0.7)*zoomFactor
            anchorPane.setScaleY(anchorPane.getScaleY() * zoom);
            scrollPane.layout(); //refresh ScrollPane scroll positions @ target bounds

            //convert absMousePos to relMousePos(anchorPane)
            Point2D anchorMousePos = anchorPane.sceneToLocal(new Point2D(scroll.getSceneX(), scroll.getSceneY()));
            //zoom towards mouse position
            Point2D delta = anchorPane.getLocalToParentTransform().deltaTransform(anchorMousePos.multiply(zoom - 1));

            scrollPane.setHvalue((offsetX + delta.getX()) / (group.getBoundsInLocal().getWidth() - scrollPane.getViewportBounds().getWidth()));
            scrollPane.setVvalue((offsetY + delta.getY()) / (group.getBoundsInLocal().getHeight() - scrollPane.getViewportBounds().getHeight()));
            //inspired by this over-engineered version with no practical difference : https://stackoverflow.com/questions/39827911/javafx-8-scaling-zooming-scrollpane-relative-to-mouse-position
        });
    }
    private void resetSpawnPos(){
        spawnPosX=Vertex.DEFAULT_RADIUS;
        spawnPosY=Vertex.DEFAULT_RADIUS;
    }

}

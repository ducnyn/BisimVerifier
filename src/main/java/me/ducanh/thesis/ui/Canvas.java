package me.ducanh.thesis.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import me.ducanh.thesis.*;
import me.ducanh.thesis.algorithms.*;
import me.ducanh.thesis.util.Colors;

import java.util.HashSet;
import java.util.Set;


public class Canvas{
    private final HashSet<java.lang.Integer> drawnVertices = new HashSet<>();
    private final ObservableSet<EdgeView> drawnEdgeViews = FXCollections.observableSet();
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

    private int colorID = 0;
    private Controller controller;

//TODO scroll pane when item dragged past boundaries

    public void inject(CanvasVM canvasVM, Controller controller) {
        this.controller = controller;
        anchorPane.setMinHeight(600);
        anchorPane.setMinWidth(900);

        canvasVM.addVertexViewListener(vertexView->{
            if(vertexView.wasAdded()){
                Platform.runLater(() -> addChild(vertexView.getValueAdded()));
            } else {
                Platform.runLater(() -> removeChild(vertexView.getValueRemoved()));
            }
        });

        canvasVM.addEdgeViewListener(edgeView->{
            if(edgeView.wasAdded()){
                Platform.runLater(()->addChild(edgeView.getValueAdded()));
            } else {
                Platform.runLater(()->removeChild(edgeView.getValueRemoved()));
            }


        anchorPane.setOnMousePressed(pressed -> {
            checkCtrlClick(pressed);
        });
        canvasVM.addColorModeListener((observable, changeToFalse, changeToTrue) -> colorToggleIndicator(changeToTrue));

//        model.getEdges().addListener((SetChangeListener<? super Edge>) change -> {
//
//            if (change.wasAdded()) {
//                Edge edgeView = change.getElementAdded();
//                Edge visEdge = new Edge(drawnVertices.get(edgeView.getSource().getID()), edgeView.getLabel(), drawnVertices.get(edgeView.getTarget().getID()));
//                drawnEdges.put(edgeView, visEdge);
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
                Integer vertex = vListChange.getValueAdded();

//                if (!drawnVertices.contains(id)) {
//                    drawnVertices.add(vertex);
//                    this.spawnPosX = Vertex.DEFAULT_RADIUS;
//                    this.spawnPosY = Vertex.DEFAULT_RADIUS;
//                }
                drawnVertices.add(vertex);
                double posX = this.spawnPosX;
                double posY = this.spawnPosY;


                });

                ObservableSet<Edge> edgeSet = vListChange.getValueAdded().getEdges();
                edgeSet.addListener((SetChangeListener<? super EdgeView>) eListChange -> {

                    if (eListChange.wasAdded()) {
                        EdgeView edgeViewAdded = eListChange.getElementAdded();
//TODO: check if parallel edgeView already exists and modify that edgeView if it doesn't include new edgeView yet
//TODO why are random edges not following this rule
                        if(!drawnEdgeViews.contains(edgeViewAdded)){
                            long parallelEdgeCount =
                                    drawnEdgeViews.stream()
                                                .filter(modeledE-> modeledE.getTarget().equals(edgeViewAdded.getTarget())
                                                        &&  modeledE.getSource().equals(edgeViewAdded.getSource())
                                                ).count();
//TODO                            JUnit System.out.println("there are already " + parallelEdgeCount + "parallel Edges");
//                            for(int i =0;i<parallelEdgeCount;i++){
                                edgeViewAdded.changeDegree(10 + parallelEdgeCount*10);
//                            }
                            Platform.runLater(() -> {
                                anchorPane.getChildren().add(edgeViewAdded);
                                drawnEdgeViews.add(edgeViewAdded);
                                edgeViewAdded.toBack();
                                edgeViewAdded.initView();

                            });
                        }

//                            HashMap<Integer,HashMap<Integer,Edge>> adjMatrix = new HashMap<>(new HashMap<>());
//                            if(adjMatrix.containsKey(1) && adjMatrix.get(1).containsKey(2)){
//                                adjMatrix.get(1).get(2).
//                            }


                    } else {
                        EdgeView edgeViewRemoved = eListChange.getElementRemoved();
                        drawnEdgeViews.remove(edgeViewRemoved);
                        anchorPane.getChildren().remove(edgeViewRemoved);
                    }
                    if (model.getBisimToggle().get()) {
                        model.setPartition(Algorithms.bisim(model.getVertices()).getValue());
                    }
                });
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
                            for (Integer vertex : drawnVertices) {
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

                    for (Integer vertex : change.getElementAdded()) {
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
        spawnPosX= Integer.DEFAULT_RADIUS;
        spawnPosY= Integer.DEFAULT_RADIUS;
    }
    private void checkCtrlClick(MouseEvent pressed) {
        if (pressed.getButton().equals(MouseButton.PRIMARY) && (pressed.isControlDown())) {
            controller.addVertex(pressed.getX(),pressed.getY());
            pressed.consume();
        }
    }
    private void colorToggleIndicator(Boolean toggle) {

        if (toggle) {
            bisimToggleLabel.setText("Color bisimilar vertices: On");
        } else {
            bisimToggleLabel.setText("Color bisimilar vertices: Off");
        }
    }
    private void addChild(Node node){
        anchorPane.getChildren().add(node);
    }
    private void removeChild(Node node){
        anchorPane.getChildren().remove(node);
    }
}

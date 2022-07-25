package me.ducanh.thesis.ui;

import javafx.beans.value.ChangeListener;
import javafx.collections.*;
import javafx.scene.paint.Paint;
import me.ducanh.thesis.*;
import me.ducanh.thesis.algorithms.Block;
import me.ducanh.thesis.ui.util.Colors;

import java.util.HashMap;
import java.util.HashSet;

public class CanvasVM {
    private final Model model;
    private final Controller controller;
    private final ObservableMap<Edge, EdgeView> edgeViews = FXCollections.observableMap(new HashMap<>());
    private final ObservableMap<Vertex, VertexView>vertexViews = FXCollections.observableMap(new HashMap<>());
    private final ObservableSet<Block> partition = FXCollections.observableSet(new HashSet<>());
    int colorID = 0;

    //    private final BooleanProperty colorUpdate = new SimpleBooleanProperty();
    public CanvasVM(Model model, Controller controller){
        this.model = model;
        this.controller = controller;

        model.getEdgesByVertex().addListener((MapChangeListener<Vertex,ObservableSet<Edge>>)mapChange->{
            Vertex vertex = mapChange.getKey();
            if(mapChange.wasAdded()) {
                ObservableSet<Edge> edgeList = mapChange.getValueAdded();
                addVertexView(vertex);
                edgeList.addListener((SetChangeListener<Edge>)edge->{
                    if(edge.wasAdded()) addEdgeView(edge.getElementAdded());
                    else removeEdgeView(edge.getElementRemoved());
                    updateColors();
                });
            }
            else removeVertexView(vertex);

            updateColors();

        });

        partition.addListener((SetChangeListener<Block>) block->{

            if (block.wasAdded()) {
                if (block.getElementAdded().vertices.size() > 1) {
                    Paint color = Colors.array[colorID++ % 120];
                    for (Vertex vertex : block.getElementAdded().vertices) {
                        vertexViews.get(vertex).setFill(color);
                    }
                } else if (block.getElementAdded().vertices.size() == 1) {
                    vertexViews.get(block.getElementAdded().vertices.iterator().next()).resetFill();
                }
            } else {
                if (block.getElementRemoved().vertices.size() > 1) {
                    colorID--;
                }
            } //TODO doesn't remove color when clearing the partition list
        });
        addColorModeListener((obs,falseV,trueV)->{
            updateColors();
        });
    }




    private void addVertexView(Vertex vertex){
        vertexViews.put(vertex, new VertexView(controller,vertex));
    }
    private void removeVertexView(Vertex vertex){
        vertexViews.remove(vertex);
    }
    private void addEdgeView(Edge edge){
        edgeViews.put(edge,new EdgeView(edge.label,vertexViews.get(edge.source),vertexViews.get(edge.target)));
    }
    private void removeEdgeView(Edge edge) {
        edgeViews.remove(edge);
    }
    public void updateColors(){
        if(model.coloringIsEnabled()){
            this.partition.clear();
            this.partition.addAll(model.getBisimulation());
        }
    }

    public void addVertexViewListener(MapChangeListener<Vertex, VertexView> listener){
        vertexViews.addListener(listener);
    }
    public void addEdgeViewListener(MapChangeListener<Edge,EdgeView> listener){
        edgeViews.addListener(listener);
    }

    public void addColorModeListener(ChangeListener<? super Boolean> listener){
        model.coloringToggle().addListener(listener);
    }
}

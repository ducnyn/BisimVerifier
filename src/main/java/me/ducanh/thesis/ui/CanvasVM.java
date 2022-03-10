package me.ducanh.thesis.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.*;
import me.ducanh.thesis.Partition;
import me.ducanh.thesis.Edge;
import me.ducanh.thesis.Model;
import me.ducanh.thesis.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CanvasVM {
    Model model;
    private final ObservableMap<Edge, EdgeView> edgeViews = FXCollections.observableMap(new HashMap<>());
    private final ObservableMap<Vertex, VertexView>vertexViews = FXCollections.observableMap(new HashMap<>());
    private final ObservableSet<Partition> partition = FXCollections.observableSet(new HashSet<>());
    private final BooleanProperty colorUpdate = new SimpleBooleanProperty();
    public CanvasVM(Model model){
        this.model = model;

        model.addVertexListener(vertexChange->{
            updateColorIfColorMode();
            if(vertexChange.wasAdded()) addVertexView(vertexChange.getKey());
            else removeVertexView(vertexChange.getKey());
        });
        model.addEdgeListener(edgeChange->{
            updateColorIfColorMode();
            if(edgeChange.wasAdded()) addEdgeView(edgeChange.getElementAdded());
            else removeEdgeView(edgeChange.getElementRemoved());
        });
    }


    private void addVertexView(Vertex vertex){
        vertexViews.put(vertex, new VertexView(this,vertex));
    }
    private void removeVertexView(Vertex vertex){
        vertexViews.remove(vertex);
    }
    private void addEdgeView(Edge edge){
        edgeViews.put(edge,new EdgeView(edge));
    }
    private void removeEdgeView(Edge edge) {
        edgeViews.remove(edge);
    }
    private void updateColorIfColorMode(){
        if(model.getColorMode()){
            setPartition(model.getBisimulation().getValue());
            colorUpdate.set(true);
            colorUpdate.set(false);
        }
    }

    public void addVertexViewListener(MapChangeListener<Vertex, VertexView> listener){
        vertexViews.addListener(listener);
    }
    public void addEdgeViewListener(MapChangeListener<Edge,EdgeView> listener){
        edgeViews.addListener(listener);
    }
    public void addColorUpdateListener(ChangeListener<? super Boolean> listener){
        colorUpdate.addListener(listener);
    }
    public void addColorModeListener(ChangeListener<? super Boolean> listener){
        model.getColorModeListener().addListener(listener);
    }
    public void setPartition(Set<Partition> newPartition) {
        this.partition.clear();
        this.partition.addAll(newPartition);
    }

    public ObservableSet<Partition> getPartition() {
        return partition;
    }
}

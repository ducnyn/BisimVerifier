package me.ducanh.thesis;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import me.ducanh.thesis.ui.EdgeView;
import me.ducanh.thesis.ui.VertexView;

import java.util.HashMap;

public class Controller {
    Model model;
    private final ObservableMap<Edge, EdgeView> edgeViews = FXCollections.observableMap(new HashMap<>());
    private final ObservableMap<Vertex, VertexView>vertexViews = FXCollections.observableMap(new HashMap<>());
    public Controller(Model model) {
        this.model = model;
    }

    public Boolean addVertex(Integer vLabel){
        return model.addVertex(new Vertex(vLabel));
    }
    public Boolean addVertex(Integer vLabel, double posX, double posY){
        return model.addVertex(new Vertex(vLabel,posX,posY));
    }
    public Boolean addVertex(){
        Integer vLabel = model.smallestFreeLabel();
        return model.addVertex(new Vertex(vLabel));
    }
    public Boolean addVertex(double posX, double posY){
        Integer vLabel = model.smallestFreeLabel();
        return model.addVertex(new Vertex(vLabel,posX,posY));
    }

    public Boolean removeVertex(Integer id) {
        return model.removeVertex(new Vertex(id));
    }

}

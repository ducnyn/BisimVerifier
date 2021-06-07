package me.ducanh.thesis.model;

import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;


public class Vertex {
    private int id;
    private int blockID;
    Graph graph;
    Multimap<String, Vertex> transitions;

public Vertex(Graph graph, int id){
    this.graph = graph;
    this.id = id;
    transitions = ArrayListMultimap.create();
}

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Vertex vertex = (Vertex) o;
    return id == vertex.id;
}

@Override
public int hashCode() {
    return Objects.hash(id);
}


public void setBlockID(int id){
    this.blockID = id;
}

public int getBlockID(){
    return blockID;
}


public Set<String> getActions(){
    return (transitions.keySet());
}

public int getId() {
    return id;
}

public Graph getGraph(){return this.graph;}

public void addTransition(String action, Vertex target){
        transitions.put(action,target);
}

public void removeTransition(String action, Vertex target){
        transitions.remove(action,target);

}


public Set<Vertex> getTargets(String action){
    return new HashSet<>(transitions.get(action));
}

public Multimap<String, Vertex> getTransitions(){return transitions;}

public Boolean actionExists(String action){
    return transitions.containsKey(action);
}

@Override
public String toString(){
    return String.valueOf(id);
}

}

package me.ducanh.thesis.model;

import java.util.*;

import com.google.common.collect.Multimap;


public class Vertex {
    private String id;
    private int blockID;
    Multimap<String, Vertex> transitions;    //A Map of actions to Lists of all targetNodes


public void setBlockID(int id){
    this.blockID = id;
}

public int getBlockID(){
    return blockID;
}
public Vertex(String id){
    this.id = id;
}

public Set<String> getActions(){
    return (transitions.keySet());
}

public String getId() {
    return id;
}

public void addTransition(String action, Vertex target){
        transitions.put(action,target);
}

public void removeTransition(String action, Vertex target){
        transitions.remove(action,target);

}


public Set<Vertex> getTargetNodes(String action){
    return new HashSet<>(transitions.get(action));
}

public Multimap<String, Vertex> getTransitions(){return transitions;}

public Boolean actionExists(String action){
    return transitions.containsKey(action);
}
@Override
    public String toString(){
        return id;
    }



}

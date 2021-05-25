package me.ducanh.thesis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    private String id;
    private int blockID;
    HashMap<String,List<Node>> transitions;    //A Map of actions to Lists of all targetNodes


public void setBlockID(int id){
    this.blockID = id;
}

public int getBlockID(){
    return blockID;
}
public Node(String id){
    this.id = id;
}

public List<String> getActions(){
    return new ArrayList<>((transitions.keySet()));
}

public String getId() {
    return id;
}

public void addTransition(String action, Node target){
    if(transitions.containsKey(action)){
        transitions.get(action).add(target);
    } else {
        ArrayList<Node> targetList = new ArrayList<>();
        targetList.add(target);
        transitions.put(action,targetList);
    }
}


public List<Node> getTransitionList(String action){
    return transitions.getOrDefault(action, null);
}

public Map<String, List<Node>> getAllTransitionLists(){return transitions;}

public Boolean transExists(String action){
    return transitions.containsKey(action);
}
@Override
    public String toString(){
        return id;
    }



}

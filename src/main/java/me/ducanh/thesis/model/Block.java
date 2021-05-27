package me.ducanh.thesis.model;

import java.util.ArrayList;
import java.util.List;

public class Block extends ArrayList<Node> {
int ID;

public void setID(int ID){
    this.ID = ID;
}

public int getID(){
    return ID;
}
}

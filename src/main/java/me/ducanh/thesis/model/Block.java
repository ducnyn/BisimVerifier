package me.ducanh.thesis.model;

import java.util.ArrayList;

public class Block extends ArrayList<Vertex> {
int ID;

public void setID(int ID){
    this.ID = ID;
}

public int getID(){
    return ID;
}
}

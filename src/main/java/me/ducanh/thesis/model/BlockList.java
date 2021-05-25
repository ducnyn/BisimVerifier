package me.ducanh.thesis.model;

import java.util.ArrayList;
import java.util.List;

public class BlockList extends ArrayList<NodeList> {
int ID;

public void setID(int ID){
    this.ID = ID;
}

public int getID(){
    return ID;
}
}

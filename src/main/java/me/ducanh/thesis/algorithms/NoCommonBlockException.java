package me.ducanh.thesis.algorithms;

import me.ducanh.thesis.Vertex;

public class NoCommonBlockException extends Exception  {
    public NoCommonBlockException(Vertex state1, Vertex state2, Block root) {
        super("At least one of the states " + state1 + " or " + state2 + " is not part of root " + root.vertices + ".");
    }

    public NoCommonBlockException(String message){
        super(message);
    }
}

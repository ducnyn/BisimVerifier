package me.ducanh.thesis.algorithms;

import me.ducanh.thesis.Vertex;

public class StateDisappearedDuringPartitioningException extends Exception  {
    public StateDisappearedDuringPartitioningException(Vertex state1, Vertex state2, Block root) {
        super("After splitting "+root+", at least one of the states " + state1 + " or " + state2 + " is not part of left child" + root.left + " or right child "+root.right);
    }

    public StateDisappearedDuringPartitioningException(String message){
        super(message);
    }
}

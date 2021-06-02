package me.ducanh.thesis.util;

import me.ducanh.thesis.model.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class BisimChecker {

static private boolean transitionExists(Vertex vertex, String act, Set<Vertex> targetBlock){
    return vertex.getTargetNodes(act)
            .stream()
            .anyMatch(targetBlock::contains);
}

private static Set<Set<Vertex>> split (Set<Vertex> sourceBlock, String act, Set<Vertex> targetBlock){
        Set<Set<Vertex>> result = new HashSet<>();
        Set<Vertex> leftChild = new HashSet<>();
        Set<Vertex> rightChild = new HashSet<>();

        for (Vertex vertex : sourceBlock) {
            if (transitionExists(vertex,act,targetBlock)) {
                leftChild.add(vertex);
            } else {
                rightChild.add(vertex);
            }
        }
        result.add(leftChild);
        result.add(rightChild);
        return result;
    }

//private static Set<Set<Vertex>> split (Set<Vertex> sourceBlock, String act, Set<Vertex> targetBlock){
//    Set<Vertex> block = new HashSet<>(sourceBlock);
//    Set<Set<Vertex>> result = new HashSet<>();
//
//    Map<Boolean,List<Vertex>> splittedBlocks =
//            block.stream().collect(Collectors.partitioningBy(v -> transitionExists(v,act,targetBlock)));
//
//    result.add(new HashSet<>(splittedBlocks.get(true)));
//    result.add(new HashSet<>(splittedBlocks.get(false)));
//    return result;
//}


    public static void bisim(Set<Vertex> vertices) {

        Set<Set<Vertex>> currentPartition = new HashSet<>();
        Set<Set<Vertex>> parentPartition = new HashSet<>();
        currentPartition.add(vertices);

        while (!currentPartition.equals(parentPartition)) {
            parentPartition = currentPartition;
            currentPartition = new HashSet<>();

            for (Set<Vertex> sourceBlock : parentPartition) {
                for (Set<Vertex> targetBlock : parentPartition) {
                    for (Vertex sourceVertex : sourceBlock){

                    }

                        currentPartition.addAll(split(sourceBlock,"a",targetBlock));
                }
//                if split then break, else run till end and current = parent.

//                        block
//                        .stream()
//                        .filter(node -> !split(block,act,block.any.transition(act).getBlock()).contains(block))
//                        .findAny()
//                        .ifPresentOrElse( act -> currentPartition.addAll(split(block,act,firstNode.getTransitionList((act)))),
//                                        () -> currentPartition.add(block) );
            }//TODO WATCH THAT VIDEO TO SEE HOW YOU CAN MAP THE RESULTS

        }
    }


//    //Per definition Bisimulation = a~b, if for a -x> a2, there is a b-x>b2 and a2~b2
//    public Boolean altBisim(NodeInt a, NodeInt b) {
//        if(!a.getActs().equals(b.getActs())){
//            return false;
//        }
//
//        for (String act:a.getActs()) {
//            //this will probably iterate infinitely when it enters a loop in the graph
//
//            for(NodeInt nodeA : a.getTargets(act)){ //or edgeA: model.getEdges(a,act) .. altBisim(edgeA.getTarget(),edgeB.getTarget())
//                for(NodeInt nodeB : b.getTargets(act)){
//                    if (!altBisim(nodeA,nodeB)){
//                        return false;
//                    }
//                }
//            }
//
//        }
//        return true;
//
//    }
}



package me.ducanh.thesis.util;

import me.ducanh.thesis.model.Vertex;

import java.util.*;

public class BisimChecker {
    /**
     * @param sourceBlock
     * @param act
     * @param targetBlock
     * @return
     */

//TODO: Might need to check if there's a need to clone lists whenever they're used as parameters, due to side effects;

private static Set<Set<Vertex>> split (Set<Vertex> sourceBlock, String act, Set<Vertex> targetBlock){
        Set<Set<Vertex>> result = new HashSet<>();
        Set<Vertex> newBlock = new HashSet<>();
        Set<Vertex> remainderBlock = new HashSet<>(sourceBlock);

        for (Vertex vertex : sourceBlock) {
            for (Vertex target: vertex.getTargetNodes(act)){
                if (targetBlock.contains(target)) {
                    newBlock.add(vertex);
                    remainderBlock.remove(vertex);
                    break;
                }
            }
        }
        result.add(newBlock);
        result.add(remainderBlock);
        return result;
    }



    public static void bisim(Set<Vertex> vertices) {

        Set<Set<Vertex>> currentPartition = new HashSet<>();
        Set<Set<Vertex>> parentPartition = new HashSet<>();

        currentPartition.add(vertices);

        while (!currentPartition.equals(parentPartition)) {
            parentPartition = currentPartition;
            currentPartition = new HashSet<>();
//TODO iterate through actions and the blocks that contains such action
//TODO Find any splitting action and get new childPartition through that
            for (Set<Vertex> block : parentPartition) {
                        block
                        .stream()
                        .filter(node -> !split(block,act,block.any.transition(act).getBlock()).contains(block))
                        .findAny()
                        .ifPresentOrElse( act -> currentPartition.addAll(split(block,act,firstNode.getTransitionList((act)))),
                                        () -> currentPartition.add(block) );
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



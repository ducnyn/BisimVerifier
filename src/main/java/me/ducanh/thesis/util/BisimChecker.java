package me.ducanh.thesis.util;

import me.ducanh.thesis.model.Node;

import java.util.*;

public class BisimChecker {

//TODO: Might need to check if there's a need to clone lists whenever they're used as parameters, due to side effects;

private static List<List<Node>> split (List<Node> sourceBlock, String act, List<Node> targetBlock){
        ArrayList<List<Node>> partition = new ArrayList<>();
        ArrayList<Node> newBlock = new ArrayList<>();
        ArrayList<Node> remainderBlock = new ArrayList<>(sourceBlock);

        for (Node node: sourceBlock) {
            for (Node target: node.getTransitionList(act)){
                if (targetBlock.contains(target)) {
                    newBlock.add(node);
                    remainderBlock.remove(node);
                }
            }
        }

        partition.add(newBlock);
        partition.add(remainderBlock);
        return partition;
    }


//    private static void testing(ArrayList<Node> anything){
//        System.out.println(anything.toString());
//    }


    public static void bisim(List<Node> nodes) {
        List<List<Node>> newPartition = new ArrayList<>();
        List<List<Node>> oldPartition = new ArrayList<>();

        newPartition.add(nodes);

        while (!newPartition.equals(oldPartition)) {
            oldPartition = newPartition;
            newPartition.clear();
//TODO Any node with the highest amount of outgoing edges can be used to look for a distinguishing transition (NOT Action btw)
            for (List<Node> block : oldPartition) {
                        block
                        .stream()
                        .filter(node -> !split(block,act,firstNode.getTransitionList(act)).contains(block))
                        .findAny()
                        .ifPresentOrElse( act -> newPartition.addAll(split(block,act,firstNode.getTransitionList((act)))),
                                        () -> newPartition.add(block) );
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



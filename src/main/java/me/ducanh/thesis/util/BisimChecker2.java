package me.ducanh.thesis.util;

import me.ducanh.thesis.model.NodeList;
import me.ducanh.thesis.model.Node;
import me.ducanh.thesis.model.BlockList;

import java.util.ArrayList;
import java.util.List;

public class BisimChecker2 {

//TODO: Might need to check if there's a need to clone lists whenever they're used as parameters, due to side effects;

private static BlockList split (NodeList sourceBlock, String act, NodeList targetBlock) throws CloneNotSupportedException {
        BlockList partition = new BlockList();
        NodeList newBlock = new NodeList();
        NodeList remainderBlock = sourceBlock;

        for (Node node: sourceBlock) {
            for (Node target: node.getBlock(act)){
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



    public static List<List<Node>> bisim(List<Node> nodes) {
        List<List<Node>> newPartition = new ArrayList<>();
        List<List<Node>> oldPartition = new ArrayList<>();

        newPartition.add(nodes);

        while (!newPartition.equals(oldPartition)) {
            oldPartition = newPartition;
            newPartition = new ArrayList<>();
            for (List<Node> block : oldPartition) {
                if (oldPartition.indexOf(oldPartition.stream().filter(b -> block.stream().filter())));
                        block
                        .stream()
                        .filter(node -> !split(block,act,block.any.transition(act).getBlock()).contains(block))
                        .findAny()
                        .ifPresentOrElse( act -> newPartition.addAll(split(block,act,firstNode.getTransitionList((act)))),
                                        () -> newPartition.add(block) );
            }//TODO WATCH THAT VIDEO TO SEE HOW YOU CAN MAP THE RESULTS

        }
        return newPartition;
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



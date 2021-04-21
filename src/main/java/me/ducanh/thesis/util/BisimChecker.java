package me.ducanh.thesis.util;

import me.ducanh.thesis.model.Edge;
import me.ducanh.thesis.model.Node;
import me.ducanh.thesis.model.NodeInt;

import java.util.*;

public class BisimChecker {


    private Set<Node> split (Set<Node> sourceBlock, String act, Set<Node> targetBlock){
        HashSet<Node> block = new HashSet<>();
        for (Node node: sourceBlock) {
            if (targetBlock.contains(node.trans(act))){
                block.add(node);
            }jh
        }
        return block;
    }

    public static void bisim(Set<Node> singleBlock, Set<String> labels, Set<Edge> edges) {
        Set<Set<Node>> blocks = new HashSet<>();
        Set<Set<Node>> tempBlocks = new HashSet<>();
        blocks.add(singleBlock);

        while (!blocks.equals(tempBlocks)) {
            tempBlocks = blocks;
            blocks.clear();

            for (Set<Node> block : tempBlocks) {
                if(block.getActs().count()>1){
                    Set<Node> refinedBlock = split(block, act, targetBlock);
                    block.removeAll(refinedBlock);

                    blocks.add(refinedBlock);
                    blocks.add(block);
                }

            }
        }
    }


    //Per definition Bisimulation = a~b, if for a -x> a2, there is a b-x>b2 and a2~b2
    public Boolean altBisim(NodeInt a, NodeInt b) {
        if(!a.getActs().equals(b.getActs())){
            return false;
        }

        for (String act:a.getActs()) {
            //this will probably iterate infinitely when it enters a loop in the graph

            for(NodeInt nodeA : a.getTargets(act)){ //or edgeA: model.getEdges(a,act) .. altBisim(edgeA.getTarget(),edgeB.getTarget())
                for(NodeInt nodeB : b.getTargets(act)){
                    if (!altBisim(nodeA,nodeB)){
                        return false;
                    }
                }
            }

        }
        return true;

    }
}



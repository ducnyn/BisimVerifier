package me.ducanh.thesis.util;

import me.ducanh.thesis.model.Vertex;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BisimChecker {

//static private boolean transitionExists(Vertex vertex, String act, Set<Vertex> targetBlock) {
//    for (Vertex target : vertex.getTargetNodes(act)) {
//        if (targetBlock.contains(target)) {
//            return true;
//        }
//    }
//    return false;
//}

//private static Set<Set<Vertex>> split (Set<Vertex> sourceBlock, String act, Set<Vertex> targetBlock){
//        Set<Set<Vertex>> result = new HashSet<>();
//        Set<Vertex> leftChild = new HashSet<>();
//        Set<Vertex> rightChild = new HashSet<>();
//
//        for (Vertex vertex : sourceBlock) {
//            if (transitionExists(vertex,act,targetBlock)) {
//                leftChild.add(vertex);
//            } else {
//                rightChild.add(vertex);
//            }
//        }
//        result.add(leftChild);
//        result.add(rightChild);
//        return result;
//
//    }

private static Stream<Set<Vertex>> split(Set<Vertex> sourceBlock, String act, Set<Vertex> targetBlock) {
    Set<Set<Vertex>> result = new HashSet<>();

    return sourceBlock  .stream() //could use parallelstream? There no side effects, so no race condition
                        .collect(Collectors.partitioningBy(
                        vertex -> vertex.getTargets(act)
                                        .stream()
                                        .anyMatch(targetBlock::contains)))
                        .values()
                        .stream()
                        .map(HashSet::new);
}


//public static void bisim(Set<Vertex> vertices) {
//
//    Set<Set<Vertex>> currentPartition = new HashSet<>();
//    Set<Set<Vertex>> parentPartition = new HashSet<>();
//    currentPartition.add(vertices);
//
//    while (!currentPartition.equals(parentPartition)) {
//        parentPartition = currentPartition;
//        currentPartition = new HashSet<>();
//
//        for (Set<Vertex> srcBlock : parentPartition) {
//
//            srcBlock
//                    .stream()
//                    .flatMap(vertex -> vertex.getTransitions().entries().stream())
//                    .distinct()
//                    .filter(trans -> !split(srcBlock,trans.getKey(),trans.getValue()).contains(srcBlock))
//                    .findAny()
//                    .ifPresentOrElse(
//                            trans -> currentPartition.addAll(split(srcBlock,trans.act(),trans.target())),
//                            () -> currentPartition.add(srcBlock));
//
//
//                    //TODO after a block has gone through split, add the new sub-blocks to the bisim tree.
//
//        }

//}
}
//
//
////    //Per definition Bisimulation = a~b, if for a -x> a2, there is a b-x>b2 and a2~b2
////    public Boolean altBisim(NodeInt a, NodeInt b) {
////        if(!a.getActs().equals(b.getActs())){
////            return false;
////        }
////
////        for (String act:a.getActs()) {
////            //this will probably iterate infinitely when it enters a loop in the graph
////
////            for(NodeInt nodeA : a.getTargets(act)){ //or edgeA: model.getEdges(a,act) .. altBisim(edgeA.getTarget(),edgeB.getTarget())
////                for(NodeInt nodeB : b.getTargets(act)){
////                    if (!altBisim(nodeA,nodeB)){
////                        return false;
////                    }
////                }
////            }
////
////        }
////        return true;
////
////    }
//}



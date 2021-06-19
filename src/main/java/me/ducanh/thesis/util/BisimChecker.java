package me.ducanh.thesis.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import me.ducanh.thesis.model.BisimTree;
import me.ducanh.thesis.model.Vertex;

import java.util.*;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toSet;

public class BisimChecker {

private static final HashMap<Vertex, Set<Vertex>> vertexInBlock = new HashMap<>();
private static BisimTree rootNode;
private static BisimTree currentNode;

//static private boolean transitionExists(Vertex vertex, String act, Set<Vertex> targetBlock) {
////    for (Vertex target : vertex.getTargets(act)) {
////        if (targetBlock.contains(target)) {
////            return true;
////        }
////    }
////    return false;
//
//    return vertex.getTargets(act)
//            .stream()
//            .map(currentBlock::get)
//            .anyMatch(targetBlock::equals);
//
//}

//private static Set<Set<Vertex>> split (Set<Vertex> sourceBlock, String act, Set<Vertex> targetBlock){
//    Set<Set<Vertex>> result = new HashSet<>();
//    Set<Vertex> leftChild = new HashSet<>();
//    Set<Vertex> rightChild = new HashSet<>();
//
//        for (Vertex vertex : sourceBlock) {
//            if (transitionExists(vertex,act,targetBlock)) {
//                leftChild.add(vertex);
//                vertex.setBlock(leftChild);
//            } else {
//                rightChild.add(vertex);
//                vertex.setBlock(rightChild);
//            }
//        }
//
//
//
//    if (rightChild.isEmpty()||leftChild.isEmpty()){
//        result.add(sourceBlock);
//    } else {
//        result.add(leftChild);
//        result.add(rightChild);
//    }
//
//
//    return result;
//
//}

//Checks if vertices have a transition to a given set of vertices and splits them into two sets accordingly.
private static Map<Boolean, Set<Vertex>> split(Set<Vertex> vertices, String act, Set<Vertex> targets) {

    return vertices.stream()
            .collect(partitioningBy(
                    vertex -> vertex.getTargets(act).stream()
                            .anyMatch(targets::contains)
                    , toSet()));
}
//private static Set<Set<Vertex>> split(Set<Vertex> sourceBlock, String act, Set<Vertex> targetBlock) {
//    Set<Set<Vertex>> partition =
//            sourceBlock.stream()
//            .collect(Collectors.partitioningBy(
//                    vertex -> vertex.getTargets(act)
//                            .stream()
//                            .anyMatch(targetBlock::contains)))
//            .values()
//            .stream()
//            .filter(not(Collection::isEmpty))
//            .map(HashSet::new)
//            .collect(Collectors.toSet());
//
//    for (Set<Vertex> block : partition) {
//        for (Vertex vertex : block) getBlock.put(vertex,block);
//    }
//
//    return partition;
//}


public static String bisim(Set<Vertex> vertices) {

    Set<Set<Vertex>> newPartition = new HashSet<>();
    Set<Set<Vertex>> parentPartition = new HashSet<>();
    newPartition.add(vertices);
    rootNode = new BisimTree(vertices);
    currentNode = rootNode;

    while (!newPartition.equals(parentPartition)) {

        parentPartition = newPartition;
        newPartition = new HashSet<>();

        for (Set<Vertex> block : parentPartition) {
            for (Vertex vertex : block) { vertexInBlock.put(vertex, block); } }


        for (Set<Vertex> block : parentPartition) {

            Set<Multimap<String, Set<Vertex>>> bisimTransitions = MapToBisimTransitions(block);
            Set<Map.Entry<String, Set<Vertex>>> someComplement = findAnyComplement(bisimTransitions);
//            Multimap<String, Set<Vertex>> someComplement = findAnyComplement(bisimTransitions);

            Map.Entry<String, Set<Vertex>> splittingEdge =
                    someComplement
                            .stream()
                            .findAny()
                            .orElse(null);

                    if(splittingEdge != null){
                        newPartition.addAll(split(block,splittingEdge.getKey(), splittingEdge.getValue()).values());
                        System.out.println("split by: " + splittingEdge);

                    } else {
                        newPartition.add(block);
                        System.out.println("not split");

                    }
        }
        System.out.println("new partition: " + newPartition);

    }
    return newPartition.toString();
}
//map each vertex to its transitions to current Blocks
private static Set<Multimap<String,Set<Vertex>>> MapToBisimTransitions(Set<Vertex> block){
    return block.stream()
            .map(vertex -> vertex.getTransitions()
                    .entries()
                    .stream()
                    .collect(Multimaps.toMultimap(Map.Entry::getKey,
                            entryKey -> vertexInBlock.get(entryKey.getValue()),
                            HashMultimap::create)))
            .collect(toSet());
}
/*Pairwise comparison of sets of BlockTransitions. If any pair is not identical, return the list of transitions that distinguishes them.
 * This code is less optimized because it iterates over the same comparisons again, compared to the one beneath
 */
//private static Multimap<String, Set<Vertex>> findAnyComplement(Set<Multimap<String, Set<Vertex>>> blockTransitionSets) {
//
//    for (Multimap<String, Set<Vertex>> v : blockTransitionSets) {
//        for (Multimap<String, Set<Vertex>> v2 : blockTransitionSets) {
//
//            Multimap<String, Set<Vertex>> difference =
//                    Multimaps.filterEntries(v, entry -> !v2.containsEntry(entry.getKey(), entry.getValue()));
//
//            if (!difference.isEmpty()) {
//                return difference;
//            }
//        }
//    }
//    return HashMultimap.create();
//}


/**Pairwise comparison of sets of BlockTransitions. If any pair is not identical, return the list of transitions that distinguishes them */
private static Set<Map.Entry<String, Set<Vertex>>> findAnyComplement(Set<Multimap<String, Set<Vertex>>> blockTransitionSets) {

    List<Multimap<String,Set<Vertex>>> list = new ArrayList<>(blockTransitionSets);

    for (int i = 0; i<list.size(); i++){
        for(int j = i+1 ; j<list.size(); j++){

            Set<Map.Entry<String, Set<Vertex>>> difference =
                    new HashSet<>(Sets.symmetricDifference(new HashSet<>(list.get(i).entries()), (new HashSet<>(list.get(j).entries()))));

            if (!difference.isEmpty()){

                return difference;
            }

        }
    } return new HashSet<>();
}


}



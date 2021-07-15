package me.ducanh.thesis.util;

import com.google.common.collect.Sets;
import javafx.util.Pair;
import me.ducanh.thesis.model.BisimTree;
import me.ducanh.thesis.model.Model;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Algorithms {

private static final HashMap<Integer, Set<Integer>> currentBlock = new HashMap<>();
private static BisimTree rootNode;
private static BisimTree currentNode;
private static Model model;


private static Map<Boolean, Set<Integer>> split(Set<Integer> block, String action, Set<Integer> targetBlock) {
    return block.stream()
            .collect(partitioningBy(
                    vertex -> model.getTargets(vertex,action).stream()
                                   .anyMatch(targetBlock::contains)
                    , toSet()));
}

public static Set<Set<Integer>> groupByBisimilarity(Model data) {
    model = data;
    currentBlock.clear();
    Set<Set<Integer>> newPartition = new HashSet<>();
    Set<Set<Integer>> parentPartition = new HashSet<>();
    newPartition.add(new HashSet<>(model.getVertices()));

    rootNode = new BisimTree(new HashSet<>(model.getVertices()));
    currentNode = rootNode;



    while (!newPartition.equals(parentPartition)) {

        parentPartition = Set.copyOf(newPartition);
        newPartition.clear();

        for (Set<Integer> block : parentPartition) {
            for (Integer vertex : block) {
                currentBlock.put(vertex, block);
            }
        }
        for(Set<Integer> block: parentPartition) {
            //get all edges from this block, reduced to edges with bisimilar targets

            Optional<Collection<Set<Integer>>> partition =
                    block.stream()
                            .flatMap(vertex -> model.getEdges(vertex).stream())
                            .map(edge -> new Pair<>(edge.getLabel(), currentBlock.get(edge.getTarget())))
                            .distinct()
                            .map(bEdge -> split(block, bEdge.getKey(), bEdge.getValue()))
                            .filter(map -> !map.values().contains(block))
                            .map(Map::values)
                            .findAny();

            partition.ifPresentOrElse(newPartition::addAll, () -> newPartition.add(block));
        }
//

//        for (Set<Integer> block : parentPartition) {
//        Pair<String,Set<Integer>> splitter = getSplitter(block);
//            if (splitter != null) {
//                newPartition.addAll(
//                        split(block, splitter.getKey(), splitter.getValue())
//                        .values().stream().peek(System.out::println).collect(toSet()));
//                System.out.println("split by: " + splitter);
//
//            } else {
//                newPartition.add(block);
//                System.out.println("not split");
//
//            }
//        }
        System.out.println("new partition: " + newPartition);

    }

    return newPartition;
}

//private static Pair<String,Set<Integer>> getSplitter(Set<Integer> block) {
//    List<Set<Pair<String, Set<Integer>>>> blockTransitions = block.stream()
//            .map(vertex -> model.getEdges(vertex)
//                    .stream()
//                    .map(Edge -> new Pair<>(Edge.getLabel(), currentBlock.get(Edge.getTarget())))
//                    .collect(toSet())
//            )
//            .collect(toList());

//    for (int i = 0; i < blockTransitions.size(); i++) {
//        for (int j = i + 1; j < blockTransitions.size(); j++) {
//            Set<Pair<String,Set<Integer>>> difference = Sets.symmetricDifference(new HashSet<>(blockTransitions.get(i)),new HashSet<>(blockTransitions.get(j)));
//            if(!difference.isEmpty()){
//                return difference.iterator().next();
//            }
//        }
//    } return null;

//}
}



package me.ducanh.thesis.model;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toSet;

public class Algorithms {


//  private static Map<Boolean, Set<Integer>> split(Model model, Set<Integer> block, BlockEdge splitter) {
//    String label = splitter.getLabel();
//    Block targetBlock = splitter.getTargetBlock();
//    return block.stream()
//            .collect(partitioningBy(
//                    vertex -> model.getTargets(vertex, label).stream()
//                            .anyMatch(targetBlock::contains)
//                    , toSet()));
//  }

  public static Set<Block> groupByBisimilarity(Model model) {
    HashMap<Integer, Block> containingBlock = new HashMap<>();
    Set<Block> newPartition = new HashSet<>();
    Set<Block> parentPartition = new HashSet<>();
    Block rootBlock = new Block(model.getVertices());
    newPartition.add(rootBlock);

    while (!newPartition.equals(parentPartition)) {
      System.out.println("current Partition: " + newPartition);
      parentPartition = Set.copyOf(newPartition);
      newPartition = new HashSet<>();
      for (Block block : parentPartition) {
        for (Integer vertex : block) {
          containingBlock.put(vertex, block);
        }
      }

      for (Block block : parentPartition) {

      System.out.println("try to split block" + block);
        List<BlockEdge> blockEdgesOut =
                block.stream()
                        .flatMap(vertex -> model.getEdges(vertex).stream())
                        .map(edge -> new BlockEdge(edge.getLabel(), containingBlock.get(edge.getTarget())))
                        .collect(Collectors.toList());
//                        .distinct()
//                        .anyMatch(bEdge -> block.split(model,bEdge));


        for (BlockEdge blockEdge : blockEdgesOut) {
          
          if (split(block, blockEdge, model).containsValue(block.getVertices())) {

            return Set.of(block.getLeftChild(), block.getRightChild());
          } else {

            System.out.println("block split by " + blockEdge);
            System.out.println("true: "+ block.getLeftChild() +", false :" + block.getRightChild());
          }
        }



      }
    }
    System.out.println("Final partition(bisimilar sets): " + newPartition);
    return newPartition;
  }
  public static Map<Boolean,Set<Integer>> split(Block block, BlockEdge blockEdge, Model model) {

    return  block.stream()
            .collect(partitioningBy(
                    vertex -> model.getTargets(vertex, blockEdge.getLabel()).stream()
                            .anyMatch(blockEdge.getTargetBlock()::contains)
                    , toSet()));


  }
}



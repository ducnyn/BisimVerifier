package me.ducanh.thesis.model;

import com.google.common.collect.Sets;
import javafx.util.Pair;

import java.util.*;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toSet;

public class Algorithms {
  private static Model model;

//  private static Map<Boolean, Set<Integer>> split(Model model, Set<Integer> block, BlockEdge splitter) {
//    String label = splitter.getLabel();
//    Block targetBlock = splitter.getTargetBlock();
//    return block.stream()
//            .collect(partitioningBy(
//                    vertex -> model.getTargets(vertex, label).stream()
//                            .anyMatch(targetBlock::contains)
//                    , toSet()));
//  }

  public static Pair<Block, Set<Block>> bisim(Model data) {
    model = data;
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

        Optional<BlockEdge> splitter =
                block.stream()
                        .flatMap(vertex -> model.getEdges(vertex).stream())
                        .map(edge -> new BlockEdge(edge.getLabel(), containingBlock.get(edge.getTarget())))
                        .filter(bEdge -> !split(block, bEdge).containsValue(block.getVertices()))
                        .findAny();

        if (splitter.isPresent()) {
          Map<Boolean, Set<Integer>> splitBlock = split(block, splitter.get());
          block.setSplitter(splitter.get());
          block.setLeftChild(new Block(splitBlock.get(true)));
          block.setRightChild(new Block(splitBlock.get(false)));
          newPartition.addAll(List.of(block.left(), block.right()));
//          System.out.println("\tblock "+block+" split by " + block.getSplitter());
//          System.out.println("\tinto " + block.left() + " and " + block.right());b
        } else {
          newPartition.add(block);
        }
      }
    }
//    System.out.println("Final partition(bisimilar sets): " + newPartition);
    return new Pair<>(rootBlock, newPartition);
  }

  public static String getDeltaFormula(Integer s1, Integer s2, Model model, Block rootBlock) {
    Block currentBlock = rootBlock;
    StringBuilder deltaFormula = new StringBuilder();

    //base case


    while (true) {
      if (currentBlock.getSplitter() == null) {
        return "tt";
      }

      if (currentBlock.left().containsAll(s1, s2)) {
        currentBlock = currentBlock.left();
      } else if (currentBlock.right().containsAll(s1, s2)) {
        currentBlock = currentBlock.right();
      } else {
        break; //neither left or right contains all -> next split will separate
      }
    }
      System.out.println("\nThe deepest block including " + s1 + " and " + s2 + " is" + currentBlock);
      System.out.println("\nThe splitter is" + currentBlock.getSplitter());
    String a = currentBlock.getSplitter().getLabel();
    Set<Integer> BP = currentBlock.getSplitter().getTargetBlock().getVertices();
    deltaFormula = new StringBuilder("<").append(a).append(">");

    if (currentBlock.left().contains(s1)) {
      Set<Integer> SL = Sets.intersection(model.getTargets(s1, a), BP);
      Set<Integer> SR = model.getTargets(s2, a);
      System.out.println(SL.toString() + SR);

      for (Integer sL : SL) {
        for (Integer sR : SR) {
          deltaFormula.append(getDeltaFormula(sL, sR, model, rootBlock));
        }
      }

    } else {
      Set<Integer> SL = Sets.intersection(model.getTargets(s2, a), BP);
      Set<Integer> SR = model.getTargets(s1, a);

      for (Integer sL : SL) {
        for (Integer sR : SR) {
          deltaFormula.append(getDeltaFormula(sL, sR, model, rootBlock));
        }
      }

    }
    System.out.println(deltaFormula.toString());
    return deltaFormula.toString();
  }

  public static Map<Boolean, Set<Integer>> split(Block block, BlockEdge blockEdge) {
//    System.out.println("attempting to split " + block);
    return block.stream()
            .collect(partitioningBy(
                    vertex -> model.getTargets(vertex, blockEdge.getLabel())
                            .stream()
                            .anyMatch(blockEdge.getTargetBlock()::contains)
                    , toSet()));

  }
}



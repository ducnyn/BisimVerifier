package me.ducanh.thesis.model;

import com.google.common.collect.Sets;
import javafx.util.Pair;

import java.util.*;

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
public static Map<Boolean, Set<Vertex>> split(Block block, BlockEdge blockEdge) {
//    System.out.println("attempting to split " + block);
  return block.stream()
          .collect(partitioningBy(
                  vertex -> vertex.getTargets(blockEdge.getLabel())
                          .stream()
                          .anyMatch(blockEdge.getTargetBlock()::contains)
                  , toSet()));

}
  public static Pair<Block, Set<Block>> bisim(Set<Vertex> vertices) {
    HashMap<Vertex, Block> containingBlock = new HashMap<>();
    Set<Block> newPartition = new HashSet<>();
    Set<Block> parentPartition = new HashSet<>();
    Block rootBlock = new Block(vertices);
    newPartition.add(rootBlock);


    while (!newPartition.equals(parentPartition)) {
      System.out.println("current Partition: " + newPartition);
      parentPartition = Set.copyOf(newPartition);
      newPartition = new HashSet<>();

      for (Block block : parentPartition) {
        for (Vertex vertex : block) {
          containingBlock.put(vertex, block);
        }
      }

      for (Block block : parentPartition) {

        Optional<BlockEdge> splitter =
                block.stream()
                        .flatMap(vertex -> vertex.getEdges().stream())
                        .map(edge -> new BlockEdge(edge.getLabel(), containingBlock.get(edge.getTarget())))
                        .filter(bEdge -> !split(block, bEdge).containsValue(block.getVertices()))
                        .findAny();

        if (splitter.isPresent()) {
          Map<Boolean, Set<Vertex>> splitBlock = split(block, splitter.get());
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

  public static String getDeltaFormula(Vertex s1, Vertex s2, Model model, Block rootBlock) {
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
    System.out.println("Comparing " + s1 + " with " + s2);
    System.out.println("The deepest block is" + currentBlock);
    System.out.println("The splitter is" + currentBlock.getSplitter());
    System.out.println("The leftChild is " + currentBlock.left());
    System.out.println("The rightChild is " + currentBlock.right());
    String a = currentBlock.getSplitter().getLabel();
    Set<Vertex> B = currentBlock.getSplitter().getTargetBlock().getVertices();
    Set<Vertex> LTargetsInB;
    Set<Vertex> RTargets;
    Vertex L;
    Vertex R;
    if (currentBlock.left().contains(s1)) {
      L = s1;
      R = s2;
      deltaFormula.append("<").append(a).append(">");

    } else {
      L = s2;
      R = s1;
      deltaFormula.append("<¬").append(a).append(">");

    }
    LTargetsInB = Sets.intersection(L.getTargets(a), B);
    RTargets = R.getTargets(a);

    System.out.println("SL = " + LTargetsInB);
    System.out.println("SR = " + RTargets);

    Set<String> formulaSet = new HashSet<>();
    for (Vertex LTarget : LTargetsInB) {
      for (Vertex RTarget : RTargets) {
        formulaSet.add(getDeltaFormula(LTarget, RTarget, model, rootBlock));
      }
//TODO to check satisfaction, you first need to define how it is checked.
// a formula PHI : formulae is kept, if there is at least one state in SR that satisfies all formulas besides PHI
      List<String> formulaList = new ArrayList<>(formulaSet);

      for (int i = 0; i < formulaList.size(); i++) {
        String formula = formulaList.get(i);
        Set<Integer> Targets;
//                RTargets.stream()
//                        .filter(target -> satisfies..)


      }
    }
//    List<String> formulaList = new ArrayList<>(formulaSet);
//    for (int i = 0; i < formulaList.size(); i++) {
//      if (i > 0) {
//        deltaFormula.append("∧");
//      }
//      deltaFormula.append(formulaList.get(i));
//    }
    System.out.println(deltaFormula);
    return deltaFormula.toString();
  }


}



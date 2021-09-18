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

  public static Boolean isFormula(String string){
  System.out.println("Evaluate if " + string + " is a formula.");
    if(string.equals("tt")){
      return true;
    }
    if(string.equals("ff")){
      return true;
    }
    if(string.matches("^\\[[a-z]\\].*")){ // <a>PHI := Es gibt einen a-Nachfolger, der PHI erfüllt...
      return isFormula(string.substring(string.indexOf(']')+1));
    }
    if(string.matches("^<[a-z]>.*")){ // [a] := Alle a-Nachfolger erfüllen ...
      return isFormula(string.substring(string.indexOf('>')+1));
    }
    if(string.matches("^!.*")){
      return isFormula(string.substring(1));
    }
    if(string.matches("^&&.*")){
      return isFormula(string.substring(2));
    }

    return false;
  }



  public static Boolean evaluate(Vertex v, String formula){

    if(formula.equals("tt")){
      return true;
    }
    if(formula.equals("ff")){
      return false;
    }
    if(formula.matches("^\\[[a-z]\\].*")){ // [a] := Alle a-Nachfolger erfüllen phi ...
      String action = formula.substring(formula.indexOf('[')+1,formula.indexOf(']'));
      String rest = formula.substring(formula.indexOf(']')+1);
      for (Vertex target: v.getTargets(action)){
        if(!evaluate(target,rest)){
          return false;
        }
      }
      return true;
    }
    if(formula.matches("^<[a-z]>.*")){ // <a> := Es gibt einen a-Nachfolger, der phi erfüllt...
      String action = formula.substring(formula.indexOf('<')+1,formula.indexOf('>'));
      String rest = formula.substring(formula.indexOf('>')+1);
      for (Vertex target: v.getTargets(action)){
        if(evaluate(target,rest)){
          return true;
        }
      }
      return false;
    }
    if(formula.matches("^!.*")){
      return !evaluate(v,formula.substring(1));
    }
    return false;
  }


  public static String getDeltaFormula(Vertex s1, Vertex s2, Block rootBlock) {
    Block currentBlock = rootBlock;
    StringBuilder deltaFormula = new StringBuilder();

    //base case
    while (true) {
      System.out.println("current Splitter is" + currentBlock.getSplitter());
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
//    System.out.println("Comparing " + s1 + " with " + s2);
//    System.out.println("The deepest block is" + currentBlock);
//    System.out.println("The splitter is" + currentBlock.getSplitter());
//    System.out.println("The leftChild is " + currentBlock.left());
//    System.out.println("The rightChild is " + currentBlock.right());

    String action = currentBlock.getSplitter().getLabel();
    Set<Vertex> B = currentBlock.getSplitter().getTargetBlock().getVertices();

    Vertex sL;
    Vertex sR;

    if (currentBlock.left().contains(s1)) {
      sL = s1;
      sR = s2;
      deltaFormula.append("<").append(action).append(">");
    } else {
      sL = s2;
      sR = s1;
      deltaFormula.append("!<").append(action).append(">");
    }
    int smallest = Integer.MAX_VALUE;
    Set<Vertex> SL; //SL
    Set<Vertex> SR; //SR

    SL = Sets.intersection(sL.getTargets(action), B);
    SR = sR.getTargets(action);

    for (Vertex LTarget : SL) {
      List<String> Formulas = new ArrayList<>(); //GAMMA

      for (Vertex RTarget : SR) {
        Formulas.add(getDeltaFormula(LTarget, RTarget, rootBlock));
      }

      for (String formula: Formulas) { //for each Phi in Gamma
        ArrayList<String> otherFormulas = new ArrayList<>(Formulas);
        otherFormulas.remove(formula);

         if(SR.stream().noneMatch(vertex->!evaluate(vertex,formula)
         && otherFormulas.stream().allMatch(otherFormula->evaluate(vertex,otherFormula)))){
           Formulas.remove(formula);
         }
      }

      if(Formulas.size()<smallest){
        smallest = Formulas.size();
        deltaFormula = new StringBuilder();
        deltaFormula.append(Formulas.get(0));
        for(int i = 1; i<Formulas.size();i++){
          deltaFormula.append("&&").append(Formulas.get(i));
        }
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



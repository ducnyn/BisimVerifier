package me.ducanh.thesis.model;

import com.google.common.collect.Sets;
import javafx.util.Pair;
import me.ducanh.thesis.formula.tree.*;

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


    private static Block findLastBlock(Vertex state1, Vertex state2, Block root) throws NoDistinguishingFormulaException {
        Block block = root;

        while (true) {
            if (block.getSplitter() == null) {
                return block;
            }
            if (block.left().containsAll(state1, state2)) {
                block = block.left();
            } else if (block.right().containsAll(state1, state2)) {
                block = block.right();
            } else {
                return block; //neither left or right contains all -> next split will separate
            }
        }

    }

    public static TreeNode getDeltaFormula(Vertex state1, Vertex state2, Block rootBlock) throws NoDistinguishingFormulaException {
        int minFormulaSize = Integer.MAX_VALUE;

        Block lastBlock = findLastBlock(state1, state2, rootBlock);
        if (lastBlock.getSplitter() == null) {
            throw new NoDistinguishingFormulaException(
                    "The states are bisimilar. No formula distinguishes them.");
        }

        Vertex leftState;
        Vertex rightState;
        boolean reversed;

        if (lastBlock.left().contains(state1)) {
            leftState = state1;
            rightState = state2;
            reversed = false;
        } else {
            leftState = state2;
            rightState = state1;
            reversed = true;
        }

        Set<Vertex> SL;
        Set<Vertex> SR;

        String splitAction = lastBlock.getSplitter().getLabel();
        Set<Vertex> splitBlock = lastBlock.getSplitter().getTargetBlock().getVertices();

        SL = Sets.intersection(leftState.getTargets(splitAction), splitBlock);
        SR = rightState.getTargets(splitAction);

        TreeNode nextNode = null;
        for (Vertex LTarget : SL) {
            List<TreeNode> Formulas = new ArrayList<>(); //GAMMA

            for (Vertex RTarget : SR) {
                Formulas.add(getDeltaFormula(LTarget, RTarget, rootBlock));
                System.out.println("deltaFormula of " + LTarget + " and " + RTarget + " in Block " + rootBlock);
            }
            System.out.println("All SL SR deltaformulas: " + Formulas);

            for (TreeNode formula : Formulas) { //for each Phi in Gamma
                ArrayList<TreeNode> otherFormulas = new ArrayList<>(Formulas);
                otherFormulas.remove(formula);

                if (SR.stream().noneMatch(vertex -> (!formula.evaluate(vertex)
                        && otherFormulas.stream().allMatch(otherFormula -> formula.evaluate(vertex))))) {
                    Formulas.remove(formula);
                    System.out.println("removed formula" + formula);
                }

            }

            if (Formulas.size() < minFormulaSize) {
                minFormulaSize = Formulas.size();
//        deltaFormula = new StringBuilder();
                if (Formulas.size() == 1) {
                    nextNode = Formulas.get(0);
                } else if (Formulas.size() > 1) {
                    nextNode = Formulas.get(0);
                    for (int i = 1; i < Formulas.size(); i++) {
                        nextNode = new AndNode(nextNode, Formulas.get(i));
                    }
                }
            }
        }
        TreeNode result = new DiamondNode(splitAction, Objects.requireNonNullElse(nextNode,new TrueNode()));
        if (reversed) result = new NotNode(result);

        System.out.println(result);
        return result;
    }


    //computes a minimalistic formula satisfied by v1, but not v2. Both formulas satisfy the formula up until the second last vertex of the described path.
//  public static String getDeltaFormula(Vertex s1, Vertex s2, Block rootBlock) throws NoDistinguishingFormulaException {
//    Block currentBlock = rootBlock;
//    StringBuilder deltaFormula = new StringBuilder();
//
//    //base case
//    System.out.println("\n\nComparing " + s1 + " with " + s2);
//    while (true) {
//      System.out.println("current Block is " + currentBlock);
//      System.out.println("current left is " + currentBlock.left());
//      System.out.println("current right is "+currentBlock.right());
//      System.out.println("current Splitter is " + currentBlock.getSplitter());
//      System.out.println("Vertex 1 = " +s1);
//      System.out.println("Vertex 2 = " +s2);
//
//
//      if (currentBlock.getSplitter() == null) {
//        throw new NoDistinguishingFormulaException();
//      }
//
//      if (currentBlock.left().containsAll(s1, s2)) {
//        currentBlock = currentBlock.left();
//      } else if (currentBlock.right().containsAll(s1, s2)) {
//        currentBlock = currentBlock.right();
//      } else {
//        break; //neither left or right contains all -> next split will separate
//      }
//    }
//
//    String action = currentBlock.getSplitter().getLabel();
//    Set<Vertex> B = currentBlock.getSplitter().getTargetBlock().getVertices();
//
//    Vertex sL;
//    Vertex sR;
//
//    if (currentBlock.left().contains(s1)) {
//      sL = s1;
//      sR = s2;
//      deltaFormula.append("<").append(action).append(">");
//    } else {
//      sL = s2;
//      sR = s1;
//      deltaFormula.append("!<").append(action).append(">");
//    }
//    int smallest = Integer.MAX_VALUE;
//    Set<Vertex> SL; //SL
//    Set<Vertex> SR; //SR
//
//    SL = Sets.intersection(sL.getTargets(action), B);
//    SR = sR.getTargets(action);
//
//    for (Vertex LTarget : SL) {
//      List<String> Formulas = new ArrayList<>(); //GAMMA
//
//      for (Vertex RTarget : SR) {
//        Formulas.add(getDeltaFormula(LTarget, RTarget, rootBlock));
//        System.out.println("deltaFormula of "+LTarget +" and "+RTarget+" in Block " +rootBlock);
//      }
//      System.out.println("All SL SR deltaformulas: "+Formulas);
//
//      for (String formula: Formulas) { //for each Phi in Gamma
//        ArrayList<String> otherFormulas = new ArrayList<>(Formulas);
//        otherFormulas.remove(formula);
//
//         if(SR.stream().noneMatch(vertex-> {
//           try {
//             return !Objects.requireNonNull(FormulaParser.parse(formula+"tt")).evaluate(vertex)
//             && otherFormulas.stream().allMatch(otherFormula-> {
//               try {
//                 return Objects.requireNonNull(FormulaParser.parse(otherFormula+"tt")).evaluate(vertex);
//               } catch (SyntaxErrorException | NoMatchingTokenException e) {
//                 throw new RuntimeException(e.getMessage());
//               }
//             });
//           } catch (SyntaxErrorException | NoMatchingTokenException e) {
//             throw new RuntimeException(e.getMessage());
//           }
//         })){
//           Formulas.remove(formula);
//         }
//      }
//
//      if(Formulas.size()<smallest){
//        smallest = Formulas.size();
////        deltaFormula = new StringBuilder();
//        if(Formulas.size()==1) deltaFormula.append(Formulas.get(0));
//        else if(Formulas.size()>1){
//          deltaFormula.append("(").append(Formulas.get(0)).append("tt");
//          for(int i = 1; i<Formulas.size();i++){
//            deltaFormula.append("&").append(Formulas.get(i)).append("tt");
//          }
//          deltaFormula.append(")");
//        }
//
//      }
//    }
//    System.out.println(deltaFormula);
//    return deltaFormula.toString();
//  }
//

}



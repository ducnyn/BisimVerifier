package me.ducanh.thesis.model;

import com.google.common.collect.Sets;
import javafx.util.Pair;
import me.ducanh.thesis.formula.tree.*;

import java.util.*;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toSet;

public class Algorithms {


    //  private static Map<Boolean, Set<Integer>> split(Model model, Set<Integer> blockNode, BlockEdge splitter) {
//    String label = splitter.getLabel();
//    BlockNode targetBlock = splitter.getTargetBlock();
//    return blockNode.stream()
//            .collect(partitioningBy(
//                    vertex -> model.getTargets(vertex, label).stream()
//                            .anyMatch(targetBlock::contains)
//                    , toSet()));
//  }
    public static Map<Boolean, Set<Vertex>> split(BlockNode blockNode, BlockEdge blockEdge) {
//    System.out.println("attempting to split " + blockNode);
        return blockNode.stream()
                .collect(partitioningBy(
                        vertex -> vertex.getTargets(blockEdge.getLabel())
                                .stream()
                                .anyMatch(blockEdge.getTargetBlock()::contains)
                        , toSet()));

    }


    public static Pair<BlockNode, Set<BlockNode>> bisim(Set<Vertex> vertices) {
        HashMap<Vertex, BlockNode> containingBlock = new HashMap<>();
        Set<BlockNode> newPartition = new HashSet<>();
        Set<BlockNode> parentPartition = new HashSet<>();
        BlockNode rootBlockNode = new BlockNode(vertices);
        newPartition.add(rootBlockNode);


        while (!newPartition.equals(parentPartition)) {
            System.out.println("current Partition: " + newPartition);
            parentPartition = Set.copyOf(newPartition);
            newPartition = new HashSet<>();

            for (BlockNode blockNode : parentPartition) {
                for (Vertex vertex : blockNode) {
                    containingBlock.put(vertex, blockNode);
                }
            }

            for (BlockNode blockNode : parentPartition) {

                Optional<BlockEdge> splitter =
                        blockNode.stream()
                                .flatMap(vertex -> vertex.getEdges().stream())
                                .map(edge -> new BlockEdge(edge.getLabel(), containingBlock.get(edge.getTarget())))
                                .filter(bEdge -> !split(blockNode, bEdge).containsValue(blockNode.getVertices()))
                                .findAny();

                if (splitter.isPresent()) {
                    Map<Boolean, Set<Vertex>> splitBlock = split(blockNode, splitter.get());
                    blockNode.setSplitter(splitter.get());
                    blockNode.setLeftChild(new BlockNode(splitBlock.get(true)));
                    blockNode.setRightChild(new BlockNode(splitBlock.get(false)));
                    newPartition.addAll(List.of(blockNode.left(), blockNode.right()));
//          System.out.println("\tblockNode "+blockNode+" split by " + blockNode.getSplitter());
//          System.out.println("\tinto " + blockNode.left() + " and " + blockNode.right());b
                } else {
                    newPartition.add(blockNode);
                }
            }
        }
//    System.out.println("Final partition(bisimilar sets): " + newPartition);
        return new Pair<>(rootBlockNode, newPartition);
    }


    private static BlockNode findLastBlock(Vertex state1, Vertex state2, BlockNode root) throws NoDistinguishingFormulaException {
        BlockNode blockNode = root;

        while (true) {
            if (blockNode.getSplitter() == null) {
                return blockNode;
            }
            if (blockNode.left().containsAll(state1, state2)) {
                blockNode = blockNode.left();
            } else if (blockNode.right().containsAll(state1, state2)) {
                blockNode = blockNode.right();
            } else {
                return blockNode; //neither left or right contains all -> next split will separate
            }
        }

    }

    public static TreeNode getDeltaFormula(Vertex state1, Vertex state2, Set<Vertex> vertexSet) throws NoDistinguishingFormulaException {

        BlockNode rootBlockNode = bisim(vertexSet).getKey();

        TreeNode result = clevelandAlgo(state1,state2, rootBlockNode);
        System.out.println(result);
        return result;
    }

    private static TreeNode clevelandAlgo(Vertex state1, Vertex state2, BlockNode rootBlockNode) throws NoDistinguishingFormulaException {

        BlockNode lastBlockNode = findLastBlock(state1, state2, rootBlockNode);
        if (lastBlockNode.getSplitter() == null) {
            throw new NoDistinguishingFormulaException(
                    "The states are bisimilar. No formula distinguishes them.");
        }
        if(!(rootBlockNode.contains(state1) & rootBlockNode.contains(state2))){
            throw new RuntimeException("one or more of the compared states does not exist in the given set of vertices");
        }

        BlockEdge splitter = lastBlockNode.getSplitter();
        String splitAction = splitter.getLabel();
        Set<Vertex> splitBlock = splitter.getTargetBlock().getVertices();

        Vertex leftState;
        Vertex rightState;
        boolean reversed;

        if (lastBlockNode.left().contains(state1) & lastBlockNode.right().contains(state2)) {
            leftState = state1;
            rightState = state2;
            reversed = false;
        } else if(lastBlockNode.left().contains(state2) & lastBlockNode.right().contains(state1)){
            leftState = state2;
            rightState = state1;
            reversed = true;
        } else { //Test should never throw this
            throw new RuntimeException("state 1 and 2 are not split or not contained in lastBlockNode.");
        }


        Set<Vertex> SL = Sets.intersection(leftState.getTargets(splitAction), splitBlock);
        Set<Vertex> SR = rightState.getTargets(splitAction);
        int minFormulaSize = Integer.MAX_VALUE;
        TreeNode nextNode = new TrueNode();
//        TreeNode result = new DiamondNode(splitAction,nextNode);

        for (Vertex LTarget : SL) {
            List<TreeNode> Formulas = new ArrayList<>(); //GAMMA

            for (Vertex RTarget : SR) {
                Formulas.add(clevelandAlgo(LTarget, RTarget, rootBlockNode));
            }

            for (TreeNode formula : Formulas) {
                ArrayList<TreeNode> otherFormulas = new ArrayList<>(Formulas);
                otherFormulas.remove(formula);

                if (SR.stream().noneMatch(vertex -> (!formula.evaluate(vertex)
                        && otherFormulas.stream().allMatch(otherFormula -> otherFormula.evaluate(vertex))))) {
                    Formulas.remove(formula);
                }
            }

            if (Formulas.size() < minFormulaSize) {
                minFormulaSize = Formulas.size();
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
        TreeNode result = new DiamondNode(splitAction,nextNode);
        return reversed? new NotNode(result) : result;
    } //TEST IT AGAIN


    //computes a minimalistic formula satisfied by v1, but not v2. Both formulas satisfy the formula up until the second last vertex of the described path.
//  public static String getDeltaFormula(Vertex s1, Vertex s2, BlockNode rootBlock) throws NoDistinguishingFormulaException {
//    BlockNode currentBlock = rootBlock;
//    StringBuilder deltaFormula = new StringBuilder();
//
//    //base case
//    System.out.println("\n\nComparing " + s1 + " with " + s2);
//    while (true) {
//      System.out.println("current BlockNode is " + currentBlock);
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
//        System.out.println("deltaFormula of "+LTarget +" and "+RTarget+" in BlockNode " +rootBlock);
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



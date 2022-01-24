package me.ducanh.thesis.algorithms;

import com.google.common.collect.Sets;
import javafx.util.Pair;
import me.ducanh.thesis.Block;
import me.ducanh.thesis.BlockEdge;
import me.ducanh.thesis.Vertex;
import me.ducanh.thesis.formula.*;

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

    //True = states that have an action a, leading to a state in B' (= leading to the same state based on current bisimulation refinement)
    //False = the rest.
    public static Map<Boolean, Set<Vertex>> split(me.ducanh.thesis.Block block, BlockEdge blockEdge) {
//    System.out.println("attempting to split " + block);
        return block.stream()
                .collect(partitioningBy(
                        vertex -> vertex.getTargets(blockEdge.getLabel())
                                .stream()
                                .anyMatch(blockEdge.getTargetBlock()::contains)
                        , toSet()));

    }

    public static Pair<me.ducanh.thesis.Block, Set<Block>> bisim(Set<Vertex> vertices) {
        HashMap<Vertex, me.ducanh.thesis.Block> containingBlock = new HashMap<>();
        Set<me.ducanh.thesis.Block> newPartition = new HashSet<>();
        Set<me.ducanh.thesis.Block> parentPartition = new HashSet<>();
        me.ducanh.thesis.Block rootBlock = new me.ducanh.thesis.Block(vertices);
        newPartition.add(rootBlock);


        while (!newPartition.equals(parentPartition)) { //while partions can still be refined
            System.out.println("current Partition: " + newPartition);
            parentPartition = Set.copyOf(newPartition);
            newPartition = new HashSet<>();

            for (me.ducanh.thesis.Block block : parentPartition) {
                for (Vertex vertex : block) {
                    containingBlock.put(vertex, block);
                }
            }

            for (me.ducanh.thesis.Block block : parentPartition) {

                Optional<BlockEdge> possibleSplitter =
                        block.stream()
                                .flatMap(vertex -> vertex.getEdges().stream())
                                .map(edge -> new BlockEdge(edge.getLabel(), containingBlock.get(edge.getTarget())))
                                .filter(bEdge -> !split(block, bEdge).containsValue(block.getVertices()))
                                .findFirst();

                if (possibleSplitter.isPresent()) {
                    BlockEdge splitter = possibleSplitter.get();
                    Map<Boolean, Set<Vertex>> dividedBlock = split(block, splitter);
                    block.setSplitter(splitter);
                    block.setLeftChild(new me.ducanh.thesis.Block(dividedBlock.get(true)));
                    block.setRightChild(new me.ducanh.thesis.Block(dividedBlock.get(false)));
                    newPartition.addAll(List.of(block.left(), block.right()));
                    System.out.println("\tblock "+ block +" split by " + block.getSplitter());
                    System.out.println("\tinto " + block.left() + " and " + block.right());
                } else {
                    newPartition.add(block);
                    System.out.println("\tblock " + block + " is not split");
                }
            }
        }
//    System.out.println("Final partition(bisimilar sets): " + newPartition);
        return new Pair<>(rootBlock, newPartition);
    }


    private static me.ducanh.thesis.Block findLastBlock(Vertex state1, Vertex state2, me.ducanh.thesis.Block root) throws NoDistinguishingFormulaException {
        me.ducanh.thesis.Block block = root;

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

    public static TreeNode getDeltaFormula(Vertex state1, Vertex state2, Set<Vertex> vertexSet) throws NoDistinguishingFormulaException {

        me.ducanh.thesis.Block rootBlock = bisim(vertexSet).getKey();

        TreeNode result = clevelandAlgo(state1,state2, rootBlock);
        System.out.println(result);
        return result;
    }

    private static TreeNode clevelandAlgo(Vertex state1, Vertex state2, me.ducanh.thesis.Block rootBlock) throws NoDistinguishingFormulaException {

        me.ducanh.thesis.Block lastBlock = findLastBlock(state1, state2, rootBlock);
        if (lastBlock.getSplitter() == null) {
            throw new NoDistinguishingFormulaException(
                    "The states are bisimilar. No formula distinguishes them.");
        }
        if(!(rootBlock.contains(state1) & rootBlock.contains(state2))){
            throw new RuntimeException("one or more of the compared states does not exist in the given set of vertices");
        }

        BlockEdge splitter = lastBlock.getSplitter();
        String splitAction = splitter.getLabel();
        Set<Vertex> splitBlock = splitter.getTargetBlock().getVertices();

        Vertex leftState;
        Vertex rightState;
        boolean reversed;

        if (lastBlock.left().contains(state1) & lastBlock.right().contains(state2)) {
            leftState = state1;
            rightState = state2;
            reversed = false;
        } else if(lastBlock.left().contains(state2) & lastBlock.right().contains(state1)){
            leftState = state2;
            rightState = state1;
            reversed = true;
        } else { //Test should never throw this
            throw new RuntimeException("state 1 and 2 are not split or not contained in lastBlock.");
        }


        Set<Vertex> SL = Sets.intersection(leftState.getTargets(splitAction), splitBlock);
        Set<Vertex> SR = rightState.getTargets(splitAction);
        int minFormulaSize = Integer.MAX_VALUE;
        TreeNode nextNode = new TrueNode();
//        TreeNode result = new DiamondNode(splitAction,nextNode);

        for (Vertex LTarget : SL) {
            List<TreeNode> formulas = new ArrayList<>(); //GAMMA

            for (Vertex RTarget : SR) {
                formulas.add(clevelandAlgo(LTarget, RTarget, rootBlock));
            }

            for (TreeNode formula : formulas) {
                ArrayList<TreeNode> otherFormulas = new ArrayList<>(formulas);
                otherFormulas.remove(formula);
                System.out.println("All formulas for " + LTarget + formulas);

                if (SR.stream().noneMatch(vertex -> (!formula.evaluate(vertex)
                        && otherFormulas.stream().allMatch(otherFormula -> otherFormula.evaluate(vertex))))) {
                    formulas.remove(formula);
                    System.out.println("Formula removed: " + formula);
                }
            }

            if (formulas.size() < minFormulaSize) {
                minFormulaSize = formulas.size();
                if (formulas.size() == 1) {
                    nextNode = formulas.get(0);
                } else if (formulas.size() > 1) {
                    nextNode = formulas.get(0);
                    for (int i = 1; i < formulas.size(); i++) {
                        nextNode = new AndNode(nextNode, formulas.get(i));
                    }
                }
            }
        }
        TreeNode result = new DiamondNode(splitAction,nextNode);
        return reversed? new NotNode(result) : result;
    } //TEST IT AGAIN


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



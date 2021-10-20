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

    //True = states that have an action a, leading to a state in B' (= leading to the same state based on current bisimulation refinement)
    //False = the rest.
    public static Map<Boolean, Set<CustomVertex>> split(Block block, BlockEdge blockEdge) {
//    System.out.println("attempting to split " + block);
        return block.stream()
                .collect(partitioningBy(
                        vertex -> vertex.getTargets(blockEdge.getLabel())
                                .stream()
                                .anyMatch(blockEdge.getTargetBlock()::contains)
                        , toSet()));

    }

    public static Pair<Block, Set<Block>> bisim(Set<CustomVertex> vertices) {
        HashMap<CustomVertex, Block> containingBlock = new HashMap<>();
        Set<Block> newPartition = new HashSet<>();
        Set<Block> parentPartition = new HashSet<>();
        Block rootBlock = new Block(vertices);
        newPartition.add(rootBlock);


        while (!newPartition.equals(parentPartition)) { //while partions can still be refined
            System.out.println("current Partition: " + newPartition);
            parentPartition = Set.copyOf(newPartition);
            newPartition = new HashSet<>();

            for (Block block : parentPartition) {
                for (CustomVertex vertex : block) {
                    containingBlock.put(vertex, block);
                }
            }

            for (Block block : parentPartition) {

                Optional<BlockEdge> possibleSplitter =
                        block.stream()
                                .flatMap(vertex -> vertex.getEdges().stream())
                                .map(edge -> new BlockEdge(edge.getLabel(), containingBlock.get(edge.getTarget())))
                                .filter(bEdge -> !split(block, bEdge).containsValue(block.getVertices()))
                                .findFirst();

                if (possibleSplitter.isPresent()) {
                    BlockEdge splitter = possibleSplitter.get();
                    Map<Boolean, Set<CustomVertex>> dividedBlock = split(block, splitter);
                    block.setSplitter(splitter);
                    block.setLeftChild(new Block(dividedBlock.get(true)));
                    block.setRightChild(new Block(dividedBlock.get(false)));
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


    private static Block findLastBlock(CustomVertex state1, CustomVertex state2, Block root) throws NoDistinguishingFormulaException {
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

    public static TreeNode getDeltaFormula(CustomVertex state1, CustomVertex state2, Set<CustomVertex> vertexSet) throws NoDistinguishingFormulaException {

        Block rootBlock = bisim(vertexSet).getKey();

        TreeNode result = clevelandAlgo(state1,state2, rootBlock);
        System.out.println(result);
        return result;
    }

    private static TreeNode clevelandAlgo(CustomVertex state1, CustomVertex state2, Block rootBlock) throws NoDistinguishingFormulaException {

        Block lastBlock = findLastBlock(state1, state2, rootBlock);
        if (lastBlock.getSplitter() == null) {
            throw new NoDistinguishingFormulaException(
                    "The states are bisimilar. No formula distinguishes them.");
        }
        if(!(rootBlock.contains(state1) & rootBlock.contains(state2))){
            throw new RuntimeException("one or more of the compared states does not exist in the given set of vertices");
        }

        BlockEdge splitter = lastBlock.getSplitter();
        String splitAction = splitter.getLabel();
        Set<CustomVertex> splitBlock = splitter.getTargetBlock().getVertices();

        CustomVertex leftState;
        CustomVertex rightState;
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


        Set<CustomVertex> SL = Sets.intersection(leftState.getTargets(splitAction), splitBlock);
        Set<CustomVertex> SR = rightState.getTargets(splitAction);
        int minFormulaSize = Integer.MAX_VALUE;
        TreeNode nextNode = new TrueNode();
//        TreeNode result = new DiamondNode(splitAction,nextNode);

        for (CustomVertex LTarget : SL) {
            List<TreeNode> formulas = new ArrayList<>(); //GAMMA

            for (CustomVertex RTarget : SR) {
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
//  public static String getDeltaFormula(CustomVertex s1, CustomVertex s2, Block rootBlock) throws NoDistinguishingFormulaException {
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
//      System.out.println("CustomVertex 1 = " +s1);
//      System.out.println("CustomVertex 2 = " +s2);
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
//    Set<CustomVertex> B = currentBlock.getSplitter().getTargetBlock().getVertices();
//
//    CustomVertex sL;
//    CustomVertex sR;
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
//    Set<CustomVertex> SL; //SL
//    Set<CustomVertex> SR; //SR
//
//    SL = Sets.intersection(sL.getTargets(action), B);
//    SR = sR.getTargets(action);
//
//    for (CustomVertex LTarget : SL) {
//      List<String> Formulas = new ArrayList<>(); //GAMMA
//
//      for (CustomVertex RTarget : SR) {
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



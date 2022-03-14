package me.ducanh.thesis.algorithms;

import com.google.common.collect.Sets;
import javafx.util.Pair;
import me.ducanh.thesis.*;
import me.ducanh.thesis.formula.*;

import java.util.*;

import static java.util.stream.Collectors.partitioningBy;

public class Algorithms  {



    //True = states that have an action a, leading to a state in B' (= leading to the same state based on current bisimulation refinement)
    //False = the rest.
    public Block split(Block block, BlockEdge blockEdge, Model graph) {
//    System.out.println("attempting to split " + block);

//        Map<Boolean,Set<Vertex>> subBlocks =
//                block.stream()
//                .collect(Collectors.partitioningBy(
//                        vertex -> graph.getEdges(vertex).stream()
//                                .anyMatch(edge-> edge.getLabel().equals(partitionEdge.getLabel())
//                                                && partitionEdge.getTargetBlock().contains(edge.getTarget()))
//                        , Collectors.toSet()
//                ));
//        if (subBlocks.get(true).isEmpty() || subBlocks.get(false).isEmpty()) return block;
//        else return new Block(block)
//                .setSplitter(blockEdge)
//                .setLeftChild(new Block(subBlocks.get(true)))
//                .setRightChild(new Block(subBlocks.get(false)));
//
        Block hasEqualEdge = new Block();
        Block hasNoEqualEdge = new Block();
        for (Vertex vertex : block) {
            if(hasEquivalentEdges(graph.getEdges(vertex), blockEdge))
                hasEqualEdge.add(vertex);
            else
                hasNoEqualEdge.add(vertex);
        }
        if (hasEqualEdge.isEmpty() || hasNoEqualEdge.isEmpty()) return block;
        else return new Block(block)
                .setSplitter(blockEdge)
                .setLeftChild(hasEqualEdge)
                .setRightChild(hasNoEqualEdge);
    }
    private boolean hasEquivalentEdges(Set<Edge> edges, BlockEdge blockEdge) { //iterate through edges
        for (Edge edge : edges) {
            if(blockEdge.getLabel().equals(edge.getLabel()) && blockEdge.getTargetBlock().contains(edge.getTarget()))
                return true;
        }
        return false;
    }

//    private Block getCurrentBlockOf(Vertex vertex, List<Block> currentPartition){
//        for(Block block : currentPartition){
//            if (block.contains(vertex)) return block;
//        }
//        return null;
//    }
    private Pair<Block, List<Block>> bisim(Model graph) {

        HashMap<Vertex, Block> currentBlockOf = new HashMap<>();
        List<Block> newPartition = new ArrayList<>();
        List<Block> partition = new ArrayList<>();
        Block rootBlock = new Block(graph.getVertices());
        newPartition.add(rootBlock);

        while (!newPartition.equals(partition)) { //while partions can still be refined
//            System.out.println("current Partition: " + nextPartitions);
            partition = newPartition;
            newPartition = new ArrayList<>();

            for (Block block : partition) { //iterates every vertex once.
                for (Vertex vertex : block) {
                    currentBlockOf.put(vertex, block);
                }
            }

            for (Block block : partition) {


                Optional<Block> refinedBlock =
                        block.stream()
                                .flatMap(vertex -> graph.getEdges(vertex).stream())
                                .map(edge -> new BlockEdge(edge.getLabel(), currentBlockOf.get(edge.getTarget())))
                                .distinct() // if not used with stream, you could add each bEdge to a hashset and only use new ones.
                                .map(pEdge -> split(block, pEdge,graph))
                                .filter(Block::hasSplitter)
                                .findAny();

                if (refinedBlock.isPresent()) {
                    block.setLeftChild(refinedBlock.get().left())
                         .setRightChild(refinedBlock.get().right())
                         .setSplitter(refinedBlock.get().getSplitter());

                    newPartition.addAll(List.of(refinedBlock.get().left(), refinedBlock.get().right()));
//                    System.out.println("\tblock "+ block +" split by " + block.getSplitter());
//                    System.out.println("\tinto " + block.left() + " and " + block.right());
                } else {
                    newPartition.add(block);
//                    System.out.println("\tblock " + block + " is not split");
                }
            }
        }
//    System.out.println("Final partition(bisimilar sets): " + newPartition);
        return new Pair<>(rootBlock, newPartition);
    }


    private Block getLastCommonBlock(Vertex state1, Vertex state2, Block root) {
        Block Block = root;

        while (true) {
            if (!Block.hasSplitter()) {
                return Block;
            }
            if (Block.left().containsAll(state1, state2)) {
                Block = Block.left();
            } else if (Block.right().containsAll(state1, state2)) {
                Block = Block.right();
            } else {
                return Block; //neither left nor right contains all -> next split will separate
            }
        }

    }

    public TreeNode getDeltaFormula(Vertex state1, Vertex state2, Set<Vertex> vertexSet, Map<Vertex,Set<Edge>>graph) throws NoDistinguishingFormulaException {

        Block rootBlock = bisim(getAdjacencyList()).getKey();

        TreeNode result = clevelandAlgo(state1, state2, rootBlock, graph);
        System.out.println(result);
        return result;
    }

    private TreeNode clevelandAlgo(Vertex state1, Vertex state2, Block rootBlock, Map<Vertex,Set<Edge>>graph) throws NoDistinguishingFormulaException {

        Block lastBlock = getLastCommonBlock(state1, state2, rootBlock);
        if (lastBlock.getSplitter() == null) {
            throw new NoDistinguishingFormulaException(
                    "The states are bisimilar. No formula distinguishes them.");
        }
        if (!(rootBlock.contains(state1) & rootBlock.contains(state2))) {
            throw new RuntimeException("one or more of the compared states does not exist in the given set of vertices");
        }

        BlockEdge splitter = lastBlock.getSplitter();
        String splitAction = splitter.getLabel();
        Set<Vertex> splitBlock = new HashSet<>((splitter.getTargetBlock()));

        Vertex leftState;
        Vertex rightState;
        boolean reversed;

        if (lastBlock.left().contains(state1) & lastBlock.right().contains(state2)) {
            leftState = state1;
            rightState = state2;
            reversed = false;
        } else if (lastBlock.left().contains(state2) & lastBlock.right().contains(state1)) {
            leftState = state2;
            rightState = state1;
            reversed = true;
        } else { //Test should never throw this
            throw new RuntimeException("state 1 and 2 are not split or not contained in lastBlock.");
        }


        Set<Vertex> SL = Sets.intersection(getTargets(leftState,graph, splitAction), splitBlock);
        Set<Vertex> SR = getTargets(rightState,graph,splitAction);
        int minFormulaSize = Integer.MAX_VALUE; //TODO long?
        TreeNode nextNode = new TrueNode();
//        TreeNode result = new DiamondNode(splitAction,nextNode);

        for (Vertex LTarget : SL) {
            List<TreeNode> formulas = new ArrayList<>(); //GAMMA

            for (Vertex RTarget : SR) {
                formulas.add(clevelandAlgo(LTarget, RTarget, rootBlock,graph));
            }

            for (TreeNode formula : formulas) {
                ArrayList<TreeNode> otherFormulas = new ArrayList<>(formulas);
                otherFormulas.remove(formula);
                System.out.println("All formulas for " + LTarget + formulas);

                if (SR.stream().noneMatch(vertex -> (!formula.evaluate(vertex,this )
                        && otherFormulas.stream().allMatch(otherFormula -> otherFormula.evaluate(vertex, this))))) {
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
        TreeNode result = new DiamondNode(splitAction, nextNode);
        return reversed ? new NotNode(result) : result;
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



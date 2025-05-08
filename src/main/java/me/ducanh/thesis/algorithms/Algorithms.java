package me.ducanh.thesis.algorithms;

import com.google.common.collect.Sets;
import javafx.util.Pair;
import me.ducanh.thesis.Edge;
import me.ducanh.thesis.Graph;
import me.ducanh.thesis.Vertex;
import me.ducanh.thesis.formula.*;

import java.util.*;

public class Algorithms {
/*
* Partitions a block into two blocks based on a
* */

    public static boolean hasEdgeEqualToSplitter(Vertex vertex, Graph<Vertex,Edge> graph, Splitter splitter){
        for ( Edge edge : graph.getEdges(vertex) ) {
            if ( splitter.label.equals(edge.label) && splitter.targetBlock.vertices.contains(edge.target) )
                return true;
        }
        return false;
    }
    public static Block split(Block block, Splitter splitter, Graph<Vertex, Edge> graph) {

        Block left = new Block();
        Block right = new Block();
        for ( Vertex vertex : block.vertices ) {
            if(hasEdgeEqualToSplitter(vertex, graph, splitter)){
                left.vertices.add(vertex);
            } else {
                right.vertices.add(vertex);
            }
        }
        Block result = new Block(block.vertices);
        if ( !left.vertices.isEmpty() && !right.vertices.isEmpty() ) {
            result.left = left;
            result.right = right;
            result.splitter = splitter;
        }
        return result;
    }


    public static Pair<Block, List<Block>> partitionByBisimilarity(Graph<Vertex, Edge> graph) {
        HashMap<Vertex, Block> vertexToBlock = new HashMap<>();
        List<Block> newPartition = new ArrayList<>();
        List<Block> partition = new ArrayList<>();
        Block rootBlock = new Block(graph.getVertices());
        newPartition.add(rootBlock);

        while ( !newPartition.equals(partition) ) {
            partition = newPartition;
            newPartition = new ArrayList<>();

            for ( Block block : partition ) {
                for ( Vertex vertex : block.vertices ) {
                    vertexToBlock.put(vertex, block);
                }
            }

            for ( Block block : partition ) {

                Optional<Block> refinedBlock =
                        block.vertices.stream()
                                .flatMap(vertex -> graph.getEdges(vertex).stream())
                                .map(edge -> new Splitter(edge.label, vertexToBlock.get(edge.target)))
                                .distinct() // if not used with stream, you could add each bEdge to a hashset and only use new ones.
                                .map(splitter -> split(block, splitter, graph))
                                .filter(b -> b.splitter != null)
                                .findAny();

                if ( refinedBlock.isPresent() ) {
                    block.left = refinedBlock.get().left;
                    block.right = refinedBlock.get().right;
                    block.splitter = refinedBlock.get().splitter;

                    newPartition.addAll(List.of(refinedBlock.get().left, refinedBlock.get().right));
                } else {
                    newPartition.add(block);
//                    System.out.println("\tblock " + block + " is not split");
                }
            }
        }
//    System.out.println("Final partition(bisimilar sets): " + newPartition);
        return new Pair<>(rootBlock, newPartition);
    }


    private static Block getLastCommonBlock(Vertex state1, Vertex state2, Block root) throws NoCommonBlockException, StateDisappearedDuringPartitioningException {
        Block commonBlock = root;

        if ( !commonBlock.vertices.contains(state1) || !commonBlock.vertices.contains(state2) ) {
            throw new NoCommonBlockException(state1, state2, root);
        }

        while ( true ) {

            if ( commonBlock.left.vertices.contains(state1) && commonBlock.right.vertices.contains(state2) ) {
                return commonBlock; //neither left nor right contains all -> next split will separate
            } else if ( commonBlock.left.vertices.contains(state2) && commonBlock.right.vertices.contains(state1) ) {
                return commonBlock;

            } else if ( commonBlock.left.vertices.contains(state1) && commonBlock.left.vertices.contains(state2) ) {
                commonBlock = commonBlock.left;
            } else if ( commonBlock.right.vertices.contains(state1) && commonBlock.right.vertices.contains(state2) ) {
                commonBlock = commonBlock.right;
            } else {
                throw new StateDisappearedDuringPartitioningException(state1, state2, root);
            }
        }
    }

    public static TreeNode getDistinguishingFormula(Vertex state1, Vertex state2, Graph<Vertex, Edge> graph) throws NoDistinguishingFormulaException, StateDisappearedDuringPartitioningException, NoCommonBlockException {

        Block partitioningTree = partitionByBisimilarity(graph).getKey();

        TreeNode result = clevelandAlgo(state1, state2, partitioningTree, graph);
        System.out.println(result);
        return result;
    }

    private static TreeNode clevelandAlgo(Vertex state1, Vertex state2, Block rootBlock, Graph<Vertex, Edge> graph) throws NoDistinguishingFormulaException, StateDisappearedDuringPartitioningException, NoCommonBlockException {

        Block lastBlock = getLastCommonBlock(state1, state2, rootBlock);
        if ( lastBlock.splitter == null ) {
            throw new NoDistinguishingFormulaException(
                    "The states are bisimilar. No formula distinguishes them.");
        }
        if ( !(rootBlock.vertices.contains(state1) & rootBlock.vertices.contains(state2)) ) {
            throw new RuntimeException("one or more of the compared states does not exist in the given set of vertices");
        }

        Splitter splitter = lastBlock.splitter;
        String splitAction = splitter.label;
        Set<Vertex> splitBlock = new HashSet<>((splitter.targetBlock.vertices));

        Vertex leftState;
        Vertex rightState;
        boolean reversed;

        if ( lastBlock.left.vertices.contains(state1) && lastBlock.right.vertices.contains(state2) ) {
            leftState = state1;
            rightState = state2;
            reversed = false;
        } else if ( lastBlock.left.vertices.contains(state2) && lastBlock.right.vertices.contains(state1) ) {
            leftState = state2;
            rightState = state1;
            reversed = true;
        } else { //Test should never throw this
            throw new RuntimeException("state 1 and 2 are not split or not contained in lastBlock.");
        }


        Collection<Vertex> SL = Sets.intersection(graph.getTargets(leftState, splitAction), splitBlock);
        Set<Vertex> SR = graph.getTargets(rightState, splitAction);
        int minFormulaSize = Integer.MAX_VALUE;
        TreeNode nextNode = new TrueNode();
//        TreeNode result = new DiamondNode(splitAction,nextNode);

        for ( Vertex LTarget : SL ) {
            List<TreeNode> formulas = new ArrayList<>(); //GAMMA

            for ( Vertex RTarget : SR ) {
                formulas.add(clevelandAlgo(LTarget, RTarget, rootBlock, graph));
            }

            for ( TreeNode formula : formulas ) {
                ArrayList<TreeNode> otherFormulas = new ArrayList<>(formulas);
                otherFormulas.remove(formula);
                System.out.println("All formulas for " + LTarget + formulas);

                if ( SR.stream().noneMatch(vertex -> (!formula.evaluate(vertex, graph)
                        && otherFormulas.stream().allMatch(otherFormula -> otherFormula.evaluate(vertex, graph)))) ) {
                    formulas.remove(formula);
                    System.out.println("Formula removed: " + formula);
                }
            }

            if ( formulas.size() < minFormulaSize ) {
                minFormulaSize = formulas.size();
                if ( formulas.size() == 1 ) {
                    nextNode = formulas.get(0);
                } else if ( formulas.size() > 1 ) {
                    nextNode = formulas.get(0);
                    for ( int i = 1; i < formulas.size(); i++ ) {
                        nextNode = new AndNode(nextNode, formulas.get(i));
                    }
                }
            }
        }
//        TODO HOW ABOUT RETURNING LIST OF FORMULAS AND THEN MINIMIZE IN EXTRA METHOD? (minimize that only works for none null)
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
//      System.out.println("current Splitter is " + currentBlock.splitter);
//      System.out.println("Vertex 1 = " +s1);
//      System.out.println("Vertex 2 = " +s2);
//
//
//      if (currentBlock.splitter == null) {
//        throw new NoDistinguishingFormulaException();
//      }
//
//      if (currentBlock.left().vertices.containsAll(s1, s2)) {
//        currentBlock = currentBlock.left();
//      } else if (currentBlock.right().vertices.containsAll(s1, s2)) {
//        currentBlock = currentBlock.right();
//      } else {
//        break; //neither left or right contains all -> next split will separate
//      }
//    }
//
//    String action = currentBlock.splitter.label;
//    Set<Vertex> B = currentBlock.splitter.targetBlock.getVertices();
//
//    Vertex sL;
//    Vertex sR;
//
//    if (currentBlock.left().vertices.contains(s1)) {
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



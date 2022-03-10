package me.ducanh.thesis.algorithms;

import com.google.common.collect.Sets;
import javafx.util.Pair;
import me.ducanh.thesis.*;
import me.ducanh.thesis.formula.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;

public class Algorithms extends Model {





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



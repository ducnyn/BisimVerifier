package me.ducanh.thesis.formula;

import me.ducanh.thesis.formula.tree.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FormulaParser {
    private static Token currentToken;
    private static Iterator<Token> iter;
    private static final Set<TokenType> connectiveTokens = Set.of(TokenType.AND, TokenType.OR);
    private static final Set<TokenType> modalTokens = Set.of(TokenType.DIAMOND, TokenType.BLOCK, TokenType.FALSE, TokenType.TRUE);


    private static void iterate() {
        currentToken = iter.next();
    }

    public static TreeNode parse(List<Token> tokenList) {
        iter = tokenList.iterator();
        TreeNode result;

        if (!iter.hasNext()) {
            return null;
        }

        result = parseConnective();

        if (iter.hasNext()) { // shold be finished after parsing the whole expression
            throw new RuntimeException("Invalid Syntax");
        }
        return result;
    }

    private static TreeNode parseConnective() {
        TreeNode result = parseModal(); //get first modal
        while (currentToken != null && connectiveTokens.contains(currentToken.getType())) {
            if (currentToken.getType() == TokenType.AND) {
                iterate(); //iterate to get to next modal
                result = new AndNode(result, parseModal());
            } else {
                iterate();
                new OrNode(result, parseModal());
            }
        }

        return result;
    }

    private static TreeNode parseModal() {
        TreeNode result; //get first modal
        String action = currentToken.getValue();
        switch (currentToken.getType()) {
            case BLOCK:
                iterate();
                result = new BlockNode(action, parseModal());
                break;
            case DIAMOND:
                iterate();
                result = new DiamondNode(action, parseModal());
                break;
            case TRUE:
                iterate();
                result = new TrueNode();
                break;
            case FALSE:
                iterate();
                result = new FalseNode();
                break;
            default:
                result = null;
        }
        return result;
    }
}

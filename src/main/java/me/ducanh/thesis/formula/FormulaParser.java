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
        if (iter.hasNext()){
            currentToken = iter.next();
        } else {
            currentToken = null;
        }
    }

    public static TreeNode parse(List<Token> tokenList) throws SyntaxErrorException {
        iter = tokenList.iterator();
        TreeNode result;

        if (!iter.hasNext()) {
            return null;
        }
        iterate();
        result = parseConnective();

        if (iter.hasNext()) { // should be finished after parsing the whole expression
            throw new SyntaxErrorException("Expected: Connective operator. Received: "+ iter.next());
        }
        return result;
    }

    private static TreeNode parseConnective() throws SyntaxErrorException{
        TreeNode result = parseModal(); //get first modal
        while (currentToken != null && connectiveTokens.contains(currentToken.getType())) {
            if(currentToken.getType() == TokenType.LEFTPAR){
                    iterate();
                    result = parseConnective();
                    if(currentToken.getType()!=TokenType.RIGHTPAR){
                        throw new SyntaxErrorException("Missing right parenthesis for >("+result);
                    } //testCase with several missing parentheses should return the deepest missing one
                    break;
            } else if (currentToken.getType() == TokenType.AND) {
                iterate(); //iterate to get to next modal
                result = new AndNode(result, parseModal());
            } else {
                iterate();
                new OrNode(result, parseModal());
            }
        }
        return result;
    }

    private static TreeNode parseModal() throws SyntaxErrorException{
        TreeNode result; //get first modal
        String action = currentToken.getValue();
        switch (currentToken.getType()) {
            case LEFTPAR:
                iterate();

                result = parseConnective();
                if(currentToken.getType()!=TokenType.RIGHTPAR){
                    throw new SyntaxErrorException("Missing right parenthesis for >("+result);
                } //testCase with several missing parentheses should return the deepest missing one
                break;
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
                throw new SyntaxErrorException("Modal formula expected, starting with <,[,tt,ff or (. Instead received : "+currentToken.getType());
        }
        return result;
    }
}

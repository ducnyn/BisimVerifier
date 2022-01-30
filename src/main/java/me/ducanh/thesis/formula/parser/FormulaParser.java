package me.ducanh.thesis.formula.parser;

import me.ducanh.thesis.formula.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class  FormulaParser {
    private static Token currentToken;
    private static Iterator<Token> iter;
    private static final Set<TokenType> connectiveTokens = Set.of(TokenType.AND, TokenType.OR);
    private static final Set<TokenType> formulaTokens = Set.of(TokenType.DIAMOND, TokenType.BLOCK, TokenType.FALSE, TokenType.TRUE, TokenType.LEFTPAR);


    private static Token iterate() {
        if (iter.hasNext()){
            return currentToken = iter.next();
        } else {
            return currentToken = null;
        }
    }

    public static TreeNode parse(String formulaString) throws SyntaxErrorException, NoMatchingTokenException {
        List<Token> tokenList = FormulaLexer.generateTokenList(formulaString);
        iter = tokenList.iterator();
        TreeNode result;

        if (!iter.hasNext()) {
            return null;
        }
        iterate();
        result = parseConnective();

        if (currentToken.getType()!=TokenType.EOL) { // should be finished after parsing the whole expression
            throw new SyntaxErrorException("Expected: Connective operator or End of Line. Received: "+ currentToken);
        }
        return result;
    }

    private static TreeNode parseConnective() throws SyntaxErrorException{
        TreeNode result = parseModal(); //get first modal
        while (currentToken != null && connectiveTokens.contains(currentToken.getType())) {
                if (currentToken.getType() == TokenType.AND) {
                iterate(); //iterate to get to next modal
                result = new AndNode(result, parseModal());
            } else {
                iterate();
                result = new OrNode(result, parseModal());
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
                if(currentToken.getType()==TokenType.RIGHTPAR){
                    throw new SyntaxErrorException("Empty parantheses");
                }
                result = parseConnective();
                if(currentToken.getType()!=TokenType.RIGHTPAR){
                    throw new SyntaxErrorException("Missing right parenthesis for ("+result);
                } //testCase with several missing parentheses should return the deepest missing one
                iterate();
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
            case NOT:
                iterate();
                result = new NotNode(parseModal());
                break;
            default:
                throw new SyntaxErrorException("Modal formula expected, starting with <,[,tt,ff or (. Instead received : "+currentToken.getType());
        }
        return result;
    }
}

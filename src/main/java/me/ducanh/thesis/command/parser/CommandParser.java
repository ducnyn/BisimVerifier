package me.ducanh.thesis.command.parser;



import me.ducanh.thesis.command.Command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Before calling a parsing method, iterate so that the method can assume the currentToken is of the return type.
public class  CommandParser {
    private static CommandToken currentToken;
    private static Iterator<CommandToken> iter;

    private static CommandToken iterate() {
        if (iter.hasNext()){
            return currentToken = iter.next();
        } else {
            return currentToken = null;
        }
    }

    public static ArrayList<Command> parse(String formulaString) throws SyntaxErrorException, NoMatchingTokenException {
        List<CommandToken> tokenList = CommandLexer.generateTokenList(formulaString);
        System.out.println("CommandParser.parse() : the generated Tokenlist is : " + tokenList);
        ArrayList<Command> methodList = new ArrayList<>();
        iter = tokenList.iterator();

        if (iter.hasNext()) {
            iterate();
            do{methodList.add(parseCommand());}
            while(currentToken.getType()!=TokenType.EOL);
        }
        return methodList;
    }

    private static Command parseCommand() throws SyntaxErrorException{
        System.out.println("start CoommandParser.parseMethod()");
        ArrayList<String> argumentList;
        String methodName;
            if (currentToken.getType()==TokenType.WORD){
                methodName = currentToken.getValue();
                String word = currentToken.getValue();
                iterate();
                if(currentToken.getType()==TokenType.LEFTPAR){
                    iterate();
                    argumentList = parseArgumentList();
                    if(currentToken.getType()==TokenType.RIGHTPAR){
                        iterate();
                        if(currentToken.getType()==TokenType.SEMICOLON){
                            iterate();
                            return new Command(methodName,argumentList);
                        } else throw new SyntaxErrorException("\";\" expected.");
                    } else throw new SyntaxErrorException("\")\" expected.");
                }
                else{
                    throw new SyntaxErrorException(word+" is not a method call.");
                }


            } else if (currentToken.getType()==TokenType.EOL){
                throw new SyntaxErrorException("empty tokenList");
            } else throw new SyntaxErrorException("method call expected.");
    }

    private static ArrayList<String> parseArgumentList() throws SyntaxErrorException {
        System.out.println("start CommandParser.parseArgumentList()");

        ArrayList<String> argumentList = new ArrayList<>();

        if (currentToken.getType()==TokenType.RIGHTPAR) return argumentList;
        else argumentList.add(parseArgument());

        while(currentToken.getType()==TokenType.COMMA){
            iterate();
            argumentList.add(parseArgument());
        }
        System.out.println("CommandParser.parseArgumentList output is: " + argumentList);
        return argumentList;

    }

    private static String parseArgument() throws SyntaxErrorException {
        System.out.println("start CommandParser.parseArgument(), currentToken =" + currentToken.getType());
        String argument = currentToken.getValue();

        if(currentToken.getType()==TokenType.NUMBER || currentToken.getType() == TokenType.WORD) {

            iterate();
            System.out.println("CommandParser.parseArgument output is: " + argument);

            return argument;
        } else {
            throw new SyntaxErrorException(argument+" is not a valid method parameter.");
        }
    }
}

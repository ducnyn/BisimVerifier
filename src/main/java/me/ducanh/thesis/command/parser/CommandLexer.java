package me.ducanh.thesis.command.parser;


import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandLexer {

    static Set<Character> whiteSpace = Set.of(' ', '\n', '\t');


    private static String getEnclosedSubString(char stringCloser, StringCharacterIterator iter) {
        StringBuilder actionBuilder = new StringBuilder();
        if (iter.next() == stringCloser) {
            throw new RuntimeException("Missing action identifier, immediate stringCloser '" + stringCloser + "'");
        }
        while (iter.current() != stringCloser) {
            actionBuilder.append(iter.current());
            iter.next();
        }
        return actionBuilder.toString();
    }

    public static List<CommandToken> generateTokenList(String commandString) throws NoMatchingTokenException {
        List<CommandToken> tokenList = new ArrayList<>();
        StringCharacterIterator iter = new StringCharacterIterator(commandString);
        while (iter.current() != CharacterIterator.DONE) {
            if (iter.current() == '(') {
                tokenList.add(new CommandToken(TokenType.LEFTPAR));

            } else if (iter.current() == ')') {
                tokenList.add(new CommandToken(TokenType.RIGHTPAR));

            } else if (iter.current() == ';') {
                tokenList.add(new CommandToken(TokenType.SEMICOLON));

            } else if (iter.current() == ',') {
                tokenList.add(new CommandToken(TokenType.COMMA));

            }else if (Character.isLetterOrDigit(iter.current())|| iter.current() == '.') {
                StringBuilder wordBuilder = new StringBuilder();
                while (Character.isLetterOrDigit(iter.current()) || iter.current() == '.') {
                    wordBuilder.append(iter.current());
                    iter.next();
                }
                tokenList.add(new CommandToken(TokenType.WORD,wordBuilder.toString()));
                continue;

            } else if (!whiteSpace.contains(iter.current())) {
                StringBuilder undefined = new StringBuilder(String.valueOf(iter.current()));
                while (iter.current() != CharacterIterator.DONE) {
                    undefined.append(iter.next());
                }
                throw new NoMatchingTokenException("undefined character sequence: " + undefined);
            }
            iter.next();
        }
        tokenList.add(new CommandToken(TokenType.EOL, "End of Line"));
        return tokenList;
    }
}

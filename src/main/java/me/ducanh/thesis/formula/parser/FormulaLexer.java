package me.ducanh.thesis.formula.parser;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FormulaLexer {

  static Set<Character> whiteSpace = Set.of(' ', '\n','\t');


  private static String getEnclosedSubString(char stringCloser, StringCharacterIterator iter){
    StringBuilder actionBuilder = new StringBuilder();
    if(iter.next()==stringCloser){
      throw new RuntimeException("Missing action identifier, immediate stringCloser '"+stringCloser+"'");
    }
    while(iter.current() != stringCloser){
      actionBuilder.append(iter.current());
      iter.next();
    }
    return actionBuilder.toString();
  }

  public static List<FormulaToken> generateTokenList(String formulaString) throws NoMatchingTokenException {
    List<FormulaToken> tokenList = new ArrayList<>();
    StringCharacterIterator iter = new StringCharacterIterator(formulaString);
    while(iter.current()!=CharacterIterator.DONE){
      if (iter.current() == '<') {
        tokenList.add(new FormulaToken(TokenType.DIAMOND,getEnclosedSubString('>',iter)));
      } else if (iter.current() == '[') {
        tokenList.add(new FormulaToken(TokenType.BLOCK,getEnclosedSubString(']',iter)));
      } else if (iter.current() == '&'){
        tokenList.add(new FormulaToken(TokenType.AND));
      } else if (iter.current() == '|') {
        tokenList.add(new FormulaToken(TokenType.OR));
      } else if (iter.current() == '!') {
        tokenList.add(new FormulaToken(TokenType.NOT));
      } else if (iter.current() == '('){
        tokenList.add(new FormulaToken(TokenType.LEFTPAR));
      } else if (iter.current() == ')'){
        tokenList.add(new FormulaToken(TokenType.RIGHTPAR));
      } else if (iter.current() == 't') {
        if (iter.next()=='t'){
          tokenList.add(new FormulaToken(TokenType.TRUE));
        } else {
          throw new NoMatchingTokenException("undefined character sequence: t"+iter.current());
//          tokenList.add(new CommandToken(TokenType.ERROR,"undefined character seq t"+iter.current()));
        }
      } else if (iter.current() == 'f') {
        if (iter.next()=='f'){
          tokenList.add(new FormulaToken(TokenType.FALSE));
        } else {
          throw new NoMatchingTokenException("undefined character sequence: f"+iter.current());
//          tokenList.add(new CommandToken(TokenType.ERROR,"undefined character seq f"+iter.current()));
        }
      } else if (!whiteSpace.contains(iter.current())){
        StringBuilder undefined = new StringBuilder(String.valueOf(iter.current()));
        while(iter.current()!=CharacterIterator.DONE){
          undefined.append(iter.next());
        }
        throw new NoMatchingTokenException("undefined character: "+undefined);
      }
      iter.next();
    } tokenList.add(new FormulaToken(TokenType.EOL,"End of Line"));
    return tokenList;
  }
}

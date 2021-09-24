package me.ducanh.thesis.formula;

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

  public static List<Token> generateTokenList(String formulaString){
    List<Token> tokenList = new ArrayList<>();
    StringCharacterIterator iter = new StringCharacterIterator(formulaString);
    while(iter.current()!=CharacterIterator.DONE){
      if (iter.current() == '<') {
        tokenList.add(new Token(TokenType.DIAMOND,getEnclosedSubString('>',iter)));
      } else if (iter.current() == '[') {
        tokenList.add(new Token(TokenType.BLOCK,getEnclosedSubString(']',iter)));
      } else if (iter.current() == '&'){
        tokenList.add(new Token(TokenType.AND));
      } else if (iter.current() == '|') {
        tokenList.add(new Token(TokenType.OR));
      } else if (iter.current() == '-') {
        tokenList.add(new Token(TokenType.NOT));
      } else if (iter.current() == '('){
        tokenList.add(new Token(TokenType.LEFTPAR));
      } else if (iter.current() == ')'){
        tokenList.add(new Token(TokenType.RIGHTPAR));
      } else if (iter.current() == 't') {
        if (iter.next()=='t'){
          tokenList.add(new Token(TokenType.TRUE));
        } else {
          tokenList.add(new Token(TokenType.ERROR,"undefined character seq t"+iter.current()));
        }
      } else if (iter.current() == 'f') {
        if (iter.next()=='f'){
          tokenList.add(new Token(TokenType.FALSE));
        } else {
          tokenList.add(new Token(TokenType.ERROR,"undefined character seq f"+iter.current()));
        }
      } else if (!whiteSpace.contains(iter.current())){
        tokenList.add(new Token(TokenType.ERROR,"undefined character \"" + iter.current() +"\""));
      }
      iter.next();
    }
    return tokenList;
  }
}

package me.ducanh.thesis.parser;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class FormulaLexer {

  String formulaString;
  CharacterIterator iter;
  Character currentChar;
  Set<Character> whiteSpace = Set.of(' ', '\n','\t');
  List<Token> tokenList = new ArrayList<>();

  public FormulaLexer(String formulaString){
    this.formulaString = formulaString;
    this.iter = new StringCharacterIterator(formulaString);
    currentChar = iter.current();
  }


  public List<Token> generateToken(){
    while(iter.current()!=CharacterIterator.DONE){
      if (whiteSpace.contains(iter.current())){
        iter.next();
      }

      else if (iter.current() == '<') {
        StringBuilder actionBuilder = new StringBuilder();

        if(iter.next()=='>'){
          throw new RuntimeException("Missing action identifier for diamond operator");
        }

        while(iter.current() != '>'){
          actionBuilder.append(iter.current());
          iter.next();
        }
        iter.next();
        tokenList.add(new Token(TokenType.DIAMOND,actionBuilder.toString()));
      }

      else if (iter.current() == '[') {
        StringBuilder actionBuilder = new StringBuilder();

        if(iter.next()==']'){
          throw new RuntimeException("Missing action identifier for block operator");
        }

        while(iter.current() != ']'){
          actionBuilder.append(iter.current());
          iter.next();
        }
        iter.next();
        tokenList.add(new Token(TokenType.BLOCK,actionBuilder.toString()));
      }

      else if (iter.current() == '&'){
        tokenList.add(new Token(TokenType.AND));
        iter.next();
      } else if (iter.current() == '|') {
        tokenList.add(new Token(TokenType.OR));
        iter.next();
      } else if (iter.current() == '-') {
        tokenList.add(new Token(TokenType.NOT));
        iter.next();

      } else if (iter.current() == '('){
        tokenList.add(new Token(TokenType.LEFTPAR));
        iter.next();

      }else if (iter.current() == ')'){
        tokenList.add(new Token(TokenType.RIGHTPAR));
        iter.next();

      }else if (iter.current() == 't') {
        if (iter.next()=='t'){
          tokenList.add(new Token(TokenType.TRUE));
          iter.next();
        } else {
          throw new RuntimeException("Syntax Error: Did you mean tt?");
        }
      } else if (iter.current() == 'f') {
        if (iter.next()=='f'){
          tokenList.add(new Token(TokenType.FALSE));
          iter.next();

        } else {
          throw new RuntimeException("Syntax Error: Did you mean ff?");
        }
      }

      else {
        throw new RuntimeException("Syntax Error");
      }
    }
    return tokenList;
  }
}

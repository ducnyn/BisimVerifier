package me.ducanh.thesis.parser;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Lexer {

  String formulaString;
  CharacterIterator iter;
  Character currentChar;
  Set<Character> whiteSpace = Set.of(' ', '\n','\t');
  List<>
  public Lexer(String formulaString){
    this.formulaString = formulaString;
    this.iter = new StringCharacterIterator(formulaString);
    currentChar = iter.current();
  }


  public List<Token> generateToken(){
    while(iter.current()!=CharacterIterator.DONE){
      if (whiteSpace.contains(iter.current())){
        iter.next();
      } else if (iter.current() == '&'){

      }

    }
    return
  }
}

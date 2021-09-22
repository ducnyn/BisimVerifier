package me.ducanh.thesis.parser;

public class FormulaParser {
  public static FormulaTree parse(String formulaString){
    char iter;
    String head;
    String tail;
    int scope = 0;
    FormulaTree parentNode;

    for(int i = 0; i<formulaString.length();i++){
      head = formulaString.substring(0,i);
      iter = formulaString.charAt(i);
      tail = formulaString.substring(i+1);

      switch(iter){
        case '(':
          scope++;
        case')':
          scope--;
        case'&':
          if (scope == 0){
            parentNode = new And(parse(head),parse(tail));
          }
          break;
        default:
          break;
      }
    }


return null;
  }
}

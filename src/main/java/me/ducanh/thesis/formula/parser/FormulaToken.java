package me.ducanh.thesis.formula.parser;

public class FormulaToken {
  private final TokenType type;
  private final String value;

  public FormulaToken(TokenType type){
    this.type = type;
    value = "";
  }

  public FormulaToken(TokenType type, String value){
    this.type = type;
    this.value = value;
  }

  @Override
  public String toString() {
    if (!value.equals("")){
      return "[" + type+"("+value+")" +"]";
    }
    else {
      return "[" + type + "]";
    }
  }

  public TokenType getType(){
    return type;
  }

  public String getValue(){
    return value;
  }
}

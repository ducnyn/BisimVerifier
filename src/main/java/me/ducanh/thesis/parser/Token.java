package me.ducanh.thesis.parser;

public class Token {
  TokenType type;
  String value;

  public Token(TokenType type){
    this.type = type;
    value = "";
  }

  public Token(TokenType type, String value){
    this.type = type;
    this.value = value;
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    if (!value.equals("")){
      return "[" + type+"("+value+")" +"]";
    }
    else {
      return "[" + type + "]";
    }
  }
}

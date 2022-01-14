package me.ducanh.thesis.command;

import me.ducanh.thesis.command.TokenType;

public class Token {
  private final TokenType type;
  private final String value;

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

  public TokenType getType(){
    return type;
  }

  public String getValue(){
    return value;
  }
}

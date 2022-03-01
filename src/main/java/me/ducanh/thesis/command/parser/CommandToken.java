package me.ducanh.thesis.command.parser;

public class CommandToken {
  private final TokenType type;
  private final String value;

  public CommandToken(TokenType type){
    this.type = type;
    this.value = type.getID();
  }

  public CommandToken(TokenType type, String value){
    this.type = type;
    this.value = value;
  }

  @Override
  public String toString() {
      return "[" + type+"("+value+")" +"]";
  }

  public TokenType getType(){
    return type;
  }

  public String getValue(){
    return value;
  }
}

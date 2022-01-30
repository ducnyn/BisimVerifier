package me.ducanh.thesis.command.parser;

public enum TokenType {

    WORD("word"),
    NUMBER("number"),
    COMMA(","),
    LEFTPAR("("),
    RIGHTPAR(")"),
    SEMICOLON(";"),
    EOL("EOL");


  private final String ID;

  TokenType(String fileName) {
    this.ID = fileName;
  }

  public String getID() {
    return ID;
  }
}
//
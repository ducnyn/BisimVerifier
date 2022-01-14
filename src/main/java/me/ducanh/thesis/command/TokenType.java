package me.ducanh.thesis.command;

public enum TokenType {

    WORD(0),
    NUMBER(1),
    COMMA(2),
    LEFTPAR(3),
    RIGHTPAR(4),
    SEMICOLON(5),
    EOL(6);


  private final int ID;

  TokenType(int fileName) {
    this.ID = fileName;
  }

  public int getID() {
    return ID;
  }
}
//
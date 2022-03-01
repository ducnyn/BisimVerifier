package me.ducanh.thesis.formula.parser;

public enum TokenType {

  AND(0),
  OR(1),
  BLOCK(2),
  DIAMOND(3),
  TRUE(4),
  FALSE(5),
  NOT(6),
  LEFTPAR(7),
  RIGHTPAR(8),
  EOL(9);

  private final int ID;

  TokenType(int fileName) {
    this.ID = fileName;
  }

  public int getID() {
    return ID;
  }
}

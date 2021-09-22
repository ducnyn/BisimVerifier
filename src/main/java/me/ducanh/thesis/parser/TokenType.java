package me.ducanh.thesis.parser;

public enum TokenType {

  AND(0),
  OR(1),
  BLOCK(2),
  DIAMOND(3),
  TRUE(4),
  FALSE(5),
  NOT(6);

  private final int fileName;

  TokenType(int fileName) {
    this.fileName = fileName;
  }

  public int getFileName() {
    return fileName;
  }
}

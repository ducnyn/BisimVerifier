package me.ducanh.thesis.command;

public enum MethodName {

    BISIM("bisim"),
    DIFFERENCE("difference"),
    VERTEX("vertex"),
    EDGE("edge");



    private final String ID;

    MethodName(String fileName) {
        this.ID = fileName;
    }

    public String getID() {
        return ID;
    }
}
//
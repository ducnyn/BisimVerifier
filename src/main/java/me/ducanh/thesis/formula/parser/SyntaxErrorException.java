package me.ducanh.thesis.formula.parser;

public class SyntaxErrorException extends Exception{

    public SyntaxErrorException(){
        super();
    }
    public SyntaxErrorException(String message){
        super(message);
    }
}

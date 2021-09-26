package me.ducanh.thesis.formula;

public class SyntaxErrorException extends Exception{

    public SyntaxErrorException(){
        super();
    }
    public SyntaxErrorException(String message){
        super(message);
    }
}

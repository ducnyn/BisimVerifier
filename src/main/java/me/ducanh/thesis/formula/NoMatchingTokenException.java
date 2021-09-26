package me.ducanh.thesis.formula;

public class NoMatchingTokenException extends Exception{

    public NoMatchingTokenException(){
        super();
    }
    public NoMatchingTokenException(String message){
        super(message);
    }
}

package me.ducanh.thesis.command;

public class NoMatchingTokenException extends Exception{

    public NoMatchingTokenException(){
        super();
    }
    public NoMatchingTokenException(String message){
        super(message);
    }
}

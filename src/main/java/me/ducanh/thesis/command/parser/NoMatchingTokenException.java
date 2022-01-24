package me.ducanh.thesis.command.parser;

public class NoMatchingTokenException extends Exception{

    public NoMatchingTokenException(){
        super();
    }
    public NoMatchingTokenException(String message){
        super(message);
    }
}

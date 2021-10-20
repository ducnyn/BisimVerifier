package me.ducanh.thesis.model;

public class NoDistinguishingFormulaException extends Exception{
    public NoDistinguishingFormulaException(){
        super("No Distinguishing Formula found");
    }

    public NoDistinguishingFormulaException(String message){
        super(message);
    }
}

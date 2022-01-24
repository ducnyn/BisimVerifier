package me.ducanh.thesis.command;

import java.util.ArrayList;

public class Command {
    private String name;
    private ArrayList<String> arguments;

    public Command(String name, ArrayList<String> arguments){
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getArgumentList() {
        return arguments;
    }
}

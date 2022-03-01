package me.ducanh.thesis.ui;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import me.ducanh.thesis.Vertex;
import me.ducanh.thesis.command.parser.CommandParser;
import me.ducanh.thesis.command.Command;
import me.ducanh.thesis.command.parser.NoMatchingTokenException;
import me.ducanh.thesis.command.parser.SyntaxErrorException;
import me.ducanh.thesis.Model;
import me.ducanh.thesis.util.StringUtils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;


public class InputOutput {

    private final ObservableSet<KeyCode> pressedKeys = FXCollections.observableSet();
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextArea inputArea;
    @FXML
    private TextArea outputArea;
    @FXML
    private SplitPane splitPane;
    @FXML
    private ContextMenu editorOutputMenu;
    @FXML
    private ContextMenu editorInputMenu;

    private final StringProperty printRequest = new SimpleStringProperty();


    public void init(Model model) {
        splitPane.setDividerPosition(0,0.7);

        Font font = Font.loadFont(getClass().getResourceAsStream("InputMono.ttf"), 14);
        inputArea.setFont(font);
        outputArea.setFont(font);
        inputArea.setText("");
        outputArea.setText("");
        outputArea.setEditable(false);
        BooleanProperty printRequestedProperty = model.printRequestedProperty();
        printRequestedProperty.addListener((obs,oldV,newV)->{
                if (newV) {
                    String printString = model.getPrintString();

                    Platform.runLater(() -> print(printString));
                }
            }
        );


        inputArea.setOnKeyPressed(keyPress->{
            if(keyPress.getCode().equals(KeyCode.ENTER) && keyPress.isShiftDown()){
                Platform.runLater(() -> {
                    print("<"+model.getUserName()+">\n>> "+ multiLineIndent(inputArea.getText())+"\n"+"</"+model.getUserName()+">\n");
                    try {
                        ArrayList<Command> parsedMethods = CommandParser.parse(inputArea.getText());

                        for (Command command: parsedMethods){
                                switch(command.getName()) {
                                    case "vertex":
                                        for(String arg:command.getArgumentList()) {
                                            String[] vArgs = arg.split(":");
                                            if (vArgs.length > 1) {
                                                double xPos = Double.parseDouble(vArgs[1]);
                                                double yPos = Double.parseDouble(vArgs[2]);
                                                model.addVertex(Integer.parseInt(vArgs[0]),xPos,yPos);//tested, should be okay..
                                            } else {
                                                model.addVertex(Integer.parseInt(vArgs[0]));
                                            }
                                        }
                                        break;

                                    case "edge":
//                                        if (command.getArgumentList().size()==3){
//                                            model.addEdge(
//                                                    Integer.parseInt(command.getArgumentList().get(0)),
//                                                    command.getArgumentList().get(1),
//                                                    Integer.parseInt(command.getArgumentList().get(2))
//                                            );
//                                        }
                                        if(command.getArgumentList().stream().allMatch(arg->arg.matches("[0-9]+\\.[a-zA-Z_0-9]+\\.[0-9]+"))){
                                            for(String arg:command.getArgumentList()){
                                                String[] edgeArgs = arg.split("\\.");
                                                model.addEdge(Integer.parseInt(edgeArgs[0]),edgeArgs[1],Integer.parseInt(edgeArgs[2]));
                                            }
                                        }
                                    case "clear":
                                        if(command.getArgumentList().size()==0){
                                            model.clear();
                                        }
                                        break;
                                    case "graph":
                                        model.requestPrint(model.getVertices().toString());
                                        model.requestPrint(model.getEdges().toString());
                                }

                        }
                    } catch (SyntaxErrorException | NoMatchingTokenException e) {
                        model.requestPrint(e.getMessage());
                        e.printStackTrace();
                    }
                    inputArea.clear();
                });
            }
        });

        editorInputMenu.getItems().get(2).setOnAction(actionEvent -> inputArea.appendText("hello"));
        inputArea.setContextMenu(editorInputMenu);

    }
    private void print(String text){
        outputArea.appendText("\n"+text);
    }
    public static String multiLineIndent(String string){
        StringCharacterIterator stringIterator = new StringCharacterIterator(string);
        StringBuilder newString = new StringBuilder();
        while(stringIterator.current()!= CharacterIterator.DONE){
            if(stringIterator.current()=='\n'){
                newString.append("\n>> ");
            } else {
                newString.append(stringIterator.current());
            }
            stringIterator.next();
        }
        return newString.toString();
    }
}



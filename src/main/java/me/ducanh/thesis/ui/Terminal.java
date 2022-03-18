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
import me.ducanh.thesis.Controller;
import me.ducanh.thesis.TerminalMessage;
import me.ducanh.thesis.command.parser.CommandParser;
import me.ducanh.thesis.command.Command;
import me.ducanh.thesis.command.parser.NoMatchingTokenException;
import me.ducanh.thesis.command.parser.SyntaxErrorException;
import me.ducanh.thesis.Model;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;


public class Terminal {

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


    public void inject(Model model, Controller controller) {
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
                    print("\n>> "+ multiLineIndent(inputArea.getText())+"\n");
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
                                                controller.addVertex(Integer.parseInt(vArgs[0]),xPos,yPos);//tested, should be okay..
                                            } else {
                                                controller.addVertex(Integer.parseInt(vArgs[0]));
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
                                                controller.addEdge(Integer.parseInt(edgeArgs[0]),edgeArgs[1],Integer.parseInt(edgeArgs[2]));
                                            }
                                        }
                                    case "clear":
                                        if(command.getArgumentList().size()==0){
                                            controller.clear();
                                            controller.requestPrint(TerminalMessage.CLEAR.getMessage());

                                        }
                                        break;
                                    case "graph":
                                        controller.requestPrint(model.getVertices().toString());
                                        controller.requestPrint(model.getEdges().toString());
                                }

                        }
                    } catch (SyntaxErrorException | NoMatchingTokenException e) {
                        controller.requestPrint(e.getMessage());
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



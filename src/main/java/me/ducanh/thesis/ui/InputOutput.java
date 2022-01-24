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
import me.ducanh.thesis.command.parser.CommandParser;
import me.ducanh.thesis.command.Command;
import me.ducanh.thesis.command.parser.NoMatchingTokenException;
import me.ducanh.thesis.command.parser.SyntaxErrorException;
import me.ducanh.thesis.Model;

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

        BooleanProperty printRequestedProperty = model.printRequestedProperty();
        printRequestedProperty.addListener((obs,oldV,newV)->{
                if (newV) {
                    String printString = model.getPrintString();

                    Platform.runLater(() -> print(printString));
                }
            }
        );


        inputArea.setOnKeyPressed(keyPress->{
            if(keyPress.getCode().equals(KeyCode.ENTER)){
                Platform.runLater(() -> {
                    print(model.getUserName()+": "+inputArea.getText());
                    try {
                        ArrayList<Command> parsedMethods = CommandParser.parse(inputArea.getText());

                        for (Command method: parsedMethods){
                                switch(method.getName()) {
                                    case "vertex":
                                        System.out.println("It's a vertex method and the arguments are" + method.getArgumentList());
                                        if (method.getArgumentList().size()==1){
                                            model.addVertex(Integer.parseInt(method.getArgumentList().get(0)));
                                        }
                                        break;
                                    case "edge":
                                        if (method.getArgumentList().size()==3){
                                            model.addEdge(
                                                    Integer.parseInt(method.getArgumentList().get(0)),
                                                    method.getArgumentList().get(1),
                                                    Integer.parseInt(method.getArgumentList().get(2))
                                            );
                                        }
                                }

                        }
                    } catch (SyntaxErrorException | NoMatchingTokenException e) {
                        model.requestPrint(e.getMessage());
                    }
                    inputArea.clear();
                });
//                parse(inputArea.getText());
            }
        });

        editorInputMenu.getItems().get(2).setOnAction(actionEvent -> inputArea.appendText("hello"));
        inputArea.setContextMenu(editorInputMenu);

    }
    private void print(String text){
        outputArea.appendText("\n"+text);
    }
}



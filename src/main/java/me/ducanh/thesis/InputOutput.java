package me.ducanh.thesis;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import me.ducanh.thesis.model.Model;


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
        model.listenToPrintRequest(printRequest);

        printRequest.addListener((obs, oldText, newText) ->
                Platform.runLater(() -> print(newText))
        );

        inputArea.setOnKeyPressed(keyPress->{
            if(keyPress.getCode().equals(KeyCode.ENTER)){
                Platform.runLater(() -> {
                    print(model.getUserName()+": "+inputArea.getText());
                    inputArea.clear();
                });
//                parse(inputArea.getText());
            }
        });

        editorInputMenu.getItems().get(2).setOnAction(actionEvent -> inputArea.appendText("hello"));
        inputArea.setContextMenu(editorInputMenu);

    }
    private void print(String text){
        outputArea.appendText(text);
    }
}



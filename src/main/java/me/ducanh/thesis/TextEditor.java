package me.ducanh.thesis;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import me.ducanh.thesis.model.Model;


public class TextEditor {

    private final ObservableSet<KeyCode> pressedKeys = FXCollections.observableSet();
    @FXML
    private TextArea editorTextArea;
    @FXML
    private ContextMenu editorCM;

    public void intialize(Model model) {
//    Font font = Font.loadFont(getClass().getResourceAsStream("InputMono.ttf"), 14);
        editorTextArea.setFont(Font.font("lucida console", 16));
        editorTextArea.setText(model.getDot());
        model.addDotListener((obs, oldText, newText) ->
                Platform.runLater(() ->
                        editorTextArea.setText(newText)
                )
        );
        editorTextArea.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        editorTextArea.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
        pressedKeys.addListener((SetChangeListener<? super KeyCode>) c -> {
            System.out.print(pressedKeys);
        });
        model.updatedProperty().addListener((observable, changeToFalse, changeToTrue) -> {
            if (changeToTrue) {
                Platform.runLater(() -> {
                    editorTextArea.setText(model.getDot());
                    editorTextArea.appendText("\n" + model.getAlertString().get());
                });

            }
        });
        editorCM.getItems().get(2).setOnAction(actionEvent -> editorTextArea.appendText("hello"));
        editorTextArea.setContextMenu(editorCM);

    }
}



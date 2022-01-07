package me.ducanh.thesis;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import me.ducanh.thesis.model.Model;

public class OutputPanel {
    @FXML
    TextArea textArea = new TextArea();


    public void initialize(Model model) {
//    Font font = Font.loadFont(getClass().getResourceAsStream("InputMono.ttf"), 14);
        textArea.setFont(Font.font("lucida console", 16));

        model.bindOutputString(textArea.textProperty());
    }
}

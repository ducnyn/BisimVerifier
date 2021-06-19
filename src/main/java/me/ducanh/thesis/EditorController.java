package me.ducanh.thesis;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import me.ducanh.thesis.model.Model;


public class EditorController {

@FXML
private TextArea editorTextArea;
@FXML
private ContextMenu editorCM;
private Model model;

public void inject(Model model) {
    this.model = model;
    editorTextArea.setText(model.getDotString());

    this.model.getDotStringProperty().addListener((obs, oldText, newText) ->
            editorTextArea.setText(newText));
    this.model.getAppendStringProperty().addListener((obs, oldText, newText) ->
            editorTextArea.appendText(newText));

//    ContextMenu contextMenu = new ContextMenu();
//    MenuItem addVertices = new MenuItem("addVertices");
//    contextMenu.getItems().add(addVertices);
    editorTextArea.setContextMenu(editorCM);
    editorCM.getItems().get(2).setOnAction(actionEvent -> editorTextArea.appendText("hello"));
}
}



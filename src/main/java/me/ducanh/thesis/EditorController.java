package me.ducanh.thesis;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import me.ducanh.thesis.model.DataModel;
import me.ducanh.thesis.util.DummyGraphTool;


public class EditorController {

    @FXML
    private TextArea editorTextArea;


    private DataModel data;
    public void inject(DataModel data){
        this.data = data;
        DummyGraphTool.setExampleGraph(data);
        System.out.println(data.currentToDot());
        editorTextArea.setText(data.currentToDot());

        editorTextArea.setOnKeyTyped(a->{
            editorTextArea.appendText(a.getCharacter()f);
        });

    }

}






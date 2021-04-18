package me.ducanh.thesis;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Test {

    public Button secondaryButton;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("homeView");
    }
}
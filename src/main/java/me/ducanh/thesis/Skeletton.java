package me.ducanh.thesis;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class Skeletton {

    public MenuItem switchToAdditionalButton;

    @FXML
    private void switchToAdditional() throws IOException {
        App.setRoot("Test");
    }
}

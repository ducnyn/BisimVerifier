package me.ducanh.thesis;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MenuController {

    public MenuItem switchToAdditionalButton;

    @FXML
    private void switchToAdditional() throws Exception {
        App.setRoot(View.ABOUT);
    }
}

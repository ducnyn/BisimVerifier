package me.ducanh.thesis;

import javafx.scene.control.MenuItem;

public class AltMenu {

public MenuItem switchToAdditionalButton;

@javafx.fxml.FXML
private void switchToAdditional() throws Exception {
    App.setRoot(FXMLPATH.ABOUT);
}
}

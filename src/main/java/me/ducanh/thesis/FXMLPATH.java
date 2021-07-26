package me.ducanh.thesis;

public enum FXMLPATH {
    ALTMENU("altmenu.fxml"),
    TEXTEDITOR("texteditor.fxml"),
    VISEDITOR("viseditor.fxml"),
    SIDEBAR("sidebar.fxml"),
    ABOUT("about.fxml"),
    VISEDGE("visedge.fxml"),
    VISVERTEX("visvertex.fxml"),
    OUTPUT("output.fxml");

  private final String fileName;

FXMLPATH(String fileName) {
    this.fileName = fileName;
}

public String getFileName() {
    return fileName;
}
}

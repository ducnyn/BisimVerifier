package me.ducanh.thesis;

public enum FXMLPATH {
    ALTMENU("AltMenu.fxml"),
    TEXTEDITOR("TextEditor.fxml"),
    VISEDITOR("VisEditor.fxml"),
    SIDEBAR("Sidebar.fxml"),
    ABOUT("About.fxml"),
    VISEDGE("VisEdge.fxml"),
    VISVERTEX("VisVertex.fxml"),
    OUTPUT("OutputPanel.fxml");

  private final String fileName;

FXMLPATH(String fileName) {
    this.fileName = fileName;
}

public String getFileName() {
    return fileName;
}
}

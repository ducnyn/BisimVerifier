package me.ducanh.thesis;

public enum FXMLPATH {
    COMMANDLINE("CommandLine.fxml"),
    CANVAS("Canvas.fxml"),
    SIDEBAR("Sidebar.fxml"),
    ABOUT("About.fxml");

    private final String fileName;

    FXMLPATH(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}

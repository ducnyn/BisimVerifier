package me.ducanh.thesis;

public enum View {
    MENU("menu.fxml"),
    EDITOR("editor.fxml"),
    CANVAS("canvas.fxml"),
    SIDEBAR("sidebar.fxml");

    private final String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }
}

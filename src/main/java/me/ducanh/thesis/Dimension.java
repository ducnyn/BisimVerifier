package me.ducanh.thesis;

public enum Dimension {

    ABOUT("about.fxml");

private final String fileName;

Dimension(String fileName){
    this.fileName = fileName;
}

public String getFileName(){
    return fileName;
}
}

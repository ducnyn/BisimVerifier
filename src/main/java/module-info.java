module me.ducanh.thesis {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.common;
    requires java.desktop;
    requires JavaFXSmartGraph;

    opens me.ducanh.thesis to javafx.fxml;
    exports me.ducanh.thesis;
}
module me.ducanh.thesis {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens me.ducanh.thesis to javafx.fxml;
    exports me.ducanh.thesis;
}
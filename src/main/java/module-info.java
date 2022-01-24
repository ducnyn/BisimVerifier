module me.ducanh.thesis {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.common;
    requires java.desktop;
    requires JavaFXSmartGraph;

    opens me.ducanh.thesis to javafx.fxml;
    exports me.ducanh.thesis;
    exports me.ducanh.thesis.algorithms;
    opens me.ducanh.thesis.algorithms to javafx.fxml;
    exports me.ducanh.thesis.ui;
    opens me.ducanh.thesis.ui to javafx.fxml;
    exports me.ducanh.thesis.formula.parser;
    opens me.ducanh.thesis.formula.parser to javafx.fxml;
}
package me.ducanh.thesis;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import me.ducanh.thesis.model.Model;

import java.util.concurrent.atomic.AtomicReference;

public class VisVertex extends StackPane {

    public static int DEFAULT_RADIUS = 40;
    public static Paint DEFAULT_FILL = Paint.valueOf("lightgray");
    public static Paint DEFAULT_STROKE = Paint.valueOf("darkgray");
    private final Text label = new Text();
    private final Circle circle = new Circle(DEFAULT_RADIUS);
    private final DoubleProperty centerXProperty = new SimpleDoubleProperty(layoutXProperty().get() + getRadius());
    private final DoubleProperty centerYProperty = new SimpleDoubleProperty(layoutYProperty().get() + getRadius());
    int id;

//  public void init(Model model, int id) {
//    init(model, id, 1, 1);
//  }

    public VisVertex(Model model, int id, double initX, double initY) {


        addEventHandler(MouseEvent.ANY, Event::consume);
        setPickOnBounds(false);
        getChildren().add(circle);
        getChildren().add(label);
        circle.setRadius(DEFAULT_RADIUS);
        circle.setFill(DEFAULT_FILL);
        circle.setStroke(DEFAULT_STROKE);
        label.setStyle("-fx-font-size: 20;");

        this.id = id;
        label.setText(String.valueOf(id));
        setLayoutX(initX - getRadius());
        setLayoutY(initY - getRadius());

        centerXProperty.bind(Bindings.createDoubleBinding(
                () -> layoutXProperty().get() + getRadius(),
                layoutXProperty()
        ));
        centerYProperty.bind(Bindings.createDoubleBinding(
                () -> layoutYProperty().get() + getRadius(),
                layoutYProperty()
        ));


        AtomicReference<Double> onPressedX = new AtomicReference<>();
        AtomicReference<Double> onPressedY = new AtomicReference<>();

        setOnMousePressed(press -> {
            if (press.isAltDown()) {
                model.removeVertex(this.id);
            }
            toFront();
            onPressedX.set(press.getScreenX());
            onPressedY.set(press.getScreenY());
        });

        setOnMouseDragged((drag) -> {

            if (drag.getButton().equals(MouseButton.PRIMARY) && !drag.isAltDown()) {
                drag.consume();
                double deltaX = getTranslateX() + (drag.getScreenX() - onPressedX.get()) / getParent().getScaleX();
                double deltaY = getTranslateY() + (drag.getScreenY() - onPressedY.get()) / getParent().getScaleY();
                setTranslateX(deltaX);
                setTranslateY(deltaY);
                setLayoutX(getLayoutX() + getTranslateX());
                setLayoutY(getLayoutY() + getTranslateY());
                setTranslateX(0);
                setTranslateY(0);
                onPressedX.set(drag.getScreenX());
                onPressedY.set(drag.getScreenY());
            }
        });


        setOnMouseReleased(released -> {
            if (released.isDragDetect()) {
                if (model.getSelectedVertices().contains(id)) {
                    model.getSelectedVertices().remove(id);
                } else {
                    model.getSelectedVertices().add(id);
                }
            }
        });

    }

    public double getCenterX() {
        return centerXProperty.get();
    }

    public double getCenterY() {
        return centerYProperty.get();
    }

    public void setFill(Paint paint) {
        circle.setFill(paint);
    }

    public void setText(String string) {
        label.setText(string);
    }

    public double getRadius() {
        return circle.getRadius();
    }

    public void setRadius(Double radius) {
        circle.setRadius(radius);
    }

    public void resetFill() {
        circle.setFill(DEFAULT_FILL);
    }

    public Paint getDefaultFill() {
        return DEFAULT_FILL;
    }

    public DoubleProperty getCenterXProperty() {
        return centerXProperty;
    }

    public DoubleProperty getCenterYProperty() {
        return centerYProperty;
    }


}

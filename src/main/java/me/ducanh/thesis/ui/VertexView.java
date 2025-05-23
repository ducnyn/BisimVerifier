package me.ducanh.thesis.ui;

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
import me.ducanh.thesis.Controller;
import me.ducanh.thesis.Vertex;

import java.util.concurrent.atomic.AtomicReference;

public class VertexView extends StackPane {
    public static int DEFAULT_RADIUS = 15;
    public static Paint DEFAULT_FILL = Paint.valueOf("darkgray");
    public static Paint DEFAULT_STROKE = Paint.valueOf("black");


    private final Text text = new Text();
    private final Circle circle = new Circle(DEFAULT_RADIUS);
    private final DoubleProperty centerXProperty = new SimpleDoubleProperty(layoutXProperty().get() + getRadius());
    private final DoubleProperty centerYProperty = new SimpleDoubleProperty(layoutYProperty().get() + getRadius());
    private final Vertex vertex;

    private final DoubleProperty clickXProperty = new SimpleDoubleProperty(0);
    private final DoubleProperty clickYProperty = new SimpleDoubleProperty(0);

    public VertexView(Controller controller, Vertex vertex){
        this.vertex = vertex;
        setText(String.valueOf(vertex.id));
        System.out.println(vertex.id);
        addEventHandler(MouseEvent.ANY, Event::consume);
        setPickOnBounds(false);
        getChildren().add(circle);
        getChildren().add(text);
        circle.setFill(DEFAULT_FILL);
        circle.setStroke(DEFAULT_STROKE);
        circle.setStyle("-fx-opacity: 0.5");
        text.setStyle("-fx-font-size: 20;");

        layoutXProperty().set(vertex.layoutXProperty.get());
        layoutYProperty().set(vertex.layoutYProperty.get());
        layoutXProperty().bindBidirectional(vertex.layoutXProperty);
        layoutYProperty().bindBidirectional(vertex.layoutYProperty);

        centerXProperty.bind(Bindings.createDoubleBinding(
                () -> layoutXProperty().get() + getRadius(),
                layoutXProperty()
        ));
        centerYProperty.bind(Bindings.createDoubleBinding(
                () -> layoutYProperty().get() + getRadius(),
                layoutYProperty()
        ));





        setOnMousePressed(press -> {
            if (press.getButton().equals(MouseButton.PRIMARY) && press.isAltDown()) {
                controller.removeVertex(getLabel());
            }
            toFront();
            clickXProperty.set(press.getScreenX());
            clickYProperty.set(press.getScreenY());
        });

        setOnMouseDragged((drag) -> {

            if (drag.getButton().equals(MouseButton.PRIMARY) && !drag.isAltDown()) {
                drag.consume();
                double deltaX = getTranslateX() + (drag.getScreenX() - clickXProperty.get()) / getParent().getScaleX();
                double deltaY = getTranslateY() + (drag.getScreenY() - clickYProperty.get()) / getParent().getScaleY();
                setTranslateX(deltaX);
                setTranslateY(deltaY);
                setLayoutX(getLayoutX() + getTranslateX());
                setLayoutY(getLayoutY() + getTranslateY());
                setTranslateX(0);
                setTranslateY(0);
                clickXProperty.set(drag.getScreenX());
                clickYProperty.set(drag.getScreenY());
            }
        });
    }


    public double getCenterX() {
        return centerXProperty.get();
    }

    public double getCenterY() {
        return centerYProperty.get();
    }

    public void setCenterX(double x){setLayoutX(x-getRadius());}
    public void setCenterY(double y){setLayoutY(y-getRadius());}

    public void setFill(Paint paint) {
        circle.setFill(paint);
    }

    public void setText(String string) {
        text.setText(string);
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

    @Override
    public String toString(){
        return this.vertex.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VertexView that = (VertexView) o;

        return vertex.equals(that.vertex);
    }

    @Override
    public int hashCode() {
        return vertex.hashCode();
    }

    public Vertex getVertex() {
        return vertex;
    }
    public Integer getLabel(){
        return vertex.id;
    }





}

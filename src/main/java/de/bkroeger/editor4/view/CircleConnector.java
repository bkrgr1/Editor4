package de.bkroeger.editor4.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class CircleConnector extends Ellipse {
	
	private double radiusX = 5.0;
	private double radiusY = 5.0;

	public CircleConnector(DoubleProperty widthProperty, DoubleProperty heightProperty,
			double addWidth, double addHeight) {
		super();
		this.setRadiusX(radiusX);
		this.setRadiusY(radiusY);
		this.translateXProperty().bind(Bindings.add(widthProperty, addWidth));
		this.translateYProperty().bind(Bindings.add(Bindings.divide(heightProperty, 2.0), addHeight));
		this.setStroke(Color.BLACK);
		this.setFill(Color.TRANSPARENT);
	}
}

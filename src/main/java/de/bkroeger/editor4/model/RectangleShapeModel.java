package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class RectangleShapeModel implements IShapeModel {
	
	private DoubleProperty xProperty;
	public DoubleProperty getXProperty() { return xProperty; }
	
	private DoubleProperty yProperty;
	public DoubleProperty getYProperty() { return yProperty; }
	
	private DoubleProperty widthProperty;
	public DoubleProperty getWidthProperty() { return widthProperty; }
	
	private DoubleProperty heightProperty;
	public DoubleProperty getHeightProperty() { return heightProperty; }

	@Override
	public ShapeType getShapeType() { return ShapeType.Rectangle; }

	
	public RectangleShapeModel(double x, double y, double width, double height) {
		this.xProperty = new SimpleDoubleProperty(x);
		this.yProperty = new SimpleDoubleProperty(y);
		this.widthProperty = new SimpleDoubleProperty(width);
		this.heightProperty = new SimpleDoubleProperty(height);
	}
}

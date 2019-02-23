package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class RectangleShapeModel implements IShapeModel {
	
	private DoubleProperty xProperty;
	public DoubleProperty xProperty() { return xProperty; }
	
	private DoubleProperty yProperty;
	public DoubleProperty yProperty() { return yProperty; }
	
	private DoubleProperty widthProperty;
	public DoubleProperty widthProperty() { return widthProperty; }
	
	private DoubleProperty heightProperty;
	public DoubleProperty heightProperty() { return heightProperty; }

	@Override
	public ShapeType getShapeType() { return ShapeType.Rectangle; }

	
	public RectangleShapeModel(double x, double y, double width, double height) {
		this.xProperty = new SimpleDoubleProperty(x);
		this.yProperty = new SimpleDoubleProperty(y);
		this.widthProperty = new SimpleDoubleProperty(width);
		this.heightProperty = new SimpleDoubleProperty(height);
	}
}

package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;

public interface IArrowModel {
	
	public ArrowType getArrowType();
	
	public DoubleProperty getX1Property();
	public DoubleProperty getY1Property();
	public DoubleProperty getX2Property();
	public DoubleProperty getY2Property();
	public DoubleProperty getRotateProperty();
}

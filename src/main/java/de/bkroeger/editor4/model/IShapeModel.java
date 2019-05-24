package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;

public interface IShapeModel extends IModel {

	public ShapeType getShapeType();
	
	public DoubleProperty getXProperty(); 
	
	public DoubleProperty getYProperty();
	
	public DoubleProperty getWidthProperty();
	
	public DoubleProperty getHeightProperty();
}

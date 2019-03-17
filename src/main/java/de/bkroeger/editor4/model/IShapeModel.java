package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;

public interface IShapeModel extends IModel {

	public ShapeType getShapeType();
	
	public DoubleProperty xProperty(); 
	public DoubleProperty yProperty();
	
	public DoubleProperty widthProperty();
	public DoubleProperty heightProperty();
}

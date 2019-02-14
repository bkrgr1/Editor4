package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;

public interface IArrowModel {
	
	public ArrowType getArrowType();
	
	public DoubleProperty x1Property();
	public DoubleProperty y1Property();
	public DoubleProperty x2Property();
	public DoubleProperty y2Property();
	public DoubleProperty rotateProperty();

	LineEndingType getLineStartType();

	LineEndingType getLineEndType();

	Color getColor();

	Double getStrokeWidth();
	void setStrokeWidth(double w);

	LineStrokeType getStrokeType();
}

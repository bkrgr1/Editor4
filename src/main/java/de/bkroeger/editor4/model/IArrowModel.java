package de.bkroeger.editor4.model;

import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;

public interface IArrowModel extends IModel {
	
	public ArrowType getArrowType();
	
	public DoubleProperty x1Property();
	public DoubleProperty y1Property();
	public DoubleProperty x2Property();
	public DoubleProperty y2Property();
	public DoubleProperty rotateProperty();
	
	public List<IConnectorModel> getConnectorModels();

	public LineEndingType getLineStartType();

	public LineEndingType getLineEndType();

	public Color getColor();

	public DoubleProperty strokeWidthProperty();

	public LineStrokeType getStrokeType();
}

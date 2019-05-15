package de.bkroeger.editor4.model;

import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
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

	public ObjectProperty<LineEndingType> lineStartTypeProperty();

	public LineEndingType getLineEndType();

	public ObjectProperty<LineEndingType> lineEndTypeProperty();

	public Color getColor();

	public ObjectProperty<Color> colorProperty();

	public DoubleProperty strokeWidthProperty();

	public LineStrokeType getStrokeType();

	public ObjectProperty<LineStrokeType> strokeTypeProperty();
}

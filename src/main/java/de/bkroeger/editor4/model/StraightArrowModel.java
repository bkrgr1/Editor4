package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class StraightArrowModel implements IArrowModel {
	
	private DoubleProperty x1Property;
	@Override
	public DoubleProperty x1Property() { return x1Property; }
	
	private DoubleProperty y1Property;
	@Override
	public DoubleProperty y1Property() { return y1Property; }
	
	private DoubleProperty x2Property;
	@Override
	public DoubleProperty x2Property() { return x2Property; }
	
	private DoubleProperty y2Property;
	@Override
	public DoubleProperty y2Property() { return y2Property; }
	
	private DoubleProperty rotateProperty;
	@Override
	public DoubleProperty rotateProperty() { return rotateProperty; }

	@Override
	public ArrowType getArrowType() { return ArrowType.straightArrow; }
	
	private ObjectProperty<LineEndingType> lineStartTypeProperty = new SimpleObjectProperty<LineEndingType>(LineEndingType.None);
	@Override
	public LineEndingType getLineStartType() { return lineStartTypeProperty.get(); }
	public ObjectProperty<LineEndingType> lineStartTypeProperty() { return lineStartTypeProperty; }
	
	private ObjectProperty<LineEndingType> lineEndTypeProperty = new SimpleObjectProperty<LineEndingType>(LineEndingType.None);
	@Override
	public LineEndingType getLineEndType() { return lineEndTypeProperty.get(); }
	public ObjectProperty<LineEndingType> lineEndTypeProperty() { return lineEndTypeProperty; }
	
	private ObjectProperty<Color> colorProperty = new SimpleObjectProperty<Color>(Color.BLACK);
	@Override
	public Color getColor() { return colorProperty.get(); }
	public ObjectProperty<Color> colorProperty() { return colorProperty; }
	
	private DoubleProperty strokeWidthProperty = new SimpleDoubleProperty(1.0);
	@Override
	public Double getStrokeWidth() { return strokeWidthProperty.get(); }
	public DoubleProperty strokeWidthProperty() { return strokeWidthProperty; }
	@Override
	public void setStrokeWidth(double w) { strokeWidthProperty.set(w);; }
	
	private ObjectProperty<LineStrokeType> strokeTypeProperty;
	@Override
	public LineStrokeType getStrokeType() { return strokeTypeProperty.get(); }
	public ObjectProperty<LineStrokeType> strokeTypeProperty() { return strokeTypeProperty; }


	/**
	 * Constructor
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param rotate
	 */
	public StraightArrowModel(double x1, double y1, double x2, double y2, double rotate) {
		
		this.x1Property = new SimpleDoubleProperty(x1);
		this.y1Property = new SimpleDoubleProperty(y1);
		this.x2Property = new SimpleDoubleProperty(x2);
		this.y2Property = new SimpleDoubleProperty(y2);
		this.rotateProperty = new SimpleDoubleProperty(rotate);
	}
}

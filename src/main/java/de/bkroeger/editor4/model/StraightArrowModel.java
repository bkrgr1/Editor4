package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class StraightArrowModel implements IArrowModel {
	
	private DoubleProperty x1Property;
	public DoubleProperty getX1Property() { return x1Property; }
	
	private DoubleProperty y1Property;
	public DoubleProperty getY1Property() { return y1Property; }
	
	private DoubleProperty x2Property;
	public DoubleProperty getX2Property() { return x2Property; }
	
	private DoubleProperty y2Property;
	public DoubleProperty getY2Property() { return y2Property; }

	@Override
	public ArrowType getArrowType() { return ArrowType.straightArrow; }


	
	public StraightArrowModel(double x1, double y1, double x2, double y2) {
		this.x1Property = new SimpleDoubleProperty(x1);
		this.y1Property = new SimpleDoubleProperty(y1);
		this.x2Property = new SimpleDoubleProperty(x2);
		this.y2Property = new SimpleDoubleProperty(y2);
	}
}

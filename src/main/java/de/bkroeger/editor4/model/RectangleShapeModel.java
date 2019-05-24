package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Ein rechteckiges Shape.</p>
 * 
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
public class RectangleShapeModel implements IShapeModel {

	/**
	 * <p>Sichtbarer Titel des Shapes.</p>
	 */
	protected String title;
	
	/**
	 * Horizontale Position
	 */
	private DoubleProperty xProperty;
	@Override
	public DoubleProperty xProperty() { return this.xProperty; }

	/**
	 * Vertikale Position
	 */
	private DoubleProperty yProperty;
	@Override
	public DoubleProperty yProperty() { return this.yProperty; }

	/**
	 * Breite
	 */
	private DoubleProperty widthProperty;

	/**
	 * Höhe
	 */
	private DoubleProperty heightProperty;

	@Override
	public ShapeType getShapeType() {
		return ShapeType.Rectangle;
	}

	/**
	 * <p>Constructor</p>
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public RectangleShapeModel(double x, double y, double width, double height) {
		this.xProperty = new SimpleDoubleProperty(x);
		this.yProperty = new SimpleDoubleProperty(y);
		this.widthProperty = new SimpleDoubleProperty(width);
		this.heightProperty = new SimpleDoubleProperty(height);
	}

}

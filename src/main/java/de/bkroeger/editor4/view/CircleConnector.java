package de.bkroeger.editor4.view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * <p>Zeichnet einen kreisförmigen Connector an der angegebenen Position.</p>
 * 
 * @author bk
 */
public class CircleConnector extends Circle implements IConnector {
	
	private static final double RADIUS = 5.0;

	/**
	 * Constructor
	 * @param centerX
	 * @param centerY
	 */
	public CircleConnector(DoubleBinding centerX, DoubleBinding centerY) {
		super();
		
		this.setRadius(RADIUS);
		this.centerXProperty().bind(centerX);
		this.centerYProperty().bind(centerY);
		this.setStroke(Color.BLACK);
		this.setFill(Color.TRANSPARENT);
	}
}

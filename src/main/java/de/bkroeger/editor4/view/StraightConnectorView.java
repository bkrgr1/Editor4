package de.bkroeger.editor4.view;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * <p>
 * Zeichnet einen kreisförmigen Connector an der angegebenen Position.
 * </p>
 * 
 * @author bk
 */
public class StraightConnectorView extends Circle implements IConnector {

	private static final double RADIUS = 5.0;
	private static final double STROKE_WIDTH = 0.5;
	private static final Color CONNECTOR_COLOR = Color.YELLOW;
	private static final Color SELECTED_COLOR = Color.RED;

	/**
	 * Constructor
	 * 
	 * @param centerX
	 * @param centerY
	 */
	public StraightConnectorView(DoubleBinding centerX, DoubleBinding centerY) {
		super();

		this.setRadius(RADIUS);
		this.centerXProperty().bind(centerX);
		this.centerYProperty().bind(centerY);
		this.setStroke(Color.BLACK);
		this.setStrokeWidth(STROKE_WIDTH);
		this.setFill(CONNECTOR_COLOR);
		this.setVisible(false);
	}

	@Override
	public void setSelected(boolean isSelected) {
		this.setVisible(isSelected);
		if (isSelected) {
			this.setFill(SELECTED_COLOR);
		} else {
			this.setFill(CONNECTOR_COLOR);
		}
	}
}

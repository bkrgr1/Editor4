package de.bkroeger.editor4.model;

import javafx.beans.binding.Bindings;

/**
 * <p>
 * This model contains the data for a straight arrow.
 * </p>
 * 
 * @author bk
 */
public class StraightArrowModel extends BaseArrowModel {

	/**
	 * Constructor
	 * 
	 * @param x1     x position of start point
	 * @param y1     y position of start point
	 * @param x2     x position of end point
	 * @param y2     y position of end point
	 * @param rotate rotation angle in degree
	 */
	public StraightArrowModel(double x1, double y1, double x2, double y2, double rotate) {
		super(x1, y1, x2, y2, rotate);

		// create a Connector model at the start point
		IConnectorModel connectorModel = new DefaultConnectorModel(Bindings.add(this.x1Property, 0.0),
				Bindings.add(this.y1Property, 0.0));
		connectorModels.add(connectorModel);

		// create a Connector at the end point
		connectorModel = new DefaultConnectorModel(Bindings.add(this.x2Property, 0.0),
				Bindings.add(this.y2Property, 0.0));
		connectorModels.add(connectorModel);
	}
}

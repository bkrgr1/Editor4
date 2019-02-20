package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class DefaultConnectorModel implements IConnectorModel {

	private DoubleProperty xProperty = new SimpleDoubleProperty();
	@Override
	public DoubleProperty xProperty() { return xProperty; }

	private DoubleProperty yProperty = new SimpleDoubleProperty();
	@Override
	public DoubleProperty yProperty() { return yProperty; }

	
	public DefaultConnectorModel(DoubleProperty xProperty, DoubleProperty yProperty) {
		this.xProperty = xProperty;
		this.yProperty = yProperty;
	}
}

package de.bkroeger.editor4.model;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class DefaultConnectorModel implements IConnectorModel {

	private DoubleProperty xProperty = new SimpleDoubleProperty();

	@Override
	public DoubleProperty xProperty() {
		return xProperty;
	}

	public void setXProperty(DoubleProperty xProperty) {
		this.xProperty = xProperty;
	}

	private DoubleProperty yProperty = new SimpleDoubleProperty();

	@Override
	public DoubleProperty yProperty() {
		return yProperty;
	}

	public void setYProperty(DoubleProperty yProperty) {
		this.xProperty = yProperty;
	}

	public DefaultConnectorModel(DoubleBinding xBinding, DoubleBinding yBinding) {
		this.xProperty.bind(xBinding);
		this.yProperty.bind(yBinding);
	}
}

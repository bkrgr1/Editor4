package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Definiert die Basisfunktionen für ein Model.
 * </p>
 */
@Getter
@Setter
public class BaseModel implements IModel {

    protected String title;

    protected DoubleProperty xProperty;

    @Override
    public DoubleProperty xProperty() {
        return xProperty;
    }

    protected DoubleProperty yProperty;

    @Override
    public DoubleProperty yProperty() {
        return yProperty;
    }

    public BaseModel() {

    }

    public BaseModel(String title) {
        this.title = title;
    }

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
}

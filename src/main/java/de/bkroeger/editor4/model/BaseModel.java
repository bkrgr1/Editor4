package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;
import lombok.Data;

/**
 * <p>
 * Definiert die Basisfunktionen für ein Model.
 * </p>
 */
@Data
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
}

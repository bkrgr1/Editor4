package de.bkroeger.editor4.model;

import lombok.Data;

/**
 * <p>
 * Definiert die Basisfunktionen für ein Model.
 * </p>
 */
@Data
public class BaseModel implements IModel {

    protected String title;

    public BaseModel() {

    }

    public BaseModel(String title) {
        this.title = title;
    }
}

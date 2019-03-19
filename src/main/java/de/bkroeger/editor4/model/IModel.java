package de.bkroeger.editor4.model;

import javafx.beans.property.DoubleProperty;

public interface IModel {

    public String getTitle();

    public DoubleProperty xProperty();

    public DoubleProperty yProperty();
}
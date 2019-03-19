package de.bkroeger.editor4.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShapeEventData {

    private Double mouseX;
    private Double mouseY;

    private IModel model;

    public ShapeEventData(IShapeModel model) {
        this.model = model;
    }
}
package de.bkroeger.editor4.view;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class StraightArrowView extends Path {

	public StraightArrowView(DoubleProperty x1Property, DoubleProperty y1Property, 
			DoubleProperty x2Property, DoubleProperty y2Property) {
		
		MoveTo moveto1 = new MoveTo();
		moveto1.xProperty().bind(x1Property);
		moveto1.yProperty().bind(y1Property);
		
		LineTo lineto1 = new LineTo();
		lineto1.xProperty().bind(x2Property);
		lineto1.yProperty().bind(y2Property);
		
		this.getElements().addAll(moveto1, lineto1);
		
		this.setStroke(Color.BLUE);
	}
}

package de.bkroeger.editor4.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class StraightArrowView extends Pane implements IArrowView {
	
	private static final double radiusX = 5.0;
	private static final double radiusY = 5.0;
	
	private static final Color BACKGROUND_COLOR = Color.LIGHTCORAL;
	
	private Ellipse startConnector;

	public StraightArrowView(DoubleProperty x1Property, DoubleProperty y1Property, 
			DoubleProperty x2Property, DoubleProperty y2Property,
			DoubleProperty rotateProperty) {
		
		// ein Pane in der notwendigen Grösse erzeugen
		this.translateXProperty().bind(Bindings.add(x1Property, radiusX / -2.0));
		this.translateYProperty().bind(Bindings.add(y1Property, radiusY / -2.0));
		this.prefWidthProperty().bind(Bindings.add(Bindings.subtract(x2Property, x1Property), radiusX));
		this.prefHeightProperty().bind(Bindings.add(Bindings.subtract(y2Property, y1Property), radiusY));
		
		// einen Path für den Pfeil erzeugen
		Path path = new Path();
		
		// Anfangsposition
		MoveTo moveto1 = new MoveTo();
		moveto1.xProperty().set(radiusX / 2.0);
		moveto1.yProperty().set(radiusX / 2.0);
		
		// Linie zum Ziel
		LineTo lineto1 = new LineTo();
		lineto1.xProperty().bind(Bindings.add(Bindings.subtract(x2Property, x1Property), radiusX / 2.0));
		lineto1.yProperty().bind(Bindings.add(Bindings.subtract(y2Property, y1Property), radiusY / 2.0));
		
		path.setStroke(Color.BLUE);
		path.getElements().addAll(moveto1, lineto1);
		
		startConnector = new Ellipse();
		startConnector.translateXProperty().set(radiusX / 2.0);
		startConnector.translateYProperty().set(radiusY / 2.0);
		startConnector.setRadiusX(radiusX);
		startConnector.setRadiusY(radiusY);
		startConnector.setVisible(false);
		
		this.getChildren().addAll(path, startConnector);
		this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		this.rotateProperty().bind(rotateProperty);
	}
	
	@Override
	public void setSelected(boolean isSelected) {
		startConnector.setVisible(isSelected);
		if (isSelected) {
			this.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		} else {
			this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		}
	}
}

package de.bkroeger.editor4.view;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleShapeView extends Pane {
	
	private double addWidth = 15.0;
	private double addHeight = 15.0;
	
	private Rectangle rect;
	public Rectangle getRectangle() { return rect; }
	
	private List<Node> connectors = new ArrayList<>();
	public List<Node> getConnectors() { return connectors; }

	/**
	 * Constructor
	 * @param xProperty
	 * @param yProperty
	 * @param widthProperty
	 * @param heightProperty
	 */
	public RectangleShapeView(DoubleProperty xProperty, DoubleProperty yProperty, 
			DoubleProperty widthProperty, DoubleProperty heightProperty) {
		
		// Pane definieren
		this.translateXProperty().bind(Bindings.add(xProperty, (addWidth * -1.0)));
		this.translateYProperty().bind(Bindings.add(yProperty, (addHeight * -1.0)));
		this.prefWidthProperty().bind(Bindings.add(widthProperty, (addWidth * 2.0)));
		this.prefHeightProperty().bind(Bindings.add(heightProperty, (addHeight * 2.0)));
		
		// Rectangle erstellen
		rect = new Rectangle();
		rect.setTranslateX(addWidth);
		rect.setTranslateY(addHeight);
		rect.widthProperty().bind(widthProperty);
		rect.heightProperty().bind(heightProperty);
		rect.setFill(Color.YELLOW);
		rect.setStroke(Color.BLACK);
		
		// die Konnectoren als Kreise zeichnen
		Node connector1 = new CircleConnector(widthProperty, heightProperty, addWidth, addHeight);
		connectors.add(connector1);
		
		// Rectangle und Konnektoren zum Pane hinzufügen
		this.getChildren().addAll(rect, connector1);
	}
}

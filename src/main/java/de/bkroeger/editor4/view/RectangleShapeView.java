package de.bkroeger.editor4.view;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <p>Zeichnet ein Rechteck-Shape.</p>
 * <p>Als Hintergrund wird ein transparentes {@link Pane} gezeichnet, das etwas größer ist als das Rechteck,
 * damit auch die Konnektoren gezeichnet werden können.</p>
 * 
 * @author bk
 */
public class RectangleShapeView extends Pane implements IShapeView {
	
	private double additionalWidth = 15.0;
	private double additionalHeight = 15.0;
	
	private Rectangle rect;
	public Rectangle getRectangle() { return rect; }
	
	private List<IConnector> connectors = new ArrayList<>();
	public List<IConnector> getConnectors() { return connectors; }

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
		this.translateXProperty().bind(Bindings.add(xProperty, (additionalWidth * -1.0)));
		this.translateYProperty().bind(Bindings.add(yProperty, (additionalHeight * -1.0)));
		this.prefWidthProperty().bind(Bindings.add(widthProperty, (additionalWidth * 2.0)));
		this.prefHeightProperty().bind(Bindings.add(heightProperty, (additionalHeight * 2.0)));
		
		// Rectangle erstellen
		rect = new Rectangle();
		rect.setTranslateX(additionalWidth);
		rect.setTranslateY(additionalHeight);
		rect.widthProperty().bind(widthProperty);
		rect.heightProperty().bind(heightProperty);
		rect.setFill(Color.YELLOW);
		rect.setStroke(Color.BLACK);
		
		// die Konnectoren als Kreise zeichnen
		IConnector connector = null;
		
		// Konnektor-1: Mitte rechte Seite
		connector = new CircleConnector(
				Bindings.add(widthProperty, additionalWidth), 
				Bindings.add(Bindings.divide(heightProperty, 2.0), additionalHeight));
		connectors.add(connector);
		
		// Konnektor-2: Mitte unten
		connector = new CircleConnector(
				Bindings.add(Bindings.divide(widthProperty, 2.0), additionalWidth),
				Bindings.add(heightProperty, additionalHeight));
		connectors.add(connector);
		
		// Konnektor-3: Mitte linke Seite
		connector = new CircleConnector(
				Bindings.add(Bindings.multiply(xProperty,  0.0), additionalWidth), 
				Bindings.add(Bindings.divide(heightProperty, 2.0), additionalHeight));
		connectors.add(connector);
		
		// Konnektor-4: Mitte oben
		connector = new CircleConnector(
				Bindings.add(Bindings.divide(widthProperty, 2.0), additionalWidth),
				Bindings.add(Bindings.multiply(heightProperty(), 0.0), additionalHeight));
		connectors.add(connector);
		
		// Rectangle und Konnektoren zum Pane hinzufügen
		this.getChildren().add(rect);
		List<Node> nodeConnectors = new ArrayList<>();
		for (IConnector c : connectors) { nodeConnectors.add((Node)c); }
		this.getChildren().addAll(nodeConnectors);
	}
}

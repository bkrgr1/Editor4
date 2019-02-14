package de.bkroeger.editor4.view;

import java.util.ArrayList;
import java.util.List;

import de.bkroeger.editor4.model.LineEndingType;
import de.bkroeger.editor4.model.LineStrokeType;
import de.bkroeger.editor4.model.Vector2D;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

/**
 * <p>Zeichnet einen direkten Pfeil.</p>
 * <p>Der Pfeil wird durch zwei Punkte definiert.</p>
 * <p>Der Pfeil zeigt von p1 (Anfang) auf p2 (Ende).</p>
 * <p>Aus den beiden Punkten wird ein Vector mit Location in p1 berechnet.
 * Zum Zeichnen wird ein Pane in der Länge des Vectors plus Randzugabe gezeichnet
 * und um den Vector-Winkel gedreht. Basis für die Drehung ist p1.</p>
 * <p>Innerhalb des Shapes wird der eigentliche Pfeil gezeichnet.</p>
 * @author bk
 */
public class StraightArrowView extends Pane implements IArrowView {
	
	private static final double radiusX = 5.0;
	private static final double radiusY = 5.0;
	
	private static final Color BACKGROUND_COLOR = Color.LIGHTGREEN;
	
	private List<IConnector> connectors = new ArrayList<>();
	@Override
	public List<IConnector> getConnectors() { return connectors; }

	/**
	 * Constructor
	 * @param x1Property
	 * @param y1Property
	 * @param x2Property
	 * @param y2Property
	 * @param rotateProperty
	 */
	public StraightArrowView(DoubleProperty x1Property, DoubleProperty y1Property, 
			DoubleProperty x2Property, DoubleProperty y2Property,
			DoubleProperty rotateProperty,
			ObjectProperty<Color> colorProperty,
			ObjectProperty<LineEndingType> lineStartTypeProperty,
			ObjectProperty<LineEndingType> lineEndTypeProperty,
			ObjectProperty<LineStrokeType> strokeTypeProperty,
			DoubleProperty strokeWidthProperty) {
		
		// aus den beiden Punkten einen Vector inkl. Winkel berechnen
		Vector2D v1 = new Vector2D(x1Property.get(), y1Property.get(), 
				x2Property.get(), y2Property.get());
		
		// ein Pane in der notwendigen Grösse erzeugen
		this.layoutXProperty().bind(Bindings.add(x1Property, radiusX / -2.0));
		this.layoutYProperty().bind(Bindings.add(y1Property, radiusY / -2.0));
		// das Pane drehen; der Winkel ergibt sich aus den beiden Punkten
		Transform rotateTransform = new Rotate(v1.getAlpha() *-1.0, 
				radiusX / 2.0, 
				radiusY / 2.0);
		this.getTransforms().add(rotateTransform);
		this.setPrefWidth(v1.getLength() + radiusX);
		this.setPrefHeight(radiusY);
		this.setSelected(false);
		
		// einen Path für den Pfeil erzeugen
		Path path = new Path();
		
		// Anfangsposition
		MoveTo moveto1 = new MoveTo();
		moveto1.xProperty().set(radiusX / 2.0);
		moveto1.yProperty().set(radiusY / 2.0);
		
		// Linie zum Ziel
		LineTo lineto1 = new LineTo();
		lineto1.xProperty().set(v1.getLength() + (radiusX / 2.0));
		lineto1.yProperty().set(radiusY / 2.0);
		
		// Farbe setzen
		path.setStroke(colorProperty.get());
		// TODO: Änderungshandler definieren
		
		// Linienstärke setzen
		path.setStrokeWidth(strokeWidthProperty.get());
		// TODO: Änderungshandler definieren
		
		// TODO: Stroke Dash setzen
		// TODO: Änderungshandler definieren
		
		// TODO: Pfeilanfang zeichnen
		
		// TODO: Pfeilende zeichnen
		
		// alle Path-Elemente zusammenfügen
		path.getElements().addAll(moveto1, lineto1);

		// Path zum Pane hinzufügen
		this.getChildren().add(path);

		// Konnektoren am Anfang und Ende
		IConnector connector = null;
		connector = new CircleConnector(
				Bindings.add(Bindings.multiply(x1Property, 0.0), radiusX / 2.0), 
				Bindings.add(Bindings.multiply(y1Property, 0.0), radiusY / 2.0));
		((Node)connector).setVisible(false);
		connectors.add(connector);
		connector = new CircleConnector(
				Bindings.add(Bindings.multiply(x1Property, 0.0), radiusX / 2.0 + v1.getLength()), 
				Bindings.add(Bindings.multiply(y1Property, 0.0), radiusY / 2.0));
		((Node)connector).setVisible(false);
		connectors.add(connector);
		
		// Konnektoren zum Pane hinzufügen
		List<Node> nodeConnectors = new ArrayList<>();
		for (IConnector con : connectors) { nodeConnectors.add((Node)con); }
		this.getChildren().addAll(nodeConnectors);
		
	}
	
	@Override
	public void setSelected(boolean isSelected) {
		for (IConnector connector : connectors) {
			((Node)connector).setVisible(isSelected);
		}
		if (isSelected) {
			this.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		} else {
			this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		}
	}
}

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
import javafx.scene.shape.PathElement;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

/**
 * <p>
 * Zeichnet einen direkten Pfeil.
 * </p>
 * <p>
 * Der Pfeil wird durch zwei Punkte definiert.
 * </p>
 * <p>
 * Der Pfeil zeigt von p1 (Anfang) auf p2 (Ende).
 * </p>
 * <p>
 * Aus den beiden Punkten wird ein Vector mit Location in p1 berechnet. Zum
 * Zeichnen wird ein Pane in der Länge des Vectors plus Randzugabe gezeichnet
 * und um den Vector-Winkel gedreht. Drehpunkt ist p1.
 * </p>
 * <p>
 * Innerhalb dieses Panes wird der eigentliche Pfeil gezeichnet.
 * </p>
 * 
 * @author bk
 */
public class StraightArrowView extends Pane implements IArrowView {

	private static final double HEADING_LENGTH = 10.0;
	private static final double RADIUS_X = 8.0;
	private static final double RADIUS_Y = 8.0;

	private static final Color BACKGROUND_COLOR = Color.LIGHTGREEN;

	private List<IConnector> connectors = new ArrayList<>();

	@Override
	public List<IConnector> getConnectors() {
		return connectors;
	}

	private Vector2D vector1;

	public DoubleProperty lengthProperty() {
		return vector1.lengthProperty();
	}

	/**
	 * Constructor für einen direkten Pfeil aus 2 Punkten mit x- und y-Koordinaten.
	 * 
	 * @param x1Property            horizontal position of start point
	 * @param y1Property            verical position of start point
	 * @param x2Property            horizontal position of end point
	 * @param y2Property            vertical position of end point
	 * @param rotateProperty        rotation angle in degree
	 * @param colorProperty         stroke color of the arrow
	 * @param lineStartTypeProperty type of line start
	 * @param lineEndTypeProperty   type of line end
	 * @param strokeTypePropery     type of line stroke
	 * @param strokeWidthPropery    with of the line
	 */
	public StraightArrowView(DoubleProperty x1Property, DoubleProperty y1Property, DoubleProperty x2Property,
			DoubleProperty y2Property, DoubleProperty rotateProperty, ObjectProperty<Color> colorProperty,
			ObjectProperty<LineEndingType> lineStartTypeProperty, ObjectProperty<LineEndingType> lineEndTypeProperty,
			ObjectProperty<LineStrokeType> strokeTypeProperty, DoubleProperty strokeWidthProperty) {

		// aus den beiden Punkten einen Vector inkl. Winkel berechnen
		vector1 = new Vector2D(x1Property, y1Property, x2Property, y2Property);

		// ein Pane in der notwendigen Grösse erzeugen
		this.layoutXProperty().bind(Bindings.add(x1Property, RADIUS_X / -2.0));
		this.layoutYProperty().bind(Bindings.add(y1Property, RADIUS_Y / -2.0));

		// das Pane drehen; der Winkel ergibt sich aus den beiden Punkten
		Transform rotateTransform = new Rotate(vector1.getAlpha() * -1.0, RADIUS_X / 2.0, RADIUS_Y / 2.0);
		this.getTransforms().add(rotateTransform);
		vector1.alphaProperty().addListener((observable, oldValue, newValue) -> {
			Transform rotTrans = new Rotate(newValue.doubleValue() * -1.0, RADIUS_X / 2.0, RADIUS_Y / 2.0);
			getTransforms().clear();
			getTransforms().add(rotTrans);
		});

		this.setPrefWidth(vector1.lengthProperty().get() + RADIUS_X);
		vector1.lengthProperty().addListener((observable, oldValue, newValue) -> {
			this.setPrefWidth(vector1.lengthProperty().get() + RADIUS_X);
		});

		this.setPrefHeight(RADIUS_Y);
		this.setSelected(false);

		// einen Path für den Pfeil erzeugen
		Path path = new Path();

		List<PathElement> pathElements = new ArrayList<>();

		// Anfangsposition
		MoveTo moveto1 = new MoveTo();
		moveto1.xProperty().set(RADIUS_X / 2.0);
		moveto1.yProperty().set(RADIUS_Y / 2.0);
		pathElements.add(moveto1);

		// Linie zum Ziel
		LineTo lineto1 = new LineTo();
		lineto1.xProperty().bind(Bindings.add(vector1.lengthProperty(), (RADIUS_X / 2.0)));
		lineto1.yProperty().set(RADIUS_Y / 2.0);
		pathElements.add(lineto1);

		// Farbe setzen
		path.setStroke(colorProperty.get());
		// TODO: Änderungshandler definieren

		// Linienstärke setzen
		path.setStrokeWidth(strokeWidthProperty.get());
		// TODO: Änderungshandler definieren

		// TODO: Stroke Dash setzen
		// TODO: Änderungshandler definieren

		// Pfeilanfang zeichnen
		drawArrowStart(lineStartTypeProperty, pathElements);
		lineStartTypeProperty.addListener((observable, oldvalue, newvalue) -> {
			drawArrowStart(lineStartTypeProperty, pathElements);
		});

		// Pfeilende zeichnen
		drawArrowEnd(lineEndTypeProperty, pathElements);
		lineEndTypeProperty.addListener((observable, oldvalue, newvalue) -> {
			drawArrowEnd(lineEndTypeProperty, pathElements);
		});

		// alle Path-Elemente zusammenfügen
		path.getElements().addAll(pathElements);

		// Path zum Pane hinzufügen
		this.getChildren().add(path);
	}

	private void drawArrowStart(ObjectProperty<LineEndingType> lineStartTypeProperty, List<PathElement> pathElements) {

		switch (lineStartTypeProperty.get()) {
		case OpenArrow:
			MoveTo movetoS1 = new MoveTo();
			movetoS1.xProperty().set(RADIUS_X / 2.0 + HEADING_LENGTH);
			movetoS1.yProperty().set(0.0);
			pathElements.add(movetoS1);

			LineTo linetoS1 = new LineTo();
			linetoS1.xProperty().set(RADIUS_X / 2.0);
			linetoS1.yProperty().set(RADIUS_Y / 2.0);
			pathElements.add(linetoS1);

			MoveTo movetoS2 = new MoveTo();
			movetoS2.xProperty().set(RADIUS_X / 2.0 + HEADING_LENGTH);
			movetoS2.yProperty().set(RADIUS_Y);
			pathElements.add(movetoS2);

			LineTo linetoS2 = new LineTo();
			linetoS2.xProperty().set(RADIUS_X / 2.0);
			linetoS2.yProperty().set(RADIUS_Y / 2.0);
			pathElements.add(linetoS2);
			break;
		case FilledArrow:
			break;
		case Circle:
			break;
		case Quader:
			break;
		case None:
			break;
		default:
			break;
		}
	}

	private void drawArrowEnd(ObjectProperty<LineEndingType> lineStartTypeProperty, List<PathElement> pathElements) {

		switch (lineStartTypeProperty.get()) {
		case OpenArrow:
			MoveTo movetoS1 = new MoveTo();
			movetoS1.xProperty().bind(Bindings.add(vector1.lengthProperty(), (RADIUS_X / 2.0) - HEADING_LENGTH));
			movetoS1.yProperty().set(0.0);
			pathElements.add(movetoS1);

			LineTo linetoS1 = new LineTo();
			linetoS1.xProperty().bind(Bindings.add(vector1.lengthProperty(), (RADIUS_X / 2.0)));
			linetoS1.yProperty().set(RADIUS_Y / 2.0);
			pathElements.add(linetoS1);

			MoveTo movetoS2 = new MoveTo();
			movetoS2.xProperty().bind(Bindings.add(vector1.lengthProperty(), (RADIUS_X / 2.0) - HEADING_LENGTH));
			movetoS2.yProperty().set(RADIUS_Y);
			pathElements.add(movetoS2);

			LineTo linetoS2 = new LineTo();
			linetoS2.xProperty().bind(Bindings.add(vector1.lengthProperty(), (RADIUS_X / 2.0)));
			linetoS2.yProperty().set(RADIUS_Y / 2.0);
			pathElements.add(linetoS2);
			break;
		case FilledArrow:
			break;
		case Circle:
			break;
		case Quader:
			break;
		case None:
			break;
		default:
			break;
		}
	}

	@Override
	public void setSelected(boolean isSelected) {
		for (IConnector connector : connectors) {
			((Node) connector).setVisible(isSelected);
		}
		if (isSelected) {
			this.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		} else {
			this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		}
	}
}
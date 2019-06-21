package de.bkroeger.editor4.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.bkroeger.editor4.model.RectangleShapeModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

/**
 * <p>Zeichnet ein Rechteck-Shape.</p>
 * <p>Als Hintergrund wird ein transparentes {@link Pane} gezeichnet, das etwas größer ist als das Rechteck,
 * damit auch die Konnektoren gezeichnet werden können.</p>
 * 
 * @author bk
 */
public class RectangleShapeView extends BaseShapeView {
	
	private static final String RIGHT_RESIZE_ICON_PATH = "/images/iconmonstr-arrow-63-16.png";
	private static final String LEFT_RESIZE_ICON_PATH = "/images/iconmonstr-arrow-64-16.png";
	private static final String TOP_RESIZE_ICON_PATH = "/images/iconmonstr-arrow-66-16.png";
	private static final String BOTTOM_RESIZE_ICON_PATH = "/images/iconmonstr-arrow-65-16.png";
	private double additionalWidth = 15.0;
	private double additionalHeight = 15.0;
	
	private Rectangle rect;
	public Rectangle getRectangle() { return rect; }

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
		
		this.javaFXShape = rect;
		
		// die Konnectoren als Kreise zeichnen
		ImageView connector = null;
		
//		// Konnektor-1: Mitte rechte Seite
//		connector = new StraightConnectorView(
//				Bindings.add(widthProperty, additionalWidth), 
//				Bindings.add(Bindings.divide(heightProperty, 2.0), additionalHeight));
//		connectors.add(connector);
//		
//		// Konnektor-2: Mitte unten
//		connector = new StraightConnectorView(
//				Bindings.add(Bindings.divide(widthProperty, 2.0), additionalWidth),
//				Bindings.add(heightProperty, additionalHeight));
//		connectors.add(connector);
//		
//		// Konnektor-3: Mitte linke Seite
//		connector = new StraightConnectorView(
//				Bindings.add(Bindings.multiply(xProperty,  0.0), additionalWidth), 
//				Bindings.add(Bindings.divide(heightProperty, 2.0), additionalHeight));
//		connectors.add(connector);
//		
//		// Konnektor-4: Mitte oben
//		connector = new StraightConnectorView(
//				Bindings.add(Bindings.divide(widthProperty, 2.0), additionalWidth),
//				Bindings.add(Bindings.multiply(heightProperty(), 0.0), additionalHeight));
//		connectors.add(connector);
		
		// Rectangle und Konnektoren zum Pane hinzufügen
		this.getChildren().add(rect);
		
		List<Node> nodeConnectors = new ArrayList<>();
		for (ImageView c : connectors) { nodeConnectors.add((Node)c); }
		this.getChildren().addAll(nodeConnectors);
		
		// rechts
        resizeIcons.add(buildRightResizeIcon(widthProperty, heightProperty));
        resizeIcons.add(buildLeftResizeIcon(xProperty, heightProperty));
        resizeIcons.add(buildTopResizeIcon(widthProperty, heightProperty));
        resizeIcons.add(buildBottomResizeIcon(widthProperty, heightProperty));
		this.getChildren().addAll(resizeIcons);
	}
	
	/**
	 * <p>Zeichnet ein Rechteck als geschlossenen Pfad aus 4 Linien,</p>
	 * <p>Der Positionspunkt befindet sich nicht links oben, sondern
	 * %LocalWidth von Links und %LocalHeight von oben.</p>
	 * @param model das Model für das Rechteck
	 * @return das @link Path}-Objekt
	 */
	public static ViewNodes draw(RectangleShapeModel model) {
		
		ViewNodes nodes = new ViewNodes();
		
		Path path = new Path();
		
		MoveTo move1 = new MoveTo(x1 - localWidth, y1);
		LineTo line1 = new LineTo(x1 + width, y1);
		LineTo line2 = new LineTo(x1 + width, y1 + height);
		LineTo line3 = new LineTo(x1, y1 + height);
		LineTo line4 = new LineTo(x1, y1);
		
		path.getElements().addAll(move1, line1, line2, line3, line4);
		
		nodes.setPrimaryNode(path);
		
		return nodes;
	}
	
	private Node buildRightResizeIcon(DoubleProperty widthProperty, DoubleProperty heightProperty) {
		
		InputStream input = this.getClass().getResourceAsStream(RIGHT_RESIZE_ICON_PATH);		 
        Image image = new Image(input);
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        ImageView icon = new ImageView(image);
        icon.xProperty().bind(Bindings.add(widthProperty, additionalWidth + imageWidth/-2.0));
        icon.yProperty().bind(Bindings.add(Bindings.divide(heightProperty, 2.0), additionalHeight + imageHeight/-2.0));
        icon.setVisible(false);
        return icon;
	}
	
	private Node buildLeftResizeIcon(DoubleProperty xProperty, DoubleProperty heightProperty) {
		
		InputStream input = this.getClass().getResourceAsStream(LEFT_RESIZE_ICON_PATH);		 
        Image image = new Image(input);
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        ImageView icon = new ImageView(image);
        icon.xProperty().bind(Bindings.add(Bindings.multiply(xProperty,  0.0), additionalWidth + imageWidth/-2.0));
        icon.yProperty().bind(Bindings.add(Bindings.divide(heightProperty, 2.0), additionalHeight + imageHeight/-2.0));
        icon.setVisible(false);
        return icon;
	}
	
	private Node buildTopResizeIcon(DoubleProperty widthProperty, DoubleProperty heightProperty) {
		
		InputStream input = this.getClass().getResourceAsStream(TOP_RESIZE_ICON_PATH);		 
        Image image = new Image(input);
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        ImageView icon = new ImageView(image);
        icon.xProperty().bind(Bindings.add(Bindings.divide(widthProperty, 2.0), additionalWidth + imageWidth/-2.0));
        icon.yProperty().bind(Bindings.add(Bindings.multiply(heightProperty(), 0.0), additionalHeight + imageHeight/-2.0));
        icon.setVisible(false);
        return icon;
	}
	
	private Node buildBottomResizeIcon(DoubleProperty widthProperty, DoubleProperty heightProperty) {
		
		InputStream input = this.getClass().getResourceAsStream(BOTTOM_RESIZE_ICON_PATH);		 
        Image image = new Image(input);
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        ImageView icon = new ImageView(image);
        icon.xProperty().bind(Bindings.add(Bindings.divide(widthProperty, 2.0), additionalWidth + imageWidth/-2.0));
        icon.yProperty().bind(Bindings.add(heightProperty, additionalHeight + imageHeight/-2.0));
        icon.setVisible(false);
        return icon;
	}
}

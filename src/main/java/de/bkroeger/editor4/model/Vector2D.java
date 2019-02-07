package de.bkroeger.editor4.model;

import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;

/**
 * <p>Ein Vector mit Double-Werten.</p>
 * 
 * @author bk
 */
public class Vector2D {

	private static final Logger logger = Logger.getLogger(Vector2D.class.getName());

	private DoubleProperty alphaProperty = new SimpleDoubleProperty();
	public  DoubleProperty alphaProperty() { return alphaProperty; }
	public Double getAlpha() { return alphaProperty.get(); }
	public void   setAlpha(Double alpha) {
		this.alphaProperty.set(alpha);
		this.bProperty.bind(Bindings.multiply(Math.sin(Math.toRadians(alphaProperty.get())), this.lengthProperty));
		this.aProperty.bind(Bindings.multiply(Math.cos(Math.toRadians(alphaProperty.get())), this.lengthProperty));
	}
	
	private DoubleProperty aProperty = new SimpleDoubleProperty();
	public  Double getA() { return this.aProperty.get(); }
	
	private DoubleProperty bProperty = new SimpleDoubleProperty();
	public  Double getB() { return this.bProperty.get(); }
	
	private DoubleProperty lengthProperty = new SimpleDoubleProperty();
	public  DoubleProperty lengthProperty() { return lengthProperty; }
	public Double getLength() { return lengthProperty.get(); }
	public void   setLength(Double c) {
		this.lengthProperty.set(c);
		this.bProperty.bind(Bindings.multiply(Math.sin(Math.toRadians(alphaProperty.get())), this.lengthProperty));
		this.aProperty.bind(Bindings.multiply(Math.cos(Math.toRadians(alphaProperty.get())), this.lengthProperty));
	}
	
	private Double x1;
	public void setX1(Double x1) { this.x1 = x1; }
	
	private Double y1;
	public void setY1(Double y1) { this.x1 = y1; }
	
	public Point2D getStartPoint() {
		return new Point2D(this.x1, this.y1);
	}
	public void setStartPoint(Point2D startPoint) {
		this.x1 = startPoint.getX();
		this.y1 = startPoint.getY();
	}
	
	public Vector2D(Double x1, Double y1, Double x2, Double y2) {
		
		this.x1 = x1;
		this.y1 = y1;
		
		this.aProperty.set(x2 - x1);	// Länge auf der X-Achse
		double aPow2 = Math.pow(this.aProperty.get(), 2.0);
		this.bProperty.set((y2 - y1) * -1.0); // Länge auf der Y-Achse; negativ, weil bei JavaFX die Y-Werte nach unten zunehmen
		double bPow2 = Math.pow(this.bProperty.get(), 2.0);
		double cPow2 = aPow2 + bPow2; // c^2 = a^2 + b^2
		this.lengthProperty.set(Math.sqrt(cPow2));
		
		// der Winkel berechnet sich aus der Umkehrung der Sin-Funktion; 
		// b / c : Umrechnung auf Einheitskreis
		this.alphaProperty.set(Math.toDegrees(Math.asin( this.bProperty.get() / this.lengthProperty.get())));
		logger.info(String.format("a=%f, b=%f, c=%f, alpha=%f", 
			aProperty.get(), bProperty.get(), 
			lengthProperty.get(), alphaProperty.get()));
	}
	
	public Vector2D(Point2D p, Point2D q) {
		this(p.getX(), p.getY(), q.getX(), q.getY());
	}
	
	public Vector2D(Double alpha, Double length) {
		setAlpha(alpha);
		setLength(length);
	}
}

package de.bkroeger.editor4.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * <p>Ein Vector aus 2 Punkten mit je 2 DoubleProperties.</p>
 * 
 * @author bk
 */
public class Vector2D {

	private static final Logger logger = LogManager.getLogger(Vector2D.class.getName());

	/**
	 * Der Winkel des Vektors
	 */
	private DoubleProperty alphaProperty = new SimpleDoubleProperty();
	public  DoubleProperty alphaProperty() { return alphaProperty; }
	public Double getAlpha() { return alphaProperty.get(); }
	
	/**
	 * Der horizontale Schenkel des Dreiecks
	 */
	private DoubleProperty aProperty = new SimpleDoubleProperty();
	public  Double getA() { return this.aProperty.get(); }
	
	/**
	 * Der vertikale Schenkel des Dreiecks
	 */
	private DoubleProperty bProperty = new SimpleDoubleProperty();
	public  Double getB() { return this.bProperty.get(); }
	
	/**
	 * Die Länge des Dreiecks = dritter Schenkel des Dreiecks
	 */
	private DoubleProperty lengthProperty = new SimpleDoubleProperty();
	public  DoubleProperty lengthProperty() { return lengthProperty; }
	public Double getLength() { return lengthProperty.get(); }
	public  Double getC() { return getLength(); }
	
	/**
	 * X-Koordinate des Startpunktes
	 */
	private DoubleProperty x1Property = new SimpleDoubleProperty();
	public void setX1(Double x1) { this.x1Property.set(x1); }
	
	/**
	 * Y-Koordinate des Startpunktes
	 */
	private DoubleProperty y1Property = new SimpleDoubleProperty();
	public void setY1(Double y1) { this.y1Property.set(y1); }
	
	/**
	 * X-Koordinate des Endpunktes
	 */
	private DoubleProperty x2Property = new SimpleDoubleProperty();
	public void setX2(Double x2) { this.x2Property.set(x2); }
	
	/**
	 * Y-Koordinate des Endpunktes
	 */
	private DoubleProperty y2Property = new SimpleDoubleProperty();
	public void setY2(Double y2) { this.y2Property.set(y2); }
	
	public Vector2D(DoubleProperty x1Property, DoubleProperty y1Property, DoubleProperty x2Property, DoubleProperty y2Property) {
		
		this.x1Property = x1Property;
		this.y1Property = y1Property;
		this.x2Property = x2Property;
		this.y2Property = y2Property;
		
		// horizontalen Schenkel berechnen
		this.aProperty.bind(Bindings.subtract(x2Property, x1Property));	// Länge auf der X-Achse
		// Quadrat über diesem Schenkel berechnen
		DoubleProperty aPow2 = new SimpleDoubleProperty( Math.pow(this.aProperty.get(), 2.0));
		
		// vertikalen Schenkel berechnen
		this.bProperty.bind(Bindings.multiply(Bindings.subtract(y2Property, y1Property), -1.0)); // Länge auf der Y-Achse; negativ, weil bei JavaFX die Y-Werte nach unten zunehmen
		// Quadrat über diesem Schenkel berechnen
		DoubleProperty bPow2 = new SimpleDoubleProperty( Math.pow(this.bProperty.get(), 2.0));
		
		// Quadrat über dem dritten Schenkel nach Pythagoras berechnen
		DoubleProperty cPow2 = new SimpleDoubleProperty(aPow2.get() + bPow2.get()); // c^2 = a^2 + b^2
		// die Länge des dritten Schenkels ist dann die Wurzel daraus
		this.lengthProperty.set(Math.sqrt(cPow2.get()));
		
		// der Winkel berechnet sich aus der Umkehrung der Sin-Funktion; 
		// b / c : Umrechnung auf Einheitskreis
		this.alphaProperty.set(Math.toDegrees(Math.asin( this.bProperty.get() / this.lengthProperty.get())));
		if (this.aProperty.get() < 0.0) {
			this.alphaProperty.set((90.0 - this.alphaProperty.get()) + 90.0);
		}
		
		// wenn sich der horizontale Schenkel ändert, die anderen Werte neu berechnen
		aProperty.addListener((observable, oldValue, newValue) -> {
			aPow2.set(Math.pow(newValue.doubleValue(), 2.0));
			cPow2.set(aPow2.get() + bPow2.get());
			this.lengthProperty.set(Math.sqrt(cPow2.get()));
		});		
		
		// wenn sich der vertikale Schenkel ändert, die anderen Werte neu berechnen
		bProperty.addListener((observable, oldValue, newValue) -> {
			bPow2.set(Math.pow(newValue.doubleValue(), 2.0));
			cPow2.set(aPow2.get() + bPow2.get());
			this.lengthProperty.set(Math.sqrt(cPow2.get()));
		});		
		
		// wenn sich die Länge des dritten Schenkels ändert, den Winkel neu bestimmen
		lengthProperty.addListener((observable, oldValue, newValue) -> {
			this.alphaProperty.set(Math.toDegrees(Math.asin( this.bProperty.get() / newValue.doubleValue())));
			if (this.aProperty.get() < 0.0) {
				this.alphaProperty.set((90.0 - this.alphaProperty.get()) + 90.0);
			}
		});		

		logger.debug(String.format("a=%f, b=%f, c=%f, alpha=%f", 
				aProperty.get(), bProperty.get(), 
				lengthProperty.get(), alphaProperty.get()));
	}
}

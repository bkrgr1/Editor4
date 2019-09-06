package de.bkroeger.editor4.functions;

import java.lang.reflect.Field;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class VariableDef<T> implements IVariable<T> {

	private static final String COLOR_CLASS_NAME = "javafx.scene.paint.Color";
	
	private T value;
	public void setValue(T value) { this.value = value; }
	public T getValue() { return this.value; }

	private String name;
	public void setName(String value) { this.name = value; }
	public String getName() { return this.name; }
	public String getKeyword() { return this.name; }
	
	public VariableDef(String name, T value) {
		this.name = name;
		this.value = value;
	}
	
	public Text buildText(int i, String[] colors) {
		
		Text t = new Text(this.getKeyword());
		Color color = null;
		try {
			// das Feld anhand des Namens aus der Klasse laden
		    Field field = Class.forName(COLOR_CLASS_NAME).getDeclaredField(colors[i].toUpperCase());
		    // den Wert des statischen Feldes ermitteln (keine Objekt-Instanz)
		    color = (Color)field.get(null);
		} catch (Exception e) {
		    color = Color.BLACK; // Not defined
		}
		t.setFill(color);
		return t;
	}
}

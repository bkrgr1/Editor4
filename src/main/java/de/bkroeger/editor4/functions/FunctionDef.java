package de.bkroeger.editor4.functions;

import java.lang.reflect.Field;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FunctionDef implements IFuncVarConst {

	private static final String COLOR_CLASS_NAME = "javafx.scene.paint.Color";
	
	private String name;
	
	public String getKeyword() { return name; }
	
	private IFunction function;
	
	public FunctionDef(String name, IFunction function) {
		this.name = name;
		this.function = function;
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

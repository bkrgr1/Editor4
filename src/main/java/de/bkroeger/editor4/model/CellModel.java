package de.bkroeger.editor4.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import de.bkroeger.editor4.Utils;
import de.bkroeger.editor4.Handler.CellValueEvent;
import de.bkroeger.editor4.Handler.CellValueListener;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Dies ist die Basis-Datenstruktur für eine Shape-Zelle.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
public class CellModel implements CellValueListener {

	private static final String COLOR_CLASS_NAME = "javafx.scene.paint.Color";

	private static final Logger logger = LogManager.getLogger(CellModel.class.getName());
	
	private static final String VARIABLE_PREFIX = "${";
	private static final String VARIABLE_SUFFIX = "}";
	private static final String APOSTROPHE = "'";

	/**
	 * Liefert den Schlüssel dieser Zelle
	 * @return Schlüssel der Zelle
	 */
	public String getKey() {
		String sectionKey = this.section.getKey();
		return sectionKey+
				"."+this.nameU;
	}
	
	/**
	 * Map der {@link CellValueListener}. 
	 * Dies sind alle Listener, die für die Berechnung der Formel erforderlich sind.
	 */
	private Map<String, CellModel> referencedCells = new HashMap<>();
	
	/**
	 * Legt fest, welchen Datentyp diese Zelle als Wert liefert.
	 */
	private CellValueType cellValueType = CellValueType.number;

	/**
	 * Die Formel für den Zellenwert als String
	 */
	private String formula;
	
	public void initFormula(String value) {
		this.formula = value;
	}
	
	/**
	 * Ändert die Formel bzw. den Wert für diese Zelle
	 * @param value
	 * @throws CellCalculationException
	 */
	public void setFormula(String value) throws CellCalculationException {
		
		this.formula = value;
		
		this.calculate(false);
		
		// registrierte Listener über Änderung informieren
		notifyListener();
	}
	
	private boolean calculated = false;

	private void notifyListener() {
		for (CellValueListener listener : this.cellListeners) {
			CellValueEvent event = new CellValueEvent(this.getKey());
			listener.cellValueChanged(event);
		}
	}
	
	/**
	 * Berechnet die Cell-Formeln und erstellt CellValueListener.
	 * @throws CellCalculationException
	 */
	public void calculate(boolean firstRound) throws CellCalculationException {
		
		// bisher registrierte Listener löschen
		clearReferencedCells();
		
		// Wert berechnen und Listener für verwendete Variablen registrieren
		switch (this.cellValueType) {
		case number:
			// numerischen Wert berechnen
			this.setDoubleValue(calcDouble(firstRound));			
			logger.debug("Calculate cell="+this.nameU+" value="+this.getDoubleValue());
			break;
		case bool:
			// Bolean-Wert berechnen
			this.setBooleanValue(calcBoolean(firstRound));
			logger.debug("Calculate cell="+this.nameU+" value="+this.getBooleanValue());
			break;
		case string:
			// String Wert berechnen
			this.setStringValue(calcString(firstRound));
			logger.debug("Calculate cell="+this.nameU+" value="+this.getStringValue());
			break;
		case object:
			// Object Wert berechnen
			this.setObjectValue(calcObject(firstRound));
			logger.debug("Calculate cell="+this.nameU+" value="+
				(this.getObjectValue() != null ? this.getObjectValue().toString() : "null"));
			break;
		default:
			throw new CellCalculationException("Invalid cell value type");
		}
		
		this.calculated = true;
	}

	/**
	 * Löscht die Liste der registrierten CellValueListener
	 */
	private void clearReferencedCells() {
		for (CellModel cell : this.referencedCells.values()) {
			cell.removeCellListener(this);
		}
		this.referencedCells.clear();
	}
	
	/**
	 * Eine Liste der {@link CellValueListener}, die auf Änderungen dieser Zelle warten.
	 */
	private List<CellValueListener> cellListeners = new ArrayList<>();
	
	public void addCellListener(CellValueListener listener) {
		cellListeners.add(listener);
	}
	
	public void removeCellListener(CellValueListener listener) {
		cellListeners.remove(listener);
	}
	
	/**
	 * Backreferenz zur Section
	 */
	private SectionModel section;
	
	/**
	 * sprachunabhängiger Name
	 */
	private String nameU;
	
	/**
	 * sprachspezifischer Name
	 */
	private String name;
	
	/**
	 * Dies ist der berechnete Wert der Zelle
	 */
	private DoubleProperty doubleProperty = new SimpleDoubleProperty();
	
	public DoubleProperty getDoubleProperty() throws CellCalculationException {
		if (!this.calculated) {
			this.calculate(false);
		}
		return this.doubleProperty;
	}
	
	public void setDoubleValue(Double value) {
		this.doubleProperty.set(value);
	}
	
	public Double getDoubleValue() {
		return this.doubleProperty.get();
	}
	
	private ObjectProperty<String> stringProperty = new SimpleObjectProperty<>();
	
	public ObjectProperty<String> getStringProperty() throws CellCalculationException {
		if (!this.calculated) {
			this.calculate(false);
		}
		return this.stringProperty;
	}

	public void setStringValue(String value) {
		this.stringProperty.set(value);
	}
	
	public String getStringValue() {
		return this.stringProperty.get();
	}
	
	private BooleanProperty booleanProperty = new SimpleBooleanProperty();
	
	public BooleanProperty getBooleanProperty() throws CellCalculationException {
		if (!this.calculated) {
			this.calculate(false);
		}
		return this.booleanProperty;
	}
	
	public void setBooleanValue(Boolean value) {
		this.booleanProperty.set(value);
	}
	
	public Boolean getBooleanValue() {
		return this.booleanProperty.get();
	}
	
	private ObjectProperty<Object> objectProperty = new SimpleObjectProperty<>();
	
	public ObjectProperty<Object> getObjectProperty() throws CellCalculationException {
		if (!this.calculated) {
			this.calculate(false);
		}
		return this.objectProperty;
	}

	public void setObjectValue(Object value) {
		this.objectProperty.set(value);
	}
	
	public Object getObjectValue() {
		return this.objectProperty.get();
	}
	
	// Constructors
	
	public CellModel(String nameU) {
		this.nameU = nameU;
	}
	
	// Methoden
	
	private Object calcObject(boolean firstRound) throws CellCalculationException {
		Object result = calculateValue(this.formula, firstRound);
		return result;
	}
	
	private String calcString(boolean firstRound) throws CellCalculationException {
		Object result = calculateValue(this.formula, firstRound);
		if (result instanceof String) return (String) result;
		throw new CellCalculationException("Invalid result type");
	}
	
	private Boolean calcBoolean(boolean firstRound) throws CellCalculationException {
		Object result = calculateValue(this.formula, firstRound);
		if (result instanceof Boolean) return (Boolean) result;
		throw new CellCalculationException("Invalid result type");
	}
	
	private Double calcDouble(boolean firstRound) throws CellCalculationException {
		Object result = calculateValue(this.formula, firstRound);
		if (result instanceof Double) return (Double) result;
		throw new CellCalculationException("Invalid result type");
	}

	/**
	 * Berechnet den Wert dieser Zelle
	 * @return
	 * @throws CellCalculationException
	 */
	private Object calculateValue(String expression, boolean firstRound) throws CellCalculationException {
		
		@SuppressWarnings("unused")
		String cellName = this.nameU;
		
		if (expression == null) return null;
		String formPart = expression.trim();
		Object result = null;
		
		// ist dies ein numerischer Wert?
		if ((result = Utils.isParsable(formPart)) != null) {
			
		} else if (formPart.startsWith(APOSTROPHE) && formPart.endsWith(APOSTROPHE)) {
			
			return formPart.substring(1, formPart.length()-1);
			
		} else if (formPart.toLowerCase().equals("true") || formPart.toLowerCase().equals("false")) {
			result = Boolean.parseBoolean(formPart.toLowerCase());
			return result;
			
		} else if (formPart.startsWith(VARIABLE_PREFIX)) {
			
			if (!firstRound) {
				return calculateVariableValue(formPart, firstRound);
			} else return null;
			
		} else {
			
			// ist dies eine Funktion?
			return calculateFunction(formPart, firstRound);
			
		}
		return result;
	}
	
	private Object calculateVariableValue(String formPart, boolean firstRound) throws CellCalculationException {
		
		Object result = null;
		if (formPart.endsWith(VARIABLE_SUFFIX)) {
			
			String variableName = formPart.substring(VARIABLE_PREFIX.length());
			variableName = variableName.substring(0, variableName.length()-1);
			
			CellModel cell = searchForVariable(variableName);
			if (cell != null) {
				switch (cell.getCellValueType()) {
				case string:
					return cell.getStringValue();
				case bool:
					return cell.getBooleanValue();
				case number:
					return cell.getDoubleValue();
				case object:
					return cell.getObjectValue();
				}
			} else {
				throw new CellCalculationException("Cell '"+variableName+"' not found");
			}
			
		} else {
			throw new CellCalculationException("Invalid variable syntax: "+formPart);
		}
		return result;
	}
	
	/**
	 * <p>Sucht nach einer Variablen.</p>
	 * @param name
	 * @return
	 * @throws CellCalculationException
	 */
	private CellModel searchForVariable(String name) throws CellCalculationException {
		
		CellModel cell = null;
		if (name.contains(".")) {
						
			cell = scanUp(name, cell);
			
		} else {
			
			cell = this.section.getCellByName(name);
		}
		
		if (cell != null) {
			return cell;
		} else {
			throw new CellCalculationException("Cell not found: "+name);
		}
	}

	private CellModel scanUp(String name, CellModel cell) throws CellCalculationException {
		
		// obersten Begriff ermitteln
		int p = name.indexOf(".");
		String sectionName = name.substring(0, p);
		String subName = name.substring(p+1);
		
		// alle übergeordneten Sections danach durchsuchen
		IModel model = this.section;
		do {				
			// gibt es diese Section dort?
			List<SectionModel> sects = model.searchSections(sectionName);
			if (sects != null && sects.size() == 1) {
				
				cell = scanDown(subName, sects.get(0));
			} else {
				
				model = model.getParentModel();
			}
		} while (cell == null && model != null);
		return cell;
	}
	
	private CellModel scanDown(String name, SectionModel model) throws CellCalculationException {
		
		CellModel cell = null;
		if (name.contains(".")) {
			
			// obersten Begriff ermitteln
			int p = name.indexOf(".");
			String sectionName = name.substring(0, p);
			String subName = name.substring(p+1);

			List<SectionModel> sects = model.searchSections(sectionName);
			if (sects.size() == 1) {
				
				cell = scanDown(subName, sects.get(0));
			} else if (sects.size() == 0) {
				// Fehler: Section nicht gefunden
				throw new CellCalculationException("Section not found: "+sectionName);
			}
		} else {
			
			String cellName = name;
			cell = model.getCellByName(cellName);
		}
		return cell;
	}
	
	private Object calculateFunction(String formPart, boolean firstRound) throws CellCalculationException {
		Object result = null;
		
		int p = formPart.indexOf("(");
		String functionName = formPart.substring(0,p);
		String expression = formPart.substring(p+1);
		expression = expression.substring(0, expression.length()-1);
		
		switch (functionName) {
		case "concat":
			result = calculate_concat(expression, firstRound);
			break;
			
		case "text":
			result = calculate_text(expression, firstRound);
			break;
			
		case "int":
			result = calculate_int(expression, firstRound);
			break;
			
		case "color":
			result = calculate_color(expression, firstRound);
			break;
			
		default:
			throw new CellCalculationException("Invalid function name: "+functionName);
		}
		return result;
	}
	
	/**
	 * <p>Calculate a color object.</p>
	 * <p>Die Klasse {@link javafx.scene.paint.Color} enthält static fields benannt
	 * nach den Farbnamen. Die Namen sind in Grossbuchstaben.</p>
	 * @param expression
	 * @param firstRound
	 * @return
	 */
	private Object calculate_color(String expression, boolean firstRound) {
		Color color = null;
		try {
			// das Feld anhand des Namens aus der Klasse laden
		    Field field = Class.forName(COLOR_CLASS_NAME).getDeclaredField(expression.toUpperCase());
		    // den Wert des statischen Feldes ermitteln (keine Objekt-Instanz)
		    color = (Color)field.get(null);
		} catch (Exception e) {
		    color = null; // Not defined
		}
		return color;
	}

	/**
	 * Setzt mehrere Strings zusammen
	 * @param expression
	 * @return
	 * @throws CellCalculationException
	 */
	private Object calculate_concat(String expression, boolean firstRound) throws CellCalculationException {
		String result = "";
		String[] parts = expression.split(",");	
		for (int i=0; i<parts.length; ++i) {
			Object r = calculateValue(parts[i], firstRound);
			result += (r != null ? r.toString() : "");
		}
		return result;
	}
	
	/**
	 * Konvertiert ein Ergebniss in einen Text
	 * @param expression
	 * @return
	 * @throws CellCalculationException
	 */
	private Object calculate_text(String expression, boolean firstRound) throws CellCalculationException {
		Object result = calculateValue(expression, firstRound);
		return (result != null ? result.toString() : null);
	}
	
	private Object calculate_int(String expression, boolean firstRound) throws CellCalculationException {
		Object r = calculateValue(expression, firstRound);
		if (r instanceof Double) {
			return ((Double)r).intValue();
		} else if (r instanceof Integer) {
			return r;
		} else if (r instanceof Boolean) {
			if ((Boolean)r) return new Integer(1); else return new Integer(0);
		} else {
			if (!firstRound) {
				throw new CellCalculationException("Cannot convert expression to integer: "+expression);
			} else return null;
		}
	}

	@Override
	public void cellValueChanged(CellValueEvent event) {
		// TODO Was muss passieren, wenn sich ein verwendeter Wert ändert
	}
	
	public CellModel(String nameU, String formula, SectionModel section) {
		this.nameU = nameU;
		this.formula = formula;
		this.section = section;
	}
	
	public CellModel(String nameU, String formula, CellValueType cellValueType) {
		this.nameU = nameU;
		this.formula = formula;
		this.cellValueType = cellValueType;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CellModel(CellModel model) throws CellCalculationException {
		this.booleanProperty = (model.getBooleanProperty() != null ? 
				new SimpleBooleanProperty(model.getBooleanProperty().get()) : null);
		this.calculated = model.calculated;
		this.cellListeners = cloneCellListeners(model.getCellListeners());
		this.cellValueType = model.cellValueType;
		this.doubleProperty = (model.getDoubleProperty() != null ?
				new SimpleDoubleProperty(model.getDoubleProperty().get()) : null);
		this.formula = (model.formula != null ? new String(model.formula) : null);
		this.name = (model.name != null ? new String(model.name) : null);
		this.nameU = new String(model.nameU);
		this.objectProperty =(model.getObjectProperty() != null ?
				new SimpleObjectProperty(model.getObjectProperty().get()) : null);
		this.referencedCells = cloneCells(model.referencedCells);
		this.section = model.section;
		this.stringProperty = (model.getStringProperty() != null ?
				new SimpleObjectProperty<String>(model.getStringProperty().get()) : null);
	}
	
	private Map<String, CellModel> cloneCells(Map<String, CellModel> referencedCells2) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<CellValueListener> cloneCellListeners(List<CellValueListener> cellListeners2) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Erstellt aus dem JSON-Element ein CellModel-Object.
	 * @param jsonCell
	 * @param nameU
	 * @param cellFormula
	 * @param section
	 * @return ein {@link CellModel}
	 * @throws TechnicalException
	 * @throws InputFileException
	 */
	public static CellModel loadCell(
			JSONObject jsonCell, SectionModel section) 
			throws TechnicalException, InputFileException {

		CellModel cellModel = null;
		if (jsonCell.containsKey("nameU")) {
			cellModel = new CellModel((String) jsonCell.get("nameU"));
		} else {
			throw new InputFileException("Cell attribute 'nameU' missing: "+jsonCell.toJSONString());
		}
		
		if (jsonCell.containsKey("name")) {
			cellModel.name = (String) jsonCell.get("name");
		}
		
		if (jsonCell.containsKey("formula")) {
			cellModel.formula = (String) jsonCell.get("formula");
		} else {
			throw new InputFileException("Cell attribute 'formula' missing: "+jsonCell.toJSONString());
		}
		
		if (jsonCell.containsKey("dataType")) {
			String dataType = (String) jsonCell.get("dataType");
			cellModel.cellValueType = CellValueType.valueOf(dataType);
		} else {
			cellModel.cellValueType = CellValueType.number;
		}
		
		cellModel.setSection(section);
		return cellModel;
	}
}

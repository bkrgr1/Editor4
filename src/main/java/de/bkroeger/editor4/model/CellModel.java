package de.bkroeger.editor4.model;

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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
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
	private CellValueType cellValueType;

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
		
		this.calculate();
		
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
	public void calculate() throws CellCalculationException {
		
		// bisher registrierte Listener löschen
		clearReferencedCells();
		
		// Wert berechnen und Listener für verwendete Variablen registrieren
		switch (this.cellValueType) {
		case number:
			// numerischen Wert berechnen
			this.setDoubleValue(calcDouble());			
			logger.debug("Calculate cell="+this.nameU+" value="+this.getDoubleValue());
			break;
		case bool:
			// Bolean-Wert berechnen
			this.setBooleanValue(calcBoolean());
			logger.debug("Calculate cell="+this.nameU+" value="+this.getBooleanValue());
			break;
		case string:
			// String Wert berechnen
			this.setStringValue(calcString());
			logger.debug("Calculate cell="+this.nameU+" value="+this.getStringValue());
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
			this.calculate();
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
			this.calculate();
		}
		return this.stringProperty;
	}

	public void setStringValue(String value) {
		this.stringProperty.set(value);
	}
	
	public String getStringValue() {
		return this.stringProperty.get();
	}
	
	private ObjectProperty<Boolean> booleanProperty = new SimpleObjectProperty<>();
	
	public ObjectProperty<Boolean> getBooleanProperty() throws CellCalculationException {
		if (!this.calculated) {
			this.calculate();
		}
		return this.booleanProperty;
	}
	
	public void setBooleanValue(Boolean value) {
		this.booleanProperty.set(value);
	}
	
	public Boolean getBooleanValue() {
		return this.booleanProperty.get();
	}
	
	// Constructors
	
	public CellModel(String nameU) {
		this.nameU = nameU;
	}
	
	// Methoden
	
	private String calcString() throws CellCalculationException {
		Object result = calculateValue(this.formula);
		if (result instanceof String) return (String) result;
		throw new CellCalculationException("Invalid result type");
	}
	
	private Boolean calcBoolean() throws CellCalculationException {
		Object result = calculateValue(this.formula);
		if (result instanceof Boolean) return (Boolean) result;
		throw new CellCalculationException("Invalid result type");
	}
	
	private Double calcDouble() throws CellCalculationException {
		Object result = calculateValue(this.formula);
		if (result instanceof Double) return (Double) result;
		throw new CellCalculationException("Invalid result type");
	}

	/**
	 * Berechnet den Wert dieser Zelle
	 * @return
	 * @throws CellCalculationException
	 */
	private Object calculateValue(String expression) throws CellCalculationException {
		
		@SuppressWarnings("unused")
		String cellName = this.nameU;
		
		if (expression == null) return null;
		String formPart = expression.trim();
		Object result = null;
		
		// ist dies ein numerischer Wert?
		if ((result = Utils.isParsable(formPart)) != null) {
			
		} else if (formPart.startsWith(APOSTROPHE) && formPart.endsWith(APOSTROPHE)) {
			
			return formPart.substring(1, formPart.length()-1);
		} else {
			
			// ist dies eine Variable?
			if (formPart.startsWith(VARIABLE_PREFIX)) {
				return calculateVariableValue(formPart);
			} else {
			
				// ist dies eine Funktion?
				return calculateFunction(formPart);
			}
		}
		return result;
	}
	
	private Object calculateVariableValue(String formPart) throws CellCalculationException {
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
				}
			} else {
				throw new CellCalculationException("Cell '"+variableName+"' not found");
			}
			
		} else {
			throw new CellCalculationException("Invalid variable syntax: "+formPart);
		}
		return result;
	}
	
	private CellModel searchForVariable(String name) throws CellCalculationException {
		
		CellModel cell = null;
		if (!name.contains(".")) {
			
			String cellName = name;
			cell = this.section.getCell(cellName);
			
		} else {
			
			int p = name.lastIndexOf(".");
			String sectionName = name.substring(0, p);
			String cellName = name.substring(p+1);
			
			SectionModel section = this.section.searchForSection(sectionName);
			
			cell = section.getCell(cellName);
		}
		
		if (cell != null) return cell;
		throw new CellCalculationException("Cell not found: "+name);
	}
	
	private Object calculateFunction(String formPart) throws CellCalculationException {
		Object result = null;
		
		int p = formPart.indexOf("(");
		String functionName = formPart.substring(0,p);
		String expression = formPart.substring(p+1);
		expression = expression.substring(0, expression.length()-1);
		
		switch (functionName) {
		case "concat":
			result = calculate_concat(expression);
			break;
			
		case "text":
			result = calculate_text(expression);
			break;
			
		case "int":
			result = calculate_int(expression);
			break;
			
		default:
			throw new CellCalculationException("Invalid function name: "+functionName);
		}
		return result;
	}
	
	/**
	 * Setzt mehrere Strings zusammen
	 * @param expression
	 * @return
	 * @throws CellCalculationException
	 */
	private Object calculate_concat(String expression) throws CellCalculationException {
		String result = "";
		String[] parts = expression.split(",");	
		for (int i=0; i<parts.length; ++i) {
			Object r = calculateValue(parts[i]);
			result += r.toString();
		}
		return result;
	}
	
	/**
	 * Konvertiert ein Ergebniss in einen Text
	 * @param expression
	 * @return
	 * @throws CellCalculationException
	 */
	private Object calculate_text(String expression) throws CellCalculationException {
		Object result = calculateValue(expression);
		return result.toString();
	}
	
	private Object calculate_int(String expression) throws CellCalculationException {
		Object r = calculateValue(expression);
		if (r instanceof Double) {
			return ((Double)r).intValue();
		} else if (r instanceof Integer) {
			return r;
		} else if (r instanceof Boolean) {
			if ((Boolean)r) return new Integer(1); else return new Integer(0);
		} else {
			throw new CellCalculationException("Cannot convert expression to integer: "+expression);
		}
	}

	@Override
	public void cellValueChanged(CellValueEvent event) {
		// TODO Was muss passieren, wenn sich ein verwendeter Wert ändert
	}
	
	public CellModel(String nameU, String formula) {
		this.nameU = nameU;
		this.formula = formula;
	}
	
	/**
	 * Erstellt aus dem JSON-Element ein CellModel-Object.
	 * @param jsonCell
	 * @param nameU
	 * @param cellFormula
	 * @param section
	 * @return
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

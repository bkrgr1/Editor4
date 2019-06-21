package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import de.bkroeger.editor4.Utils;
import de.bkroeger.editor4.Handler.CellValueEvent;
import de.bkroeger.editor4.Handler.CellValueListener;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Dies ist die Basis-Datenstruktur für eine Shape-Zelle.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString
public class CellModel implements CellValueListener {
	
	/**
	 * Liefert den Schlüssel dieser Zelle
	 * @return Schlüssel der Zelle
	 */
	public String getKey() {
		String sectionKey = this.section.getKey();
		return sectionKey+
				"."+this.getCellType().toString();
	}
	
	private Map<String, CellModel> referencedCells = new HashMap<>();

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
	public void setFormula(String value) {
		
		this.formula = value;
	}
	
	public void calculate() throws CellCalculationException {
		
		// bisher registrierte Listener löschen
		for (CellModel cell : this.referencedCells.values()) {
			cell.removeCellListener(this);
		}
		this.referencedCells.clear();
		
		// Wert berechnen und Listener für verwendete Variablen registrieren
		this.setValue(calcDouble(this.formula));
		
		// registrierte Listener über Änderung informieren
		for (CellValueListener listener : this.cellListeners) {
			CellValueEvent event = new CellValueEvent(this.getKey());
			listener.cellValueChanged(event);
		}
	}
	
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
	
	private CellModelType cellType;
	
	/**
	 * Dies ist der berechnete Wert der Zelle
	 */
	private DoubleProperty valueProperty = new SimpleDoubleProperty();
	
	public void setValue(Double value) {
		this.valueProperty.set(value);
	}
	
	public Double getValue() {
		return this.valueProperty.get();
	}
	
	// Constructors
	
	public CellModel(CellModelType ct) {
		this.cellType = ct;
	}
	
	// Methoden
	
	/**
	 * Berechnet den Wert dieser Zelle
	 * @return
	 * @throws CellCalculationException
	 */
	private Double calcDouble(String formula) throws CellCalculationException {
		
		String[] formulaParts = formula.split("s");
		if (formulaParts.length == 1) {
			
			return calcSingleValue(formulaParts[0]);
		} else {
			
		}
		return null;
	}
	
	private Double calcSingleValue(String formPart) 
			throws CellCalculationException  {
		
		Double d = null;
		if ((d = Utils.isParsable(formPart)) != null) {
			return d;
			
		} else {
			
			String[] nameParts = formPart.split("\\.");
			CellModelType ct = null;
			SectionModelType st = null;
			CellModel cell = null;
			switch (nameParts.length) { 
			case 1:
				ct = Utils.isCellType(nameParts[0]);
				cell = this.getSection().getCell(ct);
				if (!referencedCells.containsKey(cell.getKey())) {
					referencedCells.put(cell.getKey(), cell);
				}
				return cell.getValue();
				
			case 2:
				st = Utils.isSectionType(nameParts[0]);
				ct = Utils.isCellType(nameParts[1]);
				cell = this.getSection().getShape().getSection(st).getCell(ct);
				if (!referencedCells.containsKey(cell.getKey())) {
					referencedCells.put(cell.getKey(), cell);
				}
				return cell.getValue();
				
			case 3:
				String id = nameParts[0];
				st = Utils.isSectionType(nameParts[1]);
				ct = Utils.isCellType(nameParts[2]);
//				cell = this.getSection().getShape().getPage().getShape(id).getSection(st).getCell(ct);
				if (!referencedCells.containsKey(cell.getKey())) {
					referencedCells.put(cell.getKey(), cell);
				}
				return cell.getValue();
				
			default:
				throw new CellCalculationException("Variable name contains too many parts: '"+formPart+"'");				
			}
		}
	}

	@Override
	public void cellValueChanged(CellValueEvent event) {
		// TODO Was muss passieren, wenn sich ein verwendeter Wert ändert
	}
	
	public CellModel(CellModelType type, String formula) {
		this.cellType = type;
		this.formula = formula;
	}
	
	public static CellModel loadCell(
			JSONObject jsonCell, String cellType, String cellFormula, SectionModel section) 
			throws TechnicalException, InputFileException {
		
		CellModelType cellModelType = null;
		try {
			cellModelType = CellModelType.valueOf(cellType);
		} catch(Exception e) {
			throw new InputFileException("Cell model type not defined: "+cellType);
		}
		CellModel cellModel = new CellModel(cellModelType);
		cellModel.setFormula(cellFormula);
		
		cellModel.setSection(section);
		return cellModel;
	}
}

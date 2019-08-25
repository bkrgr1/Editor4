package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Dies ist die Basis-Datenstruktur für eine Shape-Section.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString
public class SectionModel implements IModel {

	private static final Logger logger = LogManager.getLogger(SectionModel.class.getName());

    private static final String CELLS_KEY = "cells";
	private static final String SECTIONS_KEY = "sections";
	private static final String DESCRIPTION_KEY = "description";
	private static final String UNIVERSAL_NAME_KEY = "nameU";
	private static final String NAME_KEY = "name";
	private static final String ID_KEY = "id";
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	/**
	 * Art der Section
	 */
	protected SectionModelType sectionType;
	
	/**
	 * die interne Id
	 */
	protected UUID id;
	
	/**
	 * der sprachspezifische Name
	 */
	protected String name;
	
	/**
	 * der sprachunabhängige Name
	 */
	protected String nameU;
	
	/**
	 * die Beschreibung
	 */
	protected String description;
	
	/**
	 * das Parent-Model
	 */
	protected IModel parentModel;

	/**
	 * Map der Sections
	 */
	protected List<SectionModel> sections = new ArrayList<>();
	
	protected String getKey() {
		return "";
	}
    
	/**
	 * Sucht nach Sections mit dem angegebenen {@link SectionModelType}.
     * @param type a {@link SectionModelType}
     * @return a list of {@link SectionModel}
	 */
    public List<SectionModel> selectSections(SectionModelType type) {

    	return sections.stream()
    		.filter(s -> (s.getSectionType() == type))
    		.collect(Collectors.toList());
    }
    
    /**
     * <p>Sucht nach Sections mit dem Universal-Name.</p>
     * @param aName the universal name of the section
     * @return a list of {@link SectionModel}
     */
    public List<SectionModel> searchSections(String aName) {
    	
    	return sections.stream()
    		.filter(s -> s.getNameU().equals(aName))
    		.collect(Collectors.toList());
    }
    
    public SectionModel getSection(SectionModelType type) 
    		throws InputFileException, CellCalculationException {
    	
    	List<SectionModel> sections = selectSections(type);
    	if (sections.size() == 1) return sections.get(0);
    	else if (sections.size() == 0) throw new InputFileException("Section with type '"+type.toString()+"' not found");
    	else throw new InputFileException("Only one location section allowed per shape");
    }
    
    public void addSection(SectionModel section) {
    	this.sections.add(section);
    }

    /**
     * Map der Zellen
     */
    protected Map<String, CellModel> cells = new HashMap<>();
    
    /**
     * Fügt eine Zelle hinzu
     * @param cell
     */
    public void addCell(CellModel cell) {
    	this.cells.put(cell.getNameU().toLowerCase(), cell);
    }
    
    /**
     * Sucht nach einer Zelle anhand des NameU oder des Name
     */
    public CellModel getCellByName(String name) {
    	
    	// Direktzugriff mit nameU
    	if (this.cells.containsKey(name.toLowerCase())) {
    		return this.cells.get(name.toLowerCase());
    	} else {
	    	
	    	// Suche nach Name
    		for (CellModel cell : this.cells.values()) {
    			if (cell.getName() != null && cell.getName().equalsIgnoreCase(name)) {
    				return cell;
    			}
    		}
    		return null;
    	}
    }
	
	/**
	 * Backreferenz auf das Shape
	 */
	private ShapeModel shape;
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public SectionModel(SectionModelType st) {
		this.sectionType = st;
	}
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/
    
    /**
     * <p>Alle Formeln berechnen.</p>
     * <p>Im ersten Durchlauf werden die absoluten Werte berechnet.
     * Im zweiten Durchlauf auch die Referenzen auf andere Variablen.</p>
     * @param firstRound True, wenn erster Durchlauf, sonst False
     * @throws CellCalculationException 
     */
    public IModel calculate(boolean firstRound) throws CellCalculationException {
    	
		String sectionName = this.sectionType.toString();
    	logger.debug("Calculate section: "+sectionName);
    	
    	// alle Cells berechnen; erste Runde ohne Variablen
    	for (CellModel cell : this.cells.values()) {
    		cell.calculate(firstRound);
    	}
    	
    	// alle Sections berechnen
    	for (IModel model : this.sections) {
    		
    		model.calculate(firstRound);
    	}
    	
    	return this;
    }
	
	/**
	 * <p>Lädt eine Section anhand des SectionType aus dem JSON-Model.</p>
	 * @param jsonSection JSON-Objekt mit den Daten der Section
	 * @param sectionType Art der Section
	 * @return das gefüllte {@link SectionModel}
	 * @throws TechnicalException
	 * @throws InputFileException
	 */
	public static SectionModel loadSection(
		JSONObject jsonSection, String sectionType, IModel parentModel) 
			throws TechnicalException, InputFileException {
		
		// Section je nach Typ laden
		switch (sectionType) {
		case "Page":
			PageModel pageModel = new PageModel();
			pageModel.setParentModel(parentModel);
			pageModel.loadModel(jsonSection, parentModel);
			return pageModel;
		case "Shape":
			ShapeModel shapeModel = new ShapeModel();
			shapeModel.setParentModel(parentModel);
			shapeModel.loadModel(jsonSection, parentModel);
			return shapeModel;
		case "Path":
			PathModel pathModel = new PathModel(PathType.DrawingPath);
			pathModel.setParentModel(parentModel);
			pathModel.loadModel(jsonSection, parentModel);
			return pathModel;
		case "Location":
			LocationModel locationModel = new LocationModel();
			locationModel.setParentModel(parentModel);
			locationModel.loadModel(jsonSection, parentModel);
			return locationModel;
		case "Center":
			CenterModel centerModel = new CenterModel();
			centerModel.setParentModel(parentModel);
			centerModel.loadModel(jsonSection, parentModel);
			return centerModel;
		case "MoveTo":
			PathElementModel elemModel1 = new PathElementModel(PathElementType.MoveTo);
			elemModel1.setParentModel(parentModel);
			elemModel1.loadModel(jsonSection, parentModel);
			return elemModel1;
		case "LineTo":
			PathElementModel elemModel2 = new PathElementModel(PathElementType.LineTo);
			elemModel2.setParentModel(parentModel);
			elemModel2.loadModel(jsonSection, parentModel);
			return elemModel2;
		case "ClosePath":
			PathElementModel elemModel3 = new PathElementModel(PathElementType.ClosePath);
			elemModel3.setParentModel(parentModel);
			elemModel3.loadModel(jsonSection, parentModel);
			return elemModel3;
		case "ArcTo":
			PathElementModel elemModel4 = new PathElementModel(PathElementType.ArcTo);
			elemModel4.setParentModel(parentModel);
			elemModel4.loadModel(jsonSection, parentModel);
			return elemModel4;
		case "Style":
			StyleModel styleModel = new StyleModel();
			styleModel.setParentModel(parentModel);
			styleModel.loadModel(jsonSection, parentModel);
			return styleModel;
		default:
			throw new InputFileException("Section model type not defined: "+sectionType);
		}
	}
	
	/**
	 * <p>Lädt gemeinsame Elemente aller Section.</p>
	 * @param jsonSection ein JSON-Objekt mit den Daten der Section
	 * @param parentSection eine Referenz auf das Model der übergeordneten Section
	 * @return dieses {@link SectionModel}
	 * @throws TechnicalException
	 * @throws InputFileException
	 */
	public SectionModel loadModel(JSONObject jsonSection, IModel parentSection) 
			throws TechnicalException, InputFileException {
		
		// Standardfields
    	for (Object key : jsonSection.keySet()) {
    		String k = key.toString();
    		switch (k) {
    		case ID_KEY:
    			String uuid = (String) jsonSection.get(ID_KEY);
    			this.setId(uuid != null ? UUID.fromString(uuid) : UUID.randomUUID());
    			break;
	    	
    		case NAME_KEY:
	    		this.name = (String) jsonSection.get(NAME_KEY);
	    		break;
	    	
    		case UNIVERSAL_NAME_KEY:
	    		this.nameU = (String) jsonSection.get(UNIVERSAL_NAME_KEY);
	    		break;
	    	
    		case DESCRIPTION_KEY:
	    		this.description = (String) jsonSection.get(DESCRIPTION_KEY);
	    		break;
	    		
    		case SECTIONS_KEY:	    
    		case CELLS_KEY:
		    	// skip
		    	break;
    			
    		default:
    			// ignore section specific entries
    		}
    	}

		
		// Liste der abhängigen Sections laden
		if (jsonSection.containsKey("sections")) {
	    	JSONArray jsonSections = (JSONArray) jsonSection.get("sections");
	    	for (int i=0; i<jsonSections.size(); i++) {
	    		
	    		// eine Section lesen
	    		JSONObject jsonSection2 = (JSONObject) jsonSections.get(i);
	    		
	    		SectionModel sectModel = 
    				SectionModel.loadSection(
						jsonSection2, 
						(String)jsonSection2.get("sectionType"), 
						this);
	    		this.addSection(sectModel);
	    	}
		}
		
		// Liste der Zellen laden
		if (jsonSection.containsKey("cells")) {
			JSONArray jsonCells = (JSONArray) jsonSection.get("cells");
			for (int i=0; i<jsonCells.size(); i++) {
				
				// eine Zelle einlesen
				JSONObject jsonCell = (JSONObject) jsonCells.get(i);
				
				CellModel cellModel = CellModel.loadCell(jsonCell, this);
				this.addCell(cellModel);
			}
		}
		
		return this;
	}

	/**
	 * <p>Sucht eine Section in der Liste der Sections in diesem Model
	 * oder in übergeordneten Modellen.</p>
	 * @param aName
	 * @return ein {@link SectionModel}
	 * @throws CellCalculationException
	 */
	public SectionModel traverseSectionsUp(String aName) throws CellCalculationException {
		
		if (!aName.contains(".")) {
			
			List<SectionModel> selected = searchSections(aName);
			if (selected.size() > 0) {
				
				return this.sections.get(0);
			} else {
				
				throw new CellCalculationException("Section not found: "+aName);
			}
			
		} else {
			
			int p = aName.lastIndexOf(".");
			String parentName = aName.substring(0, p);
			String sectionName = aName.substring(p+1);
			
			if (this.parentModel != null) {
				
				SectionModel parentSection = (SectionModel) this.parentModel.traverseSectionsUp(parentName);
		
				SectionModel section = parentSection.traverseSectionsUp(sectionName);
				return section;
				
			} else {
				
				throw new CellCalculationException("No parent found: "+aName);				
			}
		}
	}
	
	public static SectionModel cloneSection(SectionModel model) 
			throws CellCalculationException, TechnicalException {
		
		SectionModel sm = null;
		switch (model.getSectionType()) {
//		case Page:
//			sm = new PageModel((PageModel)model);
//			break;
		case Shape:
			sm = new ShapeModel((ShapeModel)model);
			break;
		case Path:
			sm = new PathModel((PathModel)model);
			break;
		case PathElement:
			sm = new PathElementModel((PathElementModel)model);
			break;
		default:
			throw new TechnicalException("Invalid section type : "+model.getSectionType().toString());
		}
		
		sm.cells = cloneCells(model.cells);
		sm.description = (model.description != null ? new String(model.description) : null);
		sm.id = model.id;
		sm.name = (model.name != null ? new String(model.name) : null);
		sm.nameU = new String(model.nameU);
		sm.parentModel = model.parentModel;
		sm.sections = cloneSections(model.sections);
		sm.sectionType = model.sectionType;
		return sm;
	}
	
	/**========================================================================
	 * Private methods
	 *=======================================================================

	/**
	 * @throws CellCalculationException 
	 */
	private static Map<String, CellModel> cloneCells(Map<String, CellModel> cells) 
			throws CellCalculationException {
		
		Map<String, CellModel> map = new HashMap<>();
		Iterator<Entry<String, CellModel>> iter = cells.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, CellModel> entry = iter.next();
			map.put(new String(entry.getKey()), new CellModel(entry.getValue()));
		}
		return map;
	}
	
	private static List<SectionModel> cloneSections(List<SectionModel> sections) 
			throws CellCalculationException, TechnicalException {
		
		List<SectionModel> list = new ArrayList<>();
		Iterator<SectionModel> iter = sections.iterator();
		while (iter.hasNext()) {
			list.add(cloneSection(iter.next()));
		}
		return list;
	}
}

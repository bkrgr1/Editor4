package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    public List<SectionModel> selectSections(SectionModelType type) {

    	return sections.stream()
    		.filter(s -> (s.getSectionType() == type))
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

    
    protected Map<String, CellModel> cells = new HashMap<>();
    
    public void addCell(CellModel cell) {
    	this.cells.put(cell.getNameU(), cell);
    }
    
    public CellModel getCell(String name) {
    	
    	// Direktzugriff mit nameU
    	if (this.cells.containsKey(name)) {
    		return this.cells.get(name);
    	} else {
	    	
	    	// Suche nach Name
    		for (CellModel cell : this.cells.values()) {
    			if (cell.getName() != null && cell.getName().equals(name)) {
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
	
	// Constructors
	
	public SectionModel(SectionModelType st) {
		this.sectionType = st;
	}
    
    /**
     * Alle Formeln analysieren
     * @throws CellCalculationException 
     */
    public IModel calculate() throws CellCalculationException {
    	
		String sectionName = this.sectionType.toString();
    	logger.debug("Calculate section: "+sectionName);
    	
    	// alle Cells berechnen
    	for (CellModel cell : this.cells.values()) {
    		cell.calculate();
    	}
    	
    	// alle Sections berechnen
    	for (IModel model : this.sections) {
    		
    		model.calculate();
    	}
    	
    	return this;
    }
	
	/**
	 * <p>Lädt eine Section anhand des SectionType.</p>
	 * @param jsonSection
	 * @param sectionType
	 * @return
	 * @throws TechnicalException
	 * @throws InputFileException
	 */
	public static SectionModel loadSection(JSONObject jsonSection, String sectionType, IModel parentModel) 
			throws TechnicalException, InputFileException {
		
		// Section je nach Typ laden
		switch (sectionType) {
		case "Page":
			PageModel pageModel = new PageModel();
			pageModel.loadModel(jsonSection, parentModel);
			return pageModel;
		case "Shape":
			ShapeModel shapeModel = new ShapeModel();
			shapeModel.loadModel(jsonSection, parentModel);
			return shapeModel;
		case "Path":
			PathModel pathModel = new PathModel(PathType.DrawingPath);
			pathModel.loadModel(jsonSection, parentModel);
			return pathModel;
		case "Location":
			LocationModel locationModel = new LocationModel();
			locationModel.loadModel(jsonSection, parentModel);
			return locationModel;
		case "Center":
			CenterModel centerModel = new CenterModel();
			centerModel.loadModel(jsonSection, parentModel);
			return centerModel;
		case "MoveTo":
			PathElementModel elemModel1 = new PathElementModel(PathElementType.MoveTo);
			elemModel1.loadModel(jsonSection, parentModel);
			return elemModel1;
		case "LineTo":
			PathElementModel elemModel2 = new PathElementModel(PathElementType.LineTo);
			elemModel2.loadModel(jsonSection, parentModel);
			return elemModel2;
		case "ClosePath":
			PathElementModel elemModel3 = new PathElementModel(PathElementType.Close);
			elemModel3.loadModel(jsonSection, parentModel);
			return elemModel3;
		case "Style":
			StyleModel styleModel = new StyleModel();
			styleModel.loadModel(jsonSection, parentModel);
			return styleModel;
		default:
			throw new InputFileException("Section model type not defined: "+sectionType);
		}
	}
	
	/**
	 * <p>Lädt gemeinsame Elemente aller Section.</p>
	 * @param jsonSection
	 * @param parentSection
	 * @return
	 * @throws TechnicalException
	 * @throws InputFileException
	 */
	public SectionModel loadModel(JSONObject jsonSection, IModel parentSection) 
			throws TechnicalException, InputFileException {
		
		this.parentModel = parentSection;
		
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

		
		// Liste der Sections
		if (jsonSection.containsKey("sections")) {
	    	JSONArray jsonSections = (JSONArray) jsonSection.get("sections");
	    	for (int i=0; i<jsonSections.size(); i++) {
	    		
	    		// eine Section lesen
	    		JSONObject jsonSection2 = (JSONObject) jsonSections.get(i);
	    		
	    		SectionModel sectModel = 
	    				SectionModel.loadSection(jsonSection2, (String)jsonSection2.get("sectionType"), this);
	    		this.addSection(sectModel);
	    	}
		}
		
		// Liste der Cells
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
	 * Sucht eine Section in der Liste der Sections in diesem Model
	 * oder in übergeordneten Modellen.
	 * @param name
	 * @return
	 * @throws CellCalculationException
	 */
	public SectionModel searchForSection(String name) throws CellCalculationException {
		
		if (!name.contains(".")) {
			
			SectionModelType sectionKey = null;
			try {
				sectionKey = SectionModelType.valueOf(name);
			} catch(Exception e) {
				throw new CellCalculationException("Invalid section type: "+name);
			}
			List<SectionModel> selected = selectSections(sectionKey);
			if (selected.size() > 0) {
				
				return this.sections.get(0);
			} else {
				
				throw new CellCalculationException("Section not found: "+name);
			}
			
		} else {
			
			int p = name.lastIndexOf(".");
			String sectionName = name.substring(0, p);
			
			if (this.parentModel != null) {
				
				SectionModel parentSection = (SectionModel) this.parentModel;
		
				return parentSection.searchForSection(sectionName);
			} else {
				
				throw new CellCalculationException("No parent found: "+name);				
			}
		}
	}
}

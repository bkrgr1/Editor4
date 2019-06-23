package de.bkroeger.editor4.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
	protected Map<SectionModelType, SectionModel> sections = new LinkedHashMap<>();
	
	protected String getKey() {
		return "";
	}
    
    public List<SectionModel> selectSections(SectionModelType type) {

    	return sections.values().stream()
    		.filter(s -> (s.getSectionType() == type))
    		.collect(Collectors.toList());
    }
    
    public void addSection(SectionModel section) {
    	this.sections.put(section.getSectionType(), section);
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
    public SectionModel calculate() throws CellCalculationException {
    	
    	@SuppressWarnings("unused")
		String sectionName = this.sectionType.toString();
    	
    	// alle Cells berechnen
    	for (CellModel cell : this.cells.values()) {
    		cell.calculate();
    	}
    	
    	// alle Sections berechnen
    	for (SectionModel model : this.sections.values()) {
    		
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
			PathModel pathModel2 = new PathModel(PathType.Location);
			pathModel2.loadModel(jsonSection, parentModel);
			return pathModel2;
		case "CenterPoint":
			PathModel pathModel3 = new PathModel(PathType.Center);
			pathModel3.loadModel(jsonSection, parentModel);
			return pathModel3;
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
			if (this.sections.containsKey(sectionKey)) {
				
				return this.sections.get(sectionKey);
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

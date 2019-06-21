package de.bkroeger.editor4.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bkroeger.editor4.controller.ControllerResult;
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

    
    protected Map<CellModelType, CellModel> cells = new HashMap<>();
    
    public void addCell(CellModel cell) {
    	this.cells.put(cell.getCellType(), cell);
    }
    
    public CellModel getCell(CellModelType type) {
    	
    	return cells.get(type);	
    }
	
	/**
	 * Backreferenz auf das Shape
	 */
	private ShapeModel shape;
	
	// Constructors
	
	public SectionModel(SectionModelType st) {
		this.sectionType = st;
	}

	public SectionModel calculate() {
		// TODO Auto-generated method stub		
		return this;
	}

	public ControllerResult buildView(ControllerResult result, int panelWidth, int panelHeight) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * <p>Lädt eine Section anhand des SectionType.</p>
	 * @param jsonSection
	 * @param sectionType
	 * @return
	 * @throws TechnicalException
	 * @throws InputFileException
	 */
	public static SectionModel loadSection(JSONObject jsonSection, String sectionType) 
			throws TechnicalException, InputFileException {
		
		// Section je nach Typ laden
		switch (sectionType) {
		case "Page":
			PageModel pageModel = new PageModel();
			pageModel.loadModel(jsonSection);
			return pageModel;
		case "Shape":
			ShapeModel shapeModel = new ShapeModel();
			shapeModel.loadModel(jsonSection);
			return shapeModel;
		case "Path":
			PathModel pathModel = new PathModel(PathType.DrawingPath);
			pathModel.loadModel(jsonSection);
			return pathModel;
		case "Location":
			PathModel pathModel2 = new PathModel(PathType.Location);
			pathModel2.loadModel(jsonSection);
			return pathModel2;
		case "CenterPoint":
			PathModel pathModel3 = new PathModel(PathType.Center);
			pathModel3.loadModel(jsonSection);
			return pathModel3;
		case "MoveTo":
			PathElementModel elemModel1 = new PathElementModel(PathElementType.MoveTo);
			elemModel1.loadModel(jsonSection);
			return elemModel1;
		case "LineTo":
			PathElementModel elemModel2 = new PathElementModel(PathElementType.LineTo);
			elemModel2.loadModel(jsonSection);
			return elemModel2;
		case "ClosePath":
			PathElementModel elemModel3 = new PathElementModel(PathElementType.Close);
			elemModel3.loadModel(jsonSection);
			return elemModel3;
		case "Style":
			StyleModel styleModel = new StyleModel();
			styleModel.loadModel(jsonSection);
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
	    		
	    		SectionModel sectModel = SectionModel.loadSection(jsonSection2, (String)jsonSection2.get("sectionType"));
	    		this.addSection(sectModel);
	    	}
		}
		
		// Liste der Cells
		if (jsonSection.containsKey("cells")) {
			JSONArray jsonCells = (JSONArray) jsonSection.get("cells");
			for (int i=0; i<jsonCells.size(); i++) {
				
				// eine Zelle einlesen
				JSONObject jsonCell = (JSONObject) jsonCells.get(i);
				
				CellModel cellModel = CellModel
						.loadCell(jsonCell,
								(String) jsonCell.get("cellType"),
								(String) jsonCell.get("value"),
								this);
				this.addCell(cellModel);
			}
		}
		
		return this;
	}
}

package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.runtime.IRuntime;
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
public class BaseModel implements IModel {

	private static final Logger logger = LogManager.getLogger(BaseModel.class.getName());

	private static final String DESCRIPTION_KEY = "description";
	private static final String UNIVERSAL_NAME_KEY = "nameU";
	private static final String NAME_KEY = "name";
	private static final String ID_KEY = "id";
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	/**
	 * Art des Models
	 */
	protected ModelType modelType;
	
	/**
	 * die interne Id
	 */
	protected UUID id;
	
	/**
	 * der sprachspezifische Name
	 */
	protected String name;
	
	/**
	 * der sprachunabhängige Name; dieser ist unabhängig von der Sprache
	 * der Name wird in Kleinschreibung gespeichert
	 */
	protected String nameU;
	
	/**
	 * das Parent-Model
	 */
	protected IModel parentModel;
	
	/**
	 * Beschreibung
	 */
	protected String description;

	/**
	 * Map der Sections
	 */
	protected List<IModel> childModels = new ArrayList<>();
	
	protected String getKey() {
		return "";
	}
    
	/**
	 * Sucht nach Sections mit dem angegebenen {@link ModelType}.
     * @param type a {@link ModelType}
     * @return a list of {@link IModel}
	 */
    public List<IModel> selectChildMotel(ModelType type) {

    	return childModels.stream()
    		.filter(s -> (s.getModelType() == type))
    		.collect(Collectors.toList());
    }
    
    /**
     * <p>Sucht nach Sections mit dem Universal-Name.</p>
     * @param aName the universal name of the section
     * @return a list of {@link IModel}
     */
    public List<IModel> searchSections(String aName) {
    	
    	return childModels.stream()
    		.filter(s -> s.getNameU().equals(aName))
    		.collect(Collectors.toList());
    }
    
    public IModel getSection(ModelType type) 
    		throws InputFileException, CellCalculationException {
    	
    	List<IModel> sections = selectChildMotel(type);
    	if (sections.size() == 1) return sections.get(0);
    	else if (sections.size() == 0) throw new InputFileException("Section with type '"+type.toString()+"' not found");
    	else throw new InputFileException("Only one location section allowed per shape");
    }
    
    public void addSection(IModel section) {
    	this.childModels.add(section);
    }

//    /**
//     * Map der Zellen
//     */
//    protected Map<String, ICell> cells = new HashMap<>();
    
//    /**
//     * Fügt eine Zelle hinzu
//     * @param cell
//     */
//    public void addCell(ICell cell) {
//    	this.cells.put(cell.getNameU().toLowerCase(), cell);
//    }
    
    /**
     * Sucht nach einer Zelle anhand des NameU oder des Name
     */
    public ICell getCellByName(String name) {
		return null;
//    	
//    	// Direktzugriff mit nameU
//    	if (this.cells.containsKey(name.toLowerCase())) {
//    		
//    		return this.cells.get(name.toLowerCase());
//    	} else {
//	    	
//	    	// Suche nach Name
//    		Optional<Entry<String, ICell>> result = cells.entrySet().stream()
//    			.filter(e -> e.getValue().getName().equalsIgnoreCase(name))
//    			.findFirst();
//    	
//    		if (result.isPresent()) return result.get().getValue();
//    		return null;
//    	}
    }
	
	/**
	 * Backreferenz auf das Shape
	 */
	private ShapeModel shape;
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public BaseModel() { }
	
	public BaseModel(ModelType st) {
		this.modelType = st;
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
    	
		String sectionName = this.modelType.toString();
    	logger.debug("Calculate section: "+sectionName);
    	
//    	// alle Cells berechnen; erste Runde ohne Variablen
//    	for (CellModel cell : this.cells.values()) {
//    		cell.calculate(firstRound);
//    	}
//    	
//    	// alle Sections berechnen
//    	for (IModel model : this.childModels) {
//    		
//    		model.calculate(firstRound);
//    	}
//    	
    	return this;
    }
//	
//	/**
//	 * <p>Lädt eine Section anhand des SectionType aus dem JSON-Model.</p>
//	 * @param jsonSection JSON-Objekt mit den Daten der Section
//	 * @param sectionType Art der Section
//	 * @return das gefüllte {@link IModel}
//	 * @throws TechnicalException
//	 * @throws InputFileException
//	 */
//	public static IModel loadSection(
//		JsonObject jsonSection, String sectionType, IModel parentModel) 
//			throws TechnicalException, InputFileException {
//		
//		// Section je nach Typ laden
//		switch (sectionType) {
//		case "Page":
//			PageModel pageModel = new PageModel();
//			pageModel.setParentModel(parentModel);
//			pageModel.loadModel(jsonSection, parentModel);
//			return pageModel;
//		case "Shape":
//			ShapeModel shapeModel = new ShapeModel();
//			shapeModel.setParentModel(parentModel);
//			shapeModel.loadModel(jsonSection, parentModel);
//			return shapeModel;
//		case "Path":
//			PathModel pathModel = new PathModel(PathType.DrawingPath);
//			pathModel.setParentModel(parentModel);
//			pathModel.loadModel(jsonSection, parentModel);
//			return pathModel;
//		case "Location":
//			LocationModel locationModel = new LocationModel();
//			locationModel.setParentModel(parentModel);
//			locationModel.loadModel(jsonSection, parentModel);
//			return locationModel;
//		case "Center":
//			CenterModel centerModel = new CenterModel();
//			centerModel.setParentModel(parentModel);
//			centerModel.loadModel(jsonSection, parentModel);
//			return centerModel;
//		case "MoveTo":
//			PathElementModel elemModel1 = new PathElementModel(PathElementType.MoveTo);
//			elemModel1.setParentModel(parentModel);
//			elemModel1.loadModel(jsonSection, parentModel);
//			return elemModel1;
//		case "LineTo":
//			PathElementModel elemModel2 = new PathElementModel(PathElementType.LineTo);
//			elemModel2.setParentModel(parentModel);
//			elemModel2.loadModel(jsonSection, parentModel);
//			return elemModel2;
//		case "ClosePath":
//			PathElementModel elemModel3 = new PathElementModel(PathElementType.ClosePath);
//			elemModel3.setParentModel(parentModel);
//			elemModel3.loadModel(jsonSection, parentModel);
//			return elemModel3;
//		case "ArcTo":
//			PathElementModel elemModel4 = new PathElementModel(PathElementType.ArcTo);
//			elemModel4.setParentModel(parentModel);
//			elemModel4.loadModel(jsonSection, parentModel);
//			return elemModel4;
//		case "Style":
//			StyleModel styleModel = new StyleModel();
//			styleModel.setParentModel(parentModel);
//			styleModel.loadModel(jsonSection, parentModel);
//			return styleModel;
//		default:
//			throw new InputFileException("Section model type not defined: "+sectionType);
//		}
//	}
//	
//	/**
//	 * <p>Lädt gemeinsame Elemente aller Section.</p>
//	 * @param jsonSection ein JSON-Objekt mit den Daten der Section
//	 * @return dieses {@link IModel}
//	 * @throws TechnicalException
//	 * @throws InputFileException
//	 */
//	public IModel loadModel(JSONObject jsonSection) 
//			throws TechnicalException, InputFileException {
//		
//		// Standardfields
//    	for (Object key : jsonSection.keySet()) {
//    		String k = key.toString();
//    		switch (k) {
//    		case ID_KEY:
//    			String uuid = (String) jsonSection.get(ID_KEY);
//    			this.setId(uuid != null ? UUID.fromString(uuid) : UUID.randomUUID());
//    			break;
//	    	
//    		case NAME_KEY:
//	    		this.name = (String) jsonSection.get(NAME_KEY);
//	    		break;
//	    	
//    		case UNIVERSAL_NAME_KEY:
//	    		this.nameU = (String) jsonSection.get(UNIVERSAL_NAME_KEY);
//	    		break;
//	    	
//    		case DESCRIPTION_KEY:
//	    		this.description = (String) jsonSection.get(DESCRIPTION_KEY);
//	    		break;
//    			
//    		default:
//    			// ignore section specific entries
//    		}
//    	}
//
//		
//		// Liste der abhängigen Sections laden
//		if (jsonSection.containsKey("sections")) {
//	    	JSONArray jsonSections = (JSONArray) jsonSection.get("sections");
//	    	for (int i=0; i<jsonSections.size(); i++) {
//	    		
//	    		// eine Section lesen
//	    		JSONObject jsonSection2 = (JSONObject) jsonSections.get(i);
//	    		
//	    		IModel sectModel = 
//    				IModel.loadSubModels(
//						jsonSection2, 
//						(String)jsonSection2.get("sectionType"), 
//						this);
//	    		this.addSection(sectModel);
//	    	}
//		}
//		
//		// Liste der Zellen laden
//		if (jsonSection.containsKey("cells")) {
//			JSONArray jsonCells = (JSONArray) jsonSection.get("cells");
//			for (int i=0; i<jsonCells.size(); i++) {
//				
//				// eine Zelle einlesen
//				JSONObject jsonCell = (JSONObject) jsonCells.get(i);
//				
//				CellModel cellModel = CellModel.loadCell(jsonCell, this);
//				this.addCell(cellModel);
//			}
//		}
//		
//		return this;
//	}
//
//	/**
//	 * <p>Sucht eine Section in der Liste der Sections in diesem Model
//	 * oder in übergeordneten Modellen.</p>
//	 * @param aName
//	 * @return ein {@link IModel}
//	 * @throws CellCalculationException
//	 */
//	public IModel traverseSectionsUp(String aName) throws CellCalculationException {
//		
//		if (!aName.contains(".")) {
//			
//			List<IModel> selected = searchSections(aName);
//			if (selected.size() > 0) {
//				
//				return this.childModels.get(0);
//			} else {
//				
//				throw new CellCalculationException("Section not found: "+aName);
//			}
//			
//		} else {
//			
//			int p = aName.lastIndexOf(".");
//			String parentName = aName.substring(0, p);
//			String sectionName = aName.substring(p+1);
//			
//			if (this.parentModel != null) {
//				
//				IModel parentSection = (IModel) this.parentModel.traverseSectionsUp(parentName);
//		
//				IModel section = parentSection.traverseSectionsUp(sectionName);
//				return section;
//				
//			} else {
//				
//				throw new CellCalculationException("No parent found: "+aName);				
//			}
//		}
//	}
//	
//	public static IModel cloneSection(IModel model) 
//			throws CellCalculationException, TechnicalException {
//		
//		IModel sm = null;
//		switch (model.getSectionType()) {
////		case Page:
////			sm = new PageModel((PageModel)model);
////			break;
//		case Shape:
//			sm = new ShapeModel((ShapeModel)model);
//			break;
//		case Path:
//			sm = new PathModel((PathModel)model);
//			break;
//		case PathElement:
//			sm = new PathElementModel((PathElementModel)model);
//			break;
//		default:
//			throw new TechnicalException("Invalid section type : "+model.getSectionType().toString());
//		}
//		
//		sm.cells = cloneCells(model.cells);
//		sm.description = (model.description != null ? new String(model.description) : null);
//		sm.id = model.id;
//		sm.name = (model.name != null ? new String(model.name) : null);
//		sm.nameU = new String(model.nameU);
//		sm.parentModel = model.parentModel;
//		sm.childModels = cloneSections(model.childModels);
//		sm.sectionType = model.sectionType;
//		return sm;
//	}
	
	/**========================================================================
	 * Private methods
	 *=======================================================================

//	/**
//	 * @throws CellCalculationException 
//	 */
//	private static Map<String, CellModel> cloneCells(Map<String, CellModel> cells) 
//			throws CellCalculationException {
//		
//		Map<String, CellModel> map = new HashMap<>();
//		Iterator<Entry<String, CellModel>> iter = cells.entrySet().iterator();
//		while (iter.hasNext()) {
//			Entry<String, CellModel> entry = iter.next();
//			map.put(new String(entry.getKey()), new CellModel(entry.getValue()));
//		}
//		return map;
//	}
//	
//	private static List<IModel> cloneSections(List<IModel> sections) 
//			throws CellCalculationException, TechnicalException {
//		
//		List<IModel> list = new ArrayList<>();
//		Iterator<IModel> iter = sections.iterator();
//		while (iter.hasNext()) {
//			list.add(cloneSection(iter.next()));
//		}
//		return list;
//	}
}

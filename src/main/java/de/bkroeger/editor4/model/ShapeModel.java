package de.bkroeger.editor4.model;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Dies ist die Basis-Datenstruktur für alle 2D-Shapes.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Getter
@Setter
@ToString(callSuper=true)
public class ShapeModel extends SectionModel {

    @SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(ShapeModel.class.getName());

    private static final String CELLS_KEY = "cells";
	private static final String SECTIONS_KEY = "sections";
	private static final String DESCRIPTION_KEY = "description";
	private static final String UNIVERSAL_NAME_KEY = "nameU";
	private static final String NAME_KEY = "name";
	private static final String ID_KEY = "id";
    private static final String SECTION_TYPE_KEY = "sectionType";
	
	public void addSection(SectionModel section) {
		sections.put(section.getSectionType(), section);
	}
	
	public void removeSection(SectionModel section) {
		sections.remove(section.getSectionType());
	}
	
	public SectionModel getSection(SectionModelType st) throws CellCalculationException {
		if (sections.containsKey(st)) {
			return sections.get(st);
		} else {
			throw new CellCalculationException("Shape "+this.id.toString()+" does not contain section "+st.toString());
		}
	}

	/**
	 * Backreferenz auf die Seite
	 */
	protected PageModel page;
	
	/**
	 * Art dieses Shapes
	 */
	protected ShapeType shapeType;
	
	// Constructors
	
	public ShapeModel() {
		super(SectionModelType.Shape);
		this.id = UUID.randomUUID();
	}
	
	public ShapeModel(ShapeType shapeType) {
		super(SectionModelType.Shape);
		this.id = UUID.randomUUID();
		this.shapeType = shapeType;
	}

	/**
	 * Load the section data from JSON
	 * @throws TechnicalException 
	 * @throws InputFileException 
	 */
	public SectionModel loadModel(JSONObject jsonSection) 
			throws TechnicalException, InputFileException {
		  	
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
	    		
    		case SECTION_TYPE_KEY:
			case SECTIONS_KEY:	    
			case CELLS_KEY:
		    	// skip
		    	break;
				
			default:
				throw new InputFileException("Invalid item in "+nameU+" section: "+k);
			}
		}
	
		super.loadModel(jsonSection, this);
		
		return this;
	}
	
	/**
	 * Calculate cell values
	 */   
    public SectionModel calculate() {
    	
    	for (SectionModel sectionModel : this.selectSections(SectionModelType.Shape)) {
    		if (sectionModel instanceof ShapeModel) {
    			ShapeModel shapeModel = (ShapeModel)sectionModel;
        		shapeModel.calculate();
    		}
    	}
    	return this;
    }
}
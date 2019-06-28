package de.bkroeger.editor4.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class PathModel extends SectionModel implements IModel {

	private static final Logger logger = LogManager.getLogger(PathModel.class.getName());
	
	public PathModel() {
		super(SectionModelType.Path);
	}
	
	private PathType pathType;
	
	public PathModel(PathType pt) {
		super(SectionModelType.Path);
		this.pathType = pt;
	}
	
	@Override
	public SectionModel loadModel(JSONObject jsonSection, IModel parentSection) 
			throws TechnicalException, InputFileException {
    
    	super.loadModel(jsonSection, this);
    	
    	logger.debug(String.format("Pathmodel has %d cells and %d sections",
    			this.cells.size(), this.sections.size()));
    	
		return this;
	}
}

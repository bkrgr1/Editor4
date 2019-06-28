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
public class StyleModel extends SectionModel {

    @SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(StyleModel.class.getName());
	
	public StyleModel() {
		super(SectionModelType.Style);
	}
	
	public SectionModel loadModel(JSONObject jsonSection, IModel parentModel) 
			throws TechnicalException, InputFileException {
    
    	super.loadModel(jsonSection, this);
    	
    	return this;
	}
}

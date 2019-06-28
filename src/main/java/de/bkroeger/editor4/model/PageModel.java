package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import de.bkroeger.editor4.controller.ControllerResult;
import de.bkroeger.editor4.controller.IShapeController;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageModel extends SectionModel {

	private static final Logger logger = LogManager.getLogger(FileModel.class.getName());

    /**
     * UUID of the section
     */
	private UUID id;
	
	protected List<IShapeController> shapeControllers;

	private int pageNo;

	private String pageTitle;
	
	private IModel parentModel;
	
	private ControllerResult parentResult;

	private List<IArrowModel> arrowModels = new ArrayList<>();

	public void addArrowModel(IArrowModel model) {
		this.arrowModels.add(model);
	}

	/**
	 * Constructor
	 */
	public PageModel() {
		super(SectionModelType.Page);
		
	}
	
	/**
	 * Load the section data from JSON
	 * @throws TechnicalException 
	 * @throws InputFileException 
	 */
	public SectionModel loadModel(JSONObject jsonSection, IModel parentModel) 
			throws TechnicalException, InputFileException {
    	
    	super.loadModel(jsonSection, this);
    	
    	logger.debug(String.format("Page model has %d cells and %d sections",
    			this.cells.size(), this.sections.size()));
    	
		return this;
	}
	
	public ControllerResult buildView(ControllerResult parentResult,
			int panelWidth, int panelHeight) {
				return parentResult;		
	}
}

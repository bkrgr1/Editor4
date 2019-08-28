package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import de.bkroeger.editor4.controller.ControllerResult;
import de.bkroeger.editor4.controller.IShapeController;
import de.bkroeger.editor4.exceptions.CellCalculationException;
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

	private static final String PAGE_TITLE_CELL = "PageTitle";
	private static final String PAGE_NO_CELL = "PageNo";
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/

    /**
     * UUID of the section
     */
	private UUID id;
	
	protected List<IShapeController> shapeControllers = new ArrayList<>();

	public Integer getPageNo() {
		CellModel cell = this.getCellByName(PAGE_NO_CELL);
		if (cell != null) {
			double d = cell.getDoubleValue();
			return (int)d;
		} else {
			return 0;
		}
	}
	
	public void setPageNo(int value) throws CellCalculationException {
		CellModel cell = this.getCellByName(PAGE_NO_CELL);
		if (cell != null) {
			cell.getDoubleProperty().set(value);
		}
	}

	public String getPageTitle() {
		CellModel cell = this.getCellByName(PAGE_TITLE_CELL);
		if (cell != null) {
			return cell.getStringValue();
		} else {
			return "";
		}
	}
	
	public void setPageTitle(String value) throws CellCalculationException {
		CellModel cell = this.getCellByName(PAGE_TITLE_CELL);
		if (cell != null) {
			cell.getStringProperty().set(value);
		}
	}

	private IModel parentModel;
	
	private ControllerResult parentResult;

	private List<IArrowModel> arrowModels = new ArrayList<>();

	public void addArrowModel(IArrowModel model) {
		this.arrowModels.add(model);
	}
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

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

	/**
	 * Übernimmt die geänderten Daten aus dem {@link PageDialogModel} in diese {@link PageModel}.
	 * @param dialog das geänderte {@link PageDialogModel}
	 * @throws CellCalculationException 
	 */
	public void acceptChanges(PageDialogModel dialog) throws CellCalculationException {
		
		// copy changed attributes to PageModel
		this.id = UUID.fromString(dialog.getPageIdProperty().get());
		this.setPageNo(Integer.parseInt(dialog.getPageNoProperty().get()));
		
		CellModel titleCell = this.getCellByName(PAGE_TITLE_CELL);
		titleCell.setStringValue(dialog.getPageTitleValueProperty().get());
		titleCell.setFormula(dialog.getPageTitleFormulaProperty().get());
	}
}

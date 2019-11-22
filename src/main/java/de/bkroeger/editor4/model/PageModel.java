package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.runtime.EditorRuntime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class PageModel extends BaseModel {

	private static final Logger logger = LogManager.getLogger(PageModel.class.getName());

	private static final String PAGE_TITLE_CELL = "PageTitle";
	private static final String PAGE_NO_CELL = "PageNo";
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private List<CellModel> cells = new ArrayList<>();
	
	private List<ShapeModel> shapes = new ArrayList<>();

	public Integer getPageNo() {
		return null;
//		ICell cell = this.getCellByName(PAGE_NO_CELL);
//		if (cell != null) {
//			double d = cell.getDoubleValue();
//			return (int)d;
//		} else {
//			return 0;
//		}
	}
	
	public void setPageNo(int value) throws CellCalculationException {
//		ICell cell = this.getCellByName(PAGE_NO_CELL);
//		if (cell != null) {
//			cell.getDoubleProperty().set(value);
//		}
	}

	public String getPageTitle() {
		return "title";
//		CellModel cell = this.getCellByName(PAGE_TITLE_CELL);
//		if (cell != null) {
//			return cell.getStringValue();
//		} else {
//			return "";
//		}
	}
	
	public void setPageTitle(String value) throws CellCalculationException {
//		CellModel cell = this.getCellByName(PAGE_TITLE_CELL);
//		if (cell != null) {
//			cell.getStringProperty().set(value);
//		}
	}

//	private IModel parentModel;
//	
//	private EditorRuntime parentResult;
//
//	private List<IArrowModel> arrowModels = new ArrayList<>();
//
//	public void addArrowModel(IArrowModel model) {
//		this.arrowModels.add(model);
//	}
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	/**
	 * Constructor
	 */
	public PageModel() {
		super(ModelType.Page);
		
	}
	
	public EditorRuntime buildView(EditorRuntime parentResult,
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
		
//		CellModel titleCell = this.getCellByName(PAGE_TITLE_CELL);
//		titleCell.setStringValue(dialog.getPageTitleValueProperty().get());
//		titleCell.setFormula(dialog.getPageTitleFormulaProperty().get());
	}
}

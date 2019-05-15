package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * This model defines the data for one page of a diagram.
 * </p>
 * 
 * @author bk
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageModel extends BaseModel {

	private int pageNo;

	private String pageTitle;

	private List<IShapeModel> shapeModels = new ArrayList<>();

	public void addShapeModel(IShapeModel model) {
		this.shapeModels.add(model);
	}

	private List<IArrowModel> arrowModels = new ArrayList<>();

	public void addArrowModel(IArrowModel model) {
		this.arrowModels.add(model);
	}

	/**
	 * Constructor
	 */
	public PageModel() {
		super("Editor4");
		this.pageNo = 1;
	}
}

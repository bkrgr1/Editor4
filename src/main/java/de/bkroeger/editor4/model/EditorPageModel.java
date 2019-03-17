package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * <p>
 * This model defines the data for one page of a diagran.
 * </p>
 */
@Data
public class EditorPageModel extends BaseModel {

	private String title = "Editor4";

	private List<IShapeModel> shapeModels = new ArrayList<>();

	public void addShapeModel(IShapeModel model) {
		this.shapeModels.add(model);
	}

	private List<IArrowModel> arrowModels = new ArrayList<>();

	public void addArrowModel(IArrowModel model) {
		this.arrowModels.add(model);
	}

	public EditorPageModel() {
		super();
	}
}

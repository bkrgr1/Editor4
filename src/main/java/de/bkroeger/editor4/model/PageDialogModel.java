package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.controller.IShapeController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageDialogModel implements IDialogModel {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PageDialogModel.class.getName());
		
	/**========================================================================
	 * Fields
	 *=======================================================================*/

	private ObjectProperty<String> pageIdProperty = new SimpleObjectProperty<String>(); 

	private ObjectProperty<String> pageNoProperty = new SimpleObjectProperty<String>();

	private ObjectProperty<String> pageTitleProperty = new SimpleObjectProperty<String>();
	
	protected List<ShapeInfo> shapeInfos = new ArrayList<>();

	/**
	 * Creates the {@link PageDialogModel} from the {@link PageModel}
	 * @param pageModel a {@link PageModel}
	 */
	public PageDialogModel(PageModel page) {
		// Daten übernehmen
		this.pageIdProperty.set(page.getId().toString());
		this.pageNoProperty.set(""+page.getPageNo());
		this.pageTitleProperty.set(page.getPageTitle());
		Iterator<IShapeController> iter = page.getShapeControllers().iterator();
		while (iter.hasNext()) {
			IShapeController ctrl = iter.next();
			shapeInfos.add(new ShapeInfo(ctrl.getModel()));
		}
	}
	
	@Getter
	@Setter
	@ToString
	public static class ShapeInfo {
		
		private UUID shapeId;
		
		private String shapeNameU;
		
		private String shapeName;
		
		private ShapeType shapeType;
		
		private String shapeDimension;
		
		public ShapeInfo(ShapeModel model) {
			this.shapeId = model.getId();
			this.shapeNameU = model.getNameU();
			this.shapeName = model.getName();
			this.shapeType = model.getShapeType();
			this.shapeDimension = model.getShapeDimension();
		}
	}
}

package de.bkroeger.editor4.Handler;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.controller.IMouseHandlerData;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.model.ShapeModel;
import de.bkroeger.editor4.view.ShapeDialogView;
import javafx.scene.input.MouseEvent;

/**
 * Dieser Command wird aufgerufen, wenn die über einem Shape gedrückt und gehalten wird.
 * Es wird die aktuellen Mouse-Position gespeichert.
 *
 * @author berthold.kroeger@gmx.de
 */
public class ShapeDialogCommand implements IMouseCommand {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(ShapeDialogCommand.class.getName());
	
	private ShapeModel shapeModel;
	
	/**
	 * Constructor
	 * @param data die {@link IMouseHandlerData Mouse-Daten}
	 */
	public ShapeDialogCommand(ShapeModel shapeModel) {
		this.shapeModel = shapeModel;
	}

	/**
	 * Event ausführen
	 * @throws CellCalculationException 
	 * @throws TechnicalException 
	 */
	public void execute(MouseEvent event) 
			throws CellCalculationException, TechnicalException {
		
		ShapeDialogView dialog = new ShapeDialogView((ShapeModel)SectionModel.cloneSection(this.shapeModel));
		Optional<ShapeModel> result = dialog.showAndWait();
		result.ifPresent(newModel -> {
			this.shapeModel.acceptChanges(newModel);
		});
	}
}

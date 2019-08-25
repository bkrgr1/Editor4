package de.bkroeger.editor4.Handler;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.controller.IMouseHandlerData;
import de.bkroeger.editor4.controller.PageDialogController;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.PageDialogModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.PageDialogView;
import javafx.scene.input.MouseEvent;

/**
 * Dieser Command wird aufgerufen, wenn die über einem Page gedrückt und gehalten wird.
 * Es wird die aktuellen Mouse-Position gespeichert.
 *
 * @author berthold.kroeger@gmx.de
 */
public class PageDialogCommand implements IMouseCommand {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PageDialogCommand.class.getName());
	
	private PageModel pageModel;
	
	/**
	 * Constructor
	 * @param data die {@link IMouseHandlerData Mouse-Daten}
	 */
	public PageDialogCommand(PageModel pageModel) {
		this.pageModel = pageModel;
	}

	/**
	 * Mouse-Klick-Event ausführen
	 * @throws CellCalculationException 
	 * @throws TechnicalException 
	 */
	public void execute(MouseEvent event) 
			throws CellCalculationException, TechnicalException {
		
		PageDialogModel dialogModel = new PageDialogModel(pageModel);
		PageDialogController dialogController = new PageDialogController(dialogModel);
		PageDialogView dialog = dialogController.buildView();
		Optional<PageDialogModel> result = dialog.showAndWait();
		result.ifPresent(resultingModel -> {
			try {
				this.pageModel.acceptChanges(resultingModel);
			} catch (CellCalculationException e) {
				logger.error(e.getMessage(), e);
			}
		});
	}
}

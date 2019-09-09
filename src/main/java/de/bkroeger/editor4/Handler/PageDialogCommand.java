package de.bkroeger.editor4.Handler;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.controller.IMouseHandlerData;
import de.bkroeger.editor4.controller.PageDialogController;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.PageDialogModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.PageDialogView;
import javafx.event.Event;

/**
 * Dieser Command wird aufgerufen, wenn die über einem Page gedrückt und gehalten wird.
 * Es wird die aktuellen Mouse-Position gespeichert.
 *
 * @author berthold.kroeger@gmx.de
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PageDialogCommand implements IMouseCommand {

	private static final Logger logger = LogManager.getLogger(PageDialogCommand.class.getName());
	
	@Autowired
	private ApplicationContext appContext;
	
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
	public void execute(Event mouseEvent) 
			throws CellCalculationException, TechnicalException {
		
		PageDialogModel dialogModel = new PageDialogModel(pageModel);
		PageDialogController dialogController = 
				appContext.getBean(PageDialogController.class, dialogModel);
				//new PageDialogController(dialogModel);
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

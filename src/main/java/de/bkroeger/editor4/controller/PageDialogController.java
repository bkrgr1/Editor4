package de.bkroeger.editor4.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.model.FormulaDialogModel;
import de.bkroeger.editor4.model.PageDialogModel;
import de.bkroeger.editor4.view.FormulaDialogView;
import de.bkroeger.editor4.view.PageDialogView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * <p>
 * Controller für einen PageDialog.
 * </p>
 *
 * @author berthold.kroeger@gmx.de
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PageDialogController implements IController {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PageDialogController.class.getName());

	/**
	 * ========================================================================
	 * Fields
	 * =======================================================================
	 */
	
	@Autowired
	private ApplicationContext appContext;

	/**
	 * Das {@link PageDialogModel Datenmodel}
	 */
	private PageDialogModel model;

	/**
	 * ========================================================================
	 * Constructors
	 * =======================================================================
	 */

	/**
	 * Constructor
	 * 
	 * @param model das {@link PageDialogModel Datenmodel} für die Dialog-Daten
	 */
	public PageDialogController(PageDialogModel model) {
		this.model = model;
	}

	/**
	 * ========================================================================
	 * Public methods
	 * =======================================================================
	 */

	/**
	 * <p>
	 * Erstellt den Page-Dialog und deklariert Listeners.
	 * </p>
	 * 
	 * @return - der generierte {@link PageDialogView PageDialog}
	 */
	public PageDialogView buildView() {

		// PageDialogView erstellen
		PageDialogView dialog = new PageDialogView(model);

		// ChangeListener für Page title formula
		dialog.getPageTitleFormula().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO: eingegebene Formel prüfen und Wert neu berechnen,
				// aber nur im PageDialogModel speichern
			}
		});

		// Maus-Klick-Handler für das Edit-Icon des PageTitleFormula-Field
		dialog.getPageTitleIcon().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				FormulaDialogModel formulaModel = new FormulaDialogModel(
						model.getPageTitleFormulaProperty().get(),
						model.getPageModel());
				FormulaDialogController formulaController = 
						appContext.getBean(FormulaDialogController.class, formulaModel); 
				FormulaDialogView dialog = formulaController.buildView();
				Optional<String> result = dialog.showAndWait();
				result.ifPresent(resultingFormel -> {
					model.getPageTitleFormulaProperty().set(resultingFormel);
				});
				event.consume();
			}
		});
		return dialog;
	}
}

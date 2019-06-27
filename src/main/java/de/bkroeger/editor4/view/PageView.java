package de.bkroeger.editor4.view;

import de.bkroeger.editor4.controller.PageController;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.model.CellModel;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * <p>Die PageView ist ein {@link Tab} mit einem {@link Pane} als Content.</p>
 * <p>Auf diesem Pane werden die Shapes gezeichnet.</p>
 *
 * @author berthold.kroeger@gmx.de
 */
public class PageView extends Tab implements IView {

	private static final Color BACKGROUND_COLOR = Color.ALICEBLUE;

	public PageView(PageController controller) throws CellCalculationException {
		super();
		
		// das Tab-Control
		CellModel pageTitleCell = controller.getCell("PageTitle");
		this.textProperty().bind(pageTitleCell.getStringProperty());
		
		// TODO: hier sollte ein Border-Pane eingefügt werden,
		// um Lineale links und oben hinzuzufügen
		
		// der Content-Pane
		Pane tabContent = new Pane();
		tabContent.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		
		// fügt den Content-Pane zum Tab hinzu
		this.setContent(tabContent);
	}
}

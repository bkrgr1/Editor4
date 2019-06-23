package de.bkroeger.editor4.view;

import de.bkroeger.editor4.controller.PageController;
import de.bkroeger.editor4.model.CellModel;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PageView extends Tab implements IView {

	private static final Color BACKGROUND_COLOR = Color.ALICEBLUE;

	public PageView(PageController controller) {
		super();
		
		CellModel pageTitleCell = controller.getCell("PageTitle");
		this.textProperty().bind(pageTitleCell.getStringProperty());
		
		Pane tabContent = new Pane();
		tabContent.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		
		this.setContent(tabContent);
	}
}

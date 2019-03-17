package de.bkroeger.editor4.view;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PageView extends Pane implements IView {

	private static final Color BACKGROUND_COLOR = Color.ALICEBLUE;

	public PageView(double PANEL_WIDTH, double PANEL_HEIGHT) {
		super();
		this.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setPrefWidth(PANEL_WIDTH);
		this.setPrefHeight(PANEL_HEIGHT);
	}
}

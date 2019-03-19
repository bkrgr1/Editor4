package de.bkroeger.editor4.view;

import javafx.geometry.Insets;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class TabView extends TabPane implements IView {

	private static final Color BACKGROUND_COLOR = Color.ALICEBLUE;

	public TabView(double PANEL_WIDTH, double PANEL_HEIGHT) {
		super();
		this.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setPrefWidth(PANEL_WIDTH);
		this.setPrefHeight(PANEL_HEIGHT);
	}
}

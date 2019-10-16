package de.bkroeger.editor4.view;

import de.bkroeger.editor4.model.EditorModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TopView extends VBox implements IView {
	
	private ToggleGroup toolModeGroup;
	public ToggleGroup getToolModeGroup() {
		return toolModeGroup;
	}
	
	private ToggleGroup lineModeGroup;
	public ToggleGroup getLineModeGroup() {
		return lineModeGroup;
	}

	/**
	 * 
	 * @param model
	 */
	public TopView(EditorModel model) {
		super();
		
		Label label = new Label("Top");
		
		MenuBar menuBar = buildMenuBar(model);
		
		HBox toolBarBox = buildToolBarBox(model);
		
		this.getChildren().addAll(label, menuBar, toolBarBox);
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	private MenuBar buildMenuBar(EditorModel model) {
		
		// create a MenuBar
		MenuBar menuBar = new MenuBar();
		
		// File Menu
		Menu fileMenu = new Menu("File");
		
		// File new item
		MenuItem fileNewItem = new MenuItem("New");
		MenuItem fileOpenItem = new MenuItem("Open");
		MenuItem fileSaveItem = new MenuItem("Save");
		MenuItem fileSaveAsItem = new MenuItem("Save as");
		MenuItem fileCloseItem = new MenuItem("Close");
		
		fileMenu.getItems().addAll(fileNewItem, fileOpenItem, fileSaveItem, 
				fileSaveAsItem, fileCloseItem);
		
		// Edit Menu
		Menu editMenu = new Menu("Edit");
		
		// View Menu
		Menu viewMenu = new Menu("View");
	
		// AutoConnect option
		CheckMenuItem autoConnectItem = new CheckMenuItem("AutoConnect");
		autoConnectItem.selectedProperty().bind(model.getAutoConnect());
		autoConnectItem.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	// toggle the autoConnect value
		        model.setAutoConnect(!model.isAutoConnect());
		    }
		});	
		
		viewMenu.getItems().addAll(autoConnectItem);
		
		menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu);
		return menuBar;
	}
	
	private HBox buildToolBarBox(EditorModel model) {
		
		HBox toolBarBox = new HBox();
		
        VBox tb1 = buildToolBar1(model);
        
        VBox tb2 = buildToolBar2(model);

        toolBarBox.getChildren().addAll(tb1, tb2);
        return toolBarBox;
	}

	private VBox buildToolBar1(EditorModel model) {
		
		VBox vbox = new VBox();
		ToolBar toolBar = new ToolBar();
        toolBar.setOrientation(Orientation.VERTICAL);

        Label label = new Label("Tools");
        
        RadioButton toolConnector = new RadioButton("Connector tool");
        toolConnector.selectedProperty().bindBidirectional(model.getToolConnector());
        Image img1 = new Image("/images/connector1_16.bmp");       
        toolConnector.setGraphic(new ImageView(img1));
        toolConnector.setUserData("Connector");
        
        RadioButton toolPointer = new RadioButton("Pointer tool");
        toolPointer.selectedProperty().bindBidirectional(model.getToolPointer());
        Image img2 = new Image("/images/Pointer1_16.bmp");       
        toolPointer.setGraphic(new ImageView(img2));
        toolPointer.setUserData("Pointer");
        
        RadioButton toolText = new RadioButton("Text tool");
        toolText.selectedProperty().bindBidirectional(model.getToolText());
        toolText.setUserData("Text");
        
        toolModeGroup = new ToggleGroup();
        toolModeGroup.getToggles().addAll(
                toolConnector,
                toolPointer,
                toolText
            );
        
        toolBar.getItems().addAll(toolPointer, toolConnector, toolText);
        
        vbox.getChildren().addAll(label, toolBar);
        return vbox;
	}

	private VBox buildToolBar2(EditorModel model) {
		
		VBox vbox = new VBox();
		ToolBar toolBar = new ToolBar();
        toolBar.setOrientation(Orientation.VERTICAL);

        Label label = new Label("Lines");
        
        RadioButton straightLine = new RadioButton("Straight line");
        straightLine.selectedProperty().bindBidirectional(model.getStraightLine());
//        Image img1 = new Image("/images/connector1_16.bmp");       
//        straightLine.setGraphic(new ImageView(img1));
        straightLine.setUserData("Straight");
        
        RadioButton ortogonalLine = new RadioButton("Ortogonal line");
        ortogonalLine.selectedProperty().bindBidirectional(model.getOrtogonalLine());
//        Image img2 = new Image("/images/Pointer1_16.bmp");       
//        ortogonalLine.setGraphic(new ImageView(img2));
        ortogonalLine.setUserData("Ortogonal");
        
        RadioButton curvedLine = new RadioButton("Curved line");
        curvedLine.selectedProperty().bindBidirectional(model.getCurvedLine());
        curvedLine.setUserData("Curved");
        
        lineModeGroup = new ToggleGroup();
        lineModeGroup.getToggles().addAll(
                straightLine,
                ortogonalLine,
                curvedLine
            );
        
        toolBar.getItems().addAll(ortogonalLine, straightLine, curvedLine);
        
        vbox.getChildren().addAll(label, toolBar);
        return vbox;
	}
}

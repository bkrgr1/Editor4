package de.bkroeger.editor4.view;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.model.FormulaDialogModel;
import de.bkroeger.editor4.model.FunctionDef;
import de.bkroeger.editor4.model.PageDialogModel;
import de.bkroeger.editor4.model.VariableDef;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;

/**
 * Die Dialogbox mit den Details eines Pages
 *
 * @author berthold.kroeger@gmx.de
 */
public class FormulaDialogView extends Dialog<String> {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(FormulaDialogView.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/

	private static final String DIALOG_TITLE = "Formula Editor";
	private static final int FUNCTION_COL = 0;
	private static final int VARIABLE_COL = 1;
	private static final int CONSTANT_COL = 2;
	
	private FormulaDialogModel model;
	
	private SimpleStringProperty errorTextProperty = new SimpleStringProperty();
	
	private ComboBox<String> categoriesBox;
	public ComboBox<String> getCategoriesBox() { return this.categoriesBox; }
	
	private ComboBox<FunctionDef> functionsBox;
	public ComboBox<FunctionDef> getFunctionsBox() { return this.functionsBox; }
	
	private ComboBox<String> sectionsBox;
	public ComboBox<String> getSectionsBox() { return this.sectionsBox; }
	
	private ComboBox<VariableDef> variablesBox;
	public ComboBox<VariableDef> getVariablesBox() { return this.variablesBox; }
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	/**
	 * Constructor
	 * @param model das {@link PageDialogModel}
	 */
	public FormulaDialogView(FormulaDialogModel model) {
		super();
		
		this.model = model;
		
		GridPane grid = buildDialogGrid();
		
		final DialogPane dialogPane = getDialogPane();
		dialogPane.setContent(grid);
		dialogPane.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
		
		this.setResizable(true);
		
		setResultConverter((dialogButton) -> {
		    ButtonData data = (dialogButton == null ? null : dialogButton.getButtonData());
		    return (data == ButtonData.OK_DONE ? model.getFormula() : null);
		});
	}
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/

	/**
	 * Erstellt den Ansicht für den Dialog
	 * @return
	 */
	private GridPane buildDialogGrid() {
		GridPane grid = new GridPane();
		int row = 0;
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		ColumnConstraints col1 = new ColumnConstraints();
	    col1.setPercentWidth(33);	// Label 33%
	    ColumnConstraints col2 = new ColumnConstraints();
	    col2.setPercentWidth(33);	// Value 33%
	    ColumnConstraints col3 = new ColumnConstraints();
	    col3.setPercentWidth(34);	// Formula 34%
	    grid.getColumnConstraints().addAll(col1,col2,col3);
	    
	    // Dialog-Titel
		Text scenetitle = new Text(DIALOG_TITLE);
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, row, 3, 1);
		row++;
		
		// Überschriften
		Label categoryLabel = new Label("Function category");
		grid.add(categoryLabel, FUNCTION_COL, row);
		
		Label sectionsLabel = new Label("Variable sections");
		grid.add(sectionsLabel, VARIABLE_COL, row);
		
		Label constantLabel = new Label("Constant");
		grid.add(constantLabel, CONSTANT_COL, row);
		
		row++;
		
		// Auswahlboxen 1. Ebene
		categoriesBox = new ComboBox<>(model.getCategories());
		grid.add(categoriesBox, FUNCTION_COL, row);
		
		sectionsBox = new ComboBox<>(model.getSections());
		grid.add(sectionsBox, VARIABLE_COL, row);
		
		TextField constantField = new TextField();
		grid.add(constantField, CONSTANT_COL, row);
		
		row++;
		
		// Auswahlboxen 2. Ebene
		buildFunctionsBox();
		grid.add(functionsBox, FUNCTION_COL, row);
		
		buildVariablesBox();
		grid.add(variablesBox, VARIABLE_COL, row);
		
		row++;
		
		// Buttons
		Button addFunctionButton = new Button("Add function");
		addFunctionButton.setDisable(true);
		grid.add(addFunctionButton, FUNCTION_COL, row);
		
		Button addVariableButton = new Button("Add variable");
		addVariableButton.setDisable(true);
		grid.add(addVariableButton, VARIABLE_COL, row);
		
		Button addConstantButton = new Button("Add constant");
		addConstantButton.setDisable(true);
		grid.add(addConstantButton, CONSTANT_COL, row);
		
		row++;
		
		TextFlow textflow = new TextFlow();
		textflow.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		Text text1 = new Text(model.getFormula());
		text1.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));
		textflow.getChildren().add(text1);
		grid.add(textflow, FUNCTION_COL, row, 3, 1);

        row += 2;
	
        // Text area
		final Text errorTextField = new Text();
		errorTextField.textProperty().bind(errorTextProperty);
        grid.add(errorTextField, 0, row, 4, 1);
        
		return grid;
	}

	private void buildFunctionsBox() {
		functionsBox = new ComboBox<>();
		functionsBox.setItems(model.getFunctions());
		functionsBox.setConverter(new StringConverter<FunctionDef>() {

		    @Override
		    public String toString(FunctionDef object) {
		        return object.getName();
		    }

		    @Override
		    public FunctionDef fromString(String string) {
		        return functionsBox.getItems().stream().filter(ap -> 
		            ap.getName().equals(string)).findFirst().orElse(null);
		    }
		});
		functionsBox.valueProperty().addListener((obs, oldval, newval) -> {
		    if(newval != null)
		        System.out.println("Selected FunctionDef: " + newval.getName() 
		            + ". ID: " + newval.getClassName());
		});
	}

	private void buildVariablesBox() {
		variablesBox = new ComboBox<>();
		variablesBox.setItems(model.getVariables());
		variablesBox.setConverter(new StringConverter<VariableDef>() {

		    @Override
		    public String toString(VariableDef object) {
		        return object.getName();
		    }

		    @Override
		    public VariableDef fromString(String string) {
		        return variablesBox.getItems().stream().filter(ap -> 
		            ap.getName().equals(string)).findFirst().orElse(null);
		    }
		});
		variablesBox.valueProperty().addListener((obs, oldval, newval) -> {
		    if(newval != null)
		        System.out.println("Selected VariablesDef: " + newval.getName() 
		            + ". ID: " + newval.getVariable());
		});
	}
}

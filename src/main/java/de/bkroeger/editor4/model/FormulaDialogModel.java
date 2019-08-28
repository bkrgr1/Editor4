package de.bkroeger.editor4.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FormulaDialogModel {
	
	private static final Logger logger = LogManager.getLogger(FormulaDialogModel.class.getName());
	
	private String formula;
	
	private IModel context;
	
	private ObservableList<String> categories = 
		    FXCollections.observableArrayList(
		        "All functions",
		        "Text functions",
		        "Numeric functions"
		    );
	
	private ObservableList<FunctionDef> functions = 
		    FXCollections.observableArrayList(

		    );
	
	private ObservableList<String> sections = 
		    FXCollections.observableArrayList(
		    );
	
	private ObservableList<VariableDef> variables = 
		    FXCollections.observableArrayList(

		    );
	
	public FormulaDialogModel(String formula, IModel context) {
		this.formula = formula;
		this.context = context;
		
		if (context instanceof PageModel) {
			sections.add("Page");
			sections.add("File");
		} else if (context instanceof ShapeModel) {
			sections.add("Shape");
			sections.add("Page");
			sections.add("File");
		} else {
			logger.warn("Invalid context object: "+context.getClass().getSimpleName());
		}
	}

}

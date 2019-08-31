package de.bkroeger.editor4.controller;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.functions.ColorFunction;
import de.bkroeger.editor4.functions.ConcatFunction;
import de.bkroeger.editor4.functions.FunctionDef;
import de.bkroeger.editor4.functions.IntFunction;
import de.bkroeger.editor4.functions.TextFunction;
import de.bkroeger.editor4.functions.VariableDef;
import de.bkroeger.editor4.model.FormulaDialogModel;
import de.bkroeger.editor4.model.IModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.model.ShapeModel;
import de.bkroeger.editor4.view.FormulaDialogView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FormulaDialogController implements IController {

	private static final Logger logger = LogManager.getLogger(FormulaDialogController.class.getName());

	/**
	 * ========================================================================
	 * Fields
	 * =======================================================================
	 */
	
	@Resource
	private ColorFunction colorFunction;
	@Resource
	private ConcatFunction concatFunction;
	@Resource
	private IntFunction intFunction;
	@Resource
	private TextFunction textFunction;

	/**
	 * Das {@link FormulaDialogModel Datenmodel}
	 */
	private FormulaDialogModel model;
	
	private FormulaDialogView view;
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	public FormulaDialogController(FormulaDialogModel model) {
		this.model = model;
	}

	/**
	 * Erstellt die Dialog-Ansicht und weisst den Elementen Handler zu.
	 * @return ein {@link FormulaDialogView}
	 */
	public FormulaDialogView buildView() {
		
		this.view = new FormulaDialogView(model);
		
		// wenn ein Eintrag in der Sections-Box ausgewählt wird,
		// werden die Einträge für die Functions-Box gefüllt
		view.getCategoriesBox().valueProperty().addListener(new ChangeListener<String>() {
            @SuppressWarnings("rawtypes")
			@Override 
            public void changed(ObservableValue ov, String t, String t1) {                
            	ObservableList<FunctionDef> funcList = model.getFunctions();
                switch (t1) {
                case "All functions":
                	break;
                case "Text functions":
                	funcList.clear();
                	funcList.add(new FunctionDef("concat(text1, ...)", concatFunction));
                	funcList.add(new FunctionDef("text(object)", textFunction));
                	break;
                case "Numeric functions":
                	funcList.clear();
                	funcList.add(new FunctionDef("int(number)", intFunction));
                	break;
                case "Attribute functions":
                	funcList.clear();
                	funcList.add(new FunctionDef("color(COLOR_CONSTANT)", colorFunction));
                	break;
                default:
                	logger.warn("Invalid function category: "+t1);
                }
            }    
        });

		// wenn ein Eintrag der Sections-Box ausgewählt wird,
		// werden die verfügbaren Variablen für das aktuelle Model angezeigt
		view.getSectionsBox().valueProperty().addListener(new ChangeListener<String>() {
            @SuppressWarnings("rawtypes")
			@Override 
            public void changed(ObservableValue ov, String t, String t1) {                
            	ObservableList<VariableDef> varList = model.getVariables();
            	IModel contextModel;
                switch (t1) {
                case "All":
                	break;
                case "Shape":
                	varList.clear();             	
                	contextModel = model.getContext();
                	if (contextModel instanceof ShapeModel) {
                		
                		ShapeModel shapeModel = (ShapeModel)contextModel;
                		varList.add(new VariableDef("id", "${"+shapeModel.getNameU()+".id}"));
                		varList.add(new VariableDef("nameU", "${"+shapeModel.getNameU()+".nameU}"));
                		varList.add(new VariableDef("name", "${"+shapeModel.getNameU()+".name}"));
                		varList.add(new VariableDef("description", "${"+shapeModel.getNameU()+".description}"));
                		varList.add(new VariableDef("dimension", "${"+shapeModel.getNameU()+".dimension}"));
                	}
                	break;
                case "Page":
                	varList.clear();
                	contextModel = model.getContext();
                	if (contextModel instanceof PageModel) {
                		PageModel pageModel = (PageModel)contextModel;
                		varList.add(new VariableDef("id", "${"+pageModel.getNameU()+".id}"));
                		varList.add(new VariableDef("nameU", "${"+pageModel.getNameU()+".nameU}"));
                		varList.add(new VariableDef("name", "${"+pageModel.getNameU()+".name}"));
                		varList.add(new VariableDef("description", "${"+pageModel.getNameU()+".description}"));
                	}
                	break;
                case "File":
                	varList.clear();
                	break;
                default:
                	logger.warn("Invalid function category: "+t1);
                }
            }    
        });

		return view;
	}
}

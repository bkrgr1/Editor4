package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.runtime.ShapeRuntime;
import de.bkroeger.editor4.view.GroupView;

public class Shape1DController extends ShapeController {

	public Shape1DController(ShapeRuntime runtime) {
		super(runtime);
	}
    
    public GroupView buildView(ShapeRuntime shapeRuntime) 
    		throws TechnicalException, InputFileException, CellCalculationException {	
    	
    	GroupView groupView = new GroupView(shapeRuntime.getModel());
    	
    	return groupView;
    }
}

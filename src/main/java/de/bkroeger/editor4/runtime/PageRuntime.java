package de.bkroeger.editor4.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.controller.PageController;
import de.bkroeger.editor4.controller.ShapeController;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.model.ShapeModel;
import de.bkroeger.editor4.view.PageView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PageRuntime extends BaseRuntime implements IRuntime {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PageRuntime.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Die Daten dieser Seite
	 */
	protected PageModel model;
	
	/**
	 * Der verantwortliche Controller
	 */
	protected PageController controller;
	
	/**
	 * Die generierte View
	 */
	protected PageView view;
	
	/**
	 * Map der Shape-Runtimes
	 */
	protected Map<UUID, ShapeRuntime> shapes = new HashMap<>();
	public void putShape(UUID key, ShapeRuntime value) {
		shapes.put(key, value);
	}
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public PageRuntime() { }

	public PageRuntime(IRuntime parent) {
		this.parent = parent;
	}
	
	public void init() {
		
		for (ShapeModel shapeModel : this.model.getShapes()) {
			
	    	ShapeRuntime shapeRuntime = applicationContext.getBean(ShapeRuntime.class);
	    	ShapeController shapeController = applicationContext.getBean(ShapeController.class);
	    	shapeRuntime.setParent(this);
	    	shapeRuntime.setModel(shapeModel);
	    	shapeRuntime.setController(shapeController);
	    	shapeRuntime.init();
		}
	}
}

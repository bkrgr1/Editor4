package de.bkroeger.editor4.runtime;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.controller.PathController;
import de.bkroeger.editor4.controller.ShapeController;
import de.bkroeger.editor4.model.PathModel;
import de.bkroeger.editor4.model.ShapeModel;
import de.bkroeger.editor4.view.GroupView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShapeRuntime extends BaseRuntime implements IRuntime {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(ShapeRuntime.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	@Autowired
	private ApplicationContext applicationContext;

	private IRuntime parent;
	public IRuntime getParent() { return this.parent; }

	private ShapeModel model;
	/**
	 * Der verantwortliche Controller
	 */
	private ShapeController controller;
	
	/**
	 * Die generierte View
	 */
	private GroupView view;
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/
	
	public ShapeRuntime() { }

	public ShapeRuntime(IRuntime parent) {
		this.parent = parent;
	}

	private List<PathRuntime> paths = new ArrayList<>();
	public void addPath(PathRuntime value) {
		paths.add(value);
	}
		
	/**========================================================================
	 * Public methods
	 *=======================================================================*/

	public void init() {
		
		for (PathModel pathModel : this.model.getPaths()) {
			
	    	PathRuntime pathRuntime = applicationContext.getBean(PathRuntime.class);
	    	PathController pathController = applicationContext.getBean(PathController.class);
	    	pathRuntime.setParent(this);
	    	pathRuntime.setModel(pathModel);
	    	pathRuntime.setController(pathController);
	    	pathRuntime.init();
		}
	}
}

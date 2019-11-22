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

import de.bkroeger.editor4.controller.FileController;
import de.bkroeger.editor4.controller.PageController;
import de.bkroeger.editor4.model.FileModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.TabPaneView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CenterRuntime extends BaseRuntime implements IRuntime {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(CenterRuntime.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Das Datenmodel für die angezeigt Datei
	 */
	protected FileModel model;
	
	/**
	 * Der verantwortliche Controller
	 */
	protected FileController controller;
	
	/**
	 * Die generierte View
	 */
	protected TabPaneView view;
	
	/**
	 * Die Map der Page-Runtimes
	 */
	private List<PageRuntime> pages = new ArrayList<>();
	public void addPageRuntime(PageRuntime pageRuntime) {
		pages.add(pageRuntime);
	}
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	/**
	 * Default constructor for Spring Bean
	 */
	public CenterRuntime() {
		// no parameters
	}
	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/
	
	public void init() {
		
		for (PageModel pageModel : this.model.getPages()) {
			
	    	PageRuntime pageRuntime = applicationContext.getBean(PageRuntime.class);
	    	PageController pageController = applicationContext.getBean(PageController.class);
	    	pageRuntime.setParent(this);
	    	pageRuntime.setModel(pageModel);
	    	pageRuntime.setController(pageController);
	    	pageRuntime.init();
		}
	}
}

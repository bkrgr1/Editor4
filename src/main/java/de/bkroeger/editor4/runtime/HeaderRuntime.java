package de.bkroeger.editor4.runtime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.controller.TopController;
import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.view.TopView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HeaderRuntime implements IRuntime {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(HeaderRuntime.class.getName());
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/
	
	private IRuntime parent;
	public IRuntime getParent() { return this.parent; }

	private EditorModel model;
	/**
	 * Der verantwortliche Controller
	 */
	private TopController controller;
	
	/**
	 * Die generierte View
	 */
	private TopView view;

	public HeaderRuntime(IRuntime parent) {
		this.parent = parent;
	}

}

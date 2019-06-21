package de.bkroeger.editor4.controller;

import de.bkroeger.editor4.view.IView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ControllerResult {

	private IController controller;
	
	private IView view;
}

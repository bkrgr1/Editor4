package de.bkroeger.editor4.services;

import de.bkroeger.editor4.Handler.IStackCommand;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StackEntry {

	private IStackCommand undoCommand;
	
	private IStackCommand redoCommand;
}

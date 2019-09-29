package de.bkroeger.editor4.Handler;

import de.bkroeger.editor4.exceptions.TechnicalException;

public interface IMenuCommand {

	public void execute() throws TechnicalException;
}

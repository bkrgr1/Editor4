package de.bkroeger.editor4.Handler;

public interface IStackCommand {

	public void execute();
	
	public void rollback();
}

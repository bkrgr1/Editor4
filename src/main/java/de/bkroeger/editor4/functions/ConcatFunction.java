package de.bkroeger.editor4.functions;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConcatFunction implements IFunction {
	
	@Override
	public String getKeyword() { return "concat"; }
	
	public ConcatFunction() { }

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}

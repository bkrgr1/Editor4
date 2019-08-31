package de.bkroeger.editor4.functions;

import org.springframework.stereotype.Service;

@Service
public class ConcatFunction implements IFunction {
	
	@Override
	public String getKeyword() { return "concat"; }

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}

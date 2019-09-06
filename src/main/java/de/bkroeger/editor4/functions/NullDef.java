package de.bkroeger.editor4.functions;

import javafx.scene.text.Text;

public class NullDef implements IFuncVarConst {

	public String getKeyword() { return "null"; }
	
	public Text buildText(int i, String[] colors) {
		
		Text t = new Text(this.getKeyword());
		t.setStyle("-fx-text-fill: "+colors[i]+";");
		return t;
	}
}

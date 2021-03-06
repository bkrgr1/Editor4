package de.bkroeger.editor4;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.model.CellModelType;
import de.bkroeger.editor4.model.ModelType;

public class Utils {

	public static Double isParsable(String value) {
		try {
			Double d = Double.parseDouble(value);
			return d;
		} catch(NumberFormatException e) {
			return null;
		}
	}

	public static CellModelType isCellType(String value) throws CellCalculationException {
		try {
			CellModelType ct = CellModelType.valueOf(value);
			return ct;
		} catch(Exception e) {
			throw new CellCalculationException("Invalid cell model type: "+value);
		}
	}

	public static ModelType isSectionType(String value) throws CellCalculationException {
		try {
			ModelType ct = ModelType.valueOf(value);
			return ct;
		} catch(Exception e) {
			throw new CellCalculationException("Invalid section model type: "+value);
		}
	}
}

package de.bkroeger.editor4.model;

import java.util.Date;
import java.util.UUID;

public class LineTemplate extends ShapeTemplate {
	
	public LineTemplate() {
		super();
	}

	/**
	 * Generiert einen rechtwinkligen Verbinder mit 3 Segmenten.
	 * @return ein {@link ShapeTemplate}
	 */
	public static ShapeTemplate buildOrthogonalLineTemplate() {

		LineTemplate template = new LineTemplate();
//		"id": "xx...xx",
		template.setId(UUID.randomUUID());
//		"description": "Ein Rechteck",
		template.setDescription("Generated line "+new Date().toString());
//		"nameU": "Shape_1",
		template.setNameU("Orth"+template.getId());
//		"shapeDimension": "1D",
		template.setShapeDimension("1D");
//		"cells": [
//			{ "nameU": "LayoutX", "formula": "0.0" },
			template.addCell(new CellModel("LayoutX", "0.0", template));
//			{ "nameU": "LayoutY", "formula": "0.0" },
			template.addCell(new CellModel("LayoutY", "0,0", template));
//			{ "nameU": "Width", "formula": "100.0" },
			template.addCell(new CellModel("Width", "100.0", template));
//			{ "nameU": "Height", "formula": "50.0" },
			template.addCell(new CellModel("Height", "50.0", template));
//			{ "nameU": "Rotate", "formula": "0.0" },
			template.addCell(new CellModel("Rotate", "0.0", template));
//			{ "nameU": "Scale", "formula": "1.0" },
			template.addCell(new CellModel("Scale", "1.0", template));
//			{ "nameU": "centerX", "formula": "0.0" },
			template.addCell(new CellModel("centerX", "0.0", template));
//			{ "nameU": "centerY", "formula": "0.0" },
			template.addCell(new CellModel("centerY", "0.0", template));
//			{ "nameU": "StrokeWidth", "formula": "1.5" },
			template.addCell(new CellModel("StrokeWidth", "1.5", template));
//			{ "nameU": "StrokeColor", "formula": "color(BLACK)", "dataType": "object" }
			template.addCell(new CellModel("StrokeColor", "color(BLACK", CellValueType.object));
//		],
//		"sections": [
//			{
				PathModel pathModel = new PathModel();
				template.addSection(pathModel);
				pathModel.setParentModel(template);
//				"sectionType": "Path",
//				"id": "xx....xx",
				pathModel.setId(UUID.randomUUID());
//				"nameU": "Path...",
				pathModel.setNameU("Path"+pathModel.getId());
//				"cells": [
//				],								
//				"sections": [
//					{
						int elemNo = 1;
						PathElementModel elem = new PathElementModel(PathElementType.MoveTo);
						pathModel.addSection(elem);
						elem.setParentModel(pathModel);
//						"sectionType": "MoveTo",
//						"nameU": "MoveTo_n_...",
						elem.setNameU("MoveTo_"+elemNo+"_"+pathModel.getId());
//						"cells": [
//							{ "nameU": "X", "formula": "0.0" },
							elem.addCell(new CellModel("X", "0.0", elem));
//							{ "nameU": "Y", "formula": "0.0" }
							elem.addCell(new CellModel("Y", "0.0", elem));
//						]
//					},
//					{
						elemNo ++;
						elem = new PathElementModel(PathElementType.LineTo);
						pathModel.addSection(elem);
						elem.setParentModel(pathModel);
//						"sectionType": "LineTo",
//						"nameU": "LineTo_n_...",
						elem.setNameU("LineTo"+elemNo+"_"+pathModel.getId());
//						"cells": [
//							{ "nameU": "X", "formula": "50.0" },
							elem.addCell(new CellModel("X", "50.0", elem));
//							{ "nameU": "Y", "formula": "0.0" }
							elem.addCell(new CellModel("Y", "0.0", elem));
//						]
//					},
//					{
						elemNo ++;
						elem = new PathElementModel(PathElementType.LineTo);
						pathModel.addSection(elem);
						elem.setParentModel(pathModel);
//						"sectionType": "LineTo",
//						"nameU": "LineTo_n_...",
						elem.setNameU("LineTo"+elemNo+"_"+pathModel.getId());
//						"cells": [
//							{ "nameU": "X", "formula": "50.0" },
							elem.addCell(new CellModel("X", "50.0", elem));
//							{ "nameU": "Y", "formula": "50.0" }
							elem.addCell(new CellModel("Y", "50.0", elem));
//						]
//					},
//					{
						elemNo ++;
						elem = new PathElementModel(PathElementType.LineTo);
						pathModel.addSection(elem);
						elem.setParentModel(pathModel);
//						"sectionType": "LineTo",
//						"nameU": "LineTo_n_...",
						elem.setNameU("LineTo"+elemNo+"_"+pathModel.getId());
//						"cells": [
//							{ "nameU": "X", "formula": "50.0" },
							elem.addCell(new CellModel("X", "100.0", elem));
//							{ "nameU": "Y", "formula": "50.0" }
							elem.addCell(new CellModel("Y", "50.0", elem));
//						]
//					}

		return template;
	}

	public static ShapeTemplate buildStraightLineTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	public static ShapeTemplate buildCurvedLineTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

}

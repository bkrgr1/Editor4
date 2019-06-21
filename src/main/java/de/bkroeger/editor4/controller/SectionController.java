package de.bkroeger.editor4.controller;

import org.json.simple.JSONObject;

import de.bkroeger.editor4.model.SectionModel;

public abstract class SectionController {

	public SectionModel loadModel(JSONObject sectionObject) {
		return null;
	}
}

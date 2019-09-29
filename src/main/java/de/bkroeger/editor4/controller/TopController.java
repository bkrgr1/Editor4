package de.bkroeger.editor4.controller;

import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.model.EditorModel;
import de.bkroeger.editor4.view.TopView;
import javafx.scene.Scene;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;

/**
 * 
 *
 * @author berthold.kroeger@gmx.de
 */
public class TopController extends BaseController implements IController {

    private static final Logger logger = LogManager.getLogger(TopController.class.getName());
	
	private EditorModel model;
	
	/**
	 * 
	 * @param model
	 */
	public TopController(EditorModel model) {
		
		this.model = model;
	}

	/**
	 * 
	 * @param controllerResult
	 * @return das {@link ControllerResult Ergebnis}
	 */
	public ControllerResult buildView(ControllerResult controllerResult) {
		
		ControllerResult result = new ControllerResult();
		result.setController(this);
		
		TopView topView = new TopView(this.model);
		result.setView(topView);
		
		ToggleGroup group = topView.getToolModeGroup();
		for (Toggle toggle : group.getToggles()) {
			RadioButton button = (RadioButton) toggle;
			button.setOnAction(e -> {
				if (button.isSelected()) {
					String userData = (String) button.getUserData();
					Scene scene = button.getScene();
					switch (userData) {
					case "Pointer":
						scene.setCursor(Cursor.DEFAULT);
						break;
					case "Connector":
					    URL url = getClass().getResource("/images/ConnectionCursor.png");
					    try {
					        Image img = new Image(url.openStream());
					        scene.setCursor(new ImageCursor(img));
					    } catch (IOException e1) {
					        logger.warn("Failed to load cursor icon from {}", url);
					    }
						break;
					case "Text":
						scene.setCursor(Cursor.TEXT);
						break;
					}
				}
			});
		}
		
		return result;
	}

}

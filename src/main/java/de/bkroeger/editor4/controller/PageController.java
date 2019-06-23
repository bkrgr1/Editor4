package de.bkroeger.editor4.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.CellModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.model.SectionModelType;
import de.bkroeger.editor4.view.PageView;

/**
 * <p>
 * This controller manges the behaviour of a diagram page.
 * </p>
 * 
 * @author bk
 */
public class PageController extends BaseController {

    @SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PageController.class.getName());

    /**
     * <p>
     * Constructor with {@link PageModel}.
     * </p>
     * 
     * @param pageModel   a {@link PageModel}
     * @param panelWidth  width of the page panel
     * @param panelHeight height of the page panel
     * @throws TechnicalException 
     */
    public PageController(PageModel pageModel) throws TechnicalException {
        super();
        this.model = pageModel; 
        if (pageModel == null) throw new TechnicalException("Page model is NULL");
    }
    
    /**
     * Bildet die View: Tab mit Content-Pane
     * @param parentController
     * @return
     */
    public ControllerResult buildView(ControllerResult parentController) {
    	
    	ControllerResult controllerResult = new ControllerResult();
    	controllerResult.setController(this);

        // für diese Seite einen Tab und darin ein Pane generieren
        PageView pageView = new PageView(this);
        this.view = pageView;
        controllerResult.setView(this.view);

        // alle Shapes dieser Seite auf dem Pane zeichnen
        for (SectionModel shapeModel : ((PageModel)this.model).selectSections(SectionModelType.Shape)) {

        	@SuppressWarnings("unused")
			ShapeController shapeController = ShapeControllerFactory.getShapeController(shapeModel);
//        	shapeController.buildView(controllerResult);
        }
        
        return controllerResult;
    }
    
    public CellModel getCell(String cellName) {
    	
    	CellModel cell = this.model.getCell(cellName);
    	return cell;
    }
}
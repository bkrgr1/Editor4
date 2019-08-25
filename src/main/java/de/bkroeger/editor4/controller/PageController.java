package de.bkroeger.editor4.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.Handler.PageDialogCommand;
import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.CellModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.model.SectionModelType;
import de.bkroeger.editor4.view.PageView;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

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
    
    private PageDialogCommand pageDialogCommand;
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

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
		
		pageDialogCommand = new PageDialogCommand((PageModel)model);
    }
 	
	/**========================================================================
	 * Public methods
	 *=======================================================================*/
   
    /**
     * Bildet die View: Tab mit Content-Pane
     * @param parentController der Controller der übergeordneten Node
     * @return ein {@link ControllerResult}
     * @throws TechnicalException 
     * @throws InputFileException 
     * @throws CellCalculationException 
     */
    public ControllerResult buildView(ControllerResult parentController) 
    		throws TechnicalException, InputFileException, CellCalculationException {
    	
    	ControllerResult controllerResult = new ControllerResult();
    	controllerResult.setController(this);

        // für diese Seite einen Tab und darin ein Pane generieren
        PageView pageView = new PageView(this);
       
        this.view = pageView.draw();
        controllerResult.setView(this.view);

        // alle Shapes dieser Seite auf dem Pane zeichnen
        for (SectionModel shapeModel : ((PageModel)this.model).selectSections(SectionModelType.Shape)) {

        	// ein Shape zeichnen
			ShapeController shapeController = ShapeControllerFactory.getShapeController(shapeModel);
        	ControllerResult result = shapeController.buildView(controllerResult);
        	
        	// und zur Seite hinzufügen
        	Pane pane = (Pane) pageView.getContent();
        	pane.getChildren().add((Node) result.getView());
        }
		
    	// when user clicks on PathView
    	pageView.getContent().setOnMouseClicked(event -> {
    		logger.debug("Clicked on shape group");
    		if (event.getButton() == MouseButton.PRIMARY) {
    			// ???
    		} else if (event.getButton() == MouseButton.SECONDARY) {
    			// Shape-Dialog anzeigen
    			try {
					pageDialogCommand.execute(event);
				} catch (CellCalculationException | TechnicalException e) {
					logger.error(e.getMessage(), e);
				}
    		}
    		event.consume();
    	});
        
        return controllerResult;
    }
    
    /**
     * Ermittelt die Zelle mit dem gegebenen Namen.
     * @param cellName Name der gesuchten Zelle
     * @return ein {@link CellModel}
     */
    public CellModel getCell(String cellName) {
    	// leitet die Anfrage weiter zum Datenmodell
    	CellModel cell = this.model.getCellByName(cellName);
    	return cell;
    }
}
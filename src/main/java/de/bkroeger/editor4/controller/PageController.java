package de.bkroeger.editor4.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.model.IShapeModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.PageView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * This controller manges the behaviour of a diagram page.
 * </p>
 * 
 * @author bk
 */
public class PageController extends BaseController {

    private static final Logger logger = LogManager.getLogger(PageController.class.getName());

    private List<IShapeController> shapeControllers = new ArrayList<>();

    private List<IArrowController> arrowControllers = new ArrayList<>();

    /**
     * <p>
     * Constructor with page number.
     * </p>
     * 
     * @param pageNo page number
     */
    public PageController(int pageNo) {
        super();
    }

    /**
     * <p>
     * Constructor with {@link PageModel}.
     * </p>
     * 
     * @param pageModel   a {@link PageModel}
     * @param panelWidth  width of the page panel
     * @param panelHeight height of the page panel
     */
    public PageController(PageModel pageModel, int panelWidth, int panelHeight) {
        super();
        this.model = pageModel;

        logger.debug("Creating PageController...");

        // create a drawing canvas
        PageView pageView = new PageView(panelWidth, panelHeight);
        this.view = pageView;

        // alle Shapes zeichnen
        for (IShapeModel editorShapeModel : pageModel.getShapeModels()) {

            // einen ShapeController für jedes einzelne Shape erzeugen
            IShapeController shapeCtrl = ShapeControllerFactory.getController(editorShapeModel, this);
            shapeControllers.add(shapeCtrl);
            ((Pane) this.view).getChildren().add((Node) shapeCtrl.getView());
        }

        // alle Verbinder zeichnen
        for (IArrowModel editorArrowModel : pageModel.getArrowModels()) {

            // einen ArrowController für jeden einzelnen Pfeil erzeugen
            IArrowController arrowCtrl = ArrowControllerFactory.getController(editorArrowModel, this, shapeControllers);
            arrowControllers.add(arrowCtrl);
            ((Pane) this.view).getChildren().add((Node) arrowCtrl.getView());
        }

        logger.debug("Page controller created for page=" + pageModel.getPageNo());
    }
}
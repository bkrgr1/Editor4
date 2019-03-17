package de.bkroeger.editor4.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.model.IArrowModel;
import de.bkroeger.editor4.model.IShapeModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.PageView;
import javafx.scene.layout.Pane;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * This controller manges the behaviour of a page view.
 * </p>
 * 
 * @author bk
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageController extends BaseController {

    private static final Logger logger = LogManager.getLogger(PageController.class.getName());

    private int pageNo;

    private List<IShapeController> shapeControllers = new ArrayList<>();

    private List<IArrowController> arrowControllers = new ArrayList<>();

    public PageController(int pageNo) {
        super();
        this.pageNo = pageNo;
    }

    public PageController(PageModel pageModel, int panelWidth, int panelHeight) {
        super();
        this.model = pageModel;
        this.pageNo = pageModel.getPageNo();

        logger.debug("Creating PageController...");

        // create a drawing canvas
        PageView pageView = new PageView(panelWidth, panelHeight);
        this.view = pageView;

        // alle Shapes zeichnen
        for (IShapeModel editorShapeModel : pageModel.getShapeModels()) {

            IShapeController shapeCtrl = ShapeControllerFactory.getController(editorShapeModel, this);
            shapeControllers.add(shapeCtrl);

            ((Pane) this.view).getChildren().add(shapeCtrl.getView());
        }

        // alle Verbinder zeichnen
        for (IArrowModel editorArrowModel : pageModel.getArrowModels()) {

            IArrowController arrowCtrl = ArrowControllerFactory.getController(editorArrowModel, this, shapeControllers);
            arrowControllers.add(arrowCtrl);

            ((Pane) this.view).getChildren().add(arrowCtrl.getView());
        }

        logger.debug("Page controller created for page=" + pageModel.getPageNo());
    }
}
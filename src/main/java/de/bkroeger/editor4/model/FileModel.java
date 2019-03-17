package de.bkroeger.editor4.model;

import java.util.ArrayList;
import java.util.List;

import de.bkroeger.editor4.model.PageModel;
import lombok.Data;

/**
 * <p>
 * Definiert die Daten für eine Diagrammdatei.
 * </p>
 */
@Data
public class FileModel extends BaseModel {

    private String path;

    private String filename;

    private List<PageModel> pages = new ArrayList<>();

    public FileModel() {

        PageModel pageModel = new PageModel();
        pages.add(pageModel);

        IShapeModel rectModel = new RectangleShapeModel(100.0, 100.0, 100.0, 100.0);
        pageModel.addShapeModel(rectModel);

        StraightArrowModel arrowModel = new StraightArrowModel(250.0, 100.0, 350.0, 50.0, -15.0);
        arrowModel.strokeWidthProperty().set(2.0);
        arrowModel.lineStartTypeProperty().set(LineEndingType.OpenArrow);
        arrowModel.lineEndTypeProperty().set(LineEndingType.OpenArrow);
        pageModel.addArrowModel(arrowModel);
    }
}
package de.bkroeger.editor4.controller;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.bkroeger.editor4.model.FileModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.IView;
import lombok.Data;

/**
 * <p>
 * This controller controls the behaviour of a diagram file.
 * </p>
 * <p>
 * A diagram file supports following actions:
 * </p>
 * <li>Select current page</li>
 * <ul>
 * </ul>
 * 
 * @author bk
 */
@Data
public class FileController extends BaseController {

    private static final Logger logger = LogManager.getLogger(FileController.class.getName());

    private PageController currentPage;

    private SortedSet<PageController> pageControllers = new TreeSet<>(new PageComparator());

    @Override
    public IView getView() {
        return currentPage.getView();
    }

    /**
     * Controller
     * 
     * @param model the {@link FileModel}
     */
    public FileController(FileModel model, int panelWidth, int panelHeight) {
        this.model = model;

        logger.debug("Creating FileController...");
        for (PageModel pageModel : model.getPages()) {

            PageController pageCtrl = new PageController(pageModel, panelWidth, panelHeight);
            pageControllers.add(pageCtrl);

            if (currentPage == null) {
                currentPage = pageCtrl;
            }
        }
        logger.debug("FileController created.");
    }

    public String getTitle() {
        return model.getTitle();
    }

    // =========================================================================

    /**
     * @author bk
     */
    public static class PageComparator implements Comparator<PageController> {

        @Override
        public int compare(PageController o1, PageController o2) {
            if (o1.getPageNo() == o2.getPageNo())
                return 0;
            else if (o1.getPageNo() < o2.getPageNo())
                return -1;
            else
                return +1;
        }

    }
}
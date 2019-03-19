package de.bkroeger.editor4.controller;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.model.FileModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.TabView;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * This controller manages the behaviour of a diagram file.
 * </p>
 * <p>
 * A diagram file supports following actions:
 * </p>
 * <li>Get current page</li>
 * <li>Set current page</li>
 * <li>Save file</li>
 * <li>Load file</li>
 * <ul>
 * </ul>
 * 
 * @author bk
 */
@Getter
@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileController extends BaseController {

    private static final Logger logger = LogManager.getLogger(FileController.class.getName());

    private PageController currentPage;

    private SortedSet<PageController> pageControllers = new TreeSet<>(new PageComparator());

    /**
     * Controller
     * 
     * @param model       the {@link FileModel}
     * @param panelWidth  width of the page panel
     * @param panelHeight height of the page panel
     */
    public FileController(FileModel model, int panelWidth, int panelHeight) {

        this.model = model;

        logger.debug("Creating FileController...");

        // create a tab view
        TabView tabView = new TabView(panelWidth, panelHeight);
        this.view = tabView;

        // TODO: add actions for TabPane

        for (PageModel pageModel : model.getPages()) {

            // create a page controller for each defined page
            PageController pageCtrl = new PageController(pageModel, panelWidth, panelHeight);
            pageControllers.add(pageCtrl);

            Tab tab = new Tab("Page " + pageModel.getPageNo());
            tab.setContent((Node) pageCtrl.getView());
            ((TabPane) tabView).getTabs().add(tab);

            if (currentPage == null) {
                // save this as the current page controller
                currentPage = pageCtrl;
                // TODO: replace currentPage by currentTab
            }
        }
        logger.debug("FileController created.");
    }

    public String getTitle() {
        return model.getTitle();
    }

    // =========================================================================

    /**
     * <p>
     * This {@link Comparator} class compares two objects of type PageController and
     * sorts them according to the page number.
     * </p>
     * 
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
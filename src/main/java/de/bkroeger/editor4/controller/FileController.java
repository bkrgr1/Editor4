package de.bkroeger.editor4.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.model.FileModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.NewPageDialog;
import de.bkroeger.editor4.view.TabView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Dialog;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * This controller manages the behaviour of a diagram file.
 * </p>
 * <p>
 * A diagram file supports following actions:
 * </p>
 * <ul
 * <li>Select current page</li>
 * <li>Add a page</li>
 * <li>Remove a page</li>
 * <li>Edit page properties</li>
 * <li>Save file (invoked by menu)</li>
 * <li>Load file (invoked by menu)</li>
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
    private Tab currentTab;

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

        for (PageModel pageModel : model.getPages()) {

            // create a page controller for each defined page
            PageController pageCtrl = new PageController(pageModel, panelWidth, panelHeight);
            pageControllers.add(pageCtrl);

            Tab tab = new Tab(
                    pageModel.getPageTitle() != null ? pageModel.getPageTitle() : "Page " + pageModel.getPageNo());
            tab.setUserData(pageCtrl); // store a reference to the page controller in the userData field
            tab.setContent((Node) pageCtrl.getView());
            ((TabPane) tabView).getTabs().add(tab);

            if (currentPage == null) {
                // save this as the current page controller
                currentPage = pageCtrl;
                currentTab = tab;
            }
        }

        // add a dummy page at the end marked with "+"
        Tab dummyTab = new Tab("+");
        ((TabPane) tabView).getTabs().add(dummyTab);

        SingleSelectionModel<Tab> selectionModel = ((TabPane) tabView).getSelectionModel();

        // select the first page
        selectionModel.select(currentTab);

        // add actions for TabPane
        // -----------------------
        // select a tab and page
        selectionModel.selectedItemProperty().addListener(new ChangeListener<Tab>() {

            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {

                if (newTab != null) {
                    // an existing tab has been selected

                    PageController pageController = (PageController) newTab.getUserData();
                    if (pageController == null) {

                        // this is the dummy page; show the "New page dialog"

                        NewPageDialogController newPageController = new NewPageDialogController(
                                getHighestPageNo(model.getPages()));
                        NewPageDialog newPageDialog = (NewPageDialog) newPageController.getView();
                        Optional<PageModel> result = ((Dialog<PageModel>) newPageDialog).showAndWait();
                        result.ifPresent(pageModel -> {

                            logger.debug("create new page");

                            currentTab = newTab;

                            currentTab.setText(pageModel.getPageTitle() != null ? pageModel.getPageTitle()
                                    : "Page " + pageModel.getPageNo());
                            PageController pageCtrl = new PageController(pageModel, panelWidth, panelHeight);
                            currentTab.setUserData(pageCtrl);

                            // add a dummy page at the end marked with "+"
                            Tab dummyTab = new Tab("+");
                            ((TabPane) tabView).getTabs().add(dummyTab);

                            SingleSelectionModel<Tab> selectionModel = ((TabPane) tabView).getSelectionModel();

                            // select the first page
                            selectionModel.select(currentTab);
                        });

                    } else {
                        // this is an existing page
                        currentTab = newTab;
                        PageModel pageModel = null;
                        if (pageController != null) {
                            pageModel = (PageModel) pageController.getModel();
                            currentPage = pageController;
                        }
                        logger.debug("Tab Selection changed. New tab = " + (pageModel != null
                                ? (pageModel.getPageTitle() != null ? pageModel.getPageTitle() : pageModel.getPageNo())
                                : "??"));
                    }
                } else {
                    // there is no new tab
                    logger.debug("Tab deleted");
                }
            }
        });

        logger.debug("FileController created.");
    }

    public String getTitle() {
        return model.getTitle();
    }

    private int getHighestPageNo(List<PageModel> pages) {

        int maxNo = 0;
        for (PageModel page : pages) {
            if (page.getPageNo() > maxNo) {
                maxNo = page.getPageNo();
            }
        }
        return maxNo;
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
            if (((PageModel) o1.getModel()).getPageNo() == ((PageModel) o2.getModel()).getPageNo())
                return 0;
            else if (((PageModel) o1.getModel()).getPageNo() < ((PageModel) o2.getModel()).getPageNo())
                return -1;
            else
                return +1;
        }

    }
}
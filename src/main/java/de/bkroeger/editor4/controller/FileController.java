package de.bkroeger.editor4.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.FileModel;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.model.SectionModelType;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.view.NewPageDialog;
import de.bkroeger.editor4.view.TabPaneView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Dialog;
import javafx.scene.control.SingleSelectionModel;
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
public class FileController implements IController  {

    private static final Logger logger = LogManager.getLogger(FileController.class.getName());

    private PageController currentPage;
    private Tab currentTab;
    
    private FileModel fileModel;
    
    private TabPaneView view;
    
    private int panelWidth;
    private int panelHeight;

    /**
     * Controller
     * 
     * @param fileModel       the {@link FileModel}
     * @param panelWidth  width of the page panel
     * @param panelHeight height of the page panel
     * @param fileModel 
     */
    public FileController(int panelWidth, int panelHeight, FileModel fileModel) {
    	
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.fileModel = fileModel;
    }

	/**
     * Berechnet die Querbeziehungen
     */
    public void calculate() {
    	
    	this.fileModel.calculate();
    }
    
    /**
     * Erstellt die View
     */
    public ControllerResult buildView() {

        logger.debug("Creating FileController...");

        ControllerResult result = new ControllerResult();
        result.setController(this);
        
        // create a tab view
        TabPaneView tabView = new TabPaneView(panelWidth, panelHeight);
        this.view = tabView;
        result.setView(this.view);

    	for (SectionModel pageModel : fileModel.selectSections(SectionModelType.Page)) {
        	
        	ControllerResult pageResult = pageModel.buildView(result, panelWidth, panelHeight);

        	((TabPane) tabView).getTabs().add((Tab) pageResult.getView());

            if (currentPage == null) {
                // save this as the current page controller
                currentPage = (PageController) pageResult.getController();
                currentTab = (Tab) pageResult.getView();
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
                                getHighestPageNo());
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
        
        return result;
    }

    public String getTitle() {
        return fileModel.getInFile().getAbsolutePath();
    }

    private int getHighestPageNo() {

        int maxNo = 0;
//        for (PageModel page : pages) {
//            if (page.getPageNo() > maxNo) {
//                maxNo = page.getPageNo();
//            }
//        }
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
package de.bkroeger.editor4.controller;

import java.util.Comparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.FileModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.model.SectionModelType;
import de.bkroeger.editor4.view.TabPaneView;
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
public class FileController extends BaseController implements IController  {

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
     * @throws InputFileException 
     * @throws TechnicalException 
     */
    public FileController(int panelWidth, int panelHeight, String inFilePath) throws InputFileException, TechnicalException {
    	
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        
	    
	    this.fileModel = new FileModel(inFilePath);
		
	    // Datenmodell aus Datei laden oder initialisieren
		fileModel.loadModel(null, null);

    }

	/**
     * Berechnet die Querbeziehungen
	 * @throws CellCalculationException 
     */
    public void calculate() throws CellCalculationException {
    	
    	// erste Runde ohne Variablen
    	this.fileModel.calculate(true);
    	
    	// zweite Runde mit Variablen
    	this.fileModel.calculate(false);
    }
    
    /**
     * Erstellt die View: ein TabView mit Tabs pro Seite
     * @throws TechnicalException 
     * @throws CellCalculationException 
     * @throws InputFileException 
     */
    public ControllerResult buildView(ControllerResult editorControllerResult) 
    		throws TechnicalException, InputFileException, CellCalculationException {

        logger.debug("Creating file view...");

        ControllerResult controllerResult = new ControllerResult();
        controllerResult.setController(this);
        
        // erzeugt die Tabview für die Seiten-Tabs
        TabPaneView tabView = new TabPaneView(panelWidth, panelHeight);
        this.view = tabView;
        controllerResult.setView(this.view);

        // für jede Seite...
    	for (SectionModel pageModel : fileModel.selectSections(SectionModelType.Page)) {
        	
    		// einen PageController erstellen
    		PageController pageController = new PageController((PageModel)pageModel);
    		
    		// und den Tab-Eintrag eintrag generieren
        	ControllerResult pageResult = pageController.buildView(controllerResult);
        	
        	// den Tab-Eintrag zu der Tabview hinzufügen
        	((TabPane) tabView).getTabs().add((Tab) pageResult.getView());

        	// die erste Seite wird zur aktuellen Seite
            if (currentPage == null) {
                currentPage = (PageController) pageResult.getController();
                currentTab = (Tab) pageResult.getView();
            }
        }

        // fügt einen Tab mit Text "+" am Ende der Tabs hinzu
        Tab dummyTab = new Tab("+");
        ((TabPane) tabView).getTabs().add(dummyTab);

        SingleSelectionModel<Tab> selectionModel = ((TabPane) tabView).getSelectionModel();
        // select the first page
        selectionModel.select(currentTab);

//        // add actions for TabPane
//        // -----------------------
//        // select a tab and page
//        selectionModel.selectedItemProperty().addListener(new ChangeListener<Tab>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
//
//                if (newTab != null) {
//                    // an existing tab has been selected
//
//                    PageController pageController = (PageController) newTab.getUserData();
//                    if (pageController == null) {
//
//                        // this is the dummy page; show the "New page dialog"
//
//                        NewPageDialogController newPageController = new NewPageDialogController(
//                                getHighestPageNo());
//                        NewPageDialog newPageDialog = (NewPageDialog) newPageController.getView();
//                        Optional<PageModel> result = ((Dialog<PageModel>) newPageDialog).showAndWait();
//                        result.ifPresent(pageModel -> {
//
//                            logger.debug("create new page");
//
//                            currentTab = newTab;
//
//                            currentTab.setText(pageModel.getPageTitle() != null ? pageModel.getPageTitle()
//                                    : "Page " + pageModel.getPageNo());
//                            PageController pageCtrl = new PageController(pageModel);
//                            currentTab.setUserData(pageCtrl);
//
//                            // add a dummy page at the end marked with "+"
//                            Tab dummyTab = new Tab("+");
//                            ((TabPane) tabView).getTabs().add(dummyTab);
//
//                            SingleSelectionModel<Tab> selectionModel = ((TabPane) tabView).getSelectionModel();
//
//                            // select the first page
//                            selectionModel.select(currentTab);
//                        });
//
//                    } else {
//                        // this is an existing page
//                        currentTab = newTab;
//                        PageModel pageModel = null;
//                        if (pageController != null) {
//                            pageModel = (PageModel) pageController.getModel();
//                            currentPage = pageController;
//                        }
//                        logger.debug("Tab Selection changed. New tab = " + (pageModel != null
//                                ? (pageModel.getPageTitle() != null ? pageModel.getPageTitle() : pageModel.getPageNo())
//                                : "??"));
//                    }
//                } else {
//                    // there is no new tab
//                    logger.debug("Tab deleted");
//                }
//            }
//        });

        logger.debug("FileController created.");
        
        return controllerResult;
    }

    /**
     * Liefert den Titel der Datei aus dem Modell
     * @return der Titel der Datei als String
     */
    public String getTitle() {
        return fileModel.getInFile().getAbsolutePath();
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
        	return ((PageModel)o1.getModel()).getPageNo().compareTo(((PageModel)o2.getModel()).getPageNo());
        }

    }
}
package de.bkroeger.editor4.controller;

import java.util.Comparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import de.bkroeger.editor4.exceptions.CellCalculationException;
import de.bkroeger.editor4.exceptions.InputFileException;
import de.bkroeger.editor4.exceptions.TechnicalException;
import de.bkroeger.editor4.model.FileModel;
import de.bkroeger.editor4.model.PageModel;
import de.bkroeger.editor4.model.SectionModel;
import de.bkroeger.editor4.model.SectionModelType;
import de.bkroeger.editor4.runtime.CenterRuntime;
import de.bkroeger.editor4.runtime.PageRuntime;
import de.bkroeger.editor4.view.PageView;
import de.bkroeger.editor4.view.TabPaneView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
public class FileController extends BaseController implements IController  {

    private static final Logger logger = LogManager.getLogger(FileController.class.getName());
    
    @Autowired
    private ApplicationContext appContext;

    private PageController currentPage;
    private Tab currentTab;
    
    private int panelWidth;
    private int panelHeight;
    
    private StringProperty titleProperty = new SimpleStringProperty("New file");
    private String changedIndicator = "";

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
    public FileController(int panelWidth, int panelHeight, FileModel fileModel) throws InputFileException, TechnicalException {
    	
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
            
	    this.model = fileModel;
		
	    // Datenmodell aus Datei laden oder initialisieren
		this.model.loadModel(null, null);
    }

	/**
     * Berechnet die Querbeziehungen
	 * @throws CellCalculationException 
     */
    public void calculate() throws CellCalculationException {
    	
    	logger.debug("Calculate file variables");
    	
    	// erste Runde ohne Variablen
    	this.model.calculate(true);
    	
    	// zweite Runde mit Variablen
    	this.model.calculate(false);
    	
    	logger.debug("File variables calculated");
    }
    
    /**
     * Erstellt die View: ein TabView mit Tabs pro Seite
     * @throws TechnicalException 
     * @throws CellCalculationException 
     * @throws InputFileException 
     */
    public TabPaneView buildView(CenterRuntime centerRuntime) 
    		throws TechnicalException, InputFileException, CellCalculationException {

        logger.debug("Creating file view...");
        
        // erzeugt die Tabview für die Seiten-Tabs
        TabPaneView tabView = new TabPaneView(panelWidth, panelHeight);
        this.view = tabView;
        
        if (((FileModel)this.model).getInFile() != null) {
        	this.titleProperty.set(((FileModel)this.model).getInFile().getAbsolutePath());
        } else {
        	this.titleProperty.set("<<New File>>");
        }

        // für jede Seite...
    	for (SectionModel pageModel : this.model.selectSections(SectionModelType.Page)) {
        	
    		PageRuntime pageRuntime = new PageRuntime(centerRuntime);
    		pageRuntime.setModel((PageModel)pageModel);
    		
    		// einen PageController erstellen
    		PageController pageController = new PageController((PageModel)pageModel);
    		pageRuntime.setController(pageController);
    		
    		// und den Tab-Eintrag eintrag generieren
        	PageView pageView = pageController.buildView(pageRuntime);
        	pageRuntime.setView(pageView);
        	
        	// den Tab-Eintrag zu der Tabview hinzufügen
        	tabView.getTabs().add(pageView);

        	// die erste Seite wird zur aktuellen Seite
            if (currentPage == null) {
                currentPage = pageRuntime.getController();
                currentTab = pageRuntime.getView();
            }
            
            centerRuntime.addPageRuntime(pageRuntime);
        }

        // fügt einen Tab mit Text "+" am Ende der Tabs hinzu
        Tab dummyTab = new Tab("+");
        tabView.getTabs().add(dummyTab);

        SingleSelectionModel<Tab> selectionModel = ((TabPane) tabView).getSelectionModel();
        // select the first page
        selectionModel.select(currentTab);

        logger.debug("File view created.");
        
        return tabView;
    }

    /**
     * Liefert den Titel der Datei aus dem Modell
     * @return der Titel der Datei als String
     */
    public StringProperty getTitleProperty() {
        return this.titleProperty;
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
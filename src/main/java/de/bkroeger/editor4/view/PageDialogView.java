package de.bkroeger.editor4.view;

import java.util.ArrayList;
import java.util.List;

import de.bkroeger.editor4.model.PageDialogModel;
import de.bkroeger.editor4.model.PageDialogModel.ShapeInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Die Dialogbox mit den Details eines Pages
 *
 * @author berthold.kroeger@gmx.de
 */
public class PageDialogView extends Dialog<PageDialogModel> {
	
	/**========================================================================
	 * Fields
	 *=======================================================================*/

	private static final String DIALOG_TITLE = "Page details";
	private static final int LABEL_COL = 0;
	private static final int VALUE_COL = 1;
	private static final int FORMULA_COL = 2;
	private static final int ICON_COL = 3;
	
	private PageDialogModel model;
	
	private List<ShapeInfoEntry> data = new ArrayList<>();
	
	private TableView<ShapeInfoEntry> table;
	
	private int rowsPerPage = 5;
	
	private SimpleStringProperty errorTextProperty = new SimpleStringProperty();
	
	private TextField pageTitleFormula;
	public TextField getPageTitleFormula() { return this.pageTitleFormula; }
	
	private ImageView pageTitleIcon;
	public ImageView getPageTitleIcon() { return this.pageTitleIcon; }
	
	/**========================================================================
	 * Constructors
	 *=======================================================================*/

	/**
	 * Constructor
	 * @param model das {@link PageDialogModel}
	 */
	public PageDialogView(PageDialogModel model) {
		super();
		
		this.model = model;
		this.data = buildData(model.getShapeInfos());
		
		GridPane grid = buildDialogGrid();
		
		final DialogPane dialogPane = getDialogPane();
		dialogPane.setContent(grid);
		dialogPane.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
		
		this.setResizable(true);
		
		setResultConverter((dialogButton) -> {
		    ButtonData data = (dialogButton == null ? null : dialogButton.getButtonData());
		    return (data == ButtonData.OK_DONE ? model : null);
		});
	}

	private List<ShapeInfoEntry> buildData(List<ShapeInfo> shapeInfos) {

		List<ShapeInfoEntry> list = new ArrayList<>();
		for (ShapeInfo info : shapeInfos) {
			list.add(new ShapeInfoEntry(
				info.getShapeId().toString(),
				info.getShapeNameU(),
				info.getShapeName(),
				info.getShapeType().toString(),
				info.getShapeDimension()
				));
		}
		return list;
	}

	private GridPane buildDialogGrid() {
		GridPane grid = new GridPane();
		int row = 1;
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		ColumnConstraints col1 = new ColumnConstraints();
	    col1.setPercentWidth(20);	// Label 20%
	    ColumnConstraints col2 = new ColumnConstraints();
	    col2.setPercentWidth(30);	// Value 30%
	    ColumnConstraints col3 = new ColumnConstraints();
	    col3.setPercentWidth(40);	// Formula 45%
	    ColumnConstraints col4= new ColumnConstraints();
	    col4.setPercentWidth(5);	// Formula 5%
	    grid.getColumnConstraints().addAll(col1,col2,col3,col4);	
	    
		// Dialog title
		Text scenetitle = new Text(DIALOG_TITLE);
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, row, 2, 1);
		row++;
		
		Label column1Header = new Label("Value");
		column1Header.minWidth(100.0);
		grid.add(column1Header, VALUE_COL, row);
		
		Label column2Header = new Label("Formula");
		column2Header.minWidth(200.0);
		grid.add(column2Header, FORMULA_COL, row);
		row++;

		// Page id
		Label pageIdLabel = new Label("Page id");
		grid.add(pageIdLabel, LABEL_COL, row);

		TextField pageIdField = new TextField();
		pageIdField.textProperty().bind(model.getPageIdProperty());
		pageIdField.setDisable(true);
		pageIdField.prefWidth(200.0);
		grid.add(pageIdField, VALUE_COL, row);
		row++;

		// Page title; kann verändert werden
		Label pageTitleLabel = new Label("Page title");
		grid.add(pageTitleLabel, LABEL_COL, row);

		TextField pageTitleValue = new TextField();
		pageTitleValue.textProperty().bind(model.getPageTitleValueProperty());
		pageTitleValue.setDisable(true);
		pageTitleValue.setPrefWidth(100.0);
		grid.add(pageTitleValue, VALUE_COL, row);	
		
		pageTitleFormula = new TextField();
		pageTitleFormula.textProperty().bindBidirectional(model.getPageTitleFormulaProperty());
		pageTitleFormula.setDisable(false);
		pageTitleFormula.minWidth(250.0);
		grid.add(pageTitleFormula, FORMULA_COL, row);
		
		Image pageTitleImage = new Image("/images/icons8-edit-16.png");
		pageTitleIcon = new ImageView();
		pageTitleIcon.setImage(pageTitleImage);
		grid.add(pageTitleIcon, ICON_COL, row);
		row++;

		// Page number
		Label pageNoLabel = new Label("Page number");
		grid.add(pageNoLabel, LABEL_COL, row);

		TextField pageNoField = new TextField();
		pageNoField.textProperty().bind(model.getPageNoProperty());
		pageNoField.setDisable(true);
		grid.add(pageNoField, VALUE_COL, row);
		row++;

		// Shapes
		Label shapesLabel = new Label("Shapes");
		grid.add(shapesLabel, LABEL_COL, row);
		
		// Grid mit den Shapes einfügen
		table = buildTabView(model.getShapeInfos());
		
		// Pagination für die TableView
		Pagination pagination = new Pagination((model.getShapeInfos().size() / rowsPerPage + 1), 0);
        pagination.setPageFactory(this::createPage);
		pagination.setDisable(false);
		pagination.minWidth(400.0);
        grid.add(pagination, VALUE_COL, row, 3, 1);
        row += 2;
	
        // Text area
		final Text errorTextField = new Text();
		errorTextField.textProperty().bind(errorTextProperty);
        grid.add(errorTextField, 0, row, 4, 1);
        
		return grid;
	}

    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
        table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(table);
    }

	@SuppressWarnings("unchecked")
	private TableView<ShapeInfoEntry> buildTabView(List<ShapeInfo> shapeInfos) {
		
	    TableView<ShapeInfoEntry> table = new TableView<>();

        TableColumn<ShapeInfoEntry, String> column1 = new TableColumn<>("Id");
        column1.setCellValueFactory(param -> param.getValue().id);
        column1.prefWidthProperty().bind(table.widthProperty().divide(4)); // w * 1/4

        TableColumn<ShapeInfoEntry, String> column2 = new TableColumn<>("nameU");
        column2.setCellValueFactory(param -> param.getValue().nameU);
        column2.prefWidthProperty().bind(table.widthProperty().divide(4)); // w * 1/4

        TableColumn<ShapeInfoEntry, String> column3 = new TableColumn<>("name");
        column3.setCellValueFactory(param -> param.getValue().name);
        column3.prefWidthProperty().bind(table.widthProperty().divide(4)); // w * 1/4

        TableColumn<ShapeInfoEntry, String> column4 = new TableColumn<>("type");
        column4.setCellValueFactory(param -> param.getValue().type);
        column4.prefWidthProperty().bind(table.widthProperty().divide(8)); // w * 1/8

        TableColumn<ShapeInfoEntry, String> column5 = new TableColumn<>("dimension");
        column5.setCellValueFactory(param -> param.getValue().dimension);
        column5.prefWidthProperty().bind(table.widthProperty().divide(8)); // w * 1/8

        table.getColumns().addAll(column1, column2, column3, column4, column5);

        return table;
    }

    public static class ShapeInfoEntry {

        private final SimpleStringProperty id;
        private final SimpleStringProperty nameU;
        private final SimpleStringProperty name;
        private final SimpleStringProperty type;
        private final SimpleStringProperty dimension;

        private ShapeInfoEntry(String id, String nameU, String name, String type, String dimension) {
            this.id = new SimpleStringProperty(id);
            this.nameU = new SimpleStringProperty(nameU);
            this.name = new SimpleStringProperty(name);
            this.type = new SimpleStringProperty(type);
            this.dimension = new SimpleStringProperty(dimension);
        }
    }}

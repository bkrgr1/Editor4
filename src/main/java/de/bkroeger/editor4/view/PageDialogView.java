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
import javafx.scene.layout.BorderPane;
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

	private static final String DIALOG_TITLE = "Page details";
	
	private PageDialogModel model;
	
	private List<ShapeInfoEntry> data = new ArrayList<>();
	
	private TableView<ShapeInfoEntry> table;
	
	private int rowsPerPage = 10;

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
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		Text scenetitle = new Text(DIALOG_TITLE);
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label pageIdLabel = new Label("Page id");
		grid.add(pageIdLabel, 0, 1);

		TextField pageIdField = new TextField();
		pageIdField.textProperty().bind(model.getPageIdProperty());
		pageIdField.setDisable(true);
		grid.add(pageIdField, 1, 1);

		Label pageTitleLabel = new Label("Page title");
		grid.add(pageTitleLabel, 0, 2);

		TextField pageTitleField = new TextField();
		pageTitleField.textProperty().bindBidirectional(model.getPageTitleProperty());
		pageTitleField.setDisable(false);
		grid.add(pageTitleField, 1, 2);		

		Label pageNoLabel = new Label("Page number");
		grid.add(pageNoLabel, 0, 3);

		TextField pageNoField = new TextField();
		pageNoField.textProperty().bind(model.getPageNoProperty());
		pageIdField.setDisable(true);
		grid.add(pageNoField, 1, 3);	

		Label shapesLabel = new Label("Shapes");
		grid.add(shapesLabel, 0, 4);
		
		// Grid mit den Shapes einfügen
		table = buildTabView(model.getShapeInfos());
		
		// Pagination für die TableView
		Pagination pagination = new Pagination((model.getShapeInfos().size() / rowsPerPage + 1), 0);
        pagination.setPageFactory(this::createPage);
		pagination.setDisable(false);
        grid.add(pagination, 1, 4);
		
//		Button btn = new Button("Ok");
//		HBox hbBtn = new HBox(10);
//		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
//		hbBtn.getChildren().add(btn);
//		grid.add(hbBtn, 1, 5);
		
		final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 8);
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
        column1.setPrefWidth(150);

        TableColumn<ShapeInfoEntry, String> column2 = new TableColumn<>("nameU");
        column2.setCellValueFactory(param -> param.getValue().nameU);
        column2.setPrefWidth(250);

        TableColumn<ShapeInfoEntry, String> column3 = new TableColumn<>("name");
        column3.setCellValueFactory(param -> param.getValue().name);
        column3.setPrefWidth(200);

        TableColumn<ShapeInfoEntry, String> column4 = new TableColumn<>("type");
        column3.setCellValueFactory(param -> param.getValue().type);
        column3.setPrefWidth(50);

        TableColumn<ShapeInfoEntry, String> column5 = new TableColumn<>("dimension");
        column3.setCellValueFactory(param -> param.getValue().dimension);
        column3.setPrefWidth(50);

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

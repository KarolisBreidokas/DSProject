package GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import voIPStats.CDRLog;

public abstract class LogTable<S> extends TableView<S> {
	public LogTable() {
		super();
		setPlaceholder(new Label(""));
	}
	void toggleTalbe(boolean toogle) {
		setManaged(toogle);
		setVisible(toogle);
	}
	public void formTable(String[] HeaderRow) {
		getColumns().clear();
		//setColumnResizePolicy(x -> true);
		for (int i = 0; i < HeaderRow.length; i++) {
			String columnTitle = HeaderRow[i];
			TableColumn<S, String> col = new TableColumn<>(columnTitle);
			col.setSortable(false);
			col.setPrefWidth(HeaderRow[i].length()*12);
			col.setCellFactory(p -> new TableCell<S, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null || item.toString().isEmpty()) {
						setText(null);
						setTooltip(null);
						setStyle(PanelsFX.TABLE_CELL_STYLE_EMPTY);
						setGraphic(null);
					} else {
						setText(item.toString());
						setTooltip(new Tooltip(item.toString()));
						setAlignment(Pos.CENTER);
					}
				}
			});
			col.setCellValueFactory(p -> returnValue(p));
			col.setId(String.valueOf(i));
			getColumns().add(col);
		}
		getColumns().add(new TableColumn<>());
	}

	public abstract ObservableValue<String> returnValue(TableColumn.CellDataFeatures<S, String> p);
}

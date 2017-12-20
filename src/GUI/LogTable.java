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

public class LogTable extends TableView<CDRLog> {
	public LogTable() {
		super();
		setPlaceholder(new Label(""));
	}

	public void formTable(int numberOfColumns, int colWidth) {
		getColumns().clear();
		setColumnResizePolicy(x->true);
		for (int i = 0; i < numberOfColumns; i++) {
			String columnTitle = CDRLog.TableHeader[i];
			TableColumn<CDRLog, String> col = new TableColumn<>(columnTitle);
			col.setSortable(false);
			final boolean toAlign = (i == 0 || i % 2 != 0);
			col.setCellFactory(p -> new TableCell<CDRLog, String>() {
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
						setAlignment(toAlign ? Pos.CENTER : Pos.CENTER_LEFT);
						if (!"-->".equals(item.toString())) {
							setStyle(PanelsFX.TABLE_CELL_STYLE_FILLED);
						} else {
							setStyle(PanelsFX.TABLE_CELL_STYLE_EMPTY);
						}
					}
				}
			});
			col.setCellValueFactory(p -> returnValue(p));
			col.setId(String.valueOf(i));
			getColumns().add(col);
		}
		getColumns().add(new TableColumn<>());
		
	}
	public ObservableValue<String> returnValue(TableColumn.CellDataFeatures<CDRLog, String> p) {
		int index = Integer.valueOf(p.getTableColumn().getId());
		return new SimpleStringProperty(p.getValue().GetSpecificParam(index));
	}
}

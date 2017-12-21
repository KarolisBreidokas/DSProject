package GUI;

import java.io.Console;
import java.nio.channels.SelectionKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.Spring;

import org.w3c.dom.CDATASection;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import voIPStats.CDRLog;
import voIPStats.CDRLog.KeyComparare;
import voIPStats.CDRLog.LogKey;
import voIPStats.CDRReader;
import voIPStats.Client;
import voIPStats.CustomSkipList;
import voIPStats.MultiLevelComparator;
import voIPStats.RegionStats;
import voIPStats.CustomSkipList.SkipListSubList;
import voIPStats.phoneNo;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import javafx.scene.control.TableColumn.CellDataFeatures;

public class MainWindow extends BorderPane implements EventHandler<ActionEvent> {
	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("GUI.messages");
	private static final int TF_WIDTH = 200;
	private static final int TF_WIDTH_SMALLER = 80;
	private static final double SPACING = 5.0;
	private static final Insets INSETS = new Insets(SPACING);
	private static final double SPACING_SMALLER = 2.0;
	private static final Insets INSETS_SMALLER = new Insets(SPACING_SMALLER);
	private final GridPane paneSelect = new GridPane();
	private final ComboBox CbUsers = new ComboBox();
	private final ComboBox CbGroupingTypes = new ComboBox();
	private MenuFX menu;
	private final Stage stage;
	// TODO add Proper Client Reader
	private Object[] clients = { "Visi", new Client("John", new phoneNo("+37055550110", null)),
			new Client("Smith", new phoneNo("+37055550100", null)) };
	CustomSkipList<CDRLog.LogKey, CDRLog> allLogs = CDRReader.getList("log.txt");
	CustomSkipList<CDRLog.LogKey, CDRLog> viewLog;

	private LogTable Logtable;
	private LogTable StatTable;
	public MainWindow(Stage stage) {
		this.stage = stage;
		initComponents();
	}

	private void initComponents() {
		Logtable = new LogTable<CDRLog>() {
			@Override
			public ObservableValue<String> returnValue(CellDataFeatures<CDRLog, String> p) {
				int index = Integer.valueOf(p.getTableColumn().getId());
				return new SimpleStringProperty(p.getValue().GetSpecificParam(index));
			}
		};
		StatTable=new LogTable<RegionStats>() {

			@Override
			public ObservableValue<String> returnValue(CellDataFeatures<RegionStats, String> p) {
					int index = Integer.valueOf(p.getTableColumn().getId());
					return new SimpleStringProperty(p.getValue().GetSpecificParam(index));
			}
			
		};
		viewLog = allLogs;
		showList();
		Button GetStatistics = new Button(MESSAGES.getString("Stats"));
		GetStatistics.setStyle(PanelsFX.BTN_STYLE);
		GridPane.setHgrow(GetStatistics, Priority.ALWAYS);
		GetStatistics.setMaxWidth(Double.MAX_VALUE);
		GetStatistics.setOnAction(this);
		Stream.of(new Label(MESSAGES.getString("Select_Users")), CbUsers,
				new Label(MESSAGES.getString("Select_Groups")), CbGroupingTypes, GetStatistics)
				.forEach(n -> paneSelect.addColumn(0, n));
		CbUsers.setItems(FXCollections.observableArrayList(clients));
		CbUsers.getSelectionModel().select(0);
		CbUsers.setPrefWidth(TF_WIDTH);
		CbUsers.setOnAction(this);
		CbGroupingTypes.setPrefWidth(TF_WIDTH);
		CbGroupingTypes.setOnAction(this);
		CbGroupingTypes.setDisable(true);

		menu = new MenuFX() {
			@Override
			public void handle(ActionEvent ae) {
				// try {
				Object source = ae.getSource();
				if (source.equals(menu.getMenus().get(0).getItems().get(0))) {
					// fileChooseMenu();
				} else if (source.equals(menu.getMenus().get(0).getItems().get(1))) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Klaida");
					alert.setHeaderText("Look, an Information Dialog");
					alert.setContentText("I have a great message for you!");
					alert.showAndWait();
				} else if (source.equals(menu.getMenus().get(0).getItems().get(3))) {
					System.exit(0);
				} else if (source.equals(menu.getMenus().get(1).getItems().get(0))) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.initStyle(StageStyle.UTILITY);
					alert.setTitle(MESSAGES.getString("menuItem21"));
					alert.setHeaderText(MESSAGES.getString("author"));
					alert.showAndWait();
				}
			}
		};
		VBox vboxTable = new VBox();
		vboxTable.setPadding(INSETS_SMALLER);
		VBox.setVgrow(Logtable, Priority.ALWAYS);
		VBox.setVgrow(StatTable, Priority.ALWAYS);
		StatTable.toggleTalbe(false);
		vboxTable.getChildren().addAll(new Label(MESSAGES.getString("TableName")),Logtable,StatTable );
		setRight(paneSelect);
		setCenter(vboxTable);
		// setBottom(paneParam123Events);
	}
	void toggleTalbe(LogTable table,boolean toogle) {
		table.setManaged(toogle);
		table.setVisible(toogle);
	}
	public static void init(Stage stage) {
		Platform.runLater(() -> {
			Locale.setDefault(Locale.US); // Suvienodiname skaičių formatus
			MainWindow window = new MainWindow(stage);
			stage.setScene(new Scene(window));
			stage.setTitle(MESSAGES.getString("title"));
			stage.getIcons().add(new Image("file:" + MESSAGES.getString("icon")));
			stage.setOnCloseRequest((WindowEvent event) -> {
				System.exit(0);
			});
			// stage.setMaximized(true);
			stage.show();
		});
	}

	void showList() {
		Object[] modelList = CreateViewable(viewLog);
		
		Logtable.formTable(CDRLog.TableHeader);
		Logtable.getItems().clear();
		Logtable.setItems(FXCollections.observableArrayList(modelList));
		Logtable.toggleTalbe(true);
		StatTable.toggleTalbe(false);
		StatTable.refresh();
		Logtable.refresh();
	}

	private Object[] CreateViewable(CustomSkipList<CDRLog.LogKey, CDRLog> allLogs2) {
		ArrayList ans = new ArrayList<>();
		int a = 0;
		for (Object s : allLogs2) {
			ans.add(s);
		}
		ans.trimToSize();
		return ans.toArray();
	}

	@Override
	public void handle(ActionEvent event) {
		System.gc();
		System.gc();
		System.gc();
		Object source = event.getSource();
		if (source instanceof ComboBox) {
			if (source.equals(CbUsers)) {
				Object SelectedUser = CbUsers.getSelectionModel().getSelectedItem();
				if (SelectedUser instanceof String) {
					CbGroupingTypes.setDisable(true);
					viewLog=allLogs;
				} else if (SelectedUser instanceof Client) {
					CbGroupingTypes.setDisable(false);
					viewLog=allLogs.getSublist(new LogKey(null, ((Client) SelectedUser).Number, null)).toSkipList();
				}
				showList();
			} else if (source.equals(CbGroupingTypes)) {
			}
		} else if (source instanceof Button) {
			GetStats();
		}
	}

	private void GetStats() {
		// TODO Auto-generated method stub

		CustomSkipList<CDRLog.LogKey, CDRLog> log=viewLog;
		if (viewLog.equals(allLogs)) {
			log = new CustomSkipList<>(new CDRLog.LogComparator() {
				{
					t = new KeyComparare[] { (x, y) -> x.getCalleeId().Region.compareTo(y.getCalleeId().Region),
							(x, y) -> x.getCallStart().compareTo(y.getCallStart()) };
				}
			});
			for (CDRLog cdrLog : allLogs) {
				log.put(cdrLog.generateKey(), cdrLog);
			}
		}ArrayList<RegionStats> stats=new ArrayList<RegionStats>();
		for (CustomSkipList<LogKey, CDRLog>.SkipListSubList sublist : log.GroupByPrimaryComparer()) {
			
			String region=sublist.GetRootValue().getCalleeId().Region;
			int count=0;
			Duration d=Duration.ZERO;
			for (CDRLog v : sublist) {
				count++;
				d=d.plus(v.getCallDuration());
			}
			stats.add(new RegionStats(region, count, d));
		}
		showStats(stats);

	}

	private void showStats(ArrayList<RegionStats> stats) {
		// TODO Auto-generated method stub
		StatTable.formTable(RegionStats.TableHeader);
		StatTable.getItems().clear();
		StatTable.setItems(FXCollections.observableArrayList(stats));
		
		Logtable.toggleTalbe(false);
		StatTable.toggleTalbe(true);
		StatTable.refresh();
		Logtable.refresh();
	}
}

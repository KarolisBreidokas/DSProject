package GUI;

import java.io.Console;
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
import voIPStats.CDRLog.LogKey;
import voIPStats.CDRReader;
import voIPStats.Client;
import voIPStats.CustomSkipList;
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
	private LogTable table;

	public MainWindow(Stage stage) {
		this.stage = stage;
		initComponents();
	}

	private void initComponents() {
		table = new LogTable();

		Stream.of(new Label(MESSAGES.getString("Select_Users")), CbUsers,
				new Label(MESSAGES.getString("Select_Groups")), CbGroupingTypes)
				.forEach(n -> paneSelect.addColumn(0, n));
		CbUsers.setItems(FXCollections.observableArrayList(clients));
		CbUsers.getSelectionModel().select(0);
		CbUsers.setPrefWidth(TF_WIDTH);
		CbUsers.setOnAction(this);
		CbGroupingTypes.setPrefWidth(TF_WIDTH);
		CbGroupingTypes.setOnAction(this);

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
		VBox.setVgrow(table, Priority.ALWAYS);
		vboxTable.getChildren().addAll(new Label(MESSAGES.getString("TableName")), table);
		setRight(paneSelect);
		setCenter(vboxTable);
		//setBottom(paneParam123Events);
		showList();
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
		CDRLog[] modelList = CreateViewable(allLogs);
		table.formTable(CDRLog.TableHeader.length, 180);
		table.getItems().clear();
		table.setItems(FXCollections.observableArrayList(modelList));
		table.refresh();
	}

	private CDRLog[] CreateViewable(CustomSkipList<LogKey, CDRLog> allLogs2) {
		CDRLog[] ans = new CDRLog[allLogs2.getSize()];
		int a = 0;
		for (CDRLog s : allLogs2) {
			ans[a++] =s;
		}
		return ans;
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
			} else if (source.equals(CbGroupingTypes)) {

			}
		}
	}
}

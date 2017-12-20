package GUI;

import java.util.Locale;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

	public static void main(String[] args) {
		MainApp.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Locale.setDefault(Locale.US); // Suvienodiname skaičių formatus
		setUserAgentStylesheet(STYLESHEET_MODENA); // Nauja FX išvaizda
		// setUserAgentStylesheet(STYLESHEET_CASPIAN); //Sena FX išvaizda
		MainWindow.init(primaryStage);
	}
}

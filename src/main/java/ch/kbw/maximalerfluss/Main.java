package ch.kbw.maximalerfluss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;

/**
 * Diese Klasse kuemmert sich um die Initalisierung der Applikation.
 * 
 * Das Projekt basiert auf eine Vorlage vom Herr Rutschmann.
 * 
 * Die FXML-Datei View.fxml basiert auf das Projekt der Gruppe "Zyklensuche".
 * 
 * @author Alex Schaub
 */
public class Main extends Application {
	/**
	 * Das ist der Model der Applikation.
	 */
	private Model model;

	/**
	 * Das ist der Controller der Applikation.
	 */
	private Controller controller;

	/**
	 * Das ist der Thread, der im Hintergrund laeuft, um den Label info_kanten zu
	 * aktualisieren.
	 */
	private CustomThread t1;

	@Override
	public void start(Stage primaryStage) {
		try {
			model = new Model();
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource("View.fxml"));
			Pane root = myLoader.load();
			controller = (Controller) myLoader.getController();
			controller.setModel(model);
			t1 = new CustomThread(controller);
			t1.start();
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Maximaler Fluss");
			primaryStage.setScene(scene);

			/*
			 * schalte den Vollbildmodus ein und sorge dafuer, dass der Nutzer diesen nicht
			 * verlassen kann
			 */
//			primaryStage.setMaximized(true);
//			primaryStage.setFullScreen(true);
//			primaryStage.setResizable(false);
//			primaryStage.setFullScreenExitHint("");
//			primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		t1.interrupt();
	}

	public static void main(String[] args) {
		System.out.println("JavaFX " + System.getProperty("javafx.version") + ", running on Java "
				+ System.getProperty("java.version") + ".");
		launch(args);
	}
}

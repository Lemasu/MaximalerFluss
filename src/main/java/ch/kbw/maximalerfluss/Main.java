package ch.kbw.maximalerfluss;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * Diese Klasse kuemmert sich um die Initalisierung der Applikation.
 * 
 * Das Projekt basiert auf eine Vorlage vom Herr Rutschmann.
 * 
 * Die FXML-Datei View.fxml basiert auf das Projekt der Gruppe "Zyklensuche".
 */
public class Main extends Application {
	private Controller controller;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource("View.fxml"));
			Pane root = myLoader.load();
			controller = (Controller) myLoader.getController();
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Maximaler Fluss");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("JavaFX " + System.getProperty("javafx.version") + ", running on Java " + System.getProperty("java.version") + ".");
		launch(args);
	}
}

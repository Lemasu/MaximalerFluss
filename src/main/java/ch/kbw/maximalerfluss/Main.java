package ch.kbw.maximalerfluss;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource("View.fxml"));
			Pane root = myLoader.load();			
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

package ch.kbw.maximalerfluss;

import ch.kbw.maximalerfluss.gui.Controller;
import ch.kbw.maximalerfluss.gui.CustomThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;

/**
 * Diese Klasse kuemmert sich um die Initalisierung der Applikation.
 * <p>
 * Das Projekt basiert auf eine Vorlage vom Herr Rutschmann.
 * <p>
 * Die FXML-Datei View.fxml basiert auf das Projekt der Gruppe "Zyklensuche".
 *
 * @author Alex Schaub, Marc Schwendemann, Aron Gassner
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

    /**
     * Teile dieses Codes wurden von <a href="https://stackoverflow.com/a/31427041">Stack Overflow</a> ├╝bernommen und angepasst. Abfragedatum 02.05.2022.
     * <a href="https://stackoverflow.com/users/1759128/itachiuchiha">Geert Schuring</a> Jahr 2015.
     */
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
             * schaltet den Vollbildmodus ein und sorgt dafuer, dass der Nutzer diesen nicht
             * verlassen kann
             */
            primaryStage.setMaximized(true);
            primaryStage.setFullScreen(true);
            primaryStage.setResizable(false);
            primaryStage.setFullScreenExitHint("");
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Teile dieses Codes wurden von <a href="https://stackoverflow.com/a/42598179">Stack Overflow</a> ├╝bernommen und angepasst. Abfragedatum 02.05.2022.
     * <a href="https://stackoverflow.com/users/2189127/james-d">James_D</a> Jahr 2017.
     */
    public void stop() {
        t1.interrupt();
    }

    public static void main(String[] args) {
        System.out.println("JavaFX " + System.getProperty("javafx.version") + ", running on Java "
                + System.getProperty("java.version") + ".");
        launch(args);
    }
}

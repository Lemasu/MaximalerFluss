package ch.kbw.maximalerfluss;

import java.util.ArrayList;
import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import javafx.application.Platform;
import ch.kbw.maximalerfluss.algorithmus.Algorithmus;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Das ist der Controller der Applikation.
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 *
 * @author Alex Schaub
 */
public class Controller {
    /**
     * Das ist das Model dieses Controllers.
     */
    private Model model;

	/**
	 * Das ist der Algorithmus dieses Controllers
	 */
	private Algorithmus algorithmus;

    /**
     * Das sind die Knoten des Graphen.
     */
    private Knoten[][] knoten;

    /**
     * Das sind die Positionen der einzelnen Knoten in X-Richtung des Graphen.
     */
    private ArrayList<ArrayList<Integer>> x_positionen;

    /**
     * Das sind die Positionen der einzelnen Knoten in Y-Richtung des Graphen.
     */
    private ArrayList<ArrayList<Integer>> y_positionen;

    /**
     * Das sind die Kanten des Graphen.
     */
    private ArrayList<Kante> kanten;

    /**
     * Das ist die Liste mit den Informationen von den Kanten.
     * Dieser Liste wird von der ListView benoetigt.
     */
    private ObservableList<String> informationen_kanten;

    /**
     * Das ist die Groesse der Kreise fuer die Knoten des Graphen.
     */
    private int kreisgroesse;

    /**
     * Dieser Boolean zeigt auf, ob die Methode "graphZeichnen()" bereits aufgerufen
     * wurde.
     */
    private boolean graph_zeichnen_aufgerufen;

    /**
     * Dieser Boolean zeigt auf, ob die Methode "graphMitMaximalerFlussZeichnen()"
     * bereits aufgerufen wurde.
     */
    private boolean graph_mit_maximaler_fluss_zeichnen_aufgerufen;

    /**
     * Das ist die ID vom aktuellen Startknoten.
     * <p>
     * Die ID wird als einen Int-Array abgespeichert, um damit spaeter einfacher auf
     * den Startknoten zugreifen zu koennen.
     */
    private int[] id_startknoten;

    /**
     * Das ist die ID vom aktuellen Zielknoten.
     * <p>
     * Die ID wird als einen Int-Array abgespeichert, um damit spaeter einfacher auf
     * den Startknoten zugreifen zu koennen.
     */
    private int[] id_zielknoten;
    
    // erstelle die Darstellung aus dem Graphen und der Properties-Datei
    SmartGraphPanel<Knoten, Kante> graphView;
    
    // erstelle eine ArrayList, um die Knoten, welche mit Hilfe von JavaFXSmartGraph ausgegeben werden, abzuspeichern.
    private ArrayList<Vertex<Knoten>> nodes;

    /**
     * Das ist die StackPane, in dem der Graph dargestellt wird.
     */
    @FXML
    private StackPane stackpane;

    /**
     * Das ist das Textfeld, in welchem der Nutzer angeben kann, welcher Knoten als
     * Startknoten gesetzt werden soll.
     */
    @FXML
    private TextField startknoten_setzen;

    /**
     * Das ist das Textfeld, in welchem der Nutzer angeben kann, welcher Knoten als
     * Zielknoten gesetzt werden soll.
     */
    @FXML
    private TextField zielknoten_setzen;

    /**
     * Das ist das Textfeld fuer die Anzahl der Zeilen im Graphen, welcher generiert werden soll.
     */
    @FXML
    private TextField anzahl_zeilen;

    /**
     * Das ist das Textfeld fuer die Anzahl der Spalten im Graphen, welcher generiert werden soll.
     */
    @FXML
    private TextField anzahl_spalten;

    /**
     * Das ist das Textfeld fuer die Anzahl der Spalten im Graphen, welcher generiert werden soll.
     */
    @FXML
    private TextField anzahl_kanten;

    /**
     * Dieses Label dient dazu, dem Nutzer Informationen zu uebermitteln.
     */
    @FXML
    private Label info;

    /**
     * Dieses Label dient dazu, dem Nutzer die Bedingungen der Anzahl Kanten zu uebermitteln.
     */
    @FXML
    private Label info_kanten;

    /**
     * Diese Methode wird bei der Initalisierung dieses Controllers aufgerufen.
     */
    @FXML
    public void initialize() {
//		// convert canvas from fxml file to GraphicsContext
//		gc = canvas.getGraphicsContext2D();
//
//		// lege die Kreisgroesse fest
//		kreisgroesse = 20;
//
//		// Da die zwei Methoden "graphZeichnen()" und "graphMitMaximalerFlussZeichnen()"
//		// noch nicht aufgerufen wurden, werden die zwei Booleans auf false gesetzt.
//		graph_zeichnen_aufgerufen = false;
//		graph_mit_maximaler_fluss_zeichnen_aufgerufen = false;
//		
//		/*
//		 * "knoten_setzen" wird am Anfang auf 1 gesetzt.
//		 * 
//		 * Der Grund dafuer ist, dass am Anfang weder der Knopf fuer die Setzung der Startknoten, noch der Knopf fuer die Setzung der Zielknoten ausgewaehlt sind.
//		 */
//		knoten_setzen = 1;
//		
//		// setzte die Methode, damit der Nutzer auf dem Canvas die Knoten anklicken kann
//		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
//		    public void handle(MouseEvent event) {
//		    	knotenAnklicken(event);
//		    }
//		});
    }

    /**
     * Mit dieser Methode wird das Model fuer diesem Controller gesetzt.
     *
     * @param model Das ist das Model dieses Controllers.
     */
    public void setModel(Model model) {
        this.model = model;
		algorithmus = new Algorithmus(model.getGraph());
    }

    /**
     * Mit dieser Methode kann diese Applikation beendet werden.
     */
    @FXML
    public void applikationBeenden() {
        Platform.exit();
    }

    /**
     * Mit dieser Methode wird ein neuer Graph generiert und ausgegeben.
     */
    @FXML
    public void generate() {
    	// loesche die IDs fuer die alten Start- und Zielknoten
    	id_startknoten = null;
    	id_zielknoten = null;
    	
        // initialisiere die Zeilen, Spalten und Kanten des Graphen
        int zeilen;
        int spalten;
        int kanten;
        
        // ändern der Textfarbe des Labels info
        info.setTextFill(Color.RED);

        // probiere die Zeilen und Spalten auf die Inhalte der Textfelder zu setzen
        try {
            zeilen = Integer.parseInt(anzahl_zeilen.getText());
            spalten = Integer.parseInt(anzahl_spalten.getText());
            kanten = Integer.parseInt(anzahl_kanten.getText());

        // ueberpruefe, ob in jedem Textfeld etwas eingegeben wurde
        } catch (NullPointerException e) {
            info.setText("Bitte geben Sie Zahlen ein.");
            return;

        // ueberpruefe, ob nur Zahlen eingegeben wurden
        } catch (NumberFormatException e) {
            info.setText("Bitte geben Sie nur Zahlen ein.");
            return;
        }

        /*
         * ueberpruefe, ob keine der folgenden Bedingungen erfuellt sind:
         * - Zeilen > 8
         * - Spalten > 8
         * - Zeilen = 1 und spalten = 1
         * - Zeilen < 1
         * - Spalten < 1
         */
        if (zeilen > 8 || spalten > 8 || (zeilen == 1 && spalten == 1) || zeilen < 1 || spalten < 1) {
            info.setText("Bitte achten Sie darauf, dass die Anzahl Zeilen und Spalten nicht grösser als 8 ist, dass entweder die Anzahl Zeilen oder die Anzahl Spalten grösser als 1 ist und, dass die Anzahl Zeilen und Spalten nicht kleiner als 1 ist.");
            return;
        }

        // initialisieren, der minimalen und maximalen Anzahl von Kanten
        int min = (zeilen * spalten - 1);
        int max = (zeilen * spalten) * (zeilen * spalten - 1);

        // ueberpruefen, ob die minimale und maximale Anzahl der Kanten eingehalten wurde
        if (Integer.parseInt(anzahl_kanten.getText()) < min || Integer.parseInt(anzahl_kanten.getText()) > max) {
            info.setText("Bitte geben Sie eine Anzahl Kanten von " + min + " bis " + max + " ein.");
            return;
        }

        // generiert den Graphen
        model.getGraph().graphGenerieren(zeilen, spalten, kanten);

        // Graph zeichnen
        graphZeichnen();
    }

    /**
     * Mit dieser Methode wird der Startknoten gesetzt. Falls es einen alten
     * Startknoten gibt, wird dieser zu einem normalen Knoten umgewandelt.
     * <p>
     * Der Nutzer kann im Textfeld die ID vom Knoten eingeben. Dieser wird dann als
     * neuer Startknoten gesetzt.
     */
    @FXML
    public void startknotenSetzen() {
        /*
         * Zuerst wird die ID, welche vom Nutzer eingegeben wurde aufgeteilt. Die beiden
         * Zahlen werden dann als einen Int-Array abgespeichert, damit spaeter auf
         * diesem zugegriffen werden kann.
         */
        String[] position_als_text = startknoten_setzen.getText().split("\\.");
        
        // leere das Infofeld
        info.setText("");
        
        // ändern der Textfarbe des Labels info
        info.setTextFill(Color.RED);
        
        /*
         * Falls die der Nutzer mehr als zwei Zahlenbloecke oder kein Text eingegeben
         * hatte oder als letztes Zeichen einen Punkt eingeben hatte, soll ein
         * entsprechender Fehlermeldung auftauchen und diese Methode abgebrochen werden.
         */
        if (startknoten_setzen.getText().length() <= 0 || position_als_text.length > 2
                || startknoten_setzen.getText().charAt(startknoten_setzen.getText().length() - 1) == '.') {
            info.setText("Bitte geben Sie die ID im folgenden Format ein: [Zeile].[Spalte]");
            return;
        }
        
        /*
         * Die ID soll vom String in Integer umgewandelt werden.
         * 
         * Die beiden Teile der ID, welche fuer die Zeile und Spalte stehen, werden fuer die spaetere Verwendung separat abgespeichert.
         */
        int[] position = new int[2];
        try {
            position[0] = Integer.parseInt(position_als_text[0]);
            position[1] = Integer.parseInt(position_als_text[1]);
        } catch (NumberFormatException e) {
            /*
             * Falls es Probleme bei Parsing vom String in Int gibt, wird der Nutzer darauf
             * aufmerksam gemacht. Danach wird diese Methode abgebrochen.
             */
            info.setText(
                    "Bitte verwenden Sie nur Zahlen und den Punkt als Trennzeichen. Bitte verwenden Sie keine Leerschläge.");
            return;
        } catch (IndexOutOfBoundsException e) {
            /*
             * Falls nicht zwei Zahlenbloecke eingegeben wurde, wird der Nutzer darauf
             * aufmerksam gemacht. Danach wird diese Methode abgebrochen.
             */
            info.setText("Bitte geben Sie die ID im folgenden Format ein: [Zeile].[Spalte]");
            return;
        }

        // hole die Knoten vom Model
        Knoten[][] knoten = model.getGraph().getKnoten();

        try {
            // wandle den neuen Startknoten in einen Startknoten um
            knoten[position[0] - 1][position[1] - 1].setKategorie(0);
        } catch (IndexOutOfBoundsException e) {
            /*
             * Falls die Zahlen zu gross oder zu klein sind und dadurch kein passender
             * Knoten angesprochen werden kann, wird eine entsprechende Fehlermeldung
             * ausgegeben.
             *
             * Danach wird diese Methode abgebrochen.
             */
            info.setText("Die Zahl für die Zeile muss kleiner sein als " + (knoten.length + 1)
                    + ". Die Zahl für die Spalte muss kleiner sein als " + (knoten[0].length + 1)
                    + ". Ausserdem müssen beide Zahlen grösser als 0 sein.");
            return;
        } catch (NullPointerException e) {
            /*
             * Falls der Graph noch nicht generiert wurde, wird dem Nutzer mitgeteilt, dass
             * dieser zuerst den Graph generieren muss.
             *
             * Danach wird diese Methode abgebrochen.
             */
            info.setText("Sie müssen zuerst den Graphen generieren lassen.");
            return;
        }

        /*
         * Zuerst wird ueberprueft, ob bereits ein alter Startknoten abgespeichert
         * wurde.
         *
         * Falls ein alter Startknoten abgespeichert wurde, wird dieser in einen
         * normalen Knoten umgewandelt.
         *
         * Der alter Startknoten wird aber nur ueberschrieben, falls dieser nicht bereits durch den Zielknoten ueberschrieben wurde.
         */
        if (id_startknoten != null && (id_startknoten[0] != position[0] || id_startknoten[1] != position[1]) && knoten[id_startknoten[0] - 1][id_startknoten[1] - 1].getKategorie() != 2) {
            knoten[id_startknoten[0] - 1][id_startknoten[1] - 1].setKategorie(1);
        }

        // setzte die ID des neuen Startknotens als ID vom neuen alten Startknoten
        id_startknoten = position;
        
        // faerbe alle Knoten
        for (int i = 0; i < nodes.size(); i++) {
            // hole den aktuellen Knoten
            Vertex<Knoten> node = nodes.get(i);

            /*
             * ueberpruefe die Kategorie des aktuellen Knotens
             *
             * falls der aktueller Knoten die Kategorie Startknoten, also 0, besitzt, wird
             * der aktueller Knoten gruen gefaerbt
             *
             * falls der aktueller Knoten die Kategorie Zielknoten, also 1, besitzt, erhaelt
             * der aktueller Knoten die Standardfarbe
             * 
             * falls der aktueller Knoten die Kategorie Zielknoten, also 2, besitzt, wird
             * der aktueller Knoten rot gefaerbt
             */
            if (node.element().getKategorie() == 0) {
                // faerbe den Knoten Gruen
                graphView.getStylableVertex(node).setStyle("-fx-fill: lightgreen; -fx-stroke: green;");
            } else if (node.element().getKategorie() == 2) {
                // faerbe den Knoten Rot
                graphView.getStylableVertex(node).setStyle("-fx-fill: pink; -fx-stroke: red;");
            } else {
            	// setze keinen Style, dadurch erhaelt die Knoten die Standardfarbe
            	graphView.getStylableVertex(node).setStyle("");
            }
        }
    }

    /**
     * Mit dieser Methode wird der Zielknoten gesetzt. Falls es einen alten
     * Zielknoten gibt, wird dieser zu einem normalen Knoten umgewandelt.
     * <p>
     * Der Nutzer kann im Textfeld die ID vom Knoten eingeben. Dieser wird dann als
     * neuer Zielknoten gesetzt.
     */
    @FXML
    public void zielknotenSetzen() {
        /*
         * Zuerst wird die ID, welche vom Nutzer eingegeben wurde aufgeteilt. Die beiden
         * Zahlen werden dann als einen Int-Array abgespeichert, damit spaeter auf
         * diesem zugegriffen werden kann.
         */
        String[] position_als_text = zielknoten_setzen.getText().split("\\.");

        // leere das Infofeld
        info.setText("");
        
        // ändern der Textfarbe des Labels info
        info.setTextFill(Color.RED);

        /*
         * Falls die der Nutzer mehr als zwei Zahlenbloecke oder kein Text eingegeben
         * hatte oder als letztes Zeichen einen Punkt eingeben hatte, soll ein
         * entsprechender Fehlermeldung auftauchen und diese Methode abgebrochen werden.
         */
        if (zielknoten_setzen.getText().length() <= 0 || position_als_text.length > 2
                || zielknoten_setzen.getText().charAt(zielknoten_setzen.getText().length() - 1) == '.') {
            info.setText("Bitte geben Sie die ID im folgenden Format ein: [Zeile].[Spalte]");
            return;
        }
        
        /*
         * Die ID soll vom String in Integer umgewandelt werden.
         * 
         * Die beiden Teile der ID, welche fuer die Zeile und Spalte stehen, werden fuer die spaetere Verwendung separat abgespeichert.
         */
        int[] position = new int[2];
        try {
            position[0] = Integer.parseInt(position_als_text[0]);
            position[1] = Integer.parseInt(position_als_text[1]);
        } catch (NumberFormatException e) {
            /*
             * Falls es Probleme bei Parsing vom String in Int gibt, wird der Nutzer darauf
             * aufmerksam gemacht. Danach wird diese Methode abgebrochen.
             */
            info.setText(
                    "Bitte verwenden Sie nur Zahlen und den Punkt als Trennzeichen. Bitte verwenden Sie keine Leerschläge.");
            return;
        } catch (IndexOutOfBoundsException e) {
            /*
             * Falls nicht zwei Zahlenbloecke eingegeben wurde, wird der Nutzer darauf
             * aufmerksam gemacht. Danach wird diese Methode abgebrochen.
             */
            info.setText("Bitte geben Sie die ID im folgenden Format ein: [Zeile].[Spalte]");
            return;
        }

        // hole die Knoten vom Model
        Knoten[][] knoten = model.getGraph().getKnoten();

        try {
            // wandle den neuen Zielknoten in einen Zielknoten um
            knoten[position[0] - 1][position[1] - 1].setKategorie(2);
        } catch (IndexOutOfBoundsException e) {
            /*
             * Falls die Zahlen zu gross oder zu klein sind und dadurch kein passender
             * Knoten angesprochen werden kann, wird eine entsprechende Fehlermeldung
             * ausgegeben.
             *
             * Danach wird diese Methode abgebrochen.
             */
            info.setText("Die Zahl für die Zeile muss kleiner sein als " + (knoten.length + 1)
                    + ". Die Zahl für die Spalte muss kleiner sein als " + (knoten[0].length + 1)
                    + ". Ausserdem müssen beide Zahlen grösser als 0 sein.");
            return;
        } catch (NullPointerException e) {
            /*
             * Falls der Graph noch nicht generiert wurde, wird dem Nutzer mitgeteilt, dass
             * dieser zuerst den Graph generieren muss.
             *
             * Danach wird diese Methode abgebrochen.
             */
            info.setText("Sie müssen zuerst den Graphen generieren lassen.");
            return;
        }

        /*
         * Zuerst wird ueberprueft, ob bereits ein alter Zielknoten abgespeichert wurde.
         *
         * Falls ein alter Zielknoten abgespeichert wurde, wird dieser in einen normalen
         * Knoten umgewandelt.
         *
         * Der alter Zielknoten wird aber nur ueberschrieben, falls dieser nicht bereits durch den Startknoten ueberschrieben wurde.
         */
        if (id_zielknoten != null && (id_zielknoten[0] != position[0] || id_zielknoten[1] != position[1]) && knoten[id_zielknoten[0] - 1][id_zielknoten[1] - 1].getKategorie() != 0) {
            knoten[id_zielknoten[0] - 1][id_zielknoten[1] - 1].setKategorie(1);
        }

        // setzte die ID des neuen Zielknotens als ID vom neuen alten Zielknoten
        id_zielknoten = position;

        // faerbe alle Knoten
        for (int i = 0; i < nodes.size(); i++) {
            // hole den aktuellen Knoten
            Vertex<Knoten> node = nodes.get(i);

            /*
             * ueberpruefe die Kategorie des aktuellen Knotens
             *
             * falls der aktueller Knoten die Kategorie Startknoten, also 0, besitzt, wird
             * der aktueller Knoten gruen gefaerbt
             *
             * falls der aktueller Knoten die Kategorie Zielknoten, also 1, besitzt, erhaelt
             * der aktueller Knoten die Standardfarbe
             * 
             * falls der aktueller Knoten die Kategorie Zielknoten, also 2, besitzt, wird
             * der aktueller Knoten rot gefaerbt
             */
            if (node.element().getKategorie() == 0) {
                // faerbe den Knoten Gruen
                graphView.getStylableVertex(node).setStyle("-fx-fill: lightgreen; -fx-stroke: green;");
            } else if (node.element().getKategorie() == 2) {
                // faerbe den Knoten Rot
                graphView.getStylableVertex(node).setStyle("-fx-fill: pink; -fx-stroke: red;");
            } else {
            	// setze keinen Style, dadurch erhaelt die Knoten die Standardfarbe
            	graphView.getStylableVertex(node).setStyle("");
            }
        }
    }

    /**
     * Diese Methode gibt den Graphen mithilfe von JavaFXSmartGraph in einem SubScene aus.
     */
    private void graphZeichnen() {
        // ändern der Textfarbe des Labels info
        info.setTextFill(Color.RED);

        // leere das Info-Feld, damit alte Informationen nicht dauernd zu sehen sind
        info.setText("");

        Digraph<Knoten, Kante> graph = new DigraphEdgeList<>();

        // alle Knoten holen
        Knoten[][] knoten = model.getGraph().getKnoten();

        // alle Kanten holen
        kanten = model.getGraph().getKanten();

        nodes = new ArrayList<Vertex<Knoten>>();

        // erstelle eine ArrayList, um die Kanten, welche mit Hilfe von JavaFXSmartGraph ausgegeben werden, abzuspeichern.
        ArrayList<Edge<Kante, Knoten>> edges = new ArrayList<Edge<Kante, Knoten>>();

        // erstelle Variablen, um die Knoten im Matrixform zu platzieren.
        // ArrayList fuer die Position der Knoten
        ArrayList<Double> x_positionen = new ArrayList<Double>();
        ArrayList<Double> y_positionen = new ArrayList<Double>();
        // Veraenderung von X und Y in Relation zur Anzahl der Knoten
        double veraenderung_x = stackpane.getWidth() / (knoten[0].length + 1);
        double veraenderung_y = stackpane.getHeight() / (knoten.length + 1);
        /*
         * Position von einem Knoten
         * 
         * Die Koordinate von einem Knoten haengt von der Anzahl Zeilen und Spalten ab.
         */
        double x = veraenderung_x;
        double y = veraenderung_y;

        // fuege jeden Knoten einzeln hinzu
        for (int i = 0; i < knoten.length; i++) {
            for (int j = 0; j < knoten[i].length; j++) {
                // erstelle den Knoten fuer die Ausgabe auf dem Graphen, der eigentliche Knoten wurde aber bereits erstellt
                nodes.add(graph.insertVertex(knoten[i][j]));

                // speichere die Koordinate des Knotens ab
                x_positionen.add(x);
                y_positionen.add(y);

                // passt die X-Koordinate dem naechsten Knoten an
                x += veraenderung_x;
            }
            
            // setze die X-Koordinate wieder auf X-Position fuer den ersten Knoten einer Zeile
            x = veraenderung_x;

            // passt die X-Koordinate der naechsten Zeile an
            y += veraenderung_y;
        }

        // fuege jede Kante einzeln hinzu
        for (int i = 0; i < kanten.size(); i++) {
            // erstelle den Knoten fuer die Ausgabe auf dem Graphen, der eigentlicher Knoten wurde aber bereits erstellt
            System.out.println(kanten.get(i).getKnoten_1() + "" + kanten.get(i).getKnoten_2() + "");
            edges.add(graph.insertEdge(kanten.get(i).getKnoten_1(), kanten.get(i).getKnoten_2(), kanten.get(i)));
            //nodess.add(graph.insertEdge(kanten.get(i).getKnoten_1(), kanten.get(i).getKnoten_2(), kanten.get(i).getKapazitaet() + ""));
        }

        // lade die Properties-Datei
        SmartGraphProperties properties = new SmartGraphProperties();

        graphView = new SmartGraphPanel<>(graph, properties, new SmartCircularSortedPlacementStrategy());

        SmartGraphDemoContainer graph_container = new SmartGraphDemoContainer(graphView);

        // gebe den Graphen auf einer SubScene aus
        SubScene subscene = new SubScene(graph_container, 700, 600);

        // ueberpruefe, ob schon einen Graphen auf dem GUI gibt
        // loesche diesen Graphen, wenn es diesen schon gibt
        if (stackpane.getChildren().size() > 0) {
            stackpane.getChildren().remove(0);
        }

        // gebe den neuerstellten Graphen auf dem GUI aus
        stackpane.getChildren().add(subscene);

        subscene.heightProperty().bind(stackpane.heightProperty());
        subscene.widthProperty().bind(stackpane.widthProperty());

        graph_container.setMinSize(stackpane.getMinHeight(), stackpane.getMinWidth());
        graph_container.setPrefSize(stackpane.getPrefHeight(), stackpane.getPrefWidth());
        graph_container.setMaxSize(stackpane.getMaxHeight(), stackpane.getMaxWidth());

        // platziere alle Knoten an die vorhin berechneten Koordinaten
        for (int i = 0; i < nodes.size(); i++) {
            graphView.setVertexPosition(nodes.get(i), x_positionen.get(i), y_positionen.get(i));
        }

        // platziere alle Knoten an die vorhin berechneten Koordinaten
        for (int i = 0; i < nodes.size(); i++) {
            // hole den aktuellen Knoten
            Vertex<Knoten> node = nodes.get(i);

            // platziere den aktuellen Knoten
            graphView.setVertexPosition(node, x_positionen.get(i), y_positionen.get(i));
        }
    }

//	/**
//	 * Diese Methode zeichnet den Graphen mit dem maximalen Fluss auf.
//	 * 
//	 * Diese Methode ist dafuer gedacht, den Graphen erneut aufzuzeichen, damit die
//	 * Kanten des maximalen Flusses nicht ueber den Knoten liegen.
//	 */
//	private void graphMitMaximalerFlussZeichnen() {
//		// Clears canvas
//		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//		
//		/*
//		 * Zuerst muss ueberprueft werden, ob die benoetigten ArrayLists bereits
//		 * erstellt wurden, dass heisst, ob die Methode "graphZeichnen()" bereits
//		 * aufgerufen wurde.
//		 * 
//		 * Sollte die Methode "graphZeichnen()" noch nicht aufgerufen worden sein, wird
//		 * diese Methode abgebrochen.
//		 */
//		if (!graph_zeichnen_aufgerufen) {
//			System.err.println("Die Methode \"graphZeichnen()\" muss vor dieser Methode aufgerufen werden.");
//			return;
//		}
//
//		// erstelle eine ArrayList mit den Kanten des maximalen Flusses
//		ArrayList<Kante> kanten_maximaler_fluss = model.getGraph().getKanten_maximaler_fluss();
//
//		/*
//		 * Es muss dann ueberprueft werden, ob es einen maximalen Fluss gibt.
//		 * 
//		 * Wenn es keinen maximalen Fluss gibt, wird diese Methode abgebrochen.
//		 * 
//		 * Zuerst wird ueberprueft, ob die ArrayList null ist, damit die Ueberpruefung
//		 * abgebrochen wird und es nicht zu einer NullPointerException kommt.
//		 */
//		if (kanten_maximaler_fluss == null || kanten_maximaler_fluss.size() == 0) {
//			System.err.println("Es gibt keinen maximalen Fluss.");
//			return;
//		}
//
//		// zeichne alle Kanten zuerst
//		kantenZeichnen(kanten, Color.BLACK, true);
//
//		// zeichne anschliessend die Kanten des maximalen Flusses
//		kantenZeichnen(kanten_maximaler_fluss, Color.BLUE, false);
//
//		// Das sind die Positionen der einzelnen Knoten in X-Richtung des Graphen.
//		x_positionen = new ArrayList<ArrayList<Integer>>();
//		// Das sind die Positionen der einzelnen Knoten in Y-Richtung des Graphen.
//		y_positionen = new ArrayList<ArrayList<Integer>>();
//
//		/*
//		 * zeichne dann die Knoten, damit diese ueber den Kanten liegt
//		 * 
//		 * Beim normalen Graphen macht es nichts die Knoten zuerst zu zeichnen, da alle
//		 * Kanten schwarz sind.
//		 */
//		knotenZeichnen(knoten, kreisgroesse);
//
//		// zeichne zum Schluss den Startknoten und den Endknoten ueber deren normalen
//		// Knoten
//		startknotenZielknotenZeichnen(kreisgroesse);
//
//		// ueberpruefe, ob der Boolean "graph_mit_maximaler_fluss_zeichnen_aufgerufen"
//		// schon auf true gesetzt wurde, wenn nicht, setzte auf true
//		if (!graph_mit_maximaler_fluss_zeichnen_aufgerufen) {
//			graph_mit_maximaler_fluss_zeichnen_aufgerufen = true;
//		}
//	}

//	/**
//	 * Diese Methode zeichnet alle Kanten eines ArrayLists, welche ihm uebergeben
//	 * wird, im Graphen.
//	 * 
//	 * @param kanten Das sind die Kanten, welche gezeichnet werden sollen.
//	 * @param farbe  Das ist die Farbe, welche die Kanten besitzen sollen.
//	 * @param informationen_speichern Dieser Boolean sagt, ob die Informationen zu der gezeichnete Kante gespeichert werden soll.
//	 */
//	private void kantenZeichnen(ArrayList<Kante> kanten, Paint farbe, boolean informationen_speichern) {
//		// setzt die Farbe vom Stroke auf die gewuenschte Farbe
//		gc.setStroke(farbe);
//		
//		// Schleife, um die einzelnen Kanten zu zeichnen
//		for (Kante kante : kanten) {
//			/*
//			 * Speichere die ID der aktuellen Knoten ab, um spaeter wieder darauf
//			 * zuzugreifen zu koennen.
//			 * 
//			 * Die IDs wurden als Strings abgespeichert. Die waagerechten Ebenen und die
//			 * senkrechten Ebenen wurden mit einem Punkt getrennt.
//			 * 
//			 * Um die ID fuer die spaetere Verwendung optimal abzuspeichern, wurden die
//			 * beiden Teile der ID aufgeteilt und der Punkt entfernt.
//			 */
//			String[] knoten_1 = kante.getKnoten_1().getId().split("\\.");
//			String[] knoten_2 = kante.getKnoten_2().getId().split("\\.");
//
//			/*
//			 * Es muss jeweils 10 zu den Positionen aus den ArrayList hinzuaddiert werden,
//			 * damit die Linien in den Knoten starten und enden. Sonst wuerden die Linien
//			 * knapp ausserhalb der Knoten starten und enden.
//			 * 
//			 * Die Zahlen aus den beiden Arrays "knoten_1" und "knoten_2" muessen um eins
//			 * verkleinert werden, da die Zaehlung der IDs nicht bei null beginnt.
//			 */
//			int x_position_1 = x_positionen.get(Integer.parseInt(knoten_1[0]) - 1)
//					.get(Integer.parseInt(knoten_1[1]) - 1) + 10;
//			int y_position_1 = y_positionen.get(Integer.parseInt(knoten_1[0]) - 1)
//					.get(Integer.parseInt(knoten_1[1]) - 1) + 10;
//			int x_position_2 = x_positionen.get(Integer.parseInt(knoten_2[0]) - 1)
//					.get(Integer.parseInt(knoten_2[1]) - 1) + 10;
//			int y_position_2 = y_positionen.get(Integer.parseInt(knoten_2[0]) - 1)
//					.get(Integer.parseInt(knoten_2[1]) - 1) + 10;
//
//			// zeichne die Kante
//			gc.strokeLine(x_position_1, y_position_1, x_position_2, y_position_2);
//		}
//	}

//	/**
//	 * Diese Methode zeichnet den Startknoten und den Zielknoten des Graphen.
//	 * 
//	 * @param kreisgroesse Das ist die Groesse der Kreise fuer die Knoten des
//	 *                     Graphen.
//	 */
//	private void startknotenZielknotenZeichnen(int kreisgroesse) {
//		// setzte die Farbe fuer die Stroke auf Schwarz, damit die Schrift schwarz ist
//		gc.setStroke(Color.BLACK);
//
//		/*
//		 * ueberschreibe den Kreis fuer den Start mit einem gruenen Kreis, in dessen
//		 * Mitte ein grosses S steht
//		 */
//		knotenFaerben(0, "S", Color.LIGHTGREEN);
//
//		/*
//		 * ueberschreibe den Kreis fuer das Ziel mit einem roten Kreis, in dessen
//		 * Mitte ein grosses Z steht
//		 */
//		knotenFaerben(2, "Z", Color.RED);
//	}

//	/**
//	 * Diese Methode faerbt einen einzelnen Knoten und beschriftet diesen mit dem gewuenschten Text.
//	 * 
//	 * Sobald der Knoten gefunden wurde, wird nicht mehr nach einem weiteren Knoten gesucht.
//	 * Der Grund dafuer ist, dass es immer nur einen Startknoten und einen Zielknoten geben kann.
//	 * 
//	 * @param kategorie Das ist die Kategorie des gesuchten Knotens.
//	 * @param text Das ist der Text, der auf der Knoten angezeigt werden soll.
//	 * @param farbe Das ist die Farbe, auf die der Knoten geaendert werden soll.
//	 */
//	private void knotenFaerben(int kategorie, String text, Paint farbe) {
//		// Zuerst wird durch die einzelnen Zeilen iteriert.
//		for (int i = 0; i < knoten.length; i++) {
//			// Es wird ein Boolean erstellt, welches dem Programm sagt, ob der zu faerbende Knoten schon gefunden wurde.
//			boolean gefunden = false;
//			
//			// Anschliessend wird durch die einzelnen Knoten, der Zeile iteriert.
//			for (int j = 0; j < knoten[i].length; j++) {
//				// ueberprueft, ob dieser Knoten zur gewuenschten Kategorie gehoert
//				if (knoten[i][j].getKategorie() == kategorie) {
//					// erstellt den Knoten
//					gc.setFill(farbe);
//					gc.fillOval(x_positionen.get(i).get(j), y_positionen.get(i).get(j), kreisgroesse, kreisgroesse);
//					gc.strokeText(text, x_positionen.get(i).get(j) + 7, y_positionen.get(i).get(j) + 14);
//					
//					// sorge dafuer, dass die for-Schleifen vorzeitig abgebrochen werden, da der gesuchte Knoten bereits gefunden wurde und jetzt nicht mehr weitergesucht werden muss
//					gefunden = true;
//					j = knoten[i].length;
//				}
//			}
//			
//			// sorge dafuer, dass die for-Schleife abgebrochen wird, wenn der gesuchte Knoten bereits gefunden wurde
//			if (gefunden) {
//				i = knoten.length;
//			}
//		}
//	}

//	/**
//	 * Mit dieser Methode kann der Graph neu aufgezeichnet werden.
//	 * 
//	 * Diese Methode achtet darauf, ob der maximaler Fluss bereits einmal
//	 * aufgezeichnet wurde und zeichnet den maximalen Fluss in dem Fall erneut auf.
//	 */
//	private void graphNeuZeichnen() {
//		// zeichne zuerst den Graphen ganz normal auf
//		graphZeichnen();
//
//		// Sollte die Methode "graphMitMaximalerFlussZeichnen()" bereits aufgerufen
//		// worden sein, zeichne auch den maximalen Fluss auf.
//		if (graph_mit_maximaler_fluss_zeichnen_aufgerufen) {
//			graphMitMaximalerFlussZeichnen();
//		}
//	}

//	/**
//	 * Diese Methode sorgt dafuer, dass der Nutzer einen Knoten anklicken kann.
//	 * 
//	 * @param event Das ist die MouseEvent vom Canvas. Die MouseEvent vom Canvas muss angegeben werden, damit diese Methode die Mausposition auf dem Canvas auslesen kann.
//	 */
//	private void knotenAnklicken(MouseEvent event) {
//		/*
//		 * Zuerst wird ueberprueft, ob eine bestimmte Knotenkategorie gesetzt werden soll.
//		 * 
//		 * 1 ist der Standardwert, dass bedeutet, dass noch keine zu setztende Knotenkategorie ausgewaehlt wurde.
//		 * Wenn der Wert also nicht 1 ist, bedeutet das, dass eine Knotenkategorie ausgewaehlt wurde.
//		 */
//		if (knoten_setzen != 1) {
//			// Zuerst wird der alte Knoten mit dieser Kategorie wieder in einen normalen Knoten umgewandelt.
//			// Zuerst wird durch die einzelnen Zeilen iteriert.
//			for (int i = 0; i < knoten.length; i++) {
//				/*
//				 * initialisiere eine Variable fuer die Laenge einer Zeile
//				 * 
//				 * Die Initialisierung erfolgt bereits hier, damit spaeter keine Exception auftaucht.
//				 * 
//				 * Urspruenglich gab es die folgende for-Schleife: "for (int j = 0; j < x_positionen.get(i).size(); j++) {".
//				 */
//				int laenge_zeile = knoten[i].length;
//				
//				// Anschliessend wird durch die einzelnen Knoten der Zeile iteriert.
//				for (int j = 0; j < laenge_zeile; j++) {
//					// Es wird ueberprueft, ob dieser Knoten von gleicher Kategorie wie die gewuenschte Kategorie ist.
//					if (knoten[i][j].getKategorie() == knoten_setzen) {
//						// der Knoten wird zu einem normalen Knoten
//						knoten[i][j].setKategorie(1);
//						
//						// sorge dafuer, dass die for-Schleifen abgebrochen werden, da der angeklickte Knoten bereits gefunden wurde
//						j = knoten[i].length;
//						i = knoten.length;
//					}
//				}
//			}
//			
//			// Anschliessend wird der passende Knoten auf die gewuenschte Kategorie gesetzt.
//			// Zuerst wird durch die einzelnen Zeilen iteriert.
//			for (int i = 0; i < x_positionen.size(); i++) {
//				/*
//				 * initialisiere eine Variable fuer die Laenge einer Zeile
//				 * 
//				 * Die Initialisierung erfolgt bereits hier, damit spaeter keine Exception auftaucht.
//				 * 
//				 * Urspruenglich gab es die folgende for-Schleife: "for (int j = 0; j < x_positionen.get(i).size(); j++) {".
//				 */
//				int laenge_zeile = x_positionen.get(i).size();
//				
//				// Anschliessend wird durch die einzelnen Knoten, beziehungsweise deren Positionen, der Zeile iteriert.
//				for (int j = 0; j < laenge_zeile; j++) {
//					// Zuerst werden die Variablen fuer die if-Verzweigung initialisiert, damit die if-Verzweigung nicht zu unuebersichtlich wird.
//					// Die X-Position vom Knoten.
//					int x_position = x_positionen.get(i).get(j);
//					// Die Y-Position vom Knoten.
//					int y_position = y_positionen.get(i).get(j);
//					// Die X-Position vom Maus.
//					double x_maus = event.getX();
//					// Die Y-Position vom Maus.
//					double y_maus = event.getY();
//					
//					// ueberprueft, ob der Knoten angeklickt wurde
//					if ((x_maus >= x_position && x_maus <= (x_position + 20)) && (y_maus >= y_position && y_maus <= (y_position + 20))) {
//						// setze die Kategorie des angeklickten Knotens auf die gewuenschte Kategorie
//						knoten[i][j].setKategorie(knoten_setzen);
//						
//						// zeichne den Graphen neu auf, damit der angeklickte Knoten richtig dargestellt wird
//						graphNeuZeichnen();
//						
//						// sorge dafuer, dass die for-Schleifen abgebrochen werden, da der angeklickte Knoten bereits gefunden wurde
//						j = x_positionen.get(i).size();
//						i = x_positionen.size();
//					}
//				}
//			}
//		}
//	}

	@FXML
	public void berechnen() {
		algorithmus.resetFlow();
		algorithmus.berechneMaxFlow();

        // setStyle für Kanten
        for (int j = 0; j < kanten.size(); j++) {
            if (0< kanten.get(j).getAuslastung()) {
            	// falls die Kante benutzt wurde, nutze die Klasse "maxFluss"
                graphView.getStylableEdge(kanten.get(j)).setStyleClass("maxFluss");
            } else {
            	// falls die Kante nicht benutzt wurde, nutze die Klasse "edge"
            	graphView.getStylableEdge(kanten.get(j)).setStyleClass("edge");
            }
        }

        // ändern der Textfarbe des Labels info
        info.setTextFill(Color.BLUE);

        info.setText("Maximaler Fluss: "+algorithmus.getMaxFlow());
    }

    public TextField getAnzahl_zeilen() {
        return anzahl_zeilen;
    }

    public void setAnzahl_zeilen(TextField anzahl_zeilen) {
        this.anzahl_zeilen = anzahl_zeilen;
    }

    public TextField getAnzahl_spalten() {
        return anzahl_spalten;
    }

    public void setAnzahl_spalten(TextField anzahl_spalten) {
        this.anzahl_spalten = anzahl_spalten;
    }

    public Label getInfo_kanten() {
        return info_kanten;
    }

    public void setInfo_kanten(Label info_kanten) {
        this.info_kanten = info_kanten;
    }

    public TextField getAnzahl_kanten() {
        return anzahl_kanten;
    }
}
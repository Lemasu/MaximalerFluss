package ch.kbw.maximalerfluss;

import java.util.ArrayList;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Das ist der Controller der Applikation.
 * <p>
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
	 * <p>
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

	/**
	 * Das ist der HBox, in dem der Graph dargestellt wird.
	 */
	@FXML
	private HBox hbox;

	/**
	 * Das ist der Textfeld, in welchem der Nutzer angeben kann, welcher Knoten als
	 * Startknoten gesetzt werden soll.
	 */
	@FXML
	private TextField startknoten_setzen;

	/**
	 * Das ist der Textfeld, in welchem der Nutzer angeben kann, welcher Knoten als
	 * Zielknoten gesetzt werden soll.
	 */
	@FXML
	private TextField zielknoten_setzen;

	/**
	 * Das ist der Textfeld fuer die Anzahl der Zeilen im Graphen, welcher generiert
	 * werden soll.
	 */
	@FXML
	private TextField anzahl_zeilen;

	/**
	 * Das ist der Textfeld fuer die Anzahl der Spalten im Graphen, welcher
	 * generiert werden soll.
	 */
	@FXML
	private TextField anzahl_spalten;

	/**
	 * Dieses Label dient dazu, dem Nutzer Informationen zu uebermitteln.
	 */
	@FXML
	private Label info;

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
	}

	/**
	 * Mit dieser Methode wird ein neuer Graph generiert und ausgegeben.
	 */
	@FXML
	public void generate() {
		// initialisiere die Zeilen und Spalten des Graphen
		int zeilen = 0;
		int spalten = 0;

		/*
		 * ueberpruefe, ob nur Zahlen eingegeben wurde und setzte die Zeilen und Spalten
		 * auf die Inhalte der Textfelder
		 */
		try {
			zeilen = Integer.parseInt(anzahl_zeilen.getText());
			spalten = Integer.parseInt(anzahl_spalten.getText());
		} catch (NumberFormatException e) {
			/*
			 * falls nicht nur Zahlen eingegeben wurde, gebe eine entsprechende Meldung aus
			 */
			info.setText("Bitte geben Sie nur Zahlen ein.");

			/*
			 * breche diese Methode ab, da nicht nur Zahlen fuer die Generierung uebergeben
			 * wurde
			 */
			return;
		}

		/*
		 * ueberpruefe, ob einer der folgenden Bedingungen erfuellt sind: - Zeilen > 8 -
		 * Spalten > 8 - Zeilen = 1 und spalten = 1 - Zeilen < 1 - Spalten < 1
		 *
		 * Falls einer der obigen Bedingungen erfuellt sind, dann wird eine Meldung
		 * ausgegeben und diese Methode abgebrochen. Die Methode wird in diesem Fall
		 * abgebrochen, da die Matrix dann entweder zu gross wird oder gar keinen Sinn
		 * machen wird.
		 */
		if (zeilen > 8 || spalten > 8 || (zeilen == 1 && spalten == 1) || zeilen < 1 || spalten < 1) {
			/*
			 * falls eine der obigen Bedingungen zutrifft, wird dem Benutzer diese Meldung
			 * gezeigt.
			 */
			info.setText(
					"Bitte achten Sie darauf, dass die Anzahl Zeilen und Spalten nicht grösser als 8 ist, dass entweder die Anzahl Zeilen oder die Anzahl Spalten grösser als 1 ist und, dass die Anzahl Zeilen und Spalten nicht kleiner als 1 ist.");

			/*
			 * breche die Methode ab, da keine sinnvolle Matrix mit den gegebenen Zahlen
			 * generiert werden kann
			 */
			return;
		}

		// generiert den Graphen
		model.getGraph().graphGenerieren(zeilen, spalten);

		// Graph zeichnen
		graphZeichnen();

		// -----------------------------------------------------------------------------------------------------
		// Dieser Abschnitt dient aktuell nur zum Testen.
		// -----------------------------------------------------------------------------------------------------

		// Graph zeichnen
//		graphMitMaximalerFlussZeichnen();

		// -----------------------------------------------------------------------------------------------------
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
		int[] position = new int[2];
		position[0] = Integer.parseInt(position_als_text[0]);
		position[1] = Integer.parseInt(position_als_text[1]);

		// hole die Knoten vom Model
		Knoten[][] knoten = model.getGraph().getKnoten();

		/*
		 * Zuerst wird ueberprueft, ob bereits ein alter Startknoten abgespeichert
		 * wurde.
		 * 
		 * Falls ein alter Startknoten abgespeichert wurde, wird dieser in einen
		 * normalen Knoten umgewandelt.
		 */
		if (id_startknoten != null) {
			knoten[id_startknoten[0] - 1][id_startknoten[1] - 1].setKategorie(1);
		}

		// wandle den neuen Startknoten in einen Startknoten um
		knoten[position[0] - 1][position[1] - 1].setKategorie(0);

		// setzte die ID des neuen Startknotens als ID vom neuen alten Startknoten
		id_startknoten = position;

		// zeichne den Graphen neu auf
		graphZeichnen();
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
		int[] position = new int[2];
		position[0] = Integer.parseInt(position_als_text[0]);
		position[1] = Integer.parseInt(position_als_text[1]);

		// hole die Knoten vom Model
		Knoten[][] knoten = model.getGraph().getKnoten();

		/*
		 * Zuerst wird ueberprueft, ob bereits ein alter Zielknoten abgespeichert wurde.
		 * 
		 * Falls ein alter Zielknoten abgespeichert wurde, wird dieser in einen normalen
		 * Knoten umgewandelt.
		 */
		if (id_zielknoten != null) {
			knoten[id_zielknoten[0] - 1][id_zielknoten[1] - 1].setKategorie(1);
		}

		// wandle den neuen Zielknoten in einen Zielknoten um
		knoten[position[0] - 1][position[1] - 1].setKategorie(2);

		// setzte die ID des neuen Zielknotens als ID vom neuen alten Zielknotens
		id_zielknoten = position;

		// zeichne den Graphen neu auf
		graphZeichnen();
	}

	/**
	 * Diese Methode gibt den Graphen mithilfe von JavaFXSmartGraph in einem
	 * SubScene aus.
	 */
	private void graphZeichnen() {
		// Graph erstellen
		Digraph<Knoten, String> graph = new DigraphEdgeList<>();

		// alle Knoten holen
		Knoten[][] knoten = model.getGraph().getKnoten();

		// alle Kanten holen
		ArrayList<Kante> kanten = model.getGraph().getKanten();

		/*
		 * erstelle eine ArrayList, um die Knoten, welche mit Hilfe von JavaFXSmartGraph
		 * ausgegeben werden, abzuspeichern.
		 */
		ArrayList<Vertex<Knoten>> nodes = new ArrayList<Vertex<Knoten>>();

		/*
		 * erstelle eine ArrayList, um die Knoten, welche mit Hilfe von JavaFXSmartGraph
		 * ausgegeben werden, abzuspeichern.
		 */
		ArrayList<Edge<String, Knoten>> nodess = new ArrayList<Edge<String, Knoten>>();

		// erstelle Variablen, um die Knoten im Matrixform zu platzieren.
		// ArrayList fuer die Position der Knoten
		ArrayList<Double> x_positionen = new ArrayList<Double>();
		ArrayList<Double> y_positionen = new ArrayList<Double>();
		// Veraenderung von X und Y in Relation zur Anzahl der Knoten
		double veraenderung_x = 400 / knoten[0].length;
		double veraenderung_y = 400 / knoten[0].length;
		// Position von einem Knoten
		double x = 100;
		double y = 100;

		// fuege jeden Knoten einzeln hinzu
		for (int i = 0; i < knoten.length; i++) {
			for (int j = 0; j < knoten[i].length; j++) {
				/*
				 * erstelle den Knoten fuer die Ausgabe auf dem Graphen, der eigentliche Knoten
				 * wurde aber bereits erstellt
				 */
				nodes.add(graph.insertVertex(knoten[i][j]));

				// speichere die Koordinate des Knotens ab
				x_positionen.add(x);
				y_positionen.add(y);

				// passt die X-Koordinate dem naechsten Knoten an
				x += veraenderung_x;
			}
			// setze die X-Koordinate wieder auf 100 fuer den ersten Knoten einer Zeile
			x = 100;

			// passt die X-Koordinate der naechsten Zeile an
			y += veraenderung_y;
		}

		// fuege jede Kante einzeln hinzu
		for (int i = 0; i < kanten.size(); i++) {
			/*
			 * erstelle den Knoten fuer die Ausgabe auf dem Graphen, der eigentlicher Knoten
			 * wurde aber bereits erstellt
			 */
			System.out.println(kanten.get(i).getKnoten_1() + "" + kanten.get(i).getKnoten_2() + "");
			nodess.add(graph.insertEdge(kanten.get(i).getKnoten_1(), kanten.get(i).getKnoten_2(),
					kanten.get(i).getKapazitaet() + ""));
			// nodess.add(graph.insertEdge(kanten.get(i).getKnoten_1(),
			// kanten.get(i).getKnoten_2(), kanten.get(i).getKapazitaet() + ""));
		}

		// lade die Properties-Datei
		SmartGraphProperties properties = new SmartGraphProperties();

		// erstelle die Darstellung aus dem Graphen und der Properties-Datei
		SmartGraphPanel<Knoten, String> graphView = new SmartGraphPanel<>(graph, properties,
				new SmartCircularSortedPlacementStrategy());

		// gebe den Graphen auf einer SubScene aus
		SubScene subscene = new SubScene(new SmartGraphDemoContainer(graphView), 700, 600);

		/*
		 * ueberpruefe, ob schon einen Graphen auf dem GUI gibt
		 * 
		 * loesche diesen Graphen, wenn es diesen schon gibt
		 */
		if (hbox.getChildren().size() > 0) {
			hbox.getChildren().remove(0);
		}

		// gebe den neu erstellten Graphen auf dem GUI aus
		hbox.getChildren().add(subscene);

		// platziere alle Knoten an die vorhin berechneten Koordinaten
		for (int i = 0; i < nodes.size(); i++) {
			// hole den aktuellen Knoten
			Vertex<Knoten> node = nodes.get(i);

			// platziere den aktuellen Knoten
			graphView.setVertexPosition(node, x_positionen.get(i), y_positionen.get(i));

			/*
			 * ueberpruefe die Kategorie des aktuellen Knotens
			 * 
			 * falls der aktueller Knoten die Kategorie Startknoten, also 0, besitzt, wird
			 * der aktueller Knoten gruen gefaerbt
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
			}
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
}
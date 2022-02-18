package ch.kbw.maximalerfluss;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Das ist der Controller der Applikation.
 * 
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
	private ArrayList<ArrayList<Knoten>> knoten;

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
	 * Das ist die Zoomgroesse vom Canvas.
	 */
	private int zoom;

	/**
	 * Das ist die X-Position des ganzen Graphen.
	 */
	private int graph_x_position;
	
	/**
	 * Das ist die Y-Position des ganzen Graphen.
	 */
	private int graph_y_position;

	/**
	 * Das ist der Canvas, auf dem der Graph dargestellt werden soll.
	 */
	@FXML
	private Canvas canvas;

	/**
	 * Das ist der GraphicsContext fuer den Canvas. Dieser wird verwendet, um auf
	 * dem Canvas zeichnen zu koennen.
	 */
	private GraphicsContext gc;

	/**
	 * Das ist der Textfeld fuer die Anzahl der waagerechten Ebenen im Graphen,
	 * welcher generiert werden soll.
	 */
	@FXML
	private TextField ebenen_waagerecht;

	/**
	 * Das ist der Textfeld fuer die Anzahl der senkrechten Ebenen im Graphen,
	 * welcher generiert werden soll.
	 */
	@FXML
	private TextField ebenen_senkrecht;

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
		// convert canvas from fxml file to GraphicsContext
		gc = canvas.getGraphicsContext2D();

		// lege die Kreisgroesse fest
		kreisgroesse = 20;

		// Da die zwei Methoden "graphZeichnen()" und "graphMitMaximalerFlussZeichnen()"
		// noch nicht aufgerufen wurden, werden die zwei Booleans auf false gesetzt.
		graph_zeichnen_aufgerufen = false;
		graph_mit_maximaler_fluss_zeichnen_aufgerufen = false;

		// Der Zoom wird festgelegt.
		zoom = 150;
		
		// Die X-Position des Graphen wird festgelegt.
		graph_x_position = 5;
		
		// Die Y-Position des Graphen wird festgelegt.
		graph_y_position = 0;
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
		try {
			// generiert den Graphen
			model.getGraph().graphGenerieren(Integer.parseInt(ebenen_waagerecht.getText()),
					Integer.parseInt(ebenen_senkrecht.getText()));
		} catch (NumberFormatException e) {
			info.setText("Bitte geben Sie nur Zahlen ein.");
			return;
		}

		// Graph zeichnen
		graphZeichnen();

		// -----------------------------------------------------------------------------------------------------
		// Dieser Abschnitt dient aktuell nur zum Testen.
		// -----------------------------------------------------------------------------------------------------

		// Graph zeichnen
		graphMitMaximalerFlussZeichnen();

		// -----------------------------------------------------------------------------------------------------
	}

	/**
	 * Mit dieser Methode kann der Nutzer den Graphen auf dem Canvas vergroessern.
	 */
	@FXML
	public void zoomIn() {
		zoom(10);
	}

	/**
	 * Mit dieser Methode kann der Nutzer den Graphen auf dem Canvas verkleinern.
	 */
	@FXML
	public void zoomOut() {
		zoom(-10);
	}

	/**
	 * Mit dieser Methode kann der Graph nach links verschoben werden.
	 */
	@FXML
	public void graphNachLinksVerschieben() {
		// verschiebe den Graphen nach links
		graphVerschieben(-10, 0);
	}
	
	/**
	 * Mit dieser Methode kann der Graph nach oben verschoben werden.
	 */
	@FXML
	public void graphNachObenVerschieben() {
		// verschiebe den Graphen nach oben
		graphVerschieben(0, -10);
	}
	
	/**
	 * Mit dieser Methode kann der Graph nach unten verschoben werden.
	 */
	@FXML
	public void graphNachUntenVerschieben() {
		// verschiebe den Graphen nach unten
		graphVerschieben(0, 10);
	}
	
	/**
	 * Mit dieser Methode kann der Graph nach rechts verschoben werden.
	 */
	@FXML
	public void graphNachRechtsVerschieben() {
		// verschiebe den Graphen nach rechts
		graphVerschieben(10, 0);
	}
	
	/**
	 * Diese Methode zeichnet den Graphen auf den Canvas.
	 * 
	 * @param graph_neu_zeichnen
	 */
	private void graphZeichnen() {
		// Clears canvas
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	
		// Erstelle die benoetigten ArrayLists.
		// Das sind die Knoten des Graphen.
		knoten = model.getGraph().getKnoten();
		// Das sind die Positionen der einzelnen Knoten in X-Richtung des Graphen.
		x_positionen = new ArrayList<ArrayList<Integer>>();
		// Das sind die Positionen der einzelnen Knoten in Y-Richtung des Graphen.
		y_positionen = new ArrayList<ArrayList<Integer>>();
		// Das sind die Kanten des Graphen.
		kanten = model.getGraph().getKanten();
	
		// zeichne die Knoten
		knotenzeichnen(knoten, kreisgroesse);
	
		// zeichne die Kanten
		kantenZeichnen(kanten, Color.BLACK);
	
		// zeichne den Startknoten und den Endknoten
		startknotenZielknotenZeichnen(kreisgroesse);
	
		// ueberpruefe, ob der Boolean "graph_zeichnen_aufgerufen" schon auf true
		// gesetzt wurde, wenn nicht, setzte auf true
		if (!graph_zeichnen_aufgerufen) {
			graph_zeichnen_aufgerufen = true;
		}
	}

	/**
	 * Diese Methode zeichnet den Graphen mit dem maximalen Fluss auf.
	 * 
	 * Diese Methode ist dafuer gedacht, den Graphen erneut aufzuzeichen, damit die
	 * Kanten des maximalen Flusses nicht ueber den Knoten liegen.
	 */
	private void graphMitMaximalerFlussZeichnen() {
		// Clears canvas
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		/*
		 * Zuerst muss ueberprueft werden, ob die benoetigten ArrayLists bereits
		 * erstellt wurden, dass heisst, ob die Methode "graphZeichnen()" bereits
		 * aufgerufen wurde.
		 * 
		 * Sollte die Methode "graphZeichnen()" noch nicht aufgerufen worden sein, wird
		 * diese Methode abgebrochen.
		 */
		if (!graph_zeichnen_aufgerufen) {
			System.err.println("Die Methode \"graphZeichnen()\" muss vor dieser Methode aufgerufen werden.");
			return;
		}

		// erstelle eine ArrayList mit den Kanten des maximalen Flusses
		ArrayList<Kante> kanten_maximaler_fluss = model.getGraph().getKanten_maximaler_fluss();

		/*
		 * Es muss dann ueberprueft werden, ob es einen maximalen Fluss gibt.
		 * 
		 * Wenn es keinen maximalen Fluss gibt, wird diese Methode abgebrochen.
		 * 
		 * Zuerst wird ueberprueft, ob die ArrayList null ist, damit die Ueberpruefung
		 * abgebrochen wird und es nicht zu einer NullPointerException kommt.
		 */
		if (kanten_maximaler_fluss == null || kanten_maximaler_fluss.size() == 0) {
			System.err.println("Es gibt keinen maximalen Fluss.");
			return;
		}

		// zeichne alle Kanten zuerst
		kantenZeichnen(kanten, Color.BLACK);

		// zeichne anschliessend die Kanten des maximalen Flusses
		kantenZeichnen(kanten_maximaler_fluss, Color.BLUE);

		// Das sind die Positionen der einzelnen Knoten in X-Richtung des Graphen.
		x_positionen = new ArrayList<ArrayList<Integer>>();
		// Das sind die Positionen der einzelnen Knoten in Y-Richtung des Graphen.
		y_positionen = new ArrayList<ArrayList<Integer>>();
		
		/*
		 * zeichne dann die Knoten, damit diese ueber den Kanten liegt
		 * 
		 * Beim normalen Graphen macht es nichts die Knoten zuerst zu zeichnen, da alle
		 * Kanten schwarz sind.
		 */
		knotenzeichnen(knoten, kreisgroesse);

		// zeichne zum Schluss den Startknoten und den Endknoten ueber deren normalen
		// Knoten
		startknotenZielknotenZeichnen(kreisgroesse);

		// ueberpruefe, ob der Boolean "graph_mit_maximaler_fluss_zeichnen_aufgerufen"
		// schon auf true gesetzt wurde, wenn nicht, setzte auf true
		if (!graph_mit_maximaler_fluss_zeichnen_aufgerufen) {
			graph_mit_maximaler_fluss_zeichnen_aufgerufen = true;
		}
	}
	
	/**
	 * Diese Methode zeichnet alle Knoten des Graphen.
	 * 
	 * @param knoten             Das sind alle Knoten des Graphen.
	 * @param kreisgroesse       Das ist die Groesse der Kreise fuer die Knoten des
	 *                           Graphen.
	 */
	private void knotenzeichnen(ArrayList<ArrayList<Knoten>> knoten, int kreisgroesse) {
		// setzt die Farbe fuer Fill auf Schwarz
		gc.setFill(Color.BLACK);

		// Diese Schleife zeichnet die Knoten.
		for (int i = 0; i < knoten.size(); i++) {
			// initialisiere die benoetigten Variablen

			// Das ist die X-Position eines Knotens.
			int x_position = graph_x_position + (zoom * i);
			// Das ist die Y-Position eines Knotens.
			int y_position = 400;
						
			/*
			 * Das die Groesse eines Zaehlschrittes fuer die Y-Position.
			 * 
			 * Die Groesse eines Schrittes haengt von der Laenge einer waagerechte Ebene ab.
			 */
			int y_position_zaehlschritte = y_position / (knoten.get(i).size() + 1);
			
			/*
			 * Passe die Y-Position der Position des Graphen an.
			 * 
			 * Die Anpassung muss ausserhalb der for-Schleife erfolgen, damit die Anpassung nicht permanent erfolgt.
			 * 
			 * Die Anpassung muss aber nach der Berechnung von der Groesse der Zaehlschritte erfolgen, damit die Groesse der Zaehlschritte nicht von der Verschiebung des Graphen beeintraechtigt wird.
			 */
			y_position += graph_y_position;

			// erstelle neue ArrayLists fuer die aktuelle waagerechte Ebene
			x_positionen.add(new ArrayList<Integer>());
			y_positionen.add(new ArrayList<Integer>());

			// Schleife, um die einzelnen Knoten einer waagerechte Ebene zu zeichnen
			for (int j = 0; j < knoten.get(i).size(); j++) {
				/*
				 * Ziehe einen Zaehlschritt von der Y-Position ab, damit die Y-Position die
				 * richtige Groesse hat.
				 */
				y_position -= y_position_zaehlschritte;
				
				// zeichne den Knoten
				gc.fillOval(x_position, y_position, kreisgroesse, kreisgroesse);
				
				// speichere die Position des Knotens ab
				x_positionen.get(x_positionen.size() - 1).add(x_position);
				y_positionen.get(y_positionen.size() - 1).add(y_position);
			}
		}
	}

	/**
	 * Diese Methode zeichnet alle Kanten eines ArrayLists, welche ihm uebergeben
	 * wird, im Graphen.
	 * 
	 * @param kanten Das sind die Kanten, welche gezeichnet werden sollen.
	 * @param farbe  Das ist die Farbe, welche die Kanten besitzen sollen.
	 */
	private void kantenZeichnen(ArrayList<Kante> kanten, Paint farbe) {
		// setzt die Farbe vom Stroke auf die gewuenschte Farbe
		gc.setStroke(farbe);

		// Schleife, um die einzelnen Kanten zu zeichnen
		for (Kante kante : kanten) {
			/*
			 * Speichere die ID der aktuellen Knoten ab, um spaeter wieder darauf
			 * zuzugreifen zu koennen.
			 * 
			 * Die IDs wurden als Strings abgespeichert. Die waagerechten Ebenen und die
			 * senkrechten Ebenen wurden mit einem Punkt getrennt.
			 * 
			 * Um die ID fuer die spaetere Verwendung optimal abzuspeichern, wurden die
			 * beiden Teile der ID aufgeteilt und der Punkt entfernt.
			 */
			String[] knoten_1 = kante.getKnoten_1().getId().split("\\.");
			String[] knoten_2 = kante.getKnoten_2().getId().split("\\.");

			/*
			 * Es muss jeweils 10 zu den Positionen aus den ArrayList hinzuaddiert werden,
			 * damit die Linien in den Knoten starten und enden. Sonst wuerden die Linien
			 * knapp ausserhalb der Knoten starten und enden.
			 * 
			 * Die Zahlen aus den beiden Arrays "knoten_1" und "knoten_2" muessen um eins
			 * verkleinert werden, da die Zaehlung der IDs nicht bei null beginnt.
			 */
			int x_position_1 = x_positionen.get(Integer.parseInt(knoten_1[0]) - 1)
					.get(Integer.parseInt(knoten_1[1]) - 1) + 10;
			int y_position_1 = y_positionen.get(Integer.parseInt(knoten_1[0]) - 1)
					.get(Integer.parseInt(knoten_1[1]) - 1) + 10;
			int x_position_2 = x_positionen.get(Integer.parseInt(knoten_2[0]) - 1)
					.get(Integer.parseInt(knoten_2[1]) - 1) + 10;
			int y_position_2 = y_positionen.get(Integer.parseInt(knoten_2[0]) - 1)
					.get(Integer.parseInt(knoten_2[1]) - 1) + 10;

			// zeichne die Kante
			gc.strokeLine(x_position_1, y_position_1, x_position_2, y_position_2);

			// gebe die Informationen der Kante auf dem Canvas aus
			informationenZurKanteAusgeben(x_position_1, y_position_1, x_position_2, y_position_2, kante);
		}
	}

	/**
	 * Diese Methode zeichnet den Startknoten und den Zielknoten des Graphen.
	 * 
	 * @param kreisgroesse Das ist die Groesse der Kreise fuer die Knoten des
	 *                     Graphen.
	 */
	private void startknotenZielknotenZeichnen(int kreisgroesse) {
		// setzte die Farbe fuer die Stroke auf Schwarz, damit die Schrift schwarz ist
		gc.setStroke(Color.BLACK);

		/*
		 * ueberschreibe den Kreis fuer den Start mit einem gruenen Kreis, in dessen
		 * Mitte ein grosses S steht
		 */
		gc.setFill(Color.LIGHTGREEN);
		gc.fillOval(x_positionen.get(0).get(0), y_positionen.get(0).get(0), kreisgroesse, kreisgroesse);
		gc.strokeText("S", x_positionen.get(0).get(0) + 7, y_positionen.get(0).get(0) + 14);

		/*
		 * ueberschreibe den Kreis fuer den Start mit einem gruenen Kreis, in dessen
		 * Mitte ein grosses S steht
		 */
		gc.setFill(Color.RED);
		gc.fillOval(x_positionen.get(x_positionen.size() - 1).get(0), y_positionen.get(y_positionen.size() - 1).get(0),
				kreisgroesse, kreisgroesse);
		gc.strokeText("Z", x_positionen.get(x_positionen.size() - 1).get(0) + 7,
				y_positionen.get(y_positionen.size() - 1).get(0) + 14);
	}

	/**
	 * Diese Methode gibt die Informationen zu einer Kante aus.
	 * 
	 * Diese Informationen sind: die Kapazität einer Kante und die Auslastung einer
	 * Kante.
	 * 
	 * @param x_position_1 Das ist die X-Position des ersten Knotens.
	 * @param y_position_1 Das ist die Y-Position des ersten Knotens.
	 * @param x_position_2 Das ist die X-Position des zweiten Knotens.
	 * @param y_position_2 Das ist die Y-Position des zweiten Knotens.
	 * @param kante        Das ist die Kante, welche mit deren Informationen
	 *                     beschriftet werden soll.
	 */
	private void informationenZurKanteAusgeben(int x_position_1, int y_position_1, int x_position_2, int y_position_2,
			Kante kante) {
		// Zuerst wird die Farbe fuer den Stroke, falls diese noch nicht auf Schwarz
		// gesetzt wurde, auf Schwarz gesetzt, damit die Schrift nicht auf einmal in
		// einer anderen Farbe erscheint.
		if (gc.getStroke() != Color.BLACK) {
			gc.setStroke(Color.BLACK);
		}

		// Anschliessend werden die benoetigten Variablen initialisiert.
		/*
		 * Das ist die X-Position fuer die Informationen.
		 * 
		 * Es wird darauf geachtet, dass diese ungefaehr in der Mitte der Kante
		 * platziert ist.
		 */
		int x_position_informationen = x_position_1 + ((x_position_2 - x_position_1) / 2) - 16;
		/*
		 * Das ist die Y-Position fuer die Informationen.
		 * 
		 * Es wird darauf geachtet, dass diese ungefaehr in der Mitte der Kante
		 * platziert ist.
		 */
		int y_position_informationen = y_position_1 + ((y_position_2 - y_position_1) / 2) - 10;
		// Das ist der String mit den Informationen.
		String informationen = kante.getAuslastung() + " | " + kante.getKapazitaet();

		// Abschliessend werden die Informationen der Kante auf den Canvas geschrieben.
		gc.strokeText(informationen, x_position_informationen, y_position_informationen);
	}

	/**
	 * Mit diese Methode kann im Canvas gezoomt werden.
	 * 
	 * @param zoomstaerke Eine positive Zoomstaerke bedeutet, dass hineingezoomt
	 *                    wird und eine negative, dass hinausgezoomt wird.
	 */
	private void zoom(int zoomstaerke) {
		/*
		 * Zuerst wird ueberprueft, ob die Zoomstraeke auf einen Wert kleiner als 100 verkleinert werden soll.
		 * 
		 * Die Zoomstaerke darf nicht auf einen Wert kleiner als 100 verkleinert werden.
		 * 
		 * Wenn die Zoomstaerke auf einen Wert kleiner als 100 verkleinert werden soll, wird diese Methode abgebrochen.
		 */
		if ((zoom + zoomstaerke) <= 100) {
			System.err.println("Die Zoomstraeke kann nicht kleiner als 100 sein.");
			return;
		}
		
		// setzte die Zoomgroesse auf die aktuelle Zoomgroesse
		zoom += zoomstaerke;

		// zeichne den Graphen neu auf
		graphNeuZeichnen();
	}
	
	/**
	 * Mit dieser Methode kann der Graph verschoben werden.
	 * 
	 * @param veraenderung_x_position Das ist die Veraenderung der X-Position des Graphen.
	 * @param veraenderung_y_position Das ist die Veraenderung der Y-Position des Graphen.
	 */
	private void graphVerschieben(int veraenderung_x_position, int veraenderung_y_position) {
		// passe die Position des Graphen an
		graph_x_position += veraenderung_x_position;
		graph_y_position += veraenderung_y_position;
		
		// zeichne den Graphen neu auf
		graphNeuZeichnen();
	}
	
	/**
	 * Mit dieser Methode kann der Graph neu aufgezeichnet werden.
	 * 
	 * Diese Methode achtet darauf, ob der maximaler Fluss bereits einmal aufgezeichnet wurde und zeichnet den maximalen Fluss in dem Fall erneut auf.
	 */
	private void graphNeuZeichnen() {
		// zeichne zuerst den Graphen ganz normal auf
		graphZeichnen();

		// Sollte die Methode "graphMitMaximalerFlussZeichnen()" bereits aufgerufen
		// worden sein, zeichne auch den maximalen Fluss auf.
		if (graph_mit_maximaler_fluss_zeichnen_aufgerufen) {
			graphMitMaximalerFlussZeichnen();
		}
	}
}
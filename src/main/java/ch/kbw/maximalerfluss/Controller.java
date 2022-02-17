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
		
		// setzte Kreisgroesse auf 20
		kreisgroesse = 20;
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
		// Clears canvas
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

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

		// Clears canvas
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		// Graph zeichnen
		graphMitMaximalerFlussZeichnen();

		// -----------------------------------------------------------------------------------------------------
	}

	/**
	 * Diese Methode zeichnet den Graphen auf den Canvas.
	 */
	private void graphZeichnen() {
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
	}
	
	/**
	 * Diese Methode zeichnet den Graphen mit dem maximalen Fluss auf.
	 * 
	 * Diese Methode ist dafuer gedacht, den Graphen erneut aufzuzeichen, damit die Kanten des maximalen Flusses nicht ueber den Knoten liegen.
	 */
	private void graphMitMaximalerFlussZeichnen() {
		/*
		 * Zuerst muss ueberprueft werden, ob die benoetigten ArrayLists bereits erstellt wurden, dass heisst, ob die Methode "graphZeichnen()" bereits aufgerufen wurde.
		 * 
		 * Wenn einer der folgenden ArrayLists ist null ist, stimmt irgendetwas nicht: knoten, x_positionen, y_positionen und kanten.
		 * 
		 * Die Methode wird anschliessend abgebrochen.
		 */
		if (knoten == null || x_positionen == null || y_positionen == null || kanten == null) {
			System.err.println("----------");
			System.err.println("Die Methode \"graphZeichnen()\" muss vor dieser Methode aufgerufen werden.");
			System.err.println("Mindestens einer der folgenden ArrayLists ist null: knoten, x_positionen, y_positionen und kanten.");
			System.err.println("----------");
			return;
		}
				
		// erstelle eine ArrayList mit den Kanten des maximalen Flusses
		ArrayList<Kante> kanten_maximaler_fluss = model.getGraph().getKanten_maximaler_fluss();
		
		/*
		 * Es muss dann ueberprueft werden, ob es einen maximalen Fluss gibt.
		 * 
		 * Wenn es keinen maximalen Fluss gibt, wird diese Methode abgebrochen.
		 * 
		 * Zuerst wird ueberprueft, ob die ArrayList null ist, damit die Ueberpruefung abgebrochen wird und es nicht zu einer NullPointerException kommt.
		 */
		if (kanten_maximaler_fluss == null || kanten_maximaler_fluss.size() == 0) {
			System.err.println("Es gibt keinen maximalen Fluss.");
			return;
		}
		
		// zeichne alle Kanten zuerst
		kantenZeichnen(kanten, Color.BLACK);
		
		// zeichne anschliessend die Kanten des maximalen Flusses
		kantenZeichnen(kanten_maximaler_fluss, Color.BLUE);
		
		/*
		 * zeichne dann die Knoten, damit diese ueber den Kanten liegt
		 * 
		 * Beim normalen Graphen macht es nichts die Knoten zuerst zu zeichnen, da alle Kanten schwarz sind.
		 */
		knotenzeichnen(knoten, kreisgroesse);
		
		// zeichne zum Schluss den Startknoten und den Endknoten ueber deren normalen Knoten
		startknotenZielknotenZeichnen(kreisgroesse);
	}

	/**
	 * Diese Methode zeichnet alle Knoten des Graphen.
	 * 
	 * @param knoten       Das sind alle Knoten des Graphen.
	 * @param kreisgroesse Das ist die Groesse der Kreise fuer die Knoten des
	 *                     Graphen.
	 */
	private void knotenzeichnen(ArrayList<ArrayList<Knoten>> knoten, int kreisgroesse) {
		// setzt die Farbe fuer Fill auf Schwarz
		gc.setFill(Color.BLACK);

		// Diese Schleife zeichnet die Knoten.
		for (int i = 0; i < knoten.size(); i++) {
			// initialisiere die benoetigten Variablen

			/*
			 * Das ist die X-Position eines Knotens.
			 * 
			 * Das Minimum fuer die X-Position ist fuenf. Danach wird immer jeweils 50 zu
			 * der letzten X-Position hinzuaddiert.
			 */
			int x_position = 5 + (50 * i);
			/*
			 * Das ist die Y-Position eines Knotens.
			 * 
			 * Es startet bei 400.
			 */
			int y_position = 400;
			/*
			 * Das die Groesse eines Zaehlschrittes fuer die Y-Position.
			 * 
			 * Die Groesse eines Schrittes haengt von der Laenge einer waagerechte Ebene ab.
			 */
			int y_position_zaehlschritte = y_position / (knoten.get(i).size() + 1);

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
		 * ueberschreibe den Kreis fuer den Start mit einem gruenen Kreis, in
		 * dessen Mitte ein grosses S steht
		 */
		gc.setFill(Color.LIGHTGREEN);
		gc.fillOval(x_positionen.get(0).get(0), y_positionen.get(0).get(0), kreisgroesse, kreisgroesse);
		gc.strokeText("S", x_positionen.get(0).get(0) + 7, y_positionen.get(0).get(0) + 14);

		/*
		 * ueberschreibe den Kreis fuer den Start mit einem gruenen Kreis, in
		 * dessen Mitte ein grosses S steht
		 */
		gc.setFill(Color.RED);
		gc.fillOval(x_positionen.get(x_positionen.size() - 1).get(0), y_positionen.get(y_positionen.size() - 1).get(0),
				kreisgroesse, kreisgroesse);
		gc.strokeText("Z", x_positionen.get(x_positionen.size() - 1).get(0) + 7,
				y_positionen.get(y_positionen.size() - 1).get(0) + 14);
	}
}
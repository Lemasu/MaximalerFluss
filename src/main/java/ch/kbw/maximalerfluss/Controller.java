package ch.kbw.maximalerfluss;

import java.util.ArrayList;

import ch.kbw.maximalerfluss.algorithmus.Algorithmus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
	 * Das ist der Algorithmus in diesem Controller
	 */
	private Algorithmus algorithmus;

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
	 * 
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
	 * Diese Variable sagt, welcher Knoten gesetzt werden soll.
	 * 
	 * 0 = Start
	 * 1 = normaler Knoten
	 * 2 = Ziel
	 */
	private int knoten_setzen;

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
	 * Das ist der Knopf, um den Startknoten zu setzen.
	 */
	@FXML
	private Button startknoten_setzen;
	
	/**
	 * Das ist der Knopf, um den Zielknoten zu setzen.
	 */
	@FXML
	private Button zielknoten_setzen;

	/**
	 * Das ist der Textfeld fuer die Anzahl der Zeilen im Graphen, welcher generiert werden soll.
	 */
	@FXML
	private TextField anzahl_zeilen;

	/**
	 * Das ist der Textfeld fuer die Anzahl der Spalten im Graphen, welcher generiert werden soll.
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
		// convert canvas from fxml file to GraphicsContext
		gc = canvas.getGraphicsContext2D();

		// lege die Kreisgroesse fest
		kreisgroesse = 20;

		// Da die zwei Methoden "graphZeichnen()" und "graphMitMaximalerFlussZeichnen()"
		// noch nicht aufgerufen wurden, werden die zwei Booleans auf false gesetzt.
		graph_zeichnen_aufgerufen = false;
		graph_mit_maximaler_fluss_zeichnen_aufgerufen = false;
		
		/*
		 * "knoten_setzen" wird am Anfang auf 1 gesetzt.
		 * 
		 * Der Grund dafuer ist, dass am Anfang weder der Knopf fuer die Setzung der Startknoten, noch der Knopf fuer die Setzung der Zielknoten ausgewaehlt sind.
		 */
		knoten_setzen = 1;
		
		// setzte die Methode, damit der Nutzer auf dem Canvas die Knoten anklicken kann
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent event) {
		    	knotenAnklicken(event);
		    }
		});
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
	 * Mit dieser Methode wird ein neuer Graph generiert und ausgegeben.
	 */
	@FXML
	public void generate() {
		// initialisiere die Zeilen und Spalten des Graphen
		int zeilen = 0;
		int spalten = 0;
		
		// ueberpruefe, ob nur Zahlen eingegeben wurde und setzte die Zeilen und Spalten auf die Inhalte der Textfelder
		try {
			zeilen = Integer.parseInt(anzahl_zeilen.getText());
			spalten = Integer.parseInt(anzahl_spalten.getText());
		} catch (NumberFormatException e) {
			// falls nicht nur Zahlen eingegeben wurde, gebe eine entsprechende Meldung aus
			info.setText("Bitte geben Sie nur Zahlen ein.");
			
			// breche diese Methode ab, da nicht nur Zahlen fuer die Generierung uebergeben wurde
			return;
		}
		
		/*
		 * ueberpruefe, ob einer der folgenden Bedingungen erfuellt sind:
		 * - Zeilen > 8
		 * - Spalten > 8
		 * - Zeilen = 1 und spalten = 1
		 * - Zeilen < 1
		 * - Spalten < 1
		 * 
		 * Falls einer der obigen Bedingungen erfuellt sind, dann wird eine Meldung ausgegeben und diese Methode abgebrochen.
		 * Die Methode wird in diesem Fall abgebrochen, da die Matrix dann entweder zu gross wird oder gar keinen Sinn machen wird.
		 */
		if (zeilen > 8 || spalten > 8 || (zeilen == 1 && spalten == 1) || zeilen < 1 || spalten < 1) {
			// falls eine der obigen Bedingungen zutrifft, wird dem Benutzer diese Meldung gezeigt.
			info.setText("Bitte achten Sie darauf, dass die Anzahl Zeilen und Spalten nicht grösser als 8 ist, dass entweder die Anzahl Zeilen oder die Anzahl Spalten grösser als 1 ist und, dass die Anzahl Zeilen und Spalten nicht kleiner als 1 ist.");
			
			// breche die Methode ab, da keine sinnvolle Matrix mit den gegebenen Zahlen generiert werden kann
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
		graphMitMaximalerFlussZeichnen();

		// -----------------------------------------------------------------------------------------------------
	}
	
	/**
	 * Mit dieser Methode kann der Nutzer sagen, dass er einen Startknoten setzen moechte.
	 */
	@FXML
	public void startknotenSetzen() {
		// setze den gewuenschten Knoten auf den Wert fuer einen Startknoten
		knoten_setzen = 0;
		
		// aktiviere den Knopf (Zielknoten), welches nicht angeklickt wurde, und deaktiviere den Knopf (Startknoten), welches angeklickt wurde
		startknoten_setzen.setDisable(true);
		zielknoten_setzen.setDisable(false);
	}
	
	/**
	 * Mit dieser Methode kann der Nutzer sagen, dass er einen Zielknoten setzen moechte.
	 */
	@FXML
	public void zielknotenSetzen() {
		// setze den gewuenschten Knoten auf den Wert fuer einen Zielknoten
		knoten_setzen = 2;
		
		// aktiviere den Knopf (Startknoten), welches nicht angeklickt wurde, und deaktiviere den Knopf (Zielknoten), welches angeklickt wurde
		startknoten_setzen.setDisable(false);
		zielknoten_setzen.setDisable(true);
	}

	/**
	 * Diese Methode zeichnet den Graphen auf den Canvas.
	 *
	 */
	private void graphZeichnen() {
		// Die Infos werden geleert, da jetzt wieder ein neuer Graph generiert wird.
		info.setText("");
		
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
		knotenZeichnen(knoten, kreisgroesse);

		// zeichne die Kanten
		kantenZeichnen(kanten, Color.BLACK, true);

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
		kantenZeichnen(kanten, Color.BLACK, true);

		// zeichne anschliessend die Kanten des maximalen Flusses
		kantenZeichnen(kanten_maximaler_fluss, Color.BLUE, false);

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
		knotenZeichnen(knoten, kreisgroesse);

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
	 * @param knoten       Das sind alle Knoten des Graphen.
	 * @param kreisgroesse Das ist die Groesse der Kreise fuer die Knoten des
	 *                     Graphen.
	 */
	private void knotenZeichnen(Knoten[][] knoten, int kreisgroesse) {
		// setzt die Farbe fuer Fill auf Schwarz
		gc.setFill(Color.BLACK);

		// Diese Schleife zeichnet die Knoten.
		for (int i = 0; i < knoten.length; i++) {
			// initialisiere die benoetigten Variablen

			// Das ist die X-Position eines Knotens.
			int x_position = 400;
			// Das ist die Y-Position eines Knotens.
			int y_position = 5 + (150 * i);

			/*
			 * Das die Groesse eines Zaehlschrittes fuer die Y-Position.
			 * 
			 * Die Groesse eines Schrittes haengt von der Laenge einer waagerechte Ebene ab.
			 */
			int x_position_zaehlschritte = x_position / (knoten[i].length + 1);

			// erstelle neue ArrayLists fuer die aktuelle waagerechte Ebene
			x_positionen.add(new ArrayList<Integer>());
			y_positionen.add(new ArrayList<Integer>());

			// Schleife, um die einzelnen Knoten einer waagerechte Ebene zu zeichnen
			for (int j = 0; j < knoten[i].length; j++) {
				/*
				 * Ziehe einen Zaehlschritt von der Y-Position ab, damit die Y-Position die
				 * richtige Groesse hat.
				 */
				x_position -= x_position_zaehlschritte;

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
	 * @param informationen_speichern Dieser Boolean sagt, ob die Informationen zu der gezeichnete Kante gespeichert werden soll.
	 */
	private void kantenZeichnen(ArrayList<Kante> kanten, Paint farbe, boolean informationen_speichern) {
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
		 * ueberschreibe den Kreis fuer den Start mit einem gruenen Kreis, in dessen
		 * Mitte ein grosses S steht
		 */
		knotenFaerben(0, "S", Color.LIGHTGREEN);

		/*
		 * ueberschreibe den Kreis fuer das Ziel mit einem roten Kreis, in dessen
		 * Mitte ein grosses Z steht
		 */
		knotenFaerben(2, "Z", Color.RED);
	}
	
	/**
	 * Diese Methode faerbt einen einzelnen Knoten und beschriftet diesen mit dem gewuenschten Text.
	 * 
	 * Sobald der Knoten gefunden wurde, wird nicht mehr nach einem weiteren Knoten gesucht.
	 * Der Grund dafuer ist, dass es immer nur einen Startknoten und einen Zielknoten geben kann.
	 * 
	 * @param kategorie Das ist die Kategorie des gesuchten Knotens.
	 * @param text Das ist der Text, der auf der Knoten angezeigt werden soll.
	 * @param farbe Das ist die Farbe, auf die der Knoten geaendert werden soll.
	 */
	private void knotenFaerben(int kategorie, String text, Paint farbe) {
		// Zuerst wird durch die einzelnen Zeilen iteriert.
		for (int i = 0; i < knoten.length; i++) {
			// Es wird ein Boolean erstellt, welches dem Programm sagt, ob der zu faerbende Knoten schon gefunden wurde.
			boolean gefunden = false;
			
			// Anschliessend wird durch die einzelnen Knoten, der Zeile iteriert.
			for (int j = 0; j < knoten[i].length; j++) {
				// ueberprueft, ob dieser Knoten zur gewuenschten Kategorie gehoert
				if (knoten[i][j].getKategorie() == kategorie) {
					// erstellt den Knoten
					gc.setFill(farbe);
					gc.fillOval(x_positionen.get(i).get(j), y_positionen.get(i).get(j), kreisgroesse, kreisgroesse);
					gc.strokeText(text, x_positionen.get(i).get(j) + 7, y_positionen.get(i).get(j) + 14);
					
					// sorge dafuer, dass die for-Schleifen vorzeitig abgebrochen werden, da der gesuchte Knoten bereits gefunden wurde und jetzt nicht mehr weitergesucht werden muss
					gefunden = true;
					j = knoten[i].length;
				}
			}
			
			// sorge dafuer, dass die for-Schleife abgebrochen wird, wenn der gesuchte Knoten bereits gefunden wurde
			if (gefunden) {
				i = knoten.length;
			}
		}
	}

	/**
	 * Mit dieser Methode kann der Graph neu aufgezeichnet werden.
	 * 
	 * Diese Methode achtet darauf, ob der maximaler Fluss bereits einmal
	 * aufgezeichnet wurde und zeichnet den maximalen Fluss in dem Fall erneut auf.
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
		
	/**
	 * Diese Methode sorgt dafuer, dass der Nutzer einen Knoten anklicken kann.
	 * 
	 * @param event Das ist die MouseEvent vom Canvas. Die MouseEvent vom Canvas muss angegeben werden, damit diese Methode die Mausposition auf dem Canvas auslesen kann.
	 */
	private void knotenAnklicken(MouseEvent event) {
		/*
		 * Zuerst wird ueberprueft, ob eine bestimmte Knotenkategorie gesetzt werden soll.
		 * 
		 * 1 ist der Standardwert, dass bedeutet, dass noch keine zu setztende Knotenkategorie ausgewaehlt wurde.
		 * Wenn der Wert also nicht 1 ist, bedeutet das, dass eine Knotenkategorie ausgewaehlt wurde.
		 */
		if (knoten_setzen != 1) {
			// Zuerst wird der alte Knoten mit dieser Kategorie wieder in einen normalen Knoten umgewandelt.
			// Zuerst wird durch die einzelnen Zeilen iteriert.
			for (int i = 0; i < knoten.length; i++) {
				/*
				 * initialisiere eine Variable fuer die Laenge einer Zeile
				 * 
				 * Die Initialisierung erfolgt bereits hier, damit spaeter keine Exception auftaucht.
				 * 
				 * Urspruenglich gab es die folgende for-Schleife: "for (int j = 0; j < x_positionen.get(i).size(); j++) {".
				 */
				int laenge_zeile = knoten[i].length;
				
				// Anschliessend wird durch die einzelnen Knoten der Zeile iteriert.
				for (int j = 0; j < laenge_zeile; j++) {
					// Es wird ueberprueft, ob dieser Knoten von gleicher Kategorie wie die gewuenschte Kategorie ist.
					if (knoten[i][j].getKategorie() == knoten_setzen) {
						// der Knoten wird zu einem normalen Knoten
						knoten[i][j].setKategorie(1);
						
						// sorge dafuer, dass die for-Schleifen abgebrochen werden, da der angeklickte Knoten bereits gefunden wurde
						j = knoten[i].length;
						i = knoten.length;
					}
				}
			}
			
			// Anschliessend wird der passende Knoten auf die gewuenschte Kategorie gesetzt.
			// Zuerst wird durch die einzelnen Zeilen iteriert.
			for (int i = 0; i < x_positionen.size(); i++) {
				/*
				 * initialisiere eine Variable fuer die Laenge einer Zeile
				 * 
				 * Die Initialisierung erfolgt bereits hier, damit spaeter keine Exception auftaucht.
				 * 
				 * Urspruenglich gab es die folgende for-Schleife: "for (int j = 0; j < x_positionen.get(i).size(); j++) {".
				 */
				int laenge_zeile = x_positionen.get(i).size();
				
				// Anschliessend wird durch die einzelnen Knoten, beziehungsweise deren Positionen, der Zeile iteriert.
				for (int j = 0; j < laenge_zeile; j++) {
					// Zuerst werden die Variablen fuer die if-Verzweigung initialisiert, damit die if-Verzweigung nicht zu unuebersichtlich wird.
					// Die X-Position vom Knoten.
					int x_position = x_positionen.get(i).get(j);
					// Die Y-Position vom Knoten.
					int y_position = y_positionen.get(i).get(j);
					// Die X-Position vom Maus.
					double x_maus = event.getX();
					// Die Y-Position vom Maus.
					double y_maus = event.getY();
					
					// ueberprueft, ob der Knoten angeklickt wurde
					if ((x_maus >= x_position && x_maus <= (x_position + 20)) && (y_maus >= y_position && y_maus <= (y_position + 20))) {
						// setze die Kategorie des angeklickten Knotens auf die gewuenschte Kategorie
						knoten[i][j].setKategorie(knoten_setzen);
						
						// zeichne den Graphen neu auf, damit der angeklickte Knoten richtig dargestellt wird
						graphNeuZeichnen();
						
						// sorge dafuer, dass die for-Schleifen abgebrochen werden, da der angeklickte Knoten bereits gefunden wurde
						j = x_positionen.get(i).size();
						i = x_positionen.size();
					}
				}
			}
		}
	}

	@FXML
	public void berechnen() {
		algorithmus.resetFlow();
		algorithmus.berechneMaxFlow();
	}

}
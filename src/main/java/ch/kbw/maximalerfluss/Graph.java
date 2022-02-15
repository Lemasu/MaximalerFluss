package ch.kbw.maximalerfluss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Diese Klasse kuemmert sich um den Graphen.
 * 
 * Es kann Graphen generieren, aber auch zeichnen.
 * 
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 * 
 * @author Alex Schaub
 */
public class Graph {
	/**
	 * Dieser Variable wird fuer die Generierung und Darstellung des Graphen
	 * verwendet.
	 */
	private int r;

	/**
	 * Dieser Variable bestimmt, wie Spitz die Pfeilspitzen sein werden.
	 * 
	 * Bigger = smaller angle
	 */
	private int curveAngle;

	/**
	 * Das sind die Knoten im Graphen.
	 * 
	 * Die einzelnen ArrayLists in dieser ArrayList bilden den Graphen ab und wird
	 * von links nach rechts gelesen.
	 */
	private ArrayList<ArrayList<Knoten>> knoten;

	/**
	 * Das sind die Kanten im Graphen.
	 */
	private ArrayList<Kante> kanten;

	/**
	 * Das ist der Standardkonstruktor.
	 */
	public Graph() {

	}

	/**
	 * Das ist der Konstruktor mit dem Parameter fuer das curveAngle.
	 * 
	 * @param curveAngle Dieser Variable bestimmt, wie Spitz die Pfeilspitzen sein
	 *                   werden.
	 */
	public Graph(int curveAngle) {
		this.curveAngle = curveAngle;
	}

	/**
	 * Diese Funktion ist fuer die Generierung des Graphen gedacht.
	 * 
	 * @param ebenen_waagerecht Dieser Integer beschreibt, wie viele waagerechte
	 *                          Ebenen generiert werden sollen.
	 * @param ebenen_senkrecht  Dieser Integer beschreibt, wie viele senkrechte
	 *                          Ebenen generiert werden sollen.
	 * @param gc                Das ist der GraphicsContext fuer den Canvas. Dieser
	 *                          wird verwendet, um auf dem Canvas zeichnen zu
	 *                          koennen.
	 */
	public void graphGenerieren(int ebenen_waagerecht, int ebenen_senkrecht, GraphicsContext gc) {
		// Initialize variables
		this.r = 5;
		int d = r << 1;
		boolean e;
		knoten = new ArrayList<ArrayList<Knoten>>();
		kanten = new ArrayList<Kante>();

		knotenGenerieren(ebenen_waagerecht, ebenen_senkrecht);
		kantenGenerieren();
	}

	/**
	 * Diese Methode zeichnet den Graphen auf den Canvas.
	 * 
	 * @param gc Das ist der GraphicsContext fuer den Canvas. Dieser wird verwendet,
	 *           um auf dem Canvas zeichnen zu koennen.
	 */
	public void showGraph(GraphicsContext gc) {
//
//		// set color of strokes in canvas
//		gc.setStroke(Color.BLACK);
//		for (int i = 0; i < kanten.length; i++) {
//			double a = Math.atan2(kanten[i].getP1().getY() - kanten[i].getP2().getY(),
//					kanten[i].getP1().getX() - kanten[i].getP2().getX());
//			double a1 = a + Math.PI / curveAngle;
//			double a2 = a - Math.PI / curveAngle;
//			// length of pointing kanten
//			int l = 20;
//
//			// draw normal arrows from knoten to knoten
//			gc.strokeLine(kanten[i].getP1().getX() + r, kanten[i].getP1().getY() + r, kanten[i].getP2().getX() + r,
//					kanten[i].getP2().getY() + r);
//			gc.strokeLine(kanten[i].getP2().getX() + r, kanten[i].getP2().getY() + r,
//					Math.cos(a1) * l + kanten[i].getP2().getX() + r, Math.sin(a1) * l + kanten[i].getP2().getY() + r);
//			gc.strokeLine(kanten[i].getP2().getX() + r, kanten[i].getP2().getY() + r,
//					Math.cos(a2) * l + kanten[i].getP2().getX() + r, Math.sin(a2) * l + kanten[i].getP2().getY() + r);
//		}
	}

	/**
	 * Diese Methode generiert die einzelnen Knoten des Graphen.
	 * 
	 * @param ebenen_waagerecht Dieser Integer beschreibt, wie viele waagerechte
	 *                          Ebenen generiert werden sollen.
	 * @param ebenen_senkrecht  Dieser Integer beschreibt, wie viele senkrechte
	 *                          Ebenen maximal generiert werden sollen.
	 */
	private void knotenGenerieren(int ebenen_waagerecht, int ebenen_senkrecht) {
		// Zufallszahl fuer die Generierung
		final Random rand = new Random();

		// fuege den Start dem Graphen hinzu
		knoten.add(new ArrayList<Knoten>());
		knoten.get(0).add(new Knoten(1, 1, 0));

		// generiert die einzelnen normalen Knoten
		for (int i = 0; i < ebenen_waagerecht; i++) {
			knoten.add(new ArrayList<Knoten>());
			int waagerechte_position = i + 2;
			int senkrechte_position = 0;

			for (int j = 0; j < ebenen_senkrecht; j++) {
				senkrechte_position++;
				knoten.get(i + 1).add(new Knoten(waagerechte_position, senkrechte_position, 1));
				if (rand.nextInt(3) == 0) {
					j += rand.nextInt(3);
				}
			}
		}

		// generiert den Zielknoten
		knoten.add(new ArrayList<Knoten>());
		knoten.get(knoten.size() - 1).add(new Knoten(knoten.size(), 1, 2));

		// -----------------------------------------------------------------------------------------------------
		// Dieser Abschnitt dient nur zum Testen.
		// -----------------------------------------------------------------------------------------------------

		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println("Knoten");
		System.out.println("--------------------------------------------------------");
		System.out.println();

		for (int i = 0; i < knoten.size(); i++) {
			System.out.print((i + 1) + ". ArrayList: | ");
			for (int j = 0; j < knoten.get(i).size(); j++) {
				System.out.print("[Knoten " + knoten.get(i).get(j).getId() + " gehoert zum Kategorie "
						+ knoten.get(i).get(j).getKategorie() + "] | ");
			}
			System.out.println();
		}

		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println();

		// -----------------------------------------------------------------------------------------------------
	}

	/**
	 * Mit dieser Methode werden die Kanten eines Graphen generiert.
	 */
	private void kantenGenerieren() {
		// Zufallszahl fuer die Generierung
		final Random rand = new Random();

		for (int i = 0; i < (knoten.size() - 1); i++) {
			ArrayList<Knoten> ebene_1 = knoten.get(i);
			ArrayList<Knoten> ebene_2 = knoten.get(i + 1);

			for (int j = 0; j < ebene_1.size(); j++) {
				Knoten knoten_1 = ebene_1.get(j);

				for (int l = 0; l < ebene_2.size(); l++) {
					Knoten knoten_2 = ebene_2.get(l);

					kanten.add(new Kante(knoten_1, knoten_2, (rand.nextInt(20) + 1)));
				}
			}
		}
		for (int i = 0; i < (knoten.size() - 2); i++) {
			ArrayList<Knoten> ebene_1 = knoten.get(i);
			
			for (int j = (i + 2); j < knoten.size(); j++) {
				ArrayList<Knoten> ebene_2 = knoten.get(j);
				
				for (int l = 0; l < ebene_1.size(); l++) {
					Knoten knoten_1 = ebene_1.get(l);

					for (int t = 0; t < ebene_2.size(); t++) {
						Knoten knoten_2 = ebene_2.get(t);
						Kante kante = new Kante(knoten_1, knoten_2, (rand.nextInt(20) + 1));

						if (rand.nextInt(25) == 1) {
							kanten.add(kante);
						}
					}
				}
			}
		}

		// -----------------------------------------------------------------------------------------------------
		// Dieser Abschnitt dient nur zum Testen.
		// -----------------------------------------------------------------------------------------------------

		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println("Kanten");
		System.out.println("--------------------------------------------------------");
		System.out.println();

		for (Kante kante : kanten) {
			System.out.println("[Kante mit den 1. Knoten = " + kante.getKnoten_1().getId() + " und den 2. Knoten = "
					+ kante.getKnoten_2().getId() + " | Auslastung: " + kante.getAuslastung() + " / Kapazitaet: "
					+ kante.getKapazitaet() + "]");
		}

		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println();

		// -----------------------------------------------------------------------------------------------------
	}
}

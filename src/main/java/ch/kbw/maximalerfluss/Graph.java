package ch.kbw.maximalerfluss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
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
	 * Die einzelnen ArrayLists in dieser ArrayList bilden den Graphen ab und wird von links nach rechts gelesen.
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
	 * @param ebenen_waagerecht Das ist der Textfeld fuer die Anzahl der
	 *                          waagerechten Ebenen im Graphen, welcher generiert
	 *                          werden soll.
	 * @param ebenen_senkrecht  Das ist der Textfeld fuer die Anzahl der senkrechten
	 *                          Ebenen im Graphen, welcher generiert werden soll.
	 * @param gc                Das ist der GraphicsContext fuer den Canvas. Dieser
	 *                          wird verwendet, um auf dem Canvas zeichnen zu
	 *                          koennen.
	 */
	public void graphGenerieren(TextField ebenen_waagerecht, TextField ebenen_senkrecht, GraphicsContext gc) throws NumberFormatException {
		// Zufallszahl fuer die Generierung
		final Random rand = new Random();
		
		// Initialize variables
		this.r = 5;
		int d = r << 1;
		boolean e;
		knoten = new ArrayList<ArrayList<Knoten>>();
		kanten = new ArrayList<Kante>();
		
		// fuege den Start dem Graphen hinzu
		knoten.add(new ArrayList<Knoten>());
		knoten.get(0).add(new Knoten(0));
		
		// generiert die einzelnen normalen Knoten
		for (int i = 0; i < Integer.parseInt(ebenen_waagerecht.getText()); i++) {
			knoten.add(new ArrayList<Knoten>());
			for (int j = 0; j < Integer.parseInt(ebenen_senkrecht.getText()); j++) {
				knoten.get(i + 1).add(new Knoten(1));
				if (rand.nextInt(3) == 0) {
					j += rand.nextInt(3);
				}
			}
		}
		
		// generiert den Zielknoten
		knoten.add(new ArrayList<Knoten>());
		knoten.get(knoten.size() - 1).add(new Knoten(2));
		
		// -----------------------------------------------------------------------------------------------------
		// Dieser Abschnitt dient nur zum Testen.
		// -----------------------------------------------------------------------------------------------------
		
		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println();
		
		for (int i = 0; i < knoten.size(); i++) {
			System.out.print((i + 1) + ". ArrayList: | ");
			for (int j = 0; j < knoten.get(i).size(); j++) {
				System.out.print("[Knoten " + (i + 1) + "." + (j + 1) + " gehoert zum Kategorie " + knoten.get(i).get(j).getKategorie() + "] | ");
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println();
		
		// -----------------------------------------------------------------------------------------------------
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
}

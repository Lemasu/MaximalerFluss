package ch.kbw.maximalerfluss;

import java.util.ArrayList;
import java.util.Random;

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
	 * Das sind die Knoten im Graphen.
	 * 
	 * Die einzelnen ArrayLists in dieser ArrayList bilden den Graphen ab und wird
	 * von links nach rechts gelesen.
	 */
	private Knoten[][] knoten;

	/**
	 * Das sind die Kanten im Graphen.
	 */
	private ArrayList<Kante> kanten;

	/**
	 * Das sind die Rückkanten im Graphen.
	 * Sie sind nötig, um das Problem der ungünstigen Pfadwahl zu beheben.
	 */
	private ArrayList<Kante> rueckKanten;

	/**
	 * Das sind die Kanten, welche den maximalen Fluss ermoeglichen.
	 * 
	 * Aktuell ist noch nicht klar, ob diese ArrayList wirklich in die Klasse Graph
	 * gehoert.
	 */
	private ArrayList<Kante> kanten_maximaler_fluss;

	/**
	 * Das ist der Standardkonstruktor.
	 */
	public Graph() {

	}

	/**
	 * Diese Funktion ist fuer die Generierung des Graphen gedacht.
	 * 
	 * Die Knoten des Graphen werden im Form einer Matrix generiert.
	 * 
	 * @param zeilen  Das ist die Anzahl der Zeilen des Graphen.
	 * @param spalten Das ist die Anzahl der Spalten des Graphen.
	 */
	public void graphGenerieren(int zeilen, int spalten) {
		// Initialize variables
		knoten = new Knoten[zeilen][spalten];
		kanten = new ArrayList<Kante>();
		rueckKanten = new ArrayList<Kante>();

		// generiere die Knoten des Graphen
		knotenGenerieren(zeilen, spalten);

		// generiere die Kanten des Graphen
		kantenGenerieren();

		// generiere die Rückkanten des Graphen
		rueckKantenGenerieren();

		// -----------------------------------------------------------------------------------------------------
		// Dieser Abschnitt dient nur zum Testen.
		// -----------------------------------------------------------------------------------------------------

		/*
		 * Ich habe diesen Abschnitt nicht gross auf Bugs getestet. Es scheint zu
		 * funktionieren. Da dieser Abschnitt nur fuer Testzwecke erstellt wurde, habe
		 * ich auf weitere Tests verzichtet.
		 */

		kanten_maximaler_fluss = new ArrayList<Kante>();

		// Zufallszahl fuer die Generierung des "maximalen Flusses"
		final Random rand = new Random();

		ArrayList<Kante> nicht_benutzte_kanten = new ArrayList<Kante>();
		nicht_benutzte_kanten = (ArrayList) kanten.clone();

		int zahl = nicht_benutzte_kanten.size();

		for (int i = 0; i < zahl; i++) {
			int zufallszahl = rand.nextInt(nicht_benutzte_kanten.size());
			kanten_maximaler_fluss.add(nicht_benutzte_kanten.get(zufallszahl));
			nicht_benutzte_kanten.remove(zufallszahl);
			i += rand.nextInt(5);
		}

		// -----------------------------------------------------------------------------------------------------
	}

	/**
	 * Diese Methode generiert die einzelnen Knoten des Graphen.
	 * 
	 * @param zeilen  Das ist die Anzahl der Zeilen des Graphen.
	 * @param spalten Das ist die Anzahl der Spalten des Graphen.
	 */
	private void knotenGenerieren(int zeilen, int spalten) {
		// generiert die einzelnen normalen Knoten
		for (int i = 0; i < zeilen; i++) {
			// initializiere die Variablen fuer die Generierung der ID des Knotens.
			int zeile_fuer_id = i + 1;
			int spalte_fuer_id = 0;

			// fuege die einzelnen Knoten der ArrayList hinzu
			for (int j = 0; j < spalten; j++) {
				// erhoehe den Teil fuer die Spalte fuer die ID, um damit spaeter die ID des
				// Knotens zu generieren
				spalte_fuer_id++;

				// erstelle den neuen Knoten
				knoten[i][j] = new Knoten(zeile_fuer_id, spalte_fuer_id, 1);
			}
		}
		
		// -----------------------------------------------------------------------------------------------------
		// Dieser Abschnitt dient nur zum Testen.
		// -----------------------------------------------------------------------------------------------------

		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println("Knoten");
		System.out.println("--------------------------------------------------------");
		System.out.println();

		for (int i = 0; i < knoten.length; i++) {
			System.out.print((i + 1) + ". ArrayList: | ");
			for (int j = 0; j < knoten[i].length; j++) {
				System.out.print("[Knoten " + knoten[i][j].getId() + " gehoert zum Kategorie "
						+ knoten[i][j].getKategorie() + "] | ");
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

		// generiere zuerst die Kanten, welche direkt verbunden sind
		// falls es nur eine Zeile gibt, wird der Code im "else" ausgefuehrt
		if (knoten.length > 1) {
			for (int i = 0; i < (knoten.length - 1); i++) {
				// iteriere durch die Knoten der ersten Zeile
				for (int j = 0; j < knoten[i].length; j++) {
					// hole den aktuellen Knoten der ersten Spalte
					Knoten knoten_1 = knoten[i][j];

					// iteriere durch die Knoten der zweiten Spalte
					for (int l = 0; l < knoten[i + 1].length; l++) {
						// hole den aktuellen Knoten der zweiten Spalte
						Knoten knoten_2 = knoten[i + 1][l];

						// erstelle die Kante mit dem ersten Knoten und dem zweiten Knoten
						kanten.add(new Kante(knoten_1, knoten_2, (rand.nextInt(20) + 1)));
					}
				}
			}
		} else {
			for (int i = 0; i < (knoten[0].length - 1); i++) {
				// hole den aktuellen Knoten der ersten Spalte
				Knoten knoten_1 = knoten[0][i];

				// hole den aktuellen Knoten der zweiten Spalte
				Knoten knoten_2 = knoten[0][i + 1];

				// erstelle die Kante mit dem ersten Knoten und dem zweiten Knoten
				kanten.add(new Kante(knoten_1, knoten_2, (rand.nextInt(20) + 1)));
			}
		}

		// -----------------------------------------------------------------------------------------------------
		/*
		 * Diesen Abschnitt habe ich auskommentiert, da dieser noch nicht ganz
		 * fehlerfrei funktioniert.
		 * 
		 * Dieser Abschnitt ist fuer die Grundfunktionalitaeten des Graphen nicht von
		 * Bedeutung.
		 */
		// -----------------------------------------------------------------------------------------------------

//		// generiere anschliessend die Knoten, welche nicht direkt verbunden sind
//		/*
//		 * iteriere durch alle waagerechte Ebenen
//		 * 
//		 * Nur die letzten beiden Ebenen werden nicht angeschaut.
//		 * Die vorletzte wird nicht angeschaut, da dieser auch so bereits direkt mit dem Ziel verbunden ist.
//		 * Die letzte wird nicht angeschaut, da nach diesem keine Knoten mehr kommen, mit dem dieser verbunden werden kann.
//		 */
//		for (int i = 0; i < (knoten.size() - 2); i++) {
//			// // hole die waagerechte Ebene des ersten Knotens
//			ArrayList<Knoten> ebene_1 = knoten.get(i);
//			
//			/*
//			 * hole anschliessend alle folgenden Ebenen
//			 * 
//			 * starte bei "i + 2", damit dieser die naechste direkte, waagerechte Ebene ueberspringt, da dieser bereits verbunden sind 
//			 */
//			for (int j = (i + 2); j < knoten.size(); j++) {
//				// hole die waagerechte Ebene des zweiten Knotens
//				ArrayList<Knoten> ebene_2 = knoten.get(j);
//				
//				// iteriere durch die Knoten der ersten waagerechten Ebene
//				for (int l = 0; l < ebene_1.size(); l++) {
//					// hole den aktuellen Knoten der ersten waagerechten Ebene
//					Knoten knoten_1 = ebene_1.get(l);
//
//					// iteriere durch die Knoten der zweiten waagerechten Ebene
//					for (int t = 0; t < ebene_2.size(); t++) {
//						// hole den aktuellen Knoten der zweiten waagerechten Ebene
//						Knoten knoten_2 = ebene_2.get(t);
//						
//						// erstelle die neue Kante
//						Kante kante = new Kante(knoten_1, knoten_2, (rand.nextInt(20) + 1));
//
//						// entscheide zufaellig, ob diese Kante in die ArrayList aufgenommen werden soll
//						if (rand.nextInt(25) == 1) {
//							// fuege die Kante der ArrayList hinzu
//							kanten.add(kante);
//						}
//					}
//				}
//			}
//		}

		// -----------------------------------------------------------------------------------------------------

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
					+ kante.getKnoten_2().getId() + " | Auslastung: " + kante.getAuslastung() + " / restliche Kapazitaet: "
					+ kante.getRestKapazitaet() + " / maximale Kapazitaet: " + kante.getMaxKapazitaet() + "]");
		}

		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println();

		// -----------------------------------------------------------------------------------------------------
	}

	/**
	 * Mit dieser Methode werden die Rückkanten eines Graphen generiert.
	 */
	private void rueckKantenGenerieren() {

		for (Kante kante : kanten) {
			Kante neueKante = new Kante(kante.getKnoten_2(),kante.getKnoten_1(),0);
			rueckKanten.add(neueKante);
		}

		// -----------------------------------------------------------------------------------------------------
		// Dieser Abschnitt dient nur zum Testen.
		// -----------------------------------------------------------------------------------------------------

		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println("Rückkanten");
		System.out.println("--------------------------------------------------------");
		System.out.println();

		for (Kante kante : rueckKanten) {
			System.out.println("[Kante mit den 1. Knoten = " + kante.getKnoten_1().getId() + " und den 2. Knoten = "
					+ kante.getKnoten_2().getId() + " | Auslastung: " + kante.getAuslastung() + " / restliche Kapazitaet: "
					+ kante.getRestKapazitaet() + " / maximale Kapazitaet: " + kante.getMaxKapazitaet() + "]");
		}

		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println();

		// -----------------------------------------------------------------------------------------------------
	}

	/**
	 * Das ist der Getter fuer die Kanten des Graphen.
	 * 
	 * @return Das sind die Kanten des Graphen.
	 */
	public ArrayList<Kante> getKanten() {
		return kanten;
	}

	/**
	 * Das ist der Getter fuer die Rueckkanten des Graphen.
	 *
	 * @return Das sind die Rueckkanten des Graphen.
	 */
	public ArrayList<Kante> getRueckKanten() {
		return rueckKanten;
	}

	/**
	 * Das ist der Getter fuer die Knoten des Graphen.
	 * 
	 * @return Das sind die Knoten des Graphen.
	 */
	public Knoten[][] getKnoten() {
		return knoten;
	}

	/**
	 * Das ist der Getter fuer die Kanten des maximalen Flusses.
	 * 
	 * @return Das sind die Kanten des maximalen Flusses.
	 */
	public ArrayList<Kante> getKanten_maximaler_fluss() {
		return kanten_maximaler_fluss;
	}
}

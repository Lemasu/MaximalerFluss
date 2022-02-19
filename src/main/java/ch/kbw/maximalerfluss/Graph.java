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
	private ArrayList<ArrayList<Knoten>> knoten;

	/**
	 * Das sind die Kanten im Graphen.
	 */
	private ArrayList<Kante> kanten;
	
	/**
	 * Das sind die Kanten, welche den maximalen Fluss ermoeglichen.
	 * 
	 * Aktuell ist noch nicht klar, ob diese ArrayList wirklich in die Klasse Graph gehoert.
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
	 * @param ebenen_waagerecht Dieser Integer beschreibt, wie viele waagerechte
	 *                          Ebenen generiert werden sollen.
	 * @param ebenen_senkrecht  Dieser Integer beschreibt, wie viele senkrechte
	 *                          Ebenen generiert werden sollen.
	 */
	public void graphGenerieren(int ebenen_waagerecht, int ebenen_senkrecht) {
		// Initialize variables
		knoten = new ArrayList<ArrayList<Knoten>>();
		kanten = new ArrayList<Kante>();

		// generiere die Knoten des Graphen
		knotenGenerieren(ebenen_waagerecht, ebenen_senkrecht);
		
		// generiere die Kanten des Graphen
		kantenGenerieren();
		
		// -----------------------------------------------------------------------------------------------------
		// Dieser Abschnitt dient nur zum Testen.
		// -----------------------------------------------------------------------------------------------------

		/*
		 * Ich habe diesen Abschnitt nicht gross auf Bugs getestet.
		 * Es scheint zu funktionieren.
		 * Da dieser Abschnitt nur fuer Testzwecke erstellt wurde, habe ich
		 * auf weitere Tests verzichtet.
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
		// generiere zuerst die waagerechte Ebene
		for (int i = 0; i < ebenen_waagerecht; i++) {
			// fuege die waagerechte Ebene der ArrayList hinzu
			knoten.add(new ArrayList<Knoten>());
			
			// initializiere die Variablen fuer die Generierung der ID des Knotens.
			int waagerechte_position = i + 2;
			int senkrechte_position = 0;

			// fuege die einzelnen Knoten der ArrayList hinzu
			for (int j = 0; j < ebenen_senkrecht; j++) {
				// erhoehe die senkrechte Position, um damit spaeter die ID des Knotens zu generieren
				senkrechte_position++;
				
				// erstelle den neuen Knoten
				knoten.get(i + 1).add(new Knoten(waagerechte_position, senkrechte_position, 1));
				
				/*
				 * entscheide zufaellig, wie stark j erhoeht wird
				 * 
				 * Der Grund, warum die Erhoehung zufaellig erfolgt, ist der, dass der Nutzer nur die maximale Menge der Knoten in eine waagerechte Ebene festlegen kann. Wie viele Knoten effektiv in einer waagerechte Ebene dann vorhanden sind, wird zufaellig festgelegt.
				 */
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

		// generiere zuerst die Kanten, welche direkt verbunden sind
		for (int i = 0; i < (knoten.size() - 1); i++) {
			// hole die waagerechte Ebene des ersten Knotens
			ArrayList<Knoten> ebene_1 = knoten.get(i);
			// hole die waagerechte Ebene des zweiten Knotens
			ArrayList<Knoten> ebene_2 = knoten.get(i + 1);

			// iteriere durch die Knoten der ersten waagerechten Ebene
			for (int j = 0; j < ebene_1.size(); j++) {
				// hole den aktuellen Knoten der ersten waagerechten Ebene
				Knoten knoten_1 = ebene_1.get(j);

				// iteriere durch die Knoten der zweiten waagerechten Ebene
				for (int l = 0; l < ebene_2.size(); l++) {
					// hole den aktuellen Knoten der zweiten waagerechten Ebene
					Knoten knoten_2 = ebene_2.get(l);

					// erstelle die Kante mit dem ersten Knoten und dem zweiten Knoten
					kanten.add(new Kante(knoten_1, knoten_2, (rand.nextInt(20) + 1)));
				}
			}
		}
		
		// -----------------------------------------------------------------------------------------------------
		/*
		 * Diesen Abschnitt habe ich auskommentiert, da dieser noch nicht ganz fehlerfrei funktioniert.
		 * 
		 * Dieser Abschnitt ist fuer die Grundfunktionalitaeten des Graphen nicht von Bedeutung.
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
					+ kante.getKnoten_2().getId() + " | Auslastung: " + kante.getAuslastung() + " / Kapazitaet: "
					+ kante.getKapazitaet() + "]");
		}

		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println();

		// -----------------------------------------------------------------------------------------------------
	}

	/**
	 * Das ist der Getter fuer die Knoten des Graphen.
	 * 
	 * @return Das sind die Knoten des Graphen.
	 */
	public ArrayList<ArrayList<Knoten>> getKnoten() {
		return knoten;
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
	 * Das ist der Getter fuer die Kanten des maximalen Flusses.
	 * 
	 * @return Das sind die Kanten des maximalen Flusses.
	 */
	public ArrayList<Kante> getKanten_maximaler_fluss() {
		return kanten_maximaler_fluss;
	}
}

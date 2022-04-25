package ch.kbw.maximalerfluss;

import java.util.ArrayList;
import java.util.Random;

/**
 * Diese Klasse kuemmert sich um den Graphen.
 * <p>
 * Es kann Graphen generieren, aber auch zeichnen.
 * <p>
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 *
 * @author Alex Schaub
 */
public class Graph {
    /**
     * Das sind die Knoten im Graphen.
     * <p>
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
     * <p>
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
     * <p>
     * Die Knoten des Graphen werden im Form einer Matrix generiert.
     *
     * @param zeilen  Das ist die Anzahl der Zeilen des Graphen.
     * @param spalten Das ist die Anzahl der Spalten des Graphen.
     */
    public void graphGenerieren(int zeilen, int spalten, int anzahl) {
        // Initialize variables
        //doppelt?
        knoten = new Knoten[zeilen][spalten];
        kanten = new ArrayList<Kante>();
		rueckKanten = new ArrayList<Kante>();

        // generiere die Knoten des Graphen
        knotenGenerieren(zeilen, spalten);

        // generiere die Kanten des Graphen
        // kantenGenerieren();
		kantenZufaelligGenerieren(zeilen, spalten, anzahl);

		// generiere die Rückkanten des Graphen
		rueckKantenGenerieren();

		// generiere die Adjazenzlisten der Knoten
		adjazenzListenGenerieren();

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

    private void kantenZufaelligGenerieren(int zeilen, int spalten, int anzahl) {
        final Random rand = new Random();
        boolean status = false;
        boolean status2 = false;
        boolean status3 = true;
        int gleicheKanten = 0;
        int anzahlKanten = 0;
        int kapazitaet = 0;
        // Variablen, um die Knoten zu identifizieren
        Integer a, b, c, d;
        a = b = c = d = 0;

        // solange nicht jeder Knoten mindestens eine Kante hat, wird diese Schleife wiederholt
        while (status == false) {
            for (int i = 0; i < anzahl; i++) {
                status2 = false;
                // solange keine Kante erstellt wurde, läuft diese Schleife
                while (status2 == false) {
                    // zufällige Knoten für eine Kante auswählen
                    a = rand.nextInt(zeilen);
                    b = rand.nextInt(spalten);
                    c = rand.nextInt(zeilen);
                    d = rand.nextInt(spalten);
                    // überprüfen, ob es zwei verschiedene Knoten sind
                    if (knoten[a][b] != knoten[c][d]) {
                        // alle Kanten durch gehen
                        for (int l = 0; l < kanten.size(); l++) {
                            // wenn es die Kante mit den ausgewählten Knoten schon gibt, dann "gleichKanten++;"
                            if (kanten.get(l).getKnoten_1() == knoten[a][b] && kanten.get(l).getKnoten_2() == knoten[c][d]) {
                                gleicheKanten++;
                            }
                        }
                        // überprüfen, ob es schon die Kante mit den ausgewählten Knoten gab oder nicht
                        if (gleicheKanten == 0) {
                            // wenn es die Kante noch nicht gab, sie erstellen
                            kanten.add(new Kante(knoten[a][b], knoten[c][d], (rand.nextInt(20) + 1)));
                            status2 = true;
                        }
                        gleicheKanten = 0;
                    }
                }
            }
            // überprüfen, ob jeder Knoten mindestens eine Kante hat
            // jeden Knoten durchgehen
            status3 = true;
            for (int i = 0; i < knoten.length; i++) {
                for (int j = 0; j < knoten[i].length; j++) {
                    // überprüfen, ob der ausgewählte Knoten eine Kante hat
                    for (int o = 0; o < kanten.size(); o++) {
                        // überprüfen, ob der ausgewählte Knoten eine Kante hat, egal in welche Richtung
                        if (kanten.get(o).getKnoten_1() == knoten[i][j] || kanten.get(o).getKnoten_2() == knoten[i][j]) {
                            anzahlKanten++;
                        }
                    }
                    for (int k = 0; k < kanten.size(); k++) {
                    }
                    if (anzahlKanten == 0) {
                        status3 = false;
                    }
                    anzahlKanten = 0;
                }
            }
            if (status3 == false) {
                kanten.clear();
                kapazitaet = 0;
            } else {
                status = true;
            }
        }
        for (int i = 0; i < kanten.size(); i++) {
            System.out.println(kanten.get(i).getKnoten_1().getId() + "->" + kanten.get(i).getKnoten_2().getId()+ " "+kanten.get(i).getMaxKapazitaet());
        }
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
					+ kante.getRestKapazitaet() + " / maximale Kapazitaet: " +
					kante.getMaxKapazitaet() + "]");
		}

		System.out.println();
		System.out.println("--------------------------------------------------------");
		System.out.println();

		// -----------------------------------------------------------------------------------------------------
	}

	private void adjazenzListenGenerieren() {
		for (Knoten[] knotens : knoten) {
			for (Knoten knoten : knotens) {
				for (Kante kante : kanten) {
					if (kante.getKnoten_1()==knoten) {
						knoten.adjazenzListeKnoten.add(kante.getKnoten_2());
						knoten.adjazenzListeKanten.add(kante);
					}
				}
				for (Kante kante : rueckKanten) {
					if (kante.getKnoten_1()==knoten) {
						knoten.adjazenzListeKnoten.add(kante.getKnoten_2());
						knoten.adjazenzListeKanten.add(kante);
					}
				}
			}
		}
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
	 * Das ist der Getter fuer die Kanten des maximalen Flusses.
	 * 
	 * @return Das sind die Kanten des maximalen Flusses.
	 */
	public ArrayList<Kante> getKanten_maximaler_fluss() {
		return kanten_maximaler_fluss;
	}
}

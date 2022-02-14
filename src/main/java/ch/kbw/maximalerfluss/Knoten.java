package ch.kbw.maximalerfluss;

/**
 * Dies ist die Klasse fuer die Knoten in einem Graphen.
 * 
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 * 
 * @author Alex Schaub
 */
public class Knoten {
	/**
	 * Das ist die Kategorie eines Knotens.
	 * 
	 * 0 = Start
	 * 1 = normaler Knoten
	 * 2 = Ziel
	 */
	private final int kategorie;

	/**
	 * Das ist der Standardkonstruktor.
	 * 
	 * @param kategorie
	 */
	public Knoten(int kategorie) {
		this.kategorie = kategorie;
	}

    /**
     * Das ist der Getter fuer die Kategorie dieses Knotens.
     * 
     * @return Das ist die Kategorie dieses Knotens.
     */
	public int getKategorie() {
		return kategorie;
	}
}

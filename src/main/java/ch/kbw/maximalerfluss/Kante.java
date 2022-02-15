package ch.kbw.maximalerfluss;

/**
 * Dies ist die Klasse fuer die Kanten in einem Graphen.
 * 
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 * 
 * @author Alex Schaub
 */
public class Kante {
	/**
	 * Das ist der erste Knoten dieser Kante.
	 */
    private final Knoten knoten_1;
    
    /**
     * Das ist der zweite Knoten dieser Kante.
     */
    private final Knoten knoten_2;
    
    /**
     * Das ist die Auslastung einer Kante.
     */
    private int auslastung;
    
    /**
     * Das ist die Kapazitaet einer Kante.
     */
    private final int kapazitaet;
    
    /**
     * Das ist der Standardkonstruktor.
     * 
     * @param knoten_1 Das ist der erste Knote dieser Kante.
     * @param knoten_2 Das ist der zweite Knote dieser Kante.
     * @param kapazitaet Das ist die Kapazitaet dieser Kante.
     */
    public Kante(Knoten knoten_1, Knoten knoten_2, int kapazitaet) {
        this.knoten_1 = knoten_1;
        this.knoten_2 = knoten_2;
        this.auslastung = 0;
        this.kapazitaet = kapazitaet;
    }

    /**
     * Das ist der Getter fuer die Auslastung einer Kante.
     * 
     * @return Das ist die Auslastung einer Kante.
     */
	public int getAuslastung() {
		return auslastung;
	}

	/**
	 * Das ist der Setter fuer die Auslastung einer Kante.
	 * 
	 * @param auslastung Das ist die Auslastung einer Kante.
	 */
	public void setAuslastung(int auslastung) {
		this.auslastung = auslastung;
	}

    /**
     * Das ist der Getter fuer den ersten Knoten einer Kante.
     * 
     * @return Das ist der erster Knoten einer Kante.
     */
	public Knoten getKnoten_1() {
		return knoten_1;
	}

    /**
     * Das ist der Getter fuer den zweiten Knoten einer Kante.
     * 
     * @return Das ist der zweiter Knoten einer Kante.
     */
	public Knoten getKnoten_2() {
		return knoten_2;
	}

    /**
     * Das ist der Getter fuer die Kapazitaet einer Kante.
     * 
     * @return Das ist die Kapazitaet einer Kante.
     */
	public int getKapazitaet() {
		return kapazitaet;
	}
}

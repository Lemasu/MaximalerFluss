package ch.kbw.maximalerfluss;

/**
 * Dies ist die Klasse fuer die Kanten in einem Graphen.
 * 
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 * 
 * @author Alex Schaub, Marc Schwendemann, Aron Gassner
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
	 * Das ist die restliche Kapazitaet einer Kante.
	 */
	private int restKapazitaet;
    
    /**
     * Das ist die Kapazitaet einer Kante.
     */
    private final int maxKapazitaet;

	/**
     * Das ist der Standardkonstruktor.
     * 
     * @param knoten_1 Das ist der erste Knote dieser Kante.
     * @param knoten_2 Das ist der zweite Knote dieser Kante.
     * @param maxKapazitaet Das ist die Kapazitaet dieser Kante.
     */
    public Kante(Knoten knoten_1, Knoten knoten_2, int maxKapazitaet) {
        this.knoten_1 = knoten_1;
        this.knoten_2 = knoten_2;
        this.auslastung = 0;
		this.restKapazitaet = maxKapazitaet;
        this.maxKapazitaet = maxKapazitaet;
	}

	public int getAuslastung() {
		return auslastung;
	}

	public void setAuslastung(int auslastung) {
		this.auslastung = auslastung;
	}

	public int getRestKapazitaet() {
		return restKapazitaet;
	}

	public void setRestKapazitaet(int restKapazitaet) {
		this.restKapazitaet = restKapazitaet;
	}

	public Knoten getKnoten_1() { return knoten_1; }

	public Knoten getKnoten_2() {
		return knoten_2;
	}

	public int getMaxKapazitaet() {
		return maxKapazitaet;
	}

	@Override
	public String toString() {
		return ""+auslastung+"/"+maxKapazitaet;
	}
}



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
	 * Das ist die ID eines Knotens.
	 * 
	 * Die ID eines Knotens setzt sich aus der waagerechte Position und der senkrechte Position des Knotens zusammen.
	 * 
	 * Die Zaehlung beginnt bei 1.
	 */
	private final String id;
	
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
	 * @param waagerechte_position Das ist die waagerechte Position dieses Knotens.
	 * @param senkrechte_position Das ist die senkrechte Position dieses Knotens.
	 * @param kategorie Das ist die Kategorie dieses Knotens.
	 */
	public Knoten(int waagerechte_position, int senkrechte_position, int kategorie) {
		this.id = waagerechte_position + "." + senkrechte_position;
		this.kategorie = kategorie;
	}
	
	/**
     * Das ist der Getter fuer die ID dieses Knotens.
     * 
     * @return Das ist die ID dieses Knotens.
     */
    public String getId() {
		return id;
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

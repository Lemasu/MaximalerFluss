package ch.kbw.maximalerfluss;

import java.util.ArrayList;

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
	private String id;
	
	/**
	 * Das ist die Kategorie eines Knotens.
	 * 
	 * 0 = Start
	 * 1 = normaler Knoten
	 * 2 = Ziel
	 */
	private int kategorie;

	/**
	 * Die Adjenzliste eines Knoten.
	 * Beinhaltet alle benachbarten Knoten.
	 */
	public ArrayList<Knoten> adjazenzListeKnoten;

	/**
	 * Die Adjenzliste eines Knoten.
	 * Beinhaltet alle benachbarten Kanten.
	 */
	public ArrayList<Kante> adjazenzListeKanten;

	/**
	 * Das ist der Standardkonstruktor.
	 * 
	 * @param waagerechte_position Das ist die Zeile dieses Knotens.
	 * @param senkrechte_position Das ist die Spalte dieses Knotens.
	 * @param kategorie Das ist die Kategorie dieses Knotens.
	 */
	public Knoten(int zeile, int spalte, int kategorie) {
		this.id = zeile + "." + spalte;
		this.kategorie = kategorie;
		this.adjazenzListeKnoten = new ArrayList<Knoten>();
		this.adjazenzListeKanten = new ArrayList<Kante>();
	}

	public Knoten() {

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

	/**
	 * Das ist der Setter fuer die Kategorie dieses Knotens.
	 * 
	 * @param kategorie Das ist die gewuenschte Kategorie fuer diesen Knoten.
	 */
	public void setKategorie(int kategorie) {
		this.kategorie = kategorie;
	}

	/**
	 * Das ist der Getter fuer die Adjazenzliste der Knoten dieses Knotens.
	 *
	 * @return Das ist die Adjazenzliste der Knoten dieses Knotens.
	 */
	public ArrayList<Knoten> getAdjazenzListeKnoten() {
		return adjazenzListeKnoten;
	}

	/**
	 * Das ist der Getter fuer die Adjazenzliste der Kanten dieses Knotens.
	 *
	 * @return Das ist die Adjazenzliste der Kanten dieses Knotens.
	 */
	public ArrayList<Kante> getAdjazenzListeKanten() {
		return adjazenzListeKanten;
	}

	@Override
	public String toString() {
		return id;
	}
}

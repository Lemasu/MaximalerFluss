package ch.kbw.maximalerfluss;

import java.util.ArrayList;

/**
 * Dies ist die Klasse fuer die Knoten in einem Graphen.
 * 
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 * 
 * @author Alex Schaub, Marc Schwendemann, Aron Gassner
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
	 * Sie beinhaltet alle benachbarten Knoten.
	 */
	public ArrayList<Knoten> adjazenzListeKnoten;

	/**
	 * Die Adjenzliste eines Knoten.
	 * Sie beinhaltet alle benachbarten Kanten.
	 */
	public ArrayList<Kante> adjazenzListeKanten;

	/**
	 * Das ist der Standardkonstruktor.
	 *
	 * @param zeile Das ist die Zeile dieses Knotens.
	 * @param spalte Das ist die Spalte dieses Knotens.
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

    public String getId() {
		return id;
	}

	public int getKategorie() {
		return kategorie;
	}

	public void setKategorie(int kategorie) {
		this.kategorie = kategorie;
	}

	public ArrayList<Kante> getAdjazenzListeKanten() {
		return adjazenzListeKanten;
	}

	@Override
	public String toString() {
		return id;
	}
}

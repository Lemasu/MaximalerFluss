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
    private Knoten p1;
    
    /**
     * Das ist der zweite Knoten dieser Kante.
     */
    private Knoten p2;
    
    /**
     * Das ist das Gewicht dieser Kante.
     */
    private int weight;

    /**
     * Das ist ein Konstruktor.
     * 
     * @param p1 Das ist der erste Knote dieser Kante.
     * @param p2 Das ist der zweite Knote dieser Kante.
     * @param weight Das ist das Gewicht dieser Kante.
     */
    public Kante(Knoten p1, Knoten p2, int weight) {
        this.p1 = p1;
        this.p2 = p2;
        this.weight = weight;
    }

    /**
     * Das ist der Standardkonstruktor.
     */
    public Kante() {
    }

    /**
     * Das ist der Getter fuer den ersten Knoten dieser Kante.
     * 
     * @return Das ist der erste Knote dieser Kante.
     */
    public Knoten getP1() {
        return p1;
    }

    /**
     * Das ist der Getter fuer den zweiten Knoten dieser Kante.
     * 
     * @return Das ist der zweite Knote dieser Kante.
     */
    public Knoten getP2() {
        return p2;
    }

    /**
     * Das ist der Getter fuer den Gewicht dieser Kante.
     * 
     * @return Das ist das Gewicht dieser Kante.
     */
    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Line{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                ", weight=" + weight +
                '}';
    }
}

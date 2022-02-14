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
	 * Das ist die X-Koordinate des Knotens.
	 */
    private final int x;
    
    /**
     * Das ist die Y-Koordinate des Knotens.
     */
    private final int y;

    /**
     * Das ist der Standardkonstruktor.
     * 
     * @param x Das ist die X-Koordinate des Knotens.
     * @param y Das ist die Y-Koordinate des Knotens.
     */
    public Knoten(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Das ist der Getter fuer die X-Koordinate des Knotens.
     * 
     * @return Das ist die X-Koordinate des Knotens.
     */
    public int getX() {
        return x;
    }

    /**
     * Das ist der Getter fuer die Y-Koordinate des Knotens.
     * 
     * @return Das ist die Y-Koordinate des Knotens.
     */
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

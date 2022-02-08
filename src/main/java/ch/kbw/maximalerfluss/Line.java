package ch.kbw.maximalerfluss;

/**
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 */
public class Line {
    private Point p1, p2;
    private int weight;

    public Line(Point p1, Point p2, int weight) {
        this.p1 = p1;
        this.p2 = p2;
        this.weight = weight;
    }

    public Line() {
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

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

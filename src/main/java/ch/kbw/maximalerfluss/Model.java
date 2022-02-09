package ch.kbw.maximalerfluss;

/**
 * Das ist der Modelklasse fuer dieses Projekt.
 * 
 * @author Alex Schaub
 */
public class Model {
	private Graph graph;
	
	/**
	 * Das ist der Standardkonstruktor.
	 */
	public Model() {
		this.graph = new Graph();
	}
	
	/**
	 * Das ist ein Konstruktor.
	 * 
	 * @param curve_angle Dieser Variable bestimmt, wie Spitz die Pfeilspitzen sein werden.
	 */
	public Model(int curve_angle) {
		this.graph = new Graph(curve_angle);
	}

	/**
	 * Das ist der Getter fuer den Graphen.
	 * 
	 * @return Das ist der Graph, welcher zurueckgegeben wird.
	 */
	public Graph getGraph() {
		return graph;
	}
}

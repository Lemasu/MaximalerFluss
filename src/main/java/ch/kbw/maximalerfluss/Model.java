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
	 * Das ist der Getter fuer den Graphen.
	 * 
	 * @return Das ist der Graph, welcher zurueckgegeben wird.
	 */
	public Graph getGraph() {
		return graph;
	}
}

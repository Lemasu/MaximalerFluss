package ch.kbw.maximalerfluss;

/**
 * Das ist der Modelklasse fuer dieses Projekt.
 * 
 * @author Alex Schaub, Marc Schwendemann, Aron Gassner
 */
public class Model {
	private Graph graph;
	
	/**
	 * Das ist der Standardkonstruktor.
	 */
	public Model() {
		this.graph = new Graph();
	}

	public Graph getGraph() {
		return graph;
	}
}

package ch.kbw.maximalerfluss.gui;

import com.brunomnsilva.smartgraph.graph.Vertex;

import ch.kbw.maximalerfluss.Knoten;

/**
 * Diese Thread ueberprueft, ob der graphView vom Controller bereits angezeigt wird.
 * Sobald der graphView angezeigt wird, wird er von diesem Thread initialisiert.
 */
public class GraphViewInitThread extends Thread {
	/**
	 * Das ist der Controller der Applikation.
	 */
	private Controller controller;

	/**
	 * Das ist der Standardkonstruktor.
	 * 
	 * @param controller Das ist der Controller der Applikation.
	 */
	public GraphViewInitThread(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Diese Methode wartet, bis graphView angezeigt wird.
	 * Sobald graphView angezeigt wird, wird dieser initialisiert und der Graph ausgegeben.
	 * 
	 * Teile dieses Codes wurden von <a href="https://github.com/brunomnsilva/JavaFXSmartGraph">GitHub</a> übernommen und angepasst. Abfragedatum 02.05.2022.
	 * <a href="https://github.com/brunomnsilva">brunomsilva</a> Jahr 2021.
	 */
	@Override
	public void run() {
		// warte, bis der SmartGraphPanel eine Groesse hat
		while (controller.graphView.getWidth() == 0 || controller.graphView.getHeight() == 0) {
			try {
				/*
				 * Es soll fuere fuer 100 Millisekunden gewartet werden, damit die Applikation
				 * nicht permanent ueberprueft, ob der SmartGraphPanel eine Groesse hat.
				 */
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// breche diesen Thread ab, wenn dieser interrupted wird
				return;
			}
		}

		/*
		 * Initialisiere den SmartGraphPanel, damit die Labels von diesem spaeter
		 * aktualisiert werden koennen.
		 */
		controller.graphView.init();

		// platziere alle Knoten an die vorhin berechneten Koordinaten
		for (int i = 0; i < controller.nodes.size(); i++) {
			controller.graphView.setVertexPosition(controller.nodes.get(i), controller.x_positionen.get(i),
					controller.y_positionen.get(i));
		}

		// platziere alle Knoten an die vorhin berechneten Koordinaten
		for (int i = 0; i < controller.nodes.size(); i++) {
			// hole den aktuellen Knoten
			Vertex<Knoten> node = controller.nodes.get(i);

			// platziere den aktuellen Knoten
			controller.graphView.setVertexPosition(node, controller.x_positionen.get(i),
					controller.y_positionen.get(i));
		}

		// Den Fluss zurücksetzen und den Algorithmus neu initailisieren
		controller.resetFlow();
	}
}

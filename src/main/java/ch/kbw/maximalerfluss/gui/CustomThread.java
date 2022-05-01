package ch.kbw.maximalerfluss.gui;

import javafx.application.Platform;

/**
 * Dieser Thread passt den Label fuer die Angabe der Minimal- und Maxmialanzahl 
 * an Kanten an.
 */
public class CustomThread extends Thread {
	/**
	 * Das ist der Controller der Applikation.
	 */
	private Controller controller;

	/**
	 * Das ist der Standardkonstruktor.
	 * 
	 * @param controller Das ist der Controller der Applikation.
	 */
	public CustomThread(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		// wiederhole, solange dieser Thread nicht interrupted wird
		while (true) {
			try {
				// initialisieren der minimalen und maximalen Anzahl der Kanten, aufgrund von was gerade im Textfeld anzahl_zeilen
				// und anzahl_spalten steht.
				int min = (Integer.parseInt(controller.getAnzahl_zeilen().getText()) * Integer.parseInt(controller.getAnzahl_spalten().getText()) - 1);
				int max = (Integer.parseInt(controller.getAnzahl_zeilen().getText()) * Integer.parseInt(controller.getAnzahl_spalten().getText())) * (Integer.parseInt(controller.getAnzahl_zeilen().getText()) * Integer.parseInt(controller.getAnzahl_spalten().getText()) - 1);
				// aktualisieren des Textes des Labels info_kanten
				Platform.runLater(() -> controller.getInfo_kanten().setText("Anzahl Kanten (min = " + min + ", max = " + max + "):"));
			} catch (NullPointerException | NumberFormatException e) {
				/*
				 * Es gibt hier nichts, was gemacht werden muss.
				 * Die Labels sollen sich gemaess den Eingaben des Nutzers abaendern, dadurch tauchen diese Exceptions sehr oft auf.
				 * 
				 * Diese Exceptions sollen einfach abgefangen werden.
				 * 
				 * Es sollen nicht dauernd Fehlermeldungen ausgegeben werden.
				 */
			}
			
			try {
				/*
				 * Es soll fuere fuer 100 Millisekunden gewartet werden, 
				 * damit die Applikation nicht permanent am Label aktualisieren ist.
				 */
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// breche diesen Thread ab, wenn dieser interrupted wird
				return;
			}
		}
	}
}

package ch.kbw.maximalerfluss;

import javafx.application.Platform;

public class CustomThread extends Thread {
	private Controller controller;

	public CustomThread(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		boolean run = true;
		
		while (run) {
			try {
				// initialisieren der minimalen und maximalen Anzahl der Kanten, aufgrund von was gerade im Textfeld anzahl_zeilen
				// und anzahl_spalten steht.
				int min = (Integer.parseInt(controller.getAnzahl_zeilen().getText()) * Integer.parseInt(controller.getAnzahl_spalten().getText()) - 1);
				int max = (Integer.parseInt(controller.getAnzahl_zeilen().getText()) * Integer.parseInt(controller.getAnzahl_spalten().getText())) * (Integer.parseInt(controller.getAnzahl_zeilen().getText()) * Integer.parseInt(controller.getAnzahl_spalten().getText()) - 1);
				// aktualisieren des Textes des Labels info_kanten
				Platform.runLater(() -> controller.getInfo_kanten().setText("Anzahl Kanten (min = " + min + ", max = " + max + "):"));
				Thread.sleep(100);
			} catch (InterruptedException e) {
				return;
			} catch (NullPointerException | NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}
}

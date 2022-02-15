package ch.kbw.maximalerfluss;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * Das ist der Controller der Applikation.
 * 
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 * 
 * @author Alex Schaub
 */
public class Controller {
	/**
	 * Das ist das Model dieses Controllers.
	 */
	private Model model;
	
    /**
     * Das ist der Canvas, auf dem der Graph dargestellt werden soll.
     */
    @FXML
    private Canvas canvas;
	
	/**
	 * Das ist der GraphicsContext fuer den Canvas. Dieser wird verwendet, um auf dem Canvas zeichnen zu koennen.
	 */
    private GraphicsContext gc;
        
    /**
     * Das ist der Textfeld fuer die Anzahl der waagerechten Ebenen im Graphen, welcher generiert werden soll.
     */
    @FXML
    private TextField ebenen_waagerecht;
    
    /**
     * Das ist der Textfeld fuer die Anzahl der senkrechten Ebenen im Graphen, welcher generiert werden soll.
     */
    @FXML
    private TextField ebenen_senkrecht;
    
    /**
     * Dieses Label dient dazu, dem Nutzer Informationen zu uebermitteln.
     */
    @FXML
    private Label info;

    /**
     * Diese Methode wird bei der Initalisierung dieses Controllers aufgerufen.
     */
    @FXML
    public void initialize() {
        // convert canvas from fxml file to GraphicsContext
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
    }
    
    /**
     * Mit dieser Methode wird das Model fuer diesem Controller gesetzt.
     * 
     * @param model Das ist das Model dieses Controllers.
     */
    public void setModel(Model model) {
    	this.model = model;
    }

    /**
     * Mit dieser Methode wird ein neuer Graph generiert und ausgegeben.
     */
    @FXML
    public void generate() {
    	// Clears canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        try {           
        	// generiert den Graphen
        	model.getGraph().graphGenerieren(Integer.parseInt(ebenen_waagerecht.getText()), Integer.parseInt(ebenen_senkrecht.getText()), gc);
        	
            // Show everything
            model.getGraph().showGraph(gc);
        } catch(NumberFormatException e) {
        	info.setText("Bitte geben Sie nur Zahlen ein.");
        }
    }
}
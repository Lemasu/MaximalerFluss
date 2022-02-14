package ch.kbw.maximalerfluss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * Diese Klasse kuemmert sich um den Graphen.
 * 
 * Es kann Graphen generieren, aber auch zeichnen.
 * 
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 * 
 * @author Alex Schaub
 */
public class Graph {
	/**
	 * Dieser Variable wird fuer die Generierung und Darstellung des Graphen verwendet.
	 */
	private int r;
	
    /**
     * Dieser Variable bestimmt, wie Spitz die Pfeilspitzen sein werden.
     * 
     * Bigger = smaller angle
     */
    private int curveAngle;
    
    /**
     * Das sind die Knoten im Graphen.
     */
    private Knoten[] knoten;
    
    /**
     * Das sind die Kanten im Graphen.
     */
    private Kante[] kanten;
    
    /**
     * Das ist der Adjazenzmatrix vom Graphen.
     */
    private byte[][] adjazenmatrix;
    	
    /**
     * Das ist der Standardkonstruktor.
     */
    public Graph() {
    	
    }
    
    /**
     * Das ist der Konstruktor mit dem Parameter fuer das curveAngle.
     * 
     * @param curveAngle Dieser Variable bestimmt, wie Spitz die Pfeilspitzen sein werden.
     */
	public Graph(int curveAngle) {
		this.curveAngle = curveAngle;
	}
	
	/**
	 * Diese Funktion ist fuer die Generierung des Graphen gedacht.
	 * 
	 * @param ebenen_waagerecht Das ist der Textfeld fuer die Anzahl der waagerechten Ebenen im Graphen, welcher generiert werden soll.
	 * @param ebenen_senkrecht Das ist der Textfeld fuer die Anzahl der senkrechten Ebenen im Graphen, welcher generiert werden soll.
	 * @param gc Das ist der GraphicsContext fuer den Canvas. Dieser wird verwendet, um auf dem Canvas zeichnen zu koennen.
	 */
	public void graphGenerieren(TextField ebenen_waagerecht, TextField ebenen_senkrecht, GraphicsContext gc) {
		// Zufallszahl fuer die Generierung
    	final Random rand = new Random();

        // Initialize variables
        this.r = 5;
        int d = r << 1;
        int num_knoten = Integer.parseInt(ebenen_waagerecht.getText());
        int num_kanten = Integer.parseInt(ebenen_senkrecht.getText());
        adjazenmatrix = new byte[num_knoten][num_knoten];
        byte[][] adjazenmatrixPossible = new byte[num_knoten][num_knoten];
        int max_kanten = num_knoten * (num_knoten - 1);
        List<Integer[]> knoten_in_matrix = new ArrayList<>();
        boolean e;
        knoten = new Knoten[num_knoten];

        // Iterate through amount of knoten we are going to create
        for (int x = 0; x < num_knoten; x++) {
            e = false;

            // Generate a random knoten
            knoten[x] = new Knoten(rand.nextInt(490), rand.nextInt(490));

            //Adjazenmatrix aufstellen und auf 0 setzten
            for (int i = 0; i < num_knoten; i++) {
                adjazenmatrixPossible[x][i] = 0;
                adjazenmatrix[x][i] = 0;
            }

            // draw knoten with an offset so not to stack them
            x = knotenOffset(gc, d, e, knoten, x);
        }

        // If the amount of kanten exceeds the maximum possible kanten set the max as value
        if (max_kanten < num_kanten) {
            ebenen_senkrecht.setText(Integer.toString(max_kanten));
            num_kanten = max_kanten;
        }

        // Iterate through every knoten 2 times and create an array with all possible kanten
        Kante[] possible_kanten = new Kante[max_kanten];
        int countL = 0;
        for (int x = 0; x < num_knoten; x++) {
            for (int y = x + 1; y < num_knoten; y++) {
                possible_kanten[countL++] = new Kante(knoten[x], knoten[y], rand.nextInt(11) - 5);
                //kantegeneration in Adjezenmatrix auffüllen
                adjazenmatrixPossible[x][y] += 1;
                Integer[] matrix_knoten = new Integer[]{x, y};
                knoten_in_matrix.add(matrix_knoten);

                possible_kanten[countL++] = new Kante(knoten[y], knoten[x], rand.nextInt(11) - 5);
                adjazenmatrixPossible[y][x] += 1;
                matrix_knoten = new Integer[]{y, x};
                knoten_in_matrix.add(matrix_knoten);
            }
        }

        // If the amount of kanten matches the max amount of kanten just copy the array
        if (num_kanten == max_kanten) {
            kanten = possible_kanten;
            adjazenmatrix = adjazenmatrixPossible;
        } else {
            // Set the first kante random
            kanten = new Kante[num_kanten];
            int randL = rand.nextInt(num_kanten);
            kanten[0] = possible_kanten[randL];
            possible_kanten[randL] = null;
            ArrayList<Kante> tmp = new ArrayList<>();
            // Iterate through every kante
            for (int i = 1; i < num_kanten; i++) {
                // Iterate through every possible kante
                for (int j = 0; j < max_kanten; j++) {
                    // Iterate through the already Iterated possible kanten again
                    for (int r = 0; r < i; r++) {
                        // If the possible kante wasn't used already
                        if (possible_kanten[j] != null) {
                            // If the kante connects with any other kante generated so far
                            if (possible_kanten[j].getP1() == kanten[r].getP1() || possible_kanten[j].getP2() == kanten[r].getP1() || possible_kanten[j].getP1() == kanten[r].getP2() || possible_kanten[j].getP2() == kanten[r].getP2()) {
                                // Add kante to next possible kanten
                                tmp.add(possible_kanten[j]);
                            }
                        }
                    }
                }
                // Get a random kante from the next possible kanten
                int tmprand = rand.nextInt(tmp.size());
                kanten[i] = tmp.get(tmprand);
                // Iterate through all possible kanten
                for (int j = 0; j < max_kanten; j++) {
                    // If possible kante equals the random chosen one
                    if (possible_kanten[j] == tmp.get(tmprand)) {
                        // Set chosen kante in possible kanten to null (as in used)
                        possible_kanten[j] = null;
                    }
                }
            }
        }

        adjazenmatrix = kantenToAdjazenmatrix(kanten, num_knoten, knoten);
	}
	
    /**
     * function to create adjazenmatrix from Array of kanten and aarray of knoten
     * 
     * @param kanten Das sind die Kanten im Graphen.
     * @param amount_knoten Das ist die Anzahl der Punkte im Graphen.
     * @param knoten Das sind die Knoten im Graphen.
     * @return Es wird die Adjazenzmatrix zurueckgegeben.
     */
    private byte[][] kantenToAdjazenmatrix(Kante[] kanten, int amount_knoten, Knoten[] knoten) {
        //ADD if kanten = null
        byte[][] aMatrix = new byte[amount_knoten][amount_knoten];
        //for loops (i and j) are to go through the whole adjazenmatrix
        for (int i = 0; i < amount_knoten; i++) {
            for (int j = 0; j < amount_knoten; j++) {
                // for loop to go through kanten array
                for (Kante l : kanten) {
                    //Stringcomparation to get the value of adjzenmatrix
                    if (l.getP1().toString().equals(knoten[i].toString()) &&
                            l.getP2().toString().equals(knoten[j].toString())) {
                        aMatrix[i][j] = 1;
                    }
                }
            }
        }
        return aMatrix;
    }

    /**
     * Diese Methode wird fuer die versetzten Punkte benoetigt.
     * 
     * @param gc Das ist der GraphicsContext fuer den Canvas. Dieser wird verwendet, um auf dem Canvas zeichnen zu koennen.
     * @param d Das ist ein Integer.
     * @param e Das ist ein Boolean.
     * @param knoten Das sind die Knoten im Graphen.
     * @param x Das ist ein Integer.
     * @return Es wird ein Integer zurueckgegeben.
     */
    private int knotenOffset(GraphicsContext gc, int d, boolean e, Knoten[] knoten, int x) {
        // Iterate though knoten
        for (int y = 0; y < x; y++) {
            // If knoten are stacked
            if (knoten[y].getX() - d < knoten[x].getX() && knoten[x].getX() < knoten[y].getX() + d && knoten[y].getY() - d < knoten[x].getY() && knoten[x].getY() < knoten[y].getY() + d) {
                // repeat iteration
                x--;
                // e for exists
                e = true;
                // break from loop as its no longer needed
                break;
            }
        }
        // If knoten exists return
        if (e) return x;
        // draw knoten
        gc.fillOval(knoten[x].getX(), knoten[x].getY(), d, d);
        return x;
    }
    
    /**
     * Diese Methode zeichnet den Graphen auf den Canvas.
     * 
     * @param gc Das ist der GraphicsContext fuer den Canvas. Dieser wird verwendet, um auf dem Canvas zeichnen zu koennen.
     */
    public void showGraph(GraphicsContext gc) {

        // set color of strokes in canvas
        gc.setStroke(Color.BLACK);
        for (int i = 0; i < kanten.length; i++) {
            double a = Math.atan2(
                    kanten[i].getP1().getY() - kanten[i].getP2().getY(),
                    kanten[i].getP1().getX() - kanten[i].getP2().getX()
            );
            double a1 = a + Math.PI / curveAngle;
            double a2 = a - Math.PI / curveAngle;
            //length of pointing kanten
            int l = 20;
            
            // draw normal arrows from knoten to knoten
            gc.strokeLine(
                    kanten[i].getP1().getX() + r,
                    kanten[i].getP1().getY() + r,
                    kanten[i].getP2().getX() + r,
                    kanten[i].getP2().getY() + r
            );
            gc.strokeLine(
                    kanten[i].getP2().getX() + r,
                    kanten[i].getP2().getY() + r,
                    Math.cos(a1) * l + kanten[i].getP2().getX() + r,
                    Math.sin(a1) * l + kanten[i].getP2().getY() + r
            );
            gc.strokeLine(
                    kanten[i].getP2().getX() + r,
                    kanten[i].getP2().getY() + r,
                    Math.cos(a2) * l + kanten[i].getP2().getX() + r,
                    Math.sin(a2) * l + kanten[i].getP2().getY() + r
            );
        }
    }
}

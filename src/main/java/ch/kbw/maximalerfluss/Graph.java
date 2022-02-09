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
    private Point[] points;
    
    /**
     * Das sind die Kanten im Graphen.
     */
    private Line[] lines;
    
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
	 * @param pAmount Das ist der Textfeld fuer die Anzahl der Knoten im Graphen, welcher generiert werden soll.
	 * @param lAmount Das ist der Textfeld fuer die Anzahl der Kanten im Graphen, welcher generiert werden soll.
	 * @param gc Das ist der GraphicsContext fuer den Canvas. Dieser wird verwendet, um auf dem Canvas zeichnen zu koennen.
	 */
	public void graphGenerieren(TextField pAmount, TextField lAmount, GraphicsContext gc) {
		// Zufallszahl fuer die Generierung
    	final Random rand = new Random();

        // Initialize variables
        this.r = 5;
        int d = r << 1;
        int numPoints = Integer.parseInt(pAmount.getText());
        int numLines = Integer.parseInt(lAmount.getText());
        adjazenmatrix = new byte[numPoints][numPoints];
        byte[][] adjazenmatrixPossible = new byte[numPoints][numPoints];
        int maxLines = numPoints * (numPoints - 1);
        List<Integer[]> pointsInMatrix = new ArrayList<>();
        boolean e;
        points = new Point[numPoints];

        // Iterate through amount of points we are going to create
        for (int x = 0; x < numPoints; x++) {
            e = false;

            // Generate a random point
            points[x] = new Point(rand.nextInt(490), rand.nextInt(490));

            //Adjazenmatrix aufstellen und auf 0 setzten
            for (int i = 0; i < numPoints; i++) {
                adjazenmatrixPossible[x][i] = 0;
                adjazenmatrix[x][i] = 0;
            }

            // draw points with an offset so not to stack them
            x = pointOffset(gc, d, e, points, x);
        }

        // If the amount of lines exceeds the maximum possible lines set the max as value
        if (maxLines < numLines) {
            lAmount.setText(Integer.toString(maxLines));
            numLines = maxLines;
        }

        // Iterate through every point 2 times and create an array with all possible lines
        Line[] possibleLines = new Line[maxLines];
        int countL = 0;
        for (int x = 0; x < numPoints; x++) {
            for (int y = x + 1; y < numPoints; y++) {
                possibleLines[countL++] = new Line(points[x], points[y], rand.nextInt(11) - 5);
                //Linegenaration in Adjezenmatrix auffüllen
                adjazenmatrixPossible[x][y] += 1;
                Integer[] matrixPoints = new Integer[]{x, y};
                pointsInMatrix.add(matrixPoints);

                possibleLines[countL++] = new Line(points[y], points[x], rand.nextInt(11) - 5);
                adjazenmatrixPossible[y][x] += 1;
                matrixPoints = new Integer[]{y, x};
                pointsInMatrix.add(matrixPoints);
            }
        }

        // If the amount of lines matches the max amount of lines just copy the array
        if (numLines == maxLines) {
            lines = possibleLines;
            adjazenmatrix = adjazenmatrixPossible;
        } else {
            // Set the first line random
            lines = new Line[numLines];
            int randL = rand.nextInt(numLines);
            lines[0] = possibleLines[randL];
            possibleLines[randL] = null;
            ArrayList<Line> tmp = new ArrayList<>();
            // Iterate through every line
            for (int i = 1; i < numLines; i++) {
                // Iterate through every possible line
                for (int j = 0; j < maxLines; j++) {
                    // Iterate through the already Iterated possible lines again
                    for (int r = 0; r < i; r++) {
                        // If the possible line wasn't used already
                        if (possibleLines[j] != null) {
                            // If the line connects with any other line generated so far
                            if (possibleLines[j].getP1() == lines[r].getP1() || possibleLines[j].getP2() == lines[r].getP1() || possibleLines[j].getP1() == lines[r].getP2() || possibleLines[j].getP2() == lines[r].getP2()) {
                                // Add line to next possible lines
                                tmp.add(possibleLines[j]);
                            }
                        }
                    }
                }
                // Get a random line from the next possible lines
                int tmprand = rand.nextInt(tmp.size());
                lines[i] = tmp.get(tmprand);
                // Iterate through all possible lines
                for (int j = 0; j < maxLines; j++) {
                    // If possible line equals the random chosen one
                    if (possibleLines[j] == tmp.get(tmprand)) {
                        // Set chosen line in possible lines to null (as in used)
                        possibleLines[j] = null;
                    }
                }
            }
        }

        adjazenmatrix = linesToAdjazenmatrix(lines, numPoints, points);
	}
	
    /**
     * function to create adjazenmatrix from Array of lines and aarray of points
     * 
     * @param lines Das sind die Kanten im Graphen.
     * @param amountPoints Das ist die Anzahl der Punkte im Graphen.
     * @param points Das sind die Knoten im Graphen.
     * @return Es wird die Adjazenzmatrix zurueckgegeben.
     */
    private byte[][] linesToAdjazenmatrix(Line[] lines, int amountPoints, Point[] points) {
        //ADD if lines = null
        byte[][] aMatrix = new byte[amountPoints][amountPoints];
        //for loops (i and j) are to go through the whole adjazenmatrix
        for (int i = 0; i < amountPoints; i++) {
            for (int j = 0; j < amountPoints; j++) {
                // for loop to go through lines array
                for (Line l : lines) {
                    //Stringcomparation to get the value of adjzenmatrix
                    if (l.getP1().toString().equals(points[i].toString()) &&
                            l.getP2().toString().equals(points[j].toString())) {
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
     * @param points Das sind die Knoten im Graphen.
     * @param x Das ist ein Integer.
     * @return Es wird ein Integer zurueckgegeben.
     */
    private int pointOffset(GraphicsContext gc, int d, boolean e, Point[] points, int x) {
        // Iterate though points
        for (int y = 0; y < x; y++) {
            // If points are stacked
            if (points[y].getX() - d < points[x].getX() && points[x].getX() < points[y].getX() + d && points[y].getY() - d < points[x].getY() && points[x].getY() < points[y].getY() + d) {
                // repeat iteration
                x--;
                // e for exists
                e = true;
                // break from loop as its no longer needed
                break;
            }
        }
        // If point exists return
        if (e) return x;
        // draw point
        gc.fillOval(points[x].getX(), points[x].getY(), d, d);
        return x;
    }
    
    /**
     * Diese Methode zeichnet den Graphen auf den Canvas.
     * 
     * @param gc Das ist der GraphicsContext fuer den Canvas. Dieser wird verwendet, um auf dem Canvas zeichnen zu koennen.
     */
    public void showLines(GraphicsContext gc) {

        // set color of strokes in canvas
        gc.setStroke(Color.BLACK);
        for (int i = 0; i < lines.length; i++) {
            double a = Math.atan2(
                    lines[i].getP1().getY() - lines[i].getP2().getY(),
                    lines[i].getP1().getX() - lines[i].getP2().getX()
            );
            double a1 = a + Math.PI / curveAngle;
            double a2 = a - Math.PI / curveAngle;
            //length of pointing lines
            int l = 20;
            
            // draw normal arrows from point to point
            gc.strokeLine(
                    lines[i].getP1().getX() + r,
                    lines[i].getP1().getY() + r,
                    lines[i].getP2().getX() + r,
                    lines[i].getP2().getY() + r
            );
            gc.strokeLine(
                    lines[i].getP2().getX() + r,
                    lines[i].getP2().getY() + r,
                    Math.cos(a1) * l + lines[i].getP2().getX() + r,
                    Math.sin(a1) * l + lines[i].getP2().getY() + r
            );
            gc.strokeLine(
                    lines[i].getP2().getX() + r,
                    lines[i].getP2().getY() + r,
                    Math.cos(a2) * l + lines[i].getP2().getX() + r,
                    Math.sin(a2) * l + lines[i].getP2().getY() + r
            );
        }
    }
}

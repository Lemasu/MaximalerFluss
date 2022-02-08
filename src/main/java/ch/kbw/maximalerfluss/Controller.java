package ch.kbw.maximalerfluss;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Das ist der Controller der Applikation.
 * 
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 * 
 * @author Alex Schaub
 */
public class Controller {
    private int r;
    private final Random rand = new Random();
    private GraphicsContext gc;
    private ArrayList<Line> stack;    
    @FXML
    private Canvas canvas;
    @FXML
    private TextField pAmount, lAmount;

    //Bigger = smaller angle
    int curveAngle = 8;

    @FXML
    public void initialize() {
        // convert canvas from fxml file to GraphicsContext
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
    }

    @FXML
    public void generate() {

        // create new stack every time we generate a new graph
        stack = new ArrayList<>();

        // Clears canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Initialize variables
        this.r = 5;
        int d = r << 1;
        int numPoints = Integer.parseInt(pAmount.getText());
        int numLines = Integer.parseInt(lAmount.getText());
        byte[][] adjazenmatrix = new byte[numPoints][numPoints];
        byte[][] adjazenmatrixPossible = new byte[numPoints][numPoints];
        int maxLines = numPoints * (numPoints - 1);
        List<Integer[]> pointsInMatrix = new ArrayList<>();
        boolean e;
        Point[] points = new Point[numPoints];

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
            x = pointOffset(d, e, points, x);
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
        Line[] lines;
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

        // Show everything
        showLines(lines);
    }

    //function to create adjazenmatrix from Array of lines and aarray of points
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

    private int pointOffset(int d, boolean e, Point[] points, int x) {
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

    public void showLines(Line[] lines) {

        // set color of strokes in canvas
        gc.setStroke(Color.BLACK);

        // Iterate through every line
        for (int i = 0; i < lines.length; i++) {
            double a = Math.atan2(
                    lines[i].getP1().getY() - lines[i].getP2().getY(),
                    lines[i].getP1().getX() - lines[i].getP2().getX()
            );
            double a1 = a + Math.PI / curveAngle;
            double a2 = a - Math.PI / curveAngle;
            //length of pointing lines
            int l = 20;

            // Iterate though every line again
            for (int j = 0; j < i; j++) {
                // Check if lines are stacking
                boolean areLinesOverlapping = lines[i].getP1() == lines[j].getP2() && lines[i].getP2() == lines[j].getP1() || lines[i].getP2() == lines[j].getP1() && lines[i].getP1() == lines[j].getP2();
                if (areLinesOverlapping) {
                    // Add overlapping line to stack
                    stack.add(lines[i]);
                    stack.add(lines[j]);
                    // Set stroke color to red
                    gc.setStroke(Color.RED);

                    // Get positions and lengths
                    int line1X = lines[i].getP1().getX() + r;
                    int line1Y = lines[i].getP1().getY() + r;
                    int line2X = lines[j].getP1().getX() + r;
                    int line2Y = lines[j].getP1().getY() + r;
                    float widthRadius = line1X - line2X;
                    float heightRadius = line1Y - line2Y;
                    if (widthRadius < 0) widthRadius *= -1;
                    if (heightRadius < 0) heightRadius *= -1;
                    // the smaller rounded square with should be chosen as corner radius
                    double radius = Math.min(widthRadius, heightRadius);
                    // calculate the diagonal of radius square
                    double diagonal = Math.sqrt(radius * radius + radius * radius);
                    // Get the diagonal offset for rounded square
                    double diagonalOffset = (diagonal - radius) / 2;
                    // get the sides from the diagonal offset
                    double offset = diagonalOffset / Math.sqrt(2);
                    // look for where to set start point of rounded square
                    // p1tr = Point 1 top-right etc.
                    boolean p1tr = line1X > line2X && line1Y < line2Y;
                    boolean p1tl = line1X < line2X && line1Y < line2Y;
                    boolean p1br = line1X > line2X && line1Y > line2Y;
                    if (p1tr) {
                        gc.strokeRoundRect(line2X - offset, line1Y - offset, widthRadius + offset * 2, heightRadius + offset * 2, radius, radius);
                    } else if (p1tl) {
                        gc.strokeRoundRect(line1X - offset, line1Y - offset, widthRadius + offset * 2, heightRadius + offset * 2, radius, radius);
                    } else if (p1br) {
                        gc.strokeRoundRect(line2X - offset, line2Y - offset, widthRadius + offset * 2, heightRadius + offset * 2, radius, radius);
                    } else {
                        gc.strokeRoundRect(line1X - offset, line2Y - offset, widthRadius + offset * 2, heightRadius + offset * 2, radius, radius);
                    }
                    // draw arrow heads for round square
                    gc.strokeLine(
                            lines[i].getP2().getX() + r,
                            lines[i].getP2().getY() + r,
                            Math.cos(a1 + Math.PI / 2) * l + lines[i].getP2().getX() + r,
                            Math.sin(a1 + Math.PI / 2) * l + lines[i].getP2().getY() + r
                    );
                    gc.strokeLine(
                            lines[i].getP2().getX() + r,
                            lines[i].getP2().getY() + r,
                            Math.cos(a2 + Math.PI / 2) * l + lines[i].getP2().getX() + r,
                            Math.sin(a2 + Math.PI / 2) * l + lines[i].getP2().getY() + r
                    );
                    gc.strokeLine(
                            lines[i].getP1().getX() + r,
                            lines[i].getP1().getY() + r,
                            Math.cos(a1 - Math.PI / 2) * l + lines[i].getP1().getX() + r,
                            Math.sin(a1 - Math.PI / 2) * l + lines[i].getP1().getY() + r
                    );
                    gc.strokeLine(
                            lines[i].getP1().getX() + r,
                            lines[i].getP1().getY() + r,
                            Math.cos(a2 - Math.PI / 2) * l + lines[i].getP1().getX() + r,
                            Math.sin(a2 - Math.PI / 2) * l + lines[i].getP1().getY() + r
                    );
                }
            }
        }
        for (int i = 0; i < lines.length; i++) {
            double a = Math.atan2(
                    lines[i].getP1().getY() - lines[i].getP2().getY(),
                    lines[i].getP1().getX() - lines[i].getP2().getX()
            );
            double a1 = a + Math.PI / curveAngle;
            double a2 = a - Math.PI / curveAngle;
            //length of pointing lines
            int l = 20;
            if (!stack.contains(lines[i])) {
                System.out.println("WHY?");
                // Set stroke color to black
                gc.setStroke(Color.BLACK);
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
        System.out.println("Stack" + stack.toString());
    }
}
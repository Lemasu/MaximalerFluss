package ch.kbw.maximalerfluss;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 */
public class Controller {
    private int r;
    private final Random rand = new Random();
    private GraphicsContext gc;
    private ArrayList<Line> stack;
    private byte[][] currentAdjazenmatrix = null;
    private Point[] currentPoints = null;
    @FXML
    private Canvas canvas;
    @FXML
    private TextField pAmount, lAmount, fileName;
    @FXML
    private Label outSave;

    @FXML
    public void initialize() {
        // convert canvas from fxml file to GraphicsContext
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
    }

    //Setter for currentAdjazenmtrix
    public void setCurrentAdjazenmatrix(byte[][] currentAdjazenmatrix) {
        this.currentAdjazenmatrix = currentAdjazenmatrix;
    }

    //Setter for currentPoints
    public void setCurrentPoints(Point[] currentPoints) {
        this.currentPoints = currentPoints;
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
        ImpExp impExp = new ImpExp();
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
        findCycles(lines);
        showAdjzenmatrix(adjazenmatrix);
        setCurrentAdjazenmatrix(adjazenmatrix);
        setCurrentPoints(points);
        /*try {
            impExp.createJSONFile(adjazenmatrix, points);
        } catch (IOException ex) {
            e30
        }*/
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

    //Bigger = smaller angle
    int curveAngle = 8;

    @FXML
    public void generateByFile() {

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //standard Variables (see generate() method for more information)
        // create new stack every time we generate a new graph
        stack = new ArrayList<>();
        this.r = 5;
        int d = r << 1;
        ImpExp impExp = new ImpExp();
        byte[][] adjazenmatrix = impExp.getAdjazenmatrix(fileName.getText());
        int numPoints = adjazenmatrix.length;

        int maxLines = linesByAdjazenmatrix(adjazenmatrix);


        Point[] points;
        //Iterate through amount of points we are going to create (see generate() method for more information)
        if (impExp.pointsAreSet(fileName.getText())) {
            points = impExp.getPoints(fileName.getText());
            for (int i = 0; i < numPoints; i++) {
                gc.fillOval(points[i].getX(), points[i].getY(), d, d);
            }
        } else {
            boolean e;
            points = new Point[numPoints];

            for (int x = 0; x < numPoints; x++) {
                e = false;
                points[x] = new Point(rand.nextInt(490), rand.nextInt(490));

                x = pointOffset(d, e, points, x);
            }
        }


        Line[] lines = new Line[maxLines];
        //go through all elements in line array
        // ctemp INteger is an temporaray counter for lines Array
        int ctemp = 0;
        //for loops are to go through whole adjazenmatrix
        for (int i = 0; i < numPoints; i++) {
            for (int j = 0; j < numPoints; j++) {
                //if vale of adjazenmatrix[i][j] is 1 then create line
                if (adjazenmatrix[i][j] == 1) {
                    lines[ctemp] = new Line(points[i], points[j], rand.nextInt(11) - 5);
                } else {
                    ctemp--;
                }
                ctemp++;
            }
        }
        // for showing lines
        showLines(lines);
        // for finding Cycles in console
        findCycles(lines);
        //for output Adjazenmatrix in console
        showAdjzenmatrix(adjazenmatrix);
        setCurrentAdjazenmatrix(adjazenmatrix);
        setCurrentPoints(points);

    }

    @FXML
    public void save() {
        //ImportExport class
        ImpExp impExp = new ImpExp();
        //if adjazenmatrix is not created then print message
        if (currentAdjazenmatrix == null) {
            System.out.println("There is no Adjazenmatrix available");
            outSave.setText("There is no Adjazenmatrix available   ");
        } else {
            //create file
            try {
                impExp.createJSONFile(currentAdjazenmatrix, currentPoints);
                outSave.setText(impExp.getFilename() + " was created!   ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                    //if (lines[i].getWeight() + lines[j].getWeight() < 0) {
                    gc.setStroke(Color.RED);
                    //} else {
                    //gc.setStroke(Color.GREEN);
                    //}

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

    //------------------------------------------------------------------------------------

    //lines that were checked for cycles
    ArrayList<Line> checkedLines = new ArrayList<>();
    ArrayList<Point> visitedPoints = new ArrayList<>();
    //reset with every cycle, lines in cycle
    ArrayList<Line> checkedLineInCycle = new ArrayList<>();

    public void findCycles(Line[] lines) {
        //lines should only be checked once, so they get removed once they did
        ArrayList<Line> linesLeftToCheck = new ArrayList<>(Arrays.asList(lines));
        for (Line line : lines) {
            //The ArrayList needs to be cleared. The sophisticated programmer probably knows why it has to be like that but I, at the time of writing this comment,
            //do not remember why it was necessary
            visitedPoints.clear();
            if (checkedLineInCycle.contains(line)) {
                //if this line was already checked, skip to the next iteration
                continue;
            }
            //Every recursion has its own ArrayList
            ArrayList<Point> pointsInThisBranch = new ArrayList<>();
            //starting at depth 1
            checkLine(linesLeftToCheck, line, 1, pointsInThisBranch);
        }
    }

    public void checkLine(ArrayList<Line> linesLeftToCheck, Line l, int depth, ArrayList<Point> pointsInThisBranch) {
        //Points get added to the ArrayList in the branch
        pointsInThisBranch.add(l.getP1());
        //If the destination point of the current line is included in the ArrayList we have gone full circle :) and found a cycle!
        if (visitedPoints.contains(l.getP2())) {
            int cycleSize = depth;
            for (int i = 0; i < pointsInThisBranch.size(); i++) {
                despacito:
                if (pointsInThisBranch.get(i) == l.getP2()) {
                    //the actual length of the cycle is the depth of the branch minus the index of the occurrence of the starting point
                    cycleSize = cycleSize - i;
                    //Starting at the starting point of the cycling and looping through the lines of the cycle
                    for (int j = i; j < cycleSize - 1 + i; j++) {
                        boolean noOverlap = false;
                        for (Line line : stack) {
                            // if lines aren't overlapping
                            if (!(line.getP1() == pointsInThisBranch.get(j) && line.getP2() == pointsInThisBranch.get(j + 1) || line.getP1() == pointsInThisBranch.get(j + 1) && line.getP2() == pointsInThisBranch.get(j))) {
                                noOverlap = true;
                            }
                            else {
                                break despacito;
                            }
                        }
                        if (stack.size() == 0) noOverlap = true;
                        if (noOverlap) {
                            //length of pointing lines
                            int length = 20;
                            gc.setStroke(Color.RED);
                            //LINEEEEEEEEES!!!!!!
                            gc.strokeLine(
                                    pointsInThisBranch.get(j).getX() + r,
                                    pointsInThisBranch.get(j).getY() + r,
                                    //ZT
                                    pointsInThisBranch.get(j + 1).getX() + r,
                                    pointsInThisBranch.get(j + 1).getY() + r
                            );

                            //get angles between the two Points
                            double a = Math.atan2(
                                    pointsInThisBranch.get(j).getY() - pointsInThisBranch.get(j + 1).getY(),
                                    pointsInThisBranch.get(j).getX() - pointsInThisBranch.get(j + 1).getX()
                            );
                            //We want arrow heads
                            //they have a length, and they start from the destination point of the line
                            //The difference between the two should, for example, be 90 degrees, 45 degrees in each direction, which is
                            double a1 = a + Math.PI / curveAngle;
                            double a2 = a - Math.PI / curveAngle;

                            //draw some lines
                            gc.strokeLine(
                                    pointsInThisBranch.get(j + 1).getX() + r,
                                    pointsInThisBranch.get(j + 1).getY() + r,
                                    Math.cos(a1) * length + pointsInThisBranch.get(j + 1).getX() + r,
                                    Math.sin(a1) * length + pointsInThisBranch.get(j + 1).getY() + r
                            );
                            //draw more lines
                            gc.strokeLine(
                                    pointsInThisBranch.get(j + 1).getX() + r,
                                    pointsInThisBranch.get(j + 1).getY() + r,
                                    Math.cos(a2) * length + pointsInThisBranch.get(j + 1).getX() + r,
                                    Math.sin(a2) * length + pointsInThisBranch.get(j + 1).getY() + r
                            );
                        }
                    }
                    //if the correct point was found and the line drawn, the function gets broken out off
                    break;
                }
            }
            //It's not part of the debugging - It's a feature!
            System.out.println("Found Cycle of length " + cycleSize);
            System.out.println("P1: " + l.getP1().toString() + " P2: " + l.getP2().toString());
            return;
        }
        //if no cycle was found get to the next step of recursion
        visitedPoints.add(l.getP1());
        for (Line nextLine : linesLeftToCheck) {
            if (nextLine.getP1() == l.getP2()) {
                //depth + 1 = new depth of the recursion
                checkLine(linesLeftToCheck, nextLine, depth + 1, (ArrayList<Point>) pointsInThisBranch.clone());
            }
        }
    }


    //-----------------------------------------------------------------------------------------
    //method to output adjazenmtrix in console
    void showAdjzenmatrix(byte[][] matrix) {
        //for loops to go through whole elements of adjazenmatrix
        for (byte[] bytes : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(bytes[j] + " ");
            }
            System.out.println();
        }
    }

    //method for counting lines
    private Integer linesByAdjazenmatrix(byte[][] adjzenmatrix) {
        int count = 0;
        for (byte[] bytes : adjzenmatrix) {
            for (int j = 0; j < adjzenmatrix.length; j++) {
                if (bytes[j] == 1) {
                    count++;
                }
            }
        }
        return count;
    }
}
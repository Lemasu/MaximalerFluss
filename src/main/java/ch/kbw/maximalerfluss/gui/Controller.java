package ch.kbw.maximalerfluss.gui;

import java.util.ArrayList;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import javafx.application.Platform;
import ch.kbw.maximalerfluss.Kante;
import ch.kbw.maximalerfluss.Knoten;
import ch.kbw.maximalerfluss.Model;
import ch.kbw.maximalerfluss.algorithmus.Algorithmus;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Das ist der Controller der Applikation.
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 *
 * @author Alex Schaub, Marc Schwendemann, Aron Gassner
 */
public class Controller {
    /**
     * Das ist das Model dieses Controllers.
     */
    Model model;

    /**
     * Das ist der Algorithmus dieses Controllers
     */
    Algorithmus algorithmus;

    /**
     * Das sind die Knoten des Graphen.
     */
    Knoten[][] knoten;

    /**
     * Das sind die Kanten des Graphen.
     */
    ArrayList<Kante> kanten;

    /**
     * Das ist die ID vom aktuellen Startknoten.
     *
     * Die ID wird als einen Int-Array abgespeichert, um damit spaeter einfacher auf
     * den Startknoten zugreifen zu koennen.
     */
    int[] id_startknoten;

    /**
     * Das ist die ID vom aktuellen Zielknoten.
     *
     * Die ID wird als einen Int-Array abgespeichert, um damit spaeter einfacher auf
     * den Startknoten zugreifen zu koennen.
     */
    int[] id_zielknoten;

    /**
     * Das ist das Panel, in dem der Graph mithilfe der Properties-Datei dargestellt wird.
     */
    SmartGraphPanel<Knoten, Kante> graphView;

    /**
     * Das ist der Graph.
     */
    Digraph<Knoten, Kante> graph;

    /**
     * Diese ArrayList speichert die Knoten des Graphen ab.
     */
    ArrayList<Vertex<Knoten>> nodes;

    /**
     * Das sind die X-Koordinaten der Knoten.
     */
    ArrayList<Double> x_positionen;

    /**
     * Das sind die Y-Koordinaten der Knoten.
     */
    ArrayList<Double> y_positionen;

    /**
     * Das ist die StackPane, in dem der Graph dargestellt wird.
     */
    @FXML
    StackPane stackpane;

    /**
     * Das ist das Textfeld, in welchem der Nutzer angeben kann, welcher Knoten als
     * Startknoten gesetzt werden soll.
     */
    @FXML
    TextField startknoten_setzen;

    /**
     * Das ist das Textfeld, in welchem der Nutzer angeben kann, welcher Knoten als
     * Zielknoten gesetzt werden soll.
     */
    @FXML
    TextField zielknoten_setzen;

    /**
     * Das ist das Textfeld fuer die Anzahl der Zeilen im Graphen, welcher generiert werden soll.
     */
    @FXML
    TextField anzahl_zeilen;

    /**
     * Das ist das Textfeld fuer die Anzahl der Spalten im Graphen, welcher generiert werden soll.
     */
    @FXML
    TextField anzahl_spalten;

    /**
     * Das ist das Textfeld fuer die Anzahl der Kanten im Graphen, welcher generiert werden soll.
     */
    @FXML
    TextField anzahl_kanten;

    /**
     * Das ist das Textfeld fuer die Anzahl der Spalten im Graphen, welcher generiert werden soll.
     */
    @FXML
    TextArea ausgabe;

    /**
     * Dieses Label dient dazu, dem Nutzer Informationen zu uebermitteln.
     */
    @FXML
    Label info;

    /**
     * Dieses Label dient dazu, dem Nutzer die Bedingungen der Anzahl Kanten zu uebermitteln.
     */
    @FXML
    Label info_kanten;

    /**
     * Mit dieser Methode wird das Model fuer diesem Controller gesetzt.
     *
     * @param model Das ist das Model dieses Controllers.
     */
    public void setModel(Model model) {
        this.model = model;
        algorithmus = new Algorithmus(model.getGraph());
    }

    /**
     * Mit dieser Methode kann diese Applikation beendet werden.
     *
     * Teile dieses Codes wurden von <a href="https://stackoverflow.com/a/42598179">Stack Overflow</a> übernommen und angepasst. Abfragedatum 02.05.2022.
     * <a href="https://stackoverflow.com/users/2189127/james-d">James_D</a> Jahr 2017.
     */
    @FXML
    public void applikationBeenden() {
        Platform.exit();
    }

    /**
     * Mit dieser Methode wird ein neuer Graph generiert und ausgegeben.
     *
     * Teile dieses Codes wurden von <a href="https://github.com/brunomnsilva/JavaFXSmartGraph">GitHub</a> übernommen und angepasst. Abfragedatum 02.05.2022.
     * <a href="https://github.com/brunomnsilva">brunomsilva</a> Jahr 2021.
     */
    @FXML
    public void generate() {
        // loesche die IDs von den alten Start- und Zielknoten
        id_startknoten = null;
        id_zielknoten = null;

        // initialisiere die Zeilen, Spalten und Kanten des Graphens
        int zeilen;
        int spalten;
        int kanten;

        // ändern der Textfarbe des Labels info
        info.setTextFill(Color.RED);

        // probiere die Zeilen und Spalten auf die Inhalte der Textfelder zu setzen
        try {
            zeilen = Integer.parseInt(anzahl_zeilen.getText());
            spalten = Integer.parseInt(anzahl_spalten.getText());
            kanten = Integer.parseInt(anzahl_kanten.getText());

            // ueberpruefe, ob in jedem Textfeld etwas eingegeben wurde
        } catch (NullPointerException e) {
            info.setText("Bitte geben Sie Zahlen ein.");
            return;

            // ueberpruefe, ob nur Zahlen eingegeben wurden
        } catch (NumberFormatException e) {
            info.setText("Bitte geben Sie nur Zahlen ein.");
            return;
        }

        /*
         * ueberpruefe, ob keine der folgenden Bedingungen erfuellt sind:
         * - Zeilen > 8
         * - Spalten > 8
         * - Zeilen = 1 und spalten = 1
         * - Zeilen < 1
         * - Spalten < 1
         */
        if (zeilen > 8 || spalten > 8 || (zeilen == 1 && spalten == 1) || zeilen < 1 || spalten < 1) {
            info.setText("Bitte achten Sie darauf, dass die Anzahl Zeilen und Spalten nicht grösser als 8 ist, dass entweder die Anzahl Zeilen oder die Anzahl Spalten grösser als 1 ist und, dass die Anzahl Zeilen und Spalten nicht kleiner als 1 ist.");
            return;
        }

        // initialisieren, der minimalen und maximalen Anzahl von Kanten
        int min = (zeilen * spalten - 1);
        int max = (zeilen * spalten) * (zeilen * spalten - 1);

        // ueberpruefen, ob die minimale und maximale Anzahl der Kanten eingehalten wurde
        if (Integer.parseInt(anzahl_kanten.getText()) < min || Integer.parseInt(anzahl_kanten.getText()) > max) {
            info.setText("Bitte geben Sie eine Anzahl Kanten von " + min + " bis " + max + " ein.");
            return;
        }

        // generiert den Graphen
        model.getGraph().graphGenerieren(zeilen, spalten, kanten);

        // Graph zeichnen
        graphZeichnen();

        GraphViewInitThread graphview_init = new GraphViewInitThread(this);
        graphview_init.start();
    }

    /**
     * Mit dieser Methode wird der Startknoten gesetzt. Falls es einen alten
     * Startknoten gibt, wird dieser zu einem normalen Knoten umgewandelt.
     * <p>
     * Der Nutzer kann im Textfeld die ID vom Knoten eingeben. Dieser wird dann als
     * neuer Startknoten gesetzt.
     * <p>
     * Teile dieses Codes wurden von <a href="https://github.com/brunomnsilva/JavaFXSmartGraph">GitHub</a> übernommen und angepasst. Abfragedatum 02.05.2022.
     * <a href="https://github.com/brunomnsilva">brunomsilva</a> Jahr 2021.
     */
    @FXML
    public void startknotenSetzen() {
        /*
         * Zuerst wird die ID, welche vom Nutzer eingegeben wurde aufgeteilt. Die beiden
         * Zahlen werden dann als einen Int-Array abgespeichert, damit spaeter auf
         * diesem zugegriffen werden kann.
         */
        String[] position_als_text = startknoten_setzen.getText().split("\\.");

        // leere das Infofeld
        info.setText("");

        // ändern der Textfarbe des Labels info
        info.setTextFill(Color.RED);

        /*
         * Falls die der Nutzer mehr als zwei Zahlenbloecke oder kein Text eingegeben
         * hatte oder als letztes Zeichen einen Punkt eingeben hatte, soll ein
         * entsprechender Fehlermeldung auftauchen und diese Methode abgebrochen werden.
         */
        if (startknoten_setzen.getText().length() <= 0 || position_als_text.length > 2
                || startknoten_setzen.getText().charAt(startknoten_setzen.getText().length() - 1) == '.') {
            info.setText("Bitte geben Sie die ID im folgenden Format ein: [Zeile].[Spalte]");
            return;
        }

        /*
         * Die ID soll vom String in Integer umgewandelt werden.
         *
         * Die beiden Teile der ID, welche fuer die Zeile und Spalte stehen, werden fuer die spaetere Verwendung separat abgespeichert.
         */
        int[] position = new int[2];
        try {
            position[0] = Integer.parseInt(position_als_text[0]);
            position[1] = Integer.parseInt(position_als_text[1]);
        } catch (NumberFormatException e) {
            /*
             * Falls es Probleme bei Parsing vom String in Int gibt, wird der Nutzer darauf
             * aufmerksam gemacht. Danach wird diese Methode abgebrochen.
             */
            info.setText(
                    "Bitte verwenden Sie nur Zahlen und den Punkt als Trennzeichen. Bitte verwenden Sie keine Leerschläge.");
            return;
        } catch (IndexOutOfBoundsException e) {
            /*
             * Falls nicht zwei Zahlenbloecke eingegeben wurde, wird der Nutzer darauf
             * aufmerksam gemacht. Danach wird diese Methode abgebrochen.
             */
            info.setText("Bitte geben Sie die ID im folgenden Format ein: [Zeile].[Spalte]");
            return;
        }

        // hole die Knoten vom Model
        knoten = model.getGraph().getKnoten();

        try {
            /*
             * setze die ID fuer den alten Zielknoten auf Null,
             * falls dieser jetzt ueberschrieben werden soll
             */
            if (knoten[position[0] - 1][position[1] - 1].getKategorie() == 2) {
                id_zielknoten = null;
            }

            // wandle den neuen Startknoten in einen Startknoten um
            knoten[position[0] - 1][position[1] - 1].setKategorie(0);
        } catch (IndexOutOfBoundsException e) {
            /*
             * Falls die Zahlen zu gross oder zu klein sind und dadurch kein passender
             * Knoten angesprochen werden kann, wird eine entsprechende Fehlermeldung
             * ausgegeben.
             *
             * Danach wird diese Methode abgebrochen.
             */
            info.setText("Die Zahl für die Zeile muss kleiner sein als " + (knoten.length + 1)
                    + ". Die Zahl für die Spalte muss kleiner sein als " + (knoten[0].length + 1)
                    + ". Ausserdem müssen beide Zahlen grösser als 0 sein.");
            return;
        } catch (NullPointerException e) {
            /*
             * Falls der Graph noch nicht generiert wurde, wird dem Nutzer mitgeteilt, dass
             * dieser zuerst den Graph generieren muss.
             *
             * Danach wird diese Methode abgebrochen.
             */
            info.setText("Sie müssen zuerst den Graphen generieren lassen.");
            return;
        }

        /*
         * Zuerst wird ueberprueft, ob bereits ein alter Startknoten abgespeichert
         * wurde.
         *
         * Falls ein alter Startknoten abgespeichert wurde, wird dieser in einen
         * normalen Knoten umgewandelt.
         *
         * Der alte Startknoten wird aber nur ueberschrieben, falls dieser nicht
         * bereits durch den Zielknoten ueberschrieben wurde.
         */
        if (id_startknoten != null && (id_startknoten[0] != position[0] || id_startknoten[1] != position[1]) && knoten[id_startknoten[0] - 1][id_startknoten[1] - 1].getKategorie() != 2) {
            knoten[id_startknoten[0] - 1][id_startknoten[1] - 1].setKategorie(1);
        }

        // setzte die ID des neuen Startknotens als ID vom neuen alten Startknoten
        id_startknoten = position;


        // Den Fluss zurücksetzen und den Algorithmus neu initailisieren
        resetFlow();

        // faerbe alle Knoten
        for (int i = 0; i < nodes.size(); i++) {
            // hole den aktuellen Knoten
            Vertex<Knoten> node = nodes.get(i);

            /*
             * ueberpruefe die Kategorie des aktuellen Knotens
             *
             * falls der aktueller Knoten die Kategorie Startknoten, also 0, besitzt, wird
             * der aktueller Knoten gruen gefaerbt
             *
             * falls der aktueller Knoten die Kategorie Zielknoten, also 1, besitzt, erhaelt
             * der aktueller Knoten die Standardfarbe
             *
             * falls der aktueller Knoten die Kategorie Zielknoten, also 2, besitzt, wird
             * der aktueller Knoten rot gefaerbt
             */
            if (node.element().getKategorie() == 0) {
                // faerbe den Knoten Gruen
                graphView.getStylableVertex(node).setStyle("-fx-fill: lightgreen; -fx-stroke: green;");
            } else if (node.element().getKategorie() == 2) {
                // faerbe den Knoten Rot
                graphView.getStylableVertex(node).setStyle("-fx-fill: pink; -fx-stroke: red;");
            } else {
                // setze keinen Style, dadurch erhaelt die Knoten die Standardfarbe
                graphView.getStylableVertex(node).setStyle("");
            }
        }
    }

    /**
     * Mit dieser Methode wird der Zielknoten gesetzt. Falls es einen alten
     * Zielknoten gibt, wird dieser zu einem normalen Knoten umgewandelt.
     *
     * Der Nutzer kann im Textfeld die ID vom Knoten eingeben. Dieser wird dann als
     * neuer Zielknoten gesetzt.
     *
     * Teile dieses Codes wurden von <a href="https://github.com/brunomnsilva/JavaFXSmartGraph">GitHub</a> übernommen und angepasst. Abfragedatum 02.05.2022.
     * <a href="https://github.com/brunomnsilva">brunomsilva</a> Jahr 2021.
     */
    @FXML
    public void zielknotenSetzen() {
        /*
         * Zuerst wird die ID, welche vom Nutzer eingegeben wurde aufgeteilt. Die beiden
         * Zahlen werden dann als einen Int-Array abgespeichert, damit spaeter auf
         * diesem zugegriffen werden kann.
         */
        String[] position_als_text = zielknoten_setzen.getText().split("\\.");

        // leere das Infofeld
        info.setText("");

        // ändern die Textfarbe des Labels info
        info.setTextFill(Color.RED);

        /*
         * Falls der Nutzer mehr als zwei Zahlenbloecke oder kein Text eingegeben
         * oder als letztes Zeichen einen Punkt eingeben hatte, soll eine
         * entsprechende Fehlermeldung auftauchen und diese Methode abgebrochen werden.
         */
        if (zielknoten_setzen.getText().length() <= 0 || position_als_text.length > 2
                || zielknoten_setzen.getText().charAt(zielknoten_setzen.getText().length() - 1) == '.') {
            info.setText("Bitte geben Sie die ID im folgenden Format ein: [Zeile].[Spalte]");
            return;
        }

        /*
         * Die ID soll vom String in Integer umgewandelt werden.
         *
         * Die beiden Teile der ID, welche fuer die Zeile und Spalte stehen, werden fuer die spaetere Verwendung separat abgespeichert.
         */
        int[] position = new int[2];
        try {
            position[0] = Integer.parseInt(position_als_text[0]);
            position[1] = Integer.parseInt(position_als_text[1]);
        } catch (NumberFormatException e) {
            /*
             * Falls es Probleme bei Parsing vom String in Int gibt, wird der Nutzer darauf
             * aufmerksam gemacht. Danach wird diese Methode abgebrochen.
             */
            info.setText(
                    "Bitte verwenden Sie nur Zahlen und den Punkt als Trennzeichen. Bitte verwenden Sie keine Leerschläge.");
            return;
        } catch (IndexOutOfBoundsException e) {
            /*
             * Falls nicht zwei Zahlenbloecke eingegeben wurden, wird der Nutzer darauf
             * aufmerksam gemacht. Danach wird diese Methode abgebrochen.
             */
            info.setText("Bitte geben Sie die ID im folgenden Format ein: [Zeile].[Spalte]");
            return;
        }

        // hole die Knoten vom Model
        knoten = model.getGraph().getKnoten();

        try {
            /*
             * setze die ID fuer den alten Startknoten auf Null,
             * falls dieser jetzt ueberschrieben werden soll
             */
            if (knoten[position[0] - 1][position[1] - 1].getKategorie() == 0) {
                id_startknoten = null;
            }

            // wandle den neuen Zielknoten in einen Zielknoten um
            knoten[position[0] - 1][position[1] - 1].setKategorie(2);
        } catch (IndexOutOfBoundsException e) {
            /*
             * Falls die Zahlen zu gross oder zu klein sind und dadurch kein passender
             * Knoten angesprochen werden kann, wird eine entsprechende Fehlermeldung
             * ausgegeben.
             *
             * Danach wird diese Methode abgebrochen.
             */
            info.setText("Die Zahl für die Zeile muss kleiner sein als " + (knoten.length + 1)
                    + ". Die Zahl für die Spalte muss kleiner sein als " + (knoten[0].length + 1)
                    + ". Ausserdem müssen beide Zahlen grösser als 0 sein.");
            return;
        } catch (NullPointerException e) {
            /*
             * Falls der Graph noch nicht generiert wurde, wird dem Nutzer mitgeteilt, dass
             * dieser zuerst den Graph generieren muss.
             *
             * Danach wird diese Methode abgebrochen.
             */
            info.setText("Sie müssen zuerst den Graphen generieren lassen.");
            return;
        }

        /*
         * Zuerst wird ueberprueft, ob bereits ein alter Zielknoten abgespeichert wurde.
         * Falls ein alter Zielknoten abgespeichert wurde, wird dieser in einen normalen
         * Knoten umgewandelt.
         *
         * Der alte Zielknoten wird aber nur ueberschrieben, falls dieser nicht bereits durch den Startknoten ueberschrieben wurde.
         */
        if (id_zielknoten != null && (id_zielknoten[0] != position[0] || id_zielknoten[1] != position[1]) && knoten[id_zielknoten[0] - 1][id_zielknoten[1] - 1].getKategorie() != 0) {
            knoten[id_zielknoten[0] - 1][id_zielknoten[1] - 1].setKategorie(1);
        }

        // ueberschreibe die ID des alten Zielknotens mit der ID des neuen Zielknotens
        id_zielknoten = position;

        // Fluss zurücksetzen und den Algorithmus neu initailisieren
        resetFlow();

        // faerbe alle Knoten
        for (int i = 0; i < nodes.size(); i++) {
            // hole den aktuellen Knoten
            Vertex<Knoten> node = nodes.get(i);

            /*
             * ueberpruefe die Kategorie des aktuellen Knotens
             *
             * falls der aktueller Knoten die Kategorie Startknoten, also 0, besitzt, wird
             * der aktueller Knoten gruen gefaerbt
             *
             * falls der aktueller Knoten die Kategorie Zielknoten, also 1, besitzt, erhaelt
             * der aktueller Knoten die Standardfarbe
             *
             * falls der aktueller Knoten die Kategorie Zielknoten, also 2, besitzt, wird
             * der aktueller Knoten rot gefaerbt
             */
            if (node.element().getKategorie() == 0) {
                // faerbe den Knoten gruen
                graphView.getStylableVertex(node).setStyle("-fx-fill: lightgreen; -fx-stroke: green;");
            } else if (node.element().getKategorie() == 2) {
                // faerbe den Knoten rot
                graphView.getStylableVertex(node).setStyle("-fx-fill: pink; -fx-stroke: red;");
            } else {
                // setze keinen Style, dadurch erhaelt die Knoten die Standardfarbe
                graphView.getStylableVertex(node).setStyle("");
            }
        }
    }

    /**
     * Diese Methode gibt den Graphen mithilfe von JavaFXSmartGraph in einem SubScene aus.
     *
     * Teile dieses Codes wurden von <a href="https://github.com/brunomnsilva/JavaFXSmartGraph">GitHub</a> übernommen und angepasst. Abfragedatum 02.05.2022.
     * <a href="https://github.com/brunomnsilva">brunomsilva</a> Jahr 2021.
     */
    void graphZeichnen() {
        // ändern der Textfarbe des Labels info
        info.setTextFill(Color.RED);

        // leere das Info-Feld, damit alte Informationen nicht dauernd zu sehen sind
        info.setText("");

        graph = new DigraphEdgeList<>();

        // alle Knoten holen
        Knoten[][] knoten = model.getGraph().getKnoten();

        // alle Kanten holen
        kanten = model.getGraph().getKanten();

        nodes = new ArrayList<Vertex<Knoten>>();

        // erstelle eine ArrayList, um die Kanten abzuspeichern, welche mit Hilfe von JavaFXSmartGraph ausgegeben werden.
        ArrayList<Edge<Kante, Knoten>> edges = new ArrayList<Edge<Kante, Knoten>>();

        // erstelle Variablen, um die Knoten in Matrixform zu platzieren.
        // ArrayList fuer die Position der Knoten
        x_positionen = new ArrayList<Double>();
        y_positionen = new ArrayList<Double>();
        // Veraenderung von X und Y in Relation zur Anzahl der Knoten
        double veraenderung_x = stackpane.getWidth() / (knoten[0].length + 1);
        double veraenderung_y = stackpane.getHeight() / (knoten.length + 1);
        /*
         * Position von einem Knoten
         *
         * Die Koordinate von einem Knoten haengt von der Anzahl Zeilen und Spalten ab.
         */
        double x = veraenderung_x;
        double y = veraenderung_y;

        // fuege jeden Knoten einzeln hinzu
        for (int i = 0; i < knoten.length; i++) {
            for (int j = 0; j < knoten[i].length; j++) {
                // erstelle den Knoten fuer die Ausgabe auf dem Graphen, der eigentliche Knoten wurde aber bereits erstellt
                nodes.add(graph.insertVertex(knoten[i][j]));

                // speichere die Koordinate des Knotens ab
                x_positionen.add(x);
                y_positionen.add(y);

                // passt die X-Koordinate dem naechsten Knoten an
                x += veraenderung_x;
            }

            // setze die X-Koordinate wieder auf X-Position fuer den ersten Knoten einer Zeile
            x = veraenderung_x;

            // passt die X-Koordinate der naechsten Zeile an
            y += veraenderung_y;
        }

        // fuege jede Kante einzeln hinzu
        for (int i = 0; i < kanten.size(); i++) {
            // erstelle den Knoten fuer die Ausgabe auf dem Graphen, der eigentliche Knoten wurde aber bereits erstellt
            System.out.println(kanten.get(i).getKnoten_1() + "" + kanten.get(i).getKnoten_2() + "");
            edges.add(graph.insertEdge(kanten.get(i).getKnoten_1(), kanten.get(i).getKnoten_2(), kanten.get(i)));
            //nodess.add(graph.insertEdge(kanten.get(i).getKnoten_1(), kanten.get(i).getKnoten_2(), kanten.get(i).getKapazitaet() + ""));
        }

        // lade die Properties-Datei
        SmartGraphProperties properties = new SmartGraphProperties();

        // erstelle den SmartGraphPanel, auf welchem der Graph angezeigt werden soll
        graphView = new SmartGraphPanel<>(graph, properties, new SmartCircularSortedPlacementStrategy());

        /*
         * erstelle den SmartGraphDemoContainer, auf welchem der SmartGraphPanel angezeigt
         * werden soll
         */
        SmartGraphDemoContainer graph_container = new SmartGraphDemoContainer(graphView);

        // gebe den Graphen auf einer SubScene aus
        SubScene subscene = new SubScene(graph_container, 700, 600);

        // ueberpruefe, ob es schon einen Graphen auf dem GUI gibt
        // loesche diesen Graphen, wenn es diesen schon gibt
        if (stackpane.getChildren().size() > 0) {
            stackpane.getChildren().remove(0);
        }

        // gebe den neuerstellten Graphen auf dem GUI aus
        stackpane.getChildren().add(subscene);

        /*
         * binde die Groesse des Subscenes, damit dieser sich an die Bildschirmgroesse des
         * Nutzers anpasst
         */
        subscene.heightProperty().bind(stackpane.heightProperty());
        subscene.widthProperty().bind(stackpane.widthProperty());

        // platziere alle Knoten an die vorhin berechneten Koordinaten
        for (int i = 0; i < nodes.size(); i++) {
            graphView.setVertexPosition(nodes.get(i), x_positionen.get(i), y_positionen.get(i));
        }

        // platziere alle Knoten an die vorhin berechneten Koordinaten
        for (int i = 0; i < nodes.size(); i++) {
            // hole den aktuellen Knoten
            Vertex<Knoten> node = nodes.get(i);

            // platziere den aktuellen Knoten
            graphView.setVertexPosition(node, x_positionen.get(i), y_positionen.get(i));
        }
    }

    /**
     * Diese Funktion berechnet den maximalen Fluss.
     * Führt den Algorithumus durch und zeigt die verwendeten Pfade für den maximalen Fluss
     *
     * Teile dieses Codes wurden von <a href="https://github.com/brunomnsilva/JavaFXSmartGraph">GitHub</a> übernommen
     * und angepasst. Abfragedatum 02.05.2022.<a href="https://github.com/brunomnsilva">brunomsilva</a> Jahr 2021.
     */
    @FXML
    public void berechnen() {
        if (id_startknoten != null && id_zielknoten != null) {
            if (!algorithmus.getFinished()) {
                algorithmus.berechneMaxFlow();

                kantenFaerben();

                // aktualisiere den Graphen, damit die Auslastungen der benutzten Kanten angezeigt werden
                graphView.update();

                // die Pfade ausgeben
                ausgabe.setText(algorithmus.getPfade());

                // Den maximalen Fluss ausgeben
                ausgabe.setText(ausgabe.getText() + "\nMaximaler Fluss: " + algorithmus.getMaxFlow());
            }
        } else {
            // teile den Nutzer mit, dass Start- und Zielknoten gesetzt sein muessen
            info.setText("Um den maximalen Fluss berechnen zu lassen, muss erst der Start- und Zielknoten gesetzt sein.");
        }
    }

    /**
     * Diese Funktion führt den nächsten Schirtt des Algorithmus durch.
     *
     * Teile dieses Codes wurden von <a href="https://github.com/brunomnsilva/JavaFXSmartGraph">GitHub</a> übernommen
     * und angepasst. Abfragedatum 02.05.2022.<a href="https://github.com/brunomnsilva">brunomsilva</a> Jahr 2021.
     */
    @FXML
    public void nextStep() {
        if (id_startknoten != null && id_zielknoten != null) {
            algorithmus.nextStep();

            kantenFaerben();

            // Pfad ausgeben
            ausgabe.setText(algorithmus.getPfade());

            algorithmus.nextPfad();

            // aktualisiere den Graphen, damit die Auslastungen der benutzten Kanten angezeigt werden
            graphView.update();

            if (algorithmus.isFinished()) {
                // Den maximalen Fluss ausgeben
                ausgabe.setText(ausgabe.getText() + "\nMaximaler Fluss: " + algorithmus.getMaxFlow());
            }
        } else {
            // teile den Nutzer mit, dass Start- und Zielknoten gesetzt sein muss
            info.setText("Um den maximalen Fluss berechnen zu lassen, muss erst der Start- und Zielknoten gesetzt sein.");
        }
    }

    /**
     * Diese Funktion setzt den Graphen zurück.
     *
     * Teile dieses Codes wurden von <a href="https://github.com/brunomnsilva/JavaFXSmartGraph">GitHub</a> übernommen
     * und angepasst. Abfragedatum 02.05.2022.<a href="https://github.com/brunomnsilva">brunomsilva</a> Jahr 2021.
     */
    @FXML
    public void resetFlow() {
        algorithmus.initialize();
        algorithmus.resetFlow();

        kantenFaerben();

        ausgabe.setText("");

        info.setText("");
    }

    /**
     * Diese Funktion faerbt die Kanten entsprechend.
     *
     * Teile dieses Codes wurden von <a href="https://github.com/brunomnsilva/JavaFXSmartGraph">GitHub</a> übernommen
     * und angepasst. Abfragedatum 02.05.2022.<a href="https://github.com/brunomnsilva">brunomsilva</a> Jahr 2021.
     */
    private void kantenFaerben() {
        for (int j = 0; j < kanten.size(); j++) {
            if (0 < kanten.get(j).getAuslastung()) {
                // falls die Kante benutzt wurde, nutze die Klasse "maxFluss"
                graphView.getStylableEdge(kanten.get(j)).setStyleClass("maxFluss");
            } else {
                // falls die Kante nicht benutzt wurde, nutze die Klasse "edge"
                graphView.getStylableEdge(kanten.get(j)).setStyleClass("edge");
            }
        }

        // aktualisiere den Graphen, damit die Auslastungen der benutzten Kanten angezeigt werden
        graphView.update();

        ausgabe.setText("");

        info.setText("");
    }

    public TextField getAnzahl_zeilen() {
        return anzahl_zeilen;
    }

    public TextField getAnzahl_spalten() {
        return anzahl_spalten;
    }

    public Label getInfo_kanten() {
        return info_kanten;
    }

}
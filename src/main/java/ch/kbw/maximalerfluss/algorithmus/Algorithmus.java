package ch.kbw.maximalerfluss.algorithmus;

import ch.kbw.maximalerfluss.Graph;
import ch.kbw.maximalerfluss.Kante;
import ch.kbw.maximalerfluss.Knoten;

import java.util.ArrayList;
import java.util.Random;

/**
 * Diese Klasse enthält den Algorithmus, der den maximalen Fluss berechnet.
 *
 * @author Marc Schwendemann
 */

public class Algorithmus {

    /**
     * Das ist der Graph für den der Algorithmus den maximalen Fluss berechnet.
     * Er bezieht sich auf den Graph aus dem Model.
     */
    private Graph graph;

    /**
     * Das ist die ArrayList von den Pfaden, die die Tiefensuche gefunden hat.
     * Enthält {@link #pfadKanten}.
     * Der Algorithmus wählt zufällig einen davon aus.
     */
    private ArrayList<ArrayList<Kante>> pfadKantenOptionen;

    /**
     * Das ist die ArrayList, die die Tiefensuche
     * verwendet, um einen Pfad von Knoten zu speichern.
     */
    private ArrayList<Knoten> pfadKnoten;

    /**
     * Das ist die ArrayList, die die Tiefensuche
     * verwendet, um einen Pfad von Kanten zu speichern.
     */
    private ArrayList<Kante> pfadKanten;

    /**
     * Das ist der Startknoten des Graphen.
     */
    private Knoten startKnoten;

    /**
     * Das ist der Zielknoten des Graphen.
     */
    private Knoten zielKnoten;

    /**
     * Das ist der Flaschenhals eines Pfades.
     * Er wird verwendet, um den Fluss des Graphen upzudaten.
     */
    private int bottleneckValue;

    /**
     * Das ist der maximale Fluss des Graphen, den der Algorithmus berechnet.
     */
    private int maxFlow;

    /**
     * Das ist der boolean, um zu checken, ob der Algorithmus fertig ist.
     */
    private boolean finished;

    /**
     * Das ist der String um die Pfade zu speichern
     */
    private String pfade;

    /**
     * Das ist der Standardkonstruktor.
     *
     * @param graphOriginal Das ist der Graph für den der maximale Fluss berechnet wird.
     */
    public Algorithmus(Graph graphOriginal) {
        this.graph = graphOriginal;
    }

    /**
     * Diese Funktion initialisiert alles neu in dieser Klasse
     */
    public void initialize() {
        pfadKantenOptionen = new ArrayList<ArrayList<Kante>>();
        pfadKnoten = new ArrayList<>();
        pfadKanten = new ArrayList<>();
        this.bottleneckValue=0;
        this.finished=false;
        this.maxFlow=0;
        this.pfade ="";
        startUndZielKnotenBestimmen();
    }

    /**
     * Diese Funktion berechnet den maximalen Fluss.
     */
    public void berechneMaxFlow() {
        // solange neue Pfade gefunden werden, soll ein nächster Schritt durchgeführt werden.
        while (!finished) {
            nextStep();
        }
    }

    /**
     * Diese Funktion führt den nächsten Schritt des Algorithmus aus
     */
    public void nextStep() {
        nextPfad();
        if (!finished) {
            berechneBottleneck();
            updateGraph();
            savePfad();
        }
    }

    /**
     * Diese Funktion führt eine Tiefensuche durch und wählt einen Pfad aus.
     */
    public void nextPfad() {
        // Inhalt aller ArrayListen löschen
        pfadKantenOptionen.clear();
        pfadKnoten.clear();
        pfadKanten.clear();

        // Tiefensuche durchführen
        tiefensuche(startKnoten);

        // kontrollieren, ob es noch Pfade gibt
        if (pfadKantenOptionen.size()>0) {

            // Rückkanten vor normalen Kanten verwenden

            // neue Liste um einen Error zu vermeiden
            ArrayList<ArrayList<Kante>> toRemove = new ArrayList<ArrayList<Kante>>();

            for (ArrayList<Kante> pfad1 : pfadKantenOptionen) {
                for (Kante kante1 : pfad1) {
                    for (ArrayList<Kante> pfad2 : pfadKantenOptionen) {
                        if (pfad1==pfad2) {
                            continue;
                        }
                        for (Kante kante2 : pfad2) {
                            if ((kante1.getKnoten_1()==kante2.getKnoten_1())&&(kante1.getKnoten_2()==kante2.getKnoten_2())&&kante1!=kante2) {
                                if (graph.getRueckKanten().contains(kante1)&&graph.getKanten().contains(kante2)) {
                                    toRemove.add(pfad2);
                                }
                                if (graph.getRueckKanten().contains(kante2)&&graph.getKanten().contains(kante1)) {
                                    toRemove.add(pfad1);
                                }
                            }
                        }
                    }
                }
            }
            // Die Pfade mit den normalen Kante(n) entfernen
            pfadKantenOptionen.removeAll(toRemove);

            // zufällig einen Pfad auswählen
            Random rn = new Random();
            int random = rn.nextInt(pfadKantenOptionen.size());
            pfadKanten = (ArrayList<Kante>) pfadKantenOptionen.get(random).clone();
        } else {
            finished = true;
        }
    }

    /**
     * Diese Funktion führt die Tiefensuche durch. Sie sucht mögliche Pfade
     * vom Start- zum Zielknoten und fügt sie {@link #pfadKantenOptionen} hinzu
     * @param knoten Das ist der Knoten, von wo aus die Tiefensuche gestartet wird.
     */
    private void tiefensuche(Knoten knoten) {
        // Der angegebene Knoten wird dem Pfad hinzugefügt
        pfadKnoten.add(knoten);

        // Wenn man beim Zielknoten angekommen ist, wird der Pfad gespeichert und die Funktion beendet
        if (knoten == zielKnoten) {
            pfadKantenOptionen.add((ArrayList<Kante>) pfadKanten.clone());
            return;
        }

        // Wenn es vom derzeitigen Knoten nicht weitergeht wird die Funktion abgebrochen
        if (knoten.getAdjazenzListeKanten().size()==0) {
            return;
        }

        // Für jeden benachbarten Knoten wird eine neue Tiefensuche ausgeführt,
        // sofern er nicht bereits besucht wurde oder die Kante keine Kapazität mehr hat.
        for (Kante kante : knoten.getAdjazenzListeKanten()) {
            if (pfadKnoten.contains(kante.getKnoten_2())||kante.getRestKapazitaet()==0) {
                continue;
            }
            // Die Kante wird dem Pfad hinzugefügt
            pfadKanten.add(kante);

            // Eine neue Tiefensuche wird gestartet
            tiefensuche(kante.getKnoten_2());

            // Die Kante und der Knoten werden entfernt und die for Schleife geht weiter
            pfadKanten.remove(kante);
            pfadKnoten.remove(kante.getKnoten_2());
        }
    }

    /**
     * Diese Funktion bestimmt den Start- und Zielknoten.
     */
    public void startUndZielKnotenBestimmen() {
        for (Knoten[] knotens : graph.getKnoten()) {
            for (Knoten knoten : knotens) {
                if (knoten.getKategorie() == 0) {
                    startKnoten = knoten;
                    break;
                }
            }
        }
        for (Knoten[] knotens : graph.getKnoten()) {
            for (Knoten knoten : knotens) {
                if (knoten.getKategorie() == 2) {
                    zielKnoten = knoten;
                    break;
                }
            }
        }
    }

    /**
     * Diese Funktion berechnet den Bottleneck des ausgewählten Pfades.
     */
    public void berechneBottleneck() {
        this.bottleneckValue=20;
        for (Kante kante : pfadKanten) {
            if (kante.getRestKapazitaet()<bottleneckValue) {
                bottleneckValue = kante.getRestKapazitaet();
            }
        }
    }

    /**
     * Diese Funktion updated den Fluss des Graphen.
     */
    public void updateGraph() {
        // Den Fluss der normalen Kanten updaten
        for (Kante kanteGraph : graph.getKanten()) {
            for (Kante kantePfad : pfadKanten) {
                if (kanteGraph==kantePfad) {
                    kanteGraph.setAuslastung(kanteGraph.getAuslastung()+bottleneckValue);
                    kanteGraph.setRestKapazitaet(kanteGraph.getRestKapazitaet()-bottleneckValue);
                }
            }
        }
        // Den Fluss der Rückkanten updaten
        for (Kante rueckKanteGraph : graph.getRueckKanten()) {
            for (Kante kantePfad : pfadKanten) {
                if (rueckKanteGraph==kantePfad) {
                    rueckKanteGraph.setAuslastung(rueckKanteGraph.getAuslastung()+bottleneckValue);
                    rueckKanteGraph.setRestKapazitaet(rueckKanteGraph.getRestKapazitaet()-bottleneckValue);
                }
            }
        }
        // Die Kapazität der Kanten erhöhen
        for (Kante kanteGraph : graph.getKanten()) {
            for (Kante kantePfad : pfadKanten) {
                if (graph.getRueckKanten().contains(kantePfad)) {
                    if (kanteGraph.getKnoten_1()==kantePfad.getKnoten_2()&&kanteGraph.getKnoten_2()==kantePfad.getKnoten_1()) {
                        kanteGraph.setAuslastung(kanteGraph.getAuslastung()-bottleneckValue);
                        kanteGraph.setRestKapazitaet(kanteGraph.getRestKapazitaet()+bottleneckValue);
                    }
                }
            }
        }
        // Die Kapazität der Rückkanten erhöhen
        for (Kante rueckKanteGraph : graph.getRueckKanten()) {
            for (Kante kantePfad : pfadKanten) {
                if (graph.getKanten().contains(kantePfad)) {
                    if (rueckKanteGraph.getKnoten_1() == kantePfad.getKnoten_2() && rueckKanteGraph.getKnoten_2() == kantePfad.getKnoten_1()) {
                        rueckKanteGraph.setAuslastung(rueckKanteGraph.getAuslastung() - bottleneckValue);
                        rueckKanteGraph.setRestKapazitaet(rueckKanteGraph.getRestKapazitaet() + bottleneckValue);
                    }
                }
            }
        }

        // Den maximalen Fluss um den Bottleneck erhöhen
        this.maxFlow+=bottleneckValue;
    }

    /**
     * Diese Funktion setzt den Fluss des Graphen zurück.
     */
    public void resetFlow() {
        for (Kante kante : graph.getKanten()) {
            kante.setAuslastung(0);
            kante.setRestKapazitaet(kante.getMaxKapazitaet());
        }
        for (Kante kante : graph.getRueckKanten()) {
            kante.setAuslastung(0);
            kante.setRestKapazitaet(0);
        }
    }

    /**
     * Diese Funktion speichert den neusten Pfad im String {@link #pfade}
     */
    private void savePfad() {
        pfade = pfade +startKnoten.getId();
        for(Kante kante : pfadKanten) {
            pfade = pfade +", "+kante.getKnoten_2().getId();
        }
        pfade = pfade +" | Fluss: "+bottleneckValue+"\n";
    }

    public String getPfade() {
        return pfade;
    }

    public boolean isFinished() {
        if (pfadKantenOptionen.size()>0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getFinished() {
        return finished;
    }

    public int getMaxFlow() {
        return maxFlow;
    }
}

package ch.kbw.maximalerfluss.algorithmus;

import ch.kbw.maximalerfluss.Controller;
import ch.kbw.maximalerfluss.Graph;
import ch.kbw.maximalerfluss.Kante;
import ch.kbw.maximalerfluss.Knoten;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;

import java.util.ArrayList;
import java.util.Random;

/**
 * Diese Klasse enthält den Algorithmus, der den maximalen Fluss berechnet.
 *
 * @author Marc Schwendemann
 */

public class Algorithmus {

    private Graph graph;
    private Digraph<Knoten, String> graphNeu = new DigraphEdgeList<>();
    private ArrayList<ArrayList<Kante>> pfadKantenOptionen;
    private ArrayList<Knoten> pfadKnoten;
    private ArrayList<Kante> pfadKanten;
    private Knoten startKnoten, zielKnoten;
    private int bottleneckValue;
    private int maxFlow;
    private boolean finished;
    private Controller controller;

    public Algorithmus(Graph graphOriginal, Controller controller) {
        this.graph = graphOriginal;
        this.controller = controller;
    }

    public void berechneMaxFlow() {
        pfadKantenOptionen = new ArrayList<ArrayList<Kante>>();
        pfadKnoten = new ArrayList<>();
        pfadKanten = new ArrayList<>();
        this.bottleneckValue=0;
        this.finished=false;
        this.maxFlow=0;
        startUndZielKnotenBestimmen();

        while (!finished) {
            knotenInfosAusgeben();
            kantenInfosAusgeben();
            rueckKantenInfosAusgeben();

            nextIteration();

            if (!finished) {
                berechneBottleneck();
                updateGraph();
                kantenInfosAusgeben();
                rueckKantenInfosAusgeben();
            }
        }
    }

    public void nextIteration() {
        pfadKantenOptionen.clear();
        pfadKnoten.clear();
        pfadKanten.clear();
        depthSearch(startKnoten);
        System.out.println(pfadKantenOptionen.size());
        if (pfadKantenOptionen.size()==0) {
            System.out.println("Der maximale Fluss wurde berechnet: ");
            System.out.println("Maximaler Fluss = "+maxFlow);
            finished=true;
            return;
        }
        Random rn = new Random();
        int random = rn.nextInt(pfadKantenOptionen.size());
        pfadKanten = (ArrayList<Kante>) pfadKantenOptionen.get(random).clone();
    }

    private void depthSearch(Knoten start) {
        pfadKnoten.add(start);
        System.out.println(pfadKnoten.size());
        if (start == zielKnoten) {
            pfadKantenOptionen.add((ArrayList<Kante>) pfadKanten.clone());
            return;
        }
        if (start.getAdjazenzListeKanten().size()==0) {
            return;
        }
        for (Kante kante : start.getAdjazenzListeKanten()) {
            if (pfadKnoten.contains(kante.getKnoten_2())||kante.getRestKapazitaet()==0) {
                continue;
            }
            pfadKanten.add(kante);
            depthSearch(kante.getKnoten_2());
            pfadKanten.remove(kante);
            pfadKnoten.remove(kante.getKnoten_2());
        }
    }

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

    public void berechneBottleneck() {
        this.bottleneckValue=20;
        for (Kante kante : pfadKanten) {
            if (kante.getRestKapazitaet()<bottleneckValue) {
                bottleneckValue = kante.getRestKapazitaet();
            }
        }
    }

    public void updateGraph() {
        for (Kante kanteGraph : graph.getKanten()) {
            for (Kante kantePfad : pfadKanten) {
                if (kanteGraph==kantePfad) {
                    kanteGraph.setAuslastung(kanteGraph.getAuslastung()+bottleneckValue);
                    kanteGraph.setRestKapazitaet(kanteGraph.getRestKapazitaet()-bottleneckValue);
                }
            }
        }
        for (Kante rueckKanteGraph : graph.getRueckKanten()) {
            for (Kante kantePfad : pfadKanten) {
                if (rueckKanteGraph.getKnoten_1()==kantePfad.getKnoten_2()&&rueckKanteGraph.getKnoten_2()==kantePfad.getKnoten_1()) {
                    rueckKanteGraph.setAuslastung(rueckKanteGraph.getAuslastung()-bottleneckValue);
                    rueckKanteGraph.setRestKapazitaet(rueckKanteGraph.getRestKapazitaet()+bottleneckValue);
                }
            }
        }
        for (Kante rueckKanteGraph : graph.getRueckKanten()) {
            for (Kante kantePfad : pfadKanten) {
                if (rueckKanteGraph==kantePfad) {
                    rueckKanteGraph.setAuslastung(rueckKanteGraph.getAuslastung()+bottleneckValue);
                    rueckKanteGraph.setRestKapazitaet(rueckKanteGraph.getRestKapazitaet()-bottleneckValue);
                }
            }
        }
        this.maxFlow+=bottleneckValue;
    }

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


    // -----------------------------------------------------------------------------------------------------
    // Dieser Abschnitt dient nur zum Testen.
    // -----------------------------------------------------------------------------------------------------
    public void knotenInfosAusgeben() {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("Knoten");
        System.out.println("--------------------------------------------------------");
        System.out.println();

        for (int i = 0; i < graph.getKnoten().length; i++) {
            System.out.print((i + 1) + ". ArrayList: | ");
            for (int j = 0; j < graph.getKnoten()[i].length; j++) {
                System.out.print("[Knoten " + graph.getKnoten()[i][j].getId() + " gehoert zur Kategorie "
                        + graph.getKnoten()[i][j].getKategorie() + "] | ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println();
    }

    public void kantenInfosAusgeben() {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("Kanten");
        System.out.println("--------------------------------------------------------");
        System.out.println();







        for (Kante kante : graph.getKanten()) {
            System.out.println("[Kante mit den 1. Knoten = " + kante.getKnoten_1().getId() + " und den 2. Knoten = "
                    + kante.getKnoten_2().getId() + " | Auslastung: " + kante.getAuslastung() + " / restliche Kapazitaet: "
                    + kante.getRestKapazitaet() + " / maximale Kapazitaet: " +
                    kante.getMaxKapazitaet() + "]");
        }

        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println();
    }

    public void rueckKantenInfosAusgeben() {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("Rückkanten");
        System.out.println("--------------------------------------------------------");
        System.out.println();

        for (Kante kante : graph.getRueckKanten()) {
            System.out.println("[Kante mit den 1. Knoten = " + kante.getKnoten_1().getId() + " und den 2. Knoten = "
                    + kante.getKnoten_2().getId() + " | Auslastung: " + kante.getAuslastung() + " / restliche Kapazitaet: "
                    + kante.getRestKapazitaet() + " / maximale Kapazitaet: " +
                    kante.getMaxKapazitaet() + "]");
        }

        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println();
    }

    public Digraph<Knoten, String> getGraphNeu() {
        return graphNeu;
    }

    public void setGraphNeu(Digraph<Knoten, String> graphNeu) {
        this.graphNeu = graphNeu;
    }
}

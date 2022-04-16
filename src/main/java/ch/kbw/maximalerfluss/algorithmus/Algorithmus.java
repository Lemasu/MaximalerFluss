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

    private Graph graph;
    private ArrayList<ArrayList<Kante>> pfadKantenOptionen;
    private ArrayList<Knoten> pfadKnoten;
    private ArrayList<Kante> pfadKanten;
    private ArrayList<Kante> optionen;
    private Knoten startKnoten, zielKnoten;
    private int bottleneckValue;
    private int maxFlow;
    private boolean finished;
    private int counter;

    public Algorithmus() {

    }

    public void berechneMaxFlow(Graph graphOriginal) {
        graph = new Graph();
        graph = graphOriginal;

        pfadKantenOptionen = new ArrayList<ArrayList<Kante>>();
        pfadKnoten = new ArrayList<>();
        pfadKanten = new ArrayList<>();
        optionen = new ArrayList<>();
        startUndZielKnotenBestimmen();
        bottleneckValue=0;
        maxFlow=0;
        finished=false;
        counter=0;

        while (true) {
            knotenInfosAusgeben();
            kantenInfosAusgeben();
            rueckKantenInfosAusgeben();

            pfadKnoten.clear();
            pfadKanten.clear();
            nextIteration();

            berechneBottleneck();
            updateGraph();
            kantenInfosAusgeben();
            rueckKantenInfosAusgeben();
            counter++;
            System.out.println(counter);
        }
    }

    public void nextIteration() {
        pfadKantenOptionen.clear();
        depthSearch(startKnoten);
        System.out.println(pfadKantenOptionen.size());
        if (pfadKantenOptionen.size()==0) {
            System.out.println("Der maximale Fluss wurde berechnet: ");
            System.out.println("Maximaler Fluss = "+maxFlow);
            System.exit(0);
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

    public void neuerPfadBerechnen() {
        pfadKnoten.clear();
        pfadKanten.clear();
        for (Kante kante : graph.getKanten()) {
            kante.setVisited(false);
        }
        for (Kante kante : graph.getRueckKanten()) {
            kante.setVisited(false);
        }

        pfadKnoten.add(startKnoten);

        kantenInfosAusgeben();

        Random rn = new Random();

        while (pfadKnoten.get(pfadKnoten.size()-1)!=zielKnoten) {
            neueOptionenBerechnen();
            System.out.println(optionen.size());
            System.out.println(optionen);
            if (!isFinished()) {
                int random = rn.nextInt(optionen.size());
                System.out.println(optionen.get(random));
                System.out.println(optionen.get(random).getKnoten_2().getId() );
                pfadKanten.add(optionen.get(random));
                optionen.get(random).setVisited(true);
                pfadKnoten.add(optionen.get(random).getKnoten_2());
            } else {
                berechneBottleneck();
                updateGraph();
                System.out.println("Der maximale Fluss wurde berechnet: ");
                System.out.println("Maximaler Fluss = "+maxFlow);
                System.exit(0);
            }
        }
    }

    public void neueOptionenBerechnen() {
        this.optionen.clear();
        for (Kante kante : graph.getKanten()) {
            if (kante.getKnoten_1()==pfadKnoten.get(pfadKnoten.size()-1)) {
                if (kante.getRestKapazitaet()>0 && kante.getVisited()==false) {
                    optionen.add(kante);
                }
            }
        }
        for (Kante kante : graph.getRueckKanten()) {
            if (kante.getKnoten_1()==pfadKnoten.get(pfadKnoten.size()-1)) {
                if (kante.getRestKapazitaet()>0 && kante.getVisited()==false) {
                    optionen.add(kante);
                }
            }
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

    public void resetFlow(Graph graph) {
        for (Kante kante : graph.getKanten()) {
            kante.setAuslastung(0);
            kante.setRestKapazitaet(kante.getMaxKapazitaet());
        }
        for (Kante kante : graph.getRueckKanten()) {
            kante.setAuslastung(0);
            kante.setRestKapazitaet(0);
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

    public boolean isFinished() {
        if (optionen.size()==0) {
            finished = true;
        } else {
            finished = false;
        }
        return finished;
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
                System.out.print("[Knoten " + graph.getKnoten()[i][j].getId() + " gehoert zum Kategorie "
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
                    kante.getMaxKapazitaet() + " / visited: " + kante.getVisited() + "]");
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
                    kante.getMaxKapazitaet() + " / visited: " + kante.getVisited() + "]");
        }

        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println();
    }
    // -----------------------------------------------------------------------------------------------------


    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public ArrayList<Kante> getPfadKanten() {
        return pfadKanten;
    }

    public void setPfadKanten(ArrayList<Kante> pfadKanten) {
        this.pfadKanten = pfadKanten;
    }

    public ArrayList<Kante> getOptionen() {
        return optionen;
    }

    public void setOptionen(ArrayList<Kante> optionen) {
        this.optionen = optionen;
    }

    public int getBottleneckValue() {
        return bottleneckValue;
    }

    public void setBottleneckValue(int bottleneckValue) {
        this.bottleneckValue = bottleneckValue;
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public void setMaxFlow(int maxFlow) {
        this.maxFlow = maxFlow;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}

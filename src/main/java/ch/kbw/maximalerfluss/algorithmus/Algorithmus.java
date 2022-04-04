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
    private ArrayList<Knoten> pfadKnoten;
    private ArrayList<Kante> pfadKanten;
    private ArrayList<Kante> optionen;
    private int bottleneckValue;
    private int maxFlow;
    private boolean finished;
    private int counter;

    public Algorithmus() {

    }

    public void berechneMaxFlow(Graph graphOriginal) {
        graph = new Graph();
        graph = graphOriginal;

        pfadKnoten = new ArrayList<>();
        pfadKanten = new ArrayList<>();
        optionen = new ArrayList<>();
        bottleneckValue=0;
        maxFlow=0;
        finished=false;
        counter=0;

        while (true) {
            System.out.println(graph.getKnoten());
            neuerPfadBerechnen();
            KantenInfosAusgeben();

            berechneBottleneck();
            KantenInfosAusgeben();

            updateGraph();
            KantenInfosAusgeben();
            counter++;
            System.out.println(counter);
        }
    }


    public void resetFlow(Graph graph) {
        for (Kante kante : graph.getKanten()) {
            kante.setAuslastung(0);
        }
    }

    public void neueOptionenBerechnen() {
        this.optionen.clear();
        for (Kante kante : this.graph.getKanten()) {
            if (kante.getKnoten_1()==this.pfadKnoten.get(this.pfadKnoten.size()-1)) {
                if ((kante.getKapazitaet())-(kante.getAuslastung())>0) {
                    this.optionen.add(kante);
                }
            }
        }
    }

    public void neuerPfadBerechnen() {
        this.pfadKnoten.clear();
        this.pfadKanten.clear();

        pfadKnoten.add(graph.getKnoten()[0][0]);

        Random rn = new Random();
        while (this.pfadKnoten.get(this.pfadKnoten.size()-1)!=this.graph.getKnoten()[1][2]) {
            neueOptionenBerechnen();
            System.out.println(optionen.size());
            System.out.println(optionen);
            if (!isFinished()) {
                int random = rn.nextInt(this.optionen.size());
                this.pfadKanten.add(this.optionen.get(random));
                this.pfadKnoten.add(this.optionen.get(random).getKnoten_2());
            } else {
                berechneBottleneck();
                updateGraph();
                System.out.println("Der maximale Fluss wurde berechnet: ");
                System.out.println("Maximaler Fluss = "+this.maxFlow);
                System.exit(0);
            }
        }
    }

    public void berechneBottleneck() {
        this.bottleneckValue=0;
        for (Kante kante : this.pfadKanten) {
            if (kante.getKapazitaet()>this.bottleneckValue) {
                this.bottleneckValue = kante.getKapazitaet();
            }
        }
    }

    public void updateGraph() {
        for (Kante kanteGraph : this.graph.getKanten()) {
            for (Kante kantePfad : this.pfadKanten) {
                if (kanteGraph==kantePfad) {
                    kanteGraph.setAuslastung(bottleneckValue);
                }
            }
        }
        this.maxFlow+=bottleneckValue;
    }

    public boolean isFinished() {
        if (this.optionen.size()==0) {
            this.finished = true;
        } else {
            this.finished = false;
        }
        return finished;
    }



    // -----------------------------------------------------------------------------------------------------
    // Dieser Abschnitt dient nur zum Testen.
    // -----------------------------------------------------------------------------------------------------
    public void KnotenInfosAusgeben() {
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

    public void KantenInfosAusgeben() {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("Kanten");
        System.out.println("--------------------------------------------------------");
        System.out.println();

        for (Kante kante : graph.getKanten()) {
            System.out.println("[Kante mit den 1. Knoten = " + kante.getKnoten_1().getId() + " und den 2. Knoten = "
                    + kante.getKnoten_2().getId() + " | Auslastung: " + kante.getAuslastung() + " / Kapazitaet: "
                    + kante.getKapazitaet() + "]");
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

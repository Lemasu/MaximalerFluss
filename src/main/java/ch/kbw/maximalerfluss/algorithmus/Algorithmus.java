package ch.kbw.maximalerfluss.algorithmus;

import ch.kbw.maximalerfluss.Graph;
import ch.kbw.maximalerfluss.Kante;

import java.util.ArrayList;
import java.util.Random;

/**
 * Diese Klasse enthält den Algorithmus, der den maximalen Fluss berechnet.
 *
 * @author Marc Schwendemann
 */

public class Algorithmus {

    private Graph graph;
    private ArrayList<Kante> pfad;
    private ArrayList<Kante> optionen;
    private int bottleneckValue;
    private int maxFlow;

    public Algorithmus() {

    }

    public void resetFlow() {
        for (Kante kante : graph.getKanten()) {
            kante.setAuslastung(0);
        }
    }

    public ArrayList<Kante> neueOptionen() {
        optionen.clear();
        for (Kante kante : graph.getKanten()) {
            if (kante.getKnoten_1()==pfad.get(pfad.size()-1).getKnoten_2()) {
                if ((kante.getKapazitaet())-(kante.getAuslastung())>0) {
                    optionen.add(kante);
                }
            }
        }
        return optionen;
    }

    public ArrayList<Kante> neuerPfad() {
        pfad.clear();
        pfad.add(graph.getKanten().get(0));
        Random rn = new Random();
        while (pfad.get(pfad.size()-1)!=graph.getKanten().get(graph.getKanten().size()-1)) {
            if (!finished()) {
                int random = rn.nextInt(neueOptionen().size());
                pfad.add(neueOptionen().get(random));
            } else {
                break;
            }
        }
        return pfad;
    }

    public int berechneBottleneck() {
        this.bottleneckValue=0;
        for (Kante kante : pfad) {
            if (kante.getKapazitaet()>bottleneckValue) {
                this.bottleneckValue = kante.getKapazitaet();
            }
        }
        return bottleneckValue;
    }

    public void updateGraph() {
        for (Kante kanteGraph : this.graph.getKanten()) {
            for (Kante kantePfad : this.pfad) {
                if (kanteGraph==kantePfad) {
                    kanteGraph.setAuslastung(bottleneckValue);
                }
            }
        }
        maxFlow+=bottleneckValue;
    }

    public boolean finished() {
        if (neueOptionen().size()==0) {
            return true;
        } else {
            return false;
        }
    }


}

import java.util.Map;
import java.util.List;
public class FletteTraad implements Runnable {
    private static int traadTeller = 0;
    private int traadID;
    private Monitor2 monitor;

    public FletteTraad(Monitor2 monitor) {
        this.monitor = monitor;
        traadID = traadTeller;
        traadTeller++;
    }

    @Override
    public void run() {
        try {
            List<Map<String, Subsekvens>> hMapListe = monitor.taUtTo();
            while(hMapListe != null) {
                Map<String, Subsekvens> hMap1 = hMapListe.get(0);
                Map<String, Subsekvens> hMap2 = hMapListe.get(1);
                Map<String, Subsekvens> hMapSammen = SubsekvensRegister.slaaSammen(hMap1, hMap2);
                monitor.settInnFlettet(hMapSammen);
                hMapListe = monitor.taUtTo();
            }
            System.out.println("Traad " + traadID + " er ferdig med aa flette HashMaps.");
        } catch(InterruptedException e) {
            System.out.printf(
                "[ERROR] FletteTraad med id %d ble avbrutt.",
                traadID
            );
        }
    }

}

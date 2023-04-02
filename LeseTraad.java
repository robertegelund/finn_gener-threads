
public class LeseTraad implements Runnable {
    public static int traadTeller = 0;
    private int traadID;
    Monitor2 monitor;
    String filnavn;

    public LeseTraad(Monitor2 monitor, String filnavn) {
        this.monitor = monitor;
        this.filnavn = filnavn;
        traadID = traadTeller;
        traadTeller++;
    }

    @Override
    public void run() {
        try {
            monitor.settInn(
                SubsekvensRegister.lesFil(filnavn)
            );
            System.out.println("Traad " + traadID + " er ferdig med aa lese inn filen.");
        } catch(InterruptedException e) {
            System.out.printf(
                "[ERROR] Lesetraad med id %d ble avbrutt.",
                traadID);
        }
    }

    public int hentID() {
        return traadID;
    }
}


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
        monitor.settInn(
            SubsekvensRegister.lesFil(filnavn)
        );
        System.out.println("Traad " + traadID + " er ferdig med aa lese inn filen.");
    }

    public int hentID() {
        return traadID;
    }
}

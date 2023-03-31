public class LeseTraad implements Runnable {
    Monitor1 monitor;
    String filnavn;

    public LeseTraad(Monitor1 monitor, String filnavn) {
        this.monitor = monitor;
        this.filnavn = filnavn;
    }

    @Override
    public void run() {
        monitor.settInn(
            SubsekvensRegister.lesFil(filnavn)
        );
    }
}

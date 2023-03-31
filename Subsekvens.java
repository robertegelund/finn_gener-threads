public class Subsekvens {
    public final String subsekvens;
    private int antall;

    public Subsekvens(String subsekvens, int antall) {
        this.subsekvens = subsekvens;
        this.antall = antall;
    }

    public int hentAntall() {
        return antall;
    }

    public void endreAntall(int tillegg) {
        antall += tillegg;
        System.out.println("Endrer antall");
    }

    @Override
    public String toString() {
        return String.format(
            "(%s, %d)", subsekvens, antall
        );
    }

}
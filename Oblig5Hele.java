import java.util.ArrayList;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Oblig5Hele {
    public static final int ANT_TELLETRAADER = 8;
    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("[ERROR] Du maa sende inn mappestien som argument til programmet: " + 
            "java Oblig5Del1 mappesti");
            System.exit(1);
        }
        
        Scanner sc = null;
        try {
            sc = new Scanner(new File(args[0] + "/metadata.csv")); 
        } catch(FileNotFoundException e) {
            System.out.println("[ERROR] Finner ikke filen: metadata.csv");
        }

        // Oppretter monitorer, leser inn filer og legger inn subsekvenser
        Monitor2 monitorVirus = new Monitor2();
        Monitor2 monitorIkkeVirus = new Monitor2();
        
        ArrayList<Thread> lesetraader = new ArrayList<>(); 
        while(sc.hasNextLine()) {
            String[] deler = sc.nextLine().split(",");
            String filnavn = args[0] + "/" + deler[0];
            Thread nyTraad = null;

            if(deler[1].equals("True")) {
                nyTraad = new Thread(new LeseTraad(monitorVirus, filnavn));
            } else if(deler[1].equals("False")) {
                nyTraad = new Thread(new LeseTraad(monitorIkkeVirus, filnavn));
            }

            lesetraader.add(nyTraad);
            nyTraad.start();
        }
        
        // Soerger for at main-traaden venter til alle lesetraadene er ferdige foer flettetraadene opprettes
        try {
            for(Thread traad : lesetraader) {
                traad.join();
            }
        } catch (InterruptedException e) {
            System.out.println("[ERROR] En lesetraad ble avbrutt. Programmet avsluttes.");
            System.exit(1);
        }
        
        // Antall som skal flettes blir én mindre enn stoerrelsen til beholderen
        monitorVirus.settFlettingerIgjen(monitorVirus.antall() - 1);
        monitorIkkeVirus.settFlettingerIgjen(monitorIkkeVirus.antall() - 1);

        ArrayList<Thread> flettetraader = new ArrayList<>();
        for(int i = 0; i < ANT_TELLETRAADER; i++) {
            Thread fletterVirus = new Thread(new FletteTraad(monitorVirus));
            Thread fletterIkkeVirus = new Thread(new FletteTraad(monitorIkkeVirus));
            flettetraader.add(fletterVirus); flettetraader.add(fletterIkkeVirus);
            fletterVirus.start(); fletterIkkeVirus.start();
        }

        // Soerger for at main-traaden venter til alle flettetraadene er ferdige
        for(Thread traad : flettetraader) {
            try {
                traad.join();
            } catch(InterruptedException e) {
                System.out.println("[ERROR] En flettetraad med ble avbrutt," + 
                "men de andre flettetraadene fortsetter.");
            }
        }

        // Finner subsekvensene som forekommer i stoerre grad hos de som har hatt viruset
        Map<String, Subsekvens> hMapVirus = monitorVirus.hentForste();
        Map<String, Subsekvens> hMapIkkeVirus = monitorIkkeVirus.hentForste();
        ArrayList<Subsekvens> hyppigste2 = new ArrayList<>(), hyppigste5 = new ArrayList<>(),
                hyppigste7 = new ArrayList<>();

        for(String noekkel : hMapVirus.keySet()) {
            Subsekvens subsekIkkeVirus = hMapIkkeVirus.get(noekkel);
            Subsekvens subsekVirus = hMapVirus.get(noekkel);
            
            int frekvensDiff = 0;
            if(subsekIkkeVirus == null) frekvensDiff = subsekVirus.hentAntall();
            else if(subsekIkkeVirus != null) frekvensDiff = subsekVirus.hentAntall() - subsekIkkeVirus.hentAntall();

            if(frekvensDiff >= 7) hyppigste7.add(subsekVirus);
            else if(frekvensDiff >= 5) hyppigste5.add(subsekVirus);
            else if(frekvensDiff >= 2) hyppigste2.add(subsekVirus);
        }
        
        // Skriver ut subsekvensene som forekommer i stoerre grad hos de som har hatt viruset
        if(hyppigste7.size() != 0) {
            System.out.println("\n\nSubsekvens(er) som forekommer flest ganger (7 eller flere)" + 
            " hos de som har hatt viruset:");
            printHyppigsteSekvenser(hyppigste7);
        } else if(hyppigste5.size() != 0) {
            System.out.println("\n\nSubsekvens(er) som forekommer flest ganger (5 eller flere, men faerre enn 7)" +
            " hos de som har hatt viruset:");
            printHyppigsteSekvenser(hyppigste5);
        } else if(hyppigste2.size() != 0) {
            System.out.println("\n\nSubsekvens(er) som forekommer flest ganger (2 eller flere, men faerre enn 5)" +
            " hos de som har hatt viruset:");
            printHyppigsteSekvenser(hyppigste2);
        }
    }
    
    // Hjelpemetode for utskrift av hyppigste subsekvenser
    private static void printHyppigsteSekvenser(ArrayList<Subsekvens> hyppigste) {
        for(Subsekvens subsek : hyppigste) {
            System.out.println(subsek);
        }
        System.out.println();
    }
}

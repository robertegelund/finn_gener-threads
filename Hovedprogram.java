import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Hovedprogram {
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
            System.out.println("[ERROR] Finner ikke filen: metadata.csv. Soerg for at mappestien er korrekt.");
            System.exit(1);
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
        sorterFrekvenser(hMapVirus, hMapIkkeVirus);
        
    }
     
    
    public static void sorterFrekvenser(
            Map<String, Subsekvens> hMapVirus, Map<String, Subsekvens> hMapIkkeVirus) {

                Map<Integer, List<Subsekvens>> kategoriserteFrekvenser = new HashMap<>();
                
                int dominantFrekvens = 0;
                for(String noekkel : hMapVirus.keySet()) {
                    Subsekvens subsekIkkeVirus = hMapIkkeVirus.get(noekkel);
                    Subsekvens subsekVirus = hMapVirus.get(noekkel);
                    
                    int frekvensDiff = 0;
                    if(subsekIkkeVirus == null) frekvensDiff = subsekVirus.hentAntall();
                    else if(subsekIkkeVirus != null) frekvensDiff = subsekVirus.hentAntall() - subsekIkkeVirus.hentAntall();
        
                    if(!kategoriserteFrekvenser.keySet().contains(frekvensDiff) && frekvensDiff > 0) {
                        dominantFrekvens = frekvensDiff;
                        List<Subsekvens> nySubsekListe = new ArrayList<>();
                        nySubsekListe.add(subsekVirus);
                        kategoriserteFrekvenser.put(frekvensDiff, nySubsekListe);
                    } else if(kategoriserteFrekvenser.keySet().contains(frekvensDiff) && frekvensDiff > 0){
                        kategoriserteFrekvenser.get(frekvensDiff).add(subsekVirus);
                    }
                }

                printDominanteSubsekvenser(kategoriserteFrekvenser, dominantFrekvens);
    }


    public static void printDominanteSubsekvenser(
        Map<Integer, List<Subsekvens>> kategoriserte, 
        int dominantFrekvens) {

            if(dominantFrekvens < 7) {
                System.out.printf(
                    "\nSubsekvens(er) som forekommer oftest blant de smittede, med %d flere forekomster:\n",
                    dominantFrekvens
                );
                for(Subsekvens subsek : kategoriserte.get(dominantFrekvens)) {
                    System.out.println(subsek);
                }
                System.out.println();
            } else {
                for(Integer frekvensDiff : kategoriserte.keySet()) {
                    if(frekvensDiff >= 7) {
                        System.out.printf(
                            "\nSubsekvens(er) som forekommer oftest blant de smittede, med %d flere forekomster:\n",
                            frekvensDiff
                );
                        for(Subsekvens subsek : kategoriserte.get(frekvensDiff)) {
                            System.out.println(subsek);
                        }
                    }
                }
                System.out.println();
            }
    }
}

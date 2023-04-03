import java.util.ArrayList;
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

        // Oppretter subsekvensregister, leser inn filer og legger inn subsekvenser
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
        for(Thread traad : lesetraader) {
            try {
                traad.join();
            } catch(InterruptedException e) { 
                System.out.println("En lesetraad ble avbrutt.");
            }
        }

        // Antall som skal flettes blir én mindre enn stoerrelsen til beholderen
        monitorVirus.settFlettingerIgjen(monitorVirus.antall() - 1);
        monitorIkkeVirus.settFlettingerIgjen(monitorIkkeVirus.antall() - 1);

        ArrayList<Thread> flettetraader = new ArrayList<>();
        for(int i = 0; i < ANT_TELLETRAADER; i++) {
            Thread fletterVirus = new Thread(new FletteTraad(monitorVirus));
            Thread fletterIkkeVirus = new Thread(new FletteTraad(monitorVirus));
            flettetraader.add(fletterVirus); flettetraader.add(fletterIkkeVirus);
            fletterVirus.start(); fletterIkkeVirus.start();
        }

        // Soerger for at main-traaden venter til alle flettetraadene er ferdige
        for(Thread traad : flettetraader) {
            try {
                traad.join();
            } catch(InterruptedException e) {
                System.out.println("En flettetraad ble avbrutt");
            }
        }

        // // Finner og skriver ut den mest frekvente subsekvensen i siste HashMap
        // int hoeyesteFrekvens = 0;
        // Subsekvens mestFrekvente = null;
        // for(String noekkel : monitor.hentForste().keySet()) {
        //     Subsekvens subsek = monitor.hentForste().get(noekkel); int frekvens = subsek.hentAntall();
        //     if(frekvens > hoeyesteFrekvens) {
        //         hoeyesteFrekvens = frekvens;
        //         mestFrekvente = subsek;
        //     }
        // }

        // System.out.println("Mest frekvente subsekvens: " + mestFrekvente);
    } 
}

import java.util.Map;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Oblig5Del2A {
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
        Monitor2 monitor = new Monitor2();
        ArrayList<Thread> traader = new ArrayList<>(); 
        while(sc.hasNextLine()) {
            String filnavn = args[0] + "/" + sc.nextLine();
            Thread nyTraad = new Thread(new LeseTraad(monitor, filnavn));
            traader.add(nyTraad);
            nyTraad.start();
        }

        // Soerger for at main-traaden venter til alle lesetraadene er ferdige
        for(Thread traad : traader) {
            try {
                traad.join();
            } catch(InterruptedException e) { 
                System.out.println("En traad ble avbrutt.");
            }
        }

        // Antall som skal flettes blir én mindre enn stoerrelsen til beholderen
        int subSekAntall = monitor.antall();
        Map<String, Subsekvens> hMapSammen = null;
        
        // Slaar sammen to og to inntil man sitter igjen med én HashMap
        for(int i = 0; i < subSekAntall - 1; i++) {
            Map<String, Subsekvens> hMap1 = monitor.taUt(), hMap2 = monitor.taUt();
            hMapSammen = SubsekvensRegister.slaaSammen(hMap1, hMap2);
            monitor.settInn(hMapSammen);
        }

        // Finner og skriver ut den mest frekvente subsekvensen i siste HashMap
        int hoeyesteFrekvens = 0;
        Subsekvens mestFrekvente = null;
        for(String noekkel : hMapSammen.keySet()) {
            Subsekvens subsek = hMapSammen.get(noekkel); int frekvens = subsek.hentAntall();
            if(frekvens > hoeyesteFrekvens) {
                hoeyesteFrekvens = frekvens;
                mestFrekvente = subsek;
            }
        }

        System.out.println("Mest frekvente subsekvens: " + mestFrekvente);
    } 
}

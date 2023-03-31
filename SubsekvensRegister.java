import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SubsekvensRegister {
    private List<Map<String, Subsekvens>> subsekvenser = new ArrayList<>();

    public void settInn(Map<String, Subsekvens> map) {
        subsekvenser.add(map);
    }

    public Map<String, Subsekvens> taUt() {
        return subsekvenser.remove(0);
    }

    public int antall() {
        return subsekvenser.size();
    }
    
    public static Map<String, Subsekvens> lesFil(String filnavn) {
        Scanner sc = null;

        try {
            sc = new Scanner(new File(filnavn));
        } catch(FileNotFoundException e) {
            System.out.println("Finner ikke filen: " + filnavn);
        }
        
        Map<String, Subsekvens> hMap = new HashMap<>();
        while(sc.hasNextLine()) {
            String linje = sc.nextLine();
            if(linje.length() < 3) System.exit(1);

            for(int i = 0; i < linje.length()-2; i++) { 
                String sub = "";
                sub +=  Character.toString(linje.charAt(i)) + 
                        Character.toString(linje.charAt(i+1)) + 
                        Character.toString(linje.charAt(i+2));

                //Går til neste iterasjon hvis subsekvensen allerede finnes i hMap
                if(hMap.get(sub) != null) continue;

                hMap.put(sub, new Subsekvens(sub, 1));
            }
        }
        sc.close();

        return hMap;
    }

    public static Map<String, Subsekvens> slaaSammen(
                Map<String, Subsekvens> hMap1,
                Map<String, Subsekvens> hMap2) {

        Map<String, Subsekvens> stoerst = null, minst = null;
        if(hMap1.size() >= hMap2.size()) {
            stoerst = hMap1; minst = hMap2;
        } else {
            stoerst = hMap2; minst = hMap1;
        }

        for(Subsekvens sub : stoerst.values()) {
            String subSek = sub.subsekvens;
            if(minst.keySet().contains(subSek)) {
                minst.get(subSek).endreAntall(sub.hentAntall());
            } else {
                minst.put(subSek, sub);
            }
        }
        
        // Den minste HashMap er den sammenslaatte HashMap;
        return minst;
    }
}

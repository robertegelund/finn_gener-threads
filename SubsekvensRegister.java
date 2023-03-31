import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SubsekvensRegister {
    List<Map<String, Subsekvens>> subsekvenser = new ArrayList<>();

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
            if(linje.length() < 3) System.exit(1); //TODO

            for(int i = 0; i < linje.length() - 2; i++) { 
                String sub = "";
                sub += linje.charAt(i) + linje.charAt(i+1) + linje.charAt(i+2); 
                hMap.put(sub, new Subsekvens(sub, 1));
            }
        }
        return hMap;
    }
}

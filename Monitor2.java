import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

public class Monitor2 {
    private int flettingerIgjen = 0;
    private SubsekvensRegister subReg = new SubsekvensRegister();
    private Lock laas = new ReentrantLock();
    private Condition finnesIkkeTo = laas.newCondition();

    public void settInn(Map<String, Subsekvens> hMap) throws InterruptedException {
        laas.lock();
        try {
            subReg.settInn(hMap);
        } finally {
            laas.unlock();
        }   
    }

    public void settInnFlettet(Map<String, Subsekvens> hMap) throws InterruptedException {
        laas.lock();
        try {
            subReg.settInn(hMap);
            dekrementerFlettingerIgjen();
            if(antall() >= 2 || !erFlettingerIgjen()) finnesIkkeTo.signalAll();
        } finally {
            laas.unlock();
        } 
    }

    public Map<String, Subsekvens> taUt() {
        return subReg.taUt();
    }

    public ArrayList<Map<String, Subsekvens>> taUtTo() throws InterruptedException {
        laas.lock();
        try {
            while(antall() < 2 && erFlettingerIgjen()) {finnesIkkeTo.await();}
            if(!erFlettingerIgjen()) return null;
            ArrayList<Map<String, Subsekvens>> hMapListe = new ArrayList<>();
            hMapListe.add(subReg.taUt()); hMapListe.add(subReg.taUt());
            return hMapListe;
        } finally {
            laas.unlock();
        }
    }

    public Map<String, Subsekvens> hentForste() {
        return subReg.hentForste();
    }

    public int antall() {
        return subReg.antall();
    }

    public void settFlettingerIgjen(int flettingerIgjen) {
        this.flettingerIgjen = flettingerIgjen;
    }

    public void dekrementerFlettingerIgjen() {
        flettingerIgjen--;
    }

    public boolean erFlettingerIgjen() {
        return flettingerIgjen > 0;
    }
}

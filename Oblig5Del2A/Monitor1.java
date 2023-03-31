import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class Monitor1 {

    private SubsekvensRegister subReg = new SubsekvensRegister();
    private Lock laas = new ReentrantLock();

    public void settInn(Map<String, Subsekvens> hMap) {
        laas.lock();
        try {
            subReg.settInn(hMap);
        } finally {
            laas.unlock();
        }   
    }

    public Map<String, Subsekvens> taUt() {
        return subReg.taUt();
    }

    public int antall() {
        return subReg.antall();
    }

}

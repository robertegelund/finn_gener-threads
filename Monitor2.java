import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class Monitor2 {

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
        laas.lock();
        try {
            return subReg.taUt();
        } finally {
            laas.unlock();
        }
    }

    public int antall() {
        laas.lock();
        try {
            return subReg.antall();
        } finally {
            laas.unlock();
        }
    }

}

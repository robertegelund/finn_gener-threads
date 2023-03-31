import java.util.Map;

public class Test {
    public static void main(String[] args) {
        Map<String, Subsekvens> hm1 = SubsekvensRegister.lesFil("./TestDataLike/fil1.csv");
        Map<String, Subsekvens> hm2 = SubsekvensRegister.lesFil("./TestDataLike/fil2.csv");
        Map<String, Subsekvens> hm3 = SubsekvensRegister.lesFil("./TestDataLike/fil3.csv");

        Map<String, Subsekvens> hm4 = SubsekvensRegister.slaaSammen(hm1, hm2);
        Map<String, Subsekvens> hm5 = SubsekvensRegister.slaaSammen(hm4, hm3);

    }
}

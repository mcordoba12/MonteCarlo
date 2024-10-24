import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import MonteCarlo.MasterPrx;

public class Client {
    public static void main(String[] args) {
        System.out.println("Iniciando cliente...");
        try (Communicator communicator = Util.initialize(args)) {
            System.out.println("Conectando a Master en: Master:default -h localhost -p 10000");
            MasterPrx master = MasterPrx
                    .checkedCast(communicator.stringToProxy("Master:default -h localhost -p 10000"));
            if (master == null) {
                throw new Error("Proxy inválido");
            }

            int N = 1000000; // Número de puntos
            int numWorkers = 10; // Número de trabajadores
            System.out.println("Solicitando estimación de pi...");
            float piEstimate = master.estimatePi(N, numWorkers);

            System.out.println("Estimación de π: " + piEstimate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

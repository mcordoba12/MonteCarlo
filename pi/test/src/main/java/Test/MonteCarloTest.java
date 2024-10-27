package Test;

import MonteCarlo.MasterPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;

public class MonteCarloTest {
    public static void main(String[] args) {
        // Ejecutar las pruebas
        testPiEstimationWithDifferentPoints();
        testPerformanceWithMultipleWorkers();
    }

    // Método para probar la estimación de pi con diferentes valores de N
    public static void testPiEstimationWithDifferentPoints() {
        int[] points = {1000, 10000, 100000, 1000000}; // Diferentes valores de N
        int numWorkers = 5; // Número de workers

        try (Communicator communicator = Util.initialize(new String[]{})) {
            MasterPrx master = MasterPrx.checkedCast(
                    communicator.stringToProxy("Master:default -h localhost -p 10000")
            );
            if (master == null) {
                throw new Error("Proxy inválido para el maestro.");
            }

            System.out.println("Iniciando pruebas de estimación de pi con diferentes valores de N...");
            for (int N : points) {
                float piEstimate = master.estimatePi(N, numWorkers);
                System.out.println("N=" + N + ", Estimación de π=" + piEstimate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para probar el rendimiento con diferentes números de workers
    public static void testPerformanceWithMultipleWorkers() {
        int N = 1000000; // Número de puntos para la estimación
        int[] numWorkersOptions = {1, 5, 10, 20}; // Diferentes cantidades de workers

        try (Communicator communicator = Util.initialize(new String[]{})) {
            MasterPrx master = MasterPrx.checkedCast(
                    communicator.stringToProxy("Master:default -h localhost -p 10000")
            );
            if (master == null) {
                throw new Error("Proxy inválido para el maestro.");
            }

            System.out.println("Iniciando pruebas de rendimiento con diferentes números de workers...");
            for (int numWorkers : numWorkersOptions) {
                long startTime = System.currentTimeMillis();
                float piEstimate = master.estimatePi(N, numWorkers);
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                System.out.println("Workers=" + numWorkers + ", Tiempo de ejecución=" + duration + " ms, Estimación de π=" + piEstimate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

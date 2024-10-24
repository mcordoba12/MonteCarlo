import java.util.List;
import com.zeroc.Ice.Current;
import MonteCarlo.Master;
import MonteCarlo.WorkerPrx;

public class MasterI implements Master {

    private List<WorkerPrx> workers;

    public MasterI(List<WorkerPrx> workers) {
        this.workers = workers;
    }

    @Override
    public float estimatePi(int totalPoints, int numWorkers, Current current) {
        System.out.println("Estimando π con " + totalPoints + " puntos y " + numWorkers + " trabajadores.");

        // Asegurarse de que numWorkers no exceda el número de trabajadores disponibles
        if (numWorkers > workers.size()) {
            System.err.println(
                    "Error: número de trabajadores solicitados (" + numWorkers + ") excede el número disponible ("
                            + workers.size() + "). Ajustando numWorkers a " + workers.size());
            numWorkers = workers.size(); // Ajustar a la cantidad de trabajadores disponibles
        }

        int pointsPerWorker = totalPoints / numWorkers;
        int extraPoints = totalPoints % numWorkers; // Puntos sobrantes
        int pointsInCircle = 0;

        // Llamadas sincrónicas a los trabajadores
        for (int i = 0; i < numWorkers; i++) {
            int pointsForThisWorker = pointsPerWorker + (i < extraPoints ? 1 : 0); // Asignar puntos extras
            WorkerPrx worker = workers.get(i); // Acceso seguro a la lista

            try {
                int result = worker.countPointsInCircle(pointsForThisWorker); // Llamada sincrónica
                pointsInCircle += result;
                System.out.println("Resultado recibido del trabajador " + i + ", puntos en círculo: " + result);
            } catch (Exception ex) {
                System.err.println("Error al solicitar al trabajador: " + worker + ", error: " + ex.getMessage());
            }
        }

        // Estimación de π
        float piEstimate = 4.0f * pointsInCircle / totalPoints;
        System.out.println("Estimación de π: " + piEstimate);
        return piEstimate;
    }

}

import MonteCarlo.WorkerPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args)) {
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("ServerAdapter", "default -p 10000");
            adapter.activate();
            System.out.println("Servidor Maestro listo y escuchando en el puerto 10000.");

            // Inicializar la lista de proxies de workers.
            List<WorkerPrx> workers = new ArrayList<>();

            // Configurar los puertos de los workers. Aquí se espera que haya un total de 10 workers.
            int numWorkers = 10; // Ajusta según tus necesidades
            for (int i = 0; i < numWorkers; i++) {
                int workerPort = 10001 + i; // Esto asume que los workers están en los puertos 10001, 10002, ...
                String workerIdentity = "Worker" + i;
                String endpoint = "default -h localhost -p " + workerPort;

                try {
                    ObjectPrx workerProxy = communicator.stringToProxy(workerIdentity + ":" + endpoint);
                    System.out.println("Intentando conectar al trabajador en: " + workerIdentity + " en " + endpoint);
                    workerProxy.ice_ping(); // Verificar la conexión con el worker
                    System.out.println("Conexión exitosa a " + workerIdentity);

                    // Agregar el proxy a la lista.
                    workers.add(WorkerPrx.checkedCast(workerProxy));
                } catch (Exception e) {
                    System.err.println("Error al conectar a " + workerIdentity + ": " + e.getMessage());
                }
            }

            // Crear y registrar el objeto maestro, pasando la lista de workers.
            MasterI master = new MasterI(workers); // Pasa la lista de workers al constructor
            adapter.add(master, Util.stringToIdentity("Master")); // Registra el objeto con el nombre "Master"

            communicator.waitForShutdown();
        } catch (Exception e) {
            System.err.println("Error durante la inicialización del servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

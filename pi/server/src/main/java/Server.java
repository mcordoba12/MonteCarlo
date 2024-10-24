import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import MonteCarlo.WorkerPrx;

import com.zeroc.Ice.ObjectPrx;

import java.util.ArrayList; // Importar la clase ArrayList de java.util
import java.util.List;

public class Server {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args)) {
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("ServerAdapter", "default -p 10000");
            adapter.activate();
            System.out.println("Servidor Maestro listo y escuchando en el puerto 10000.");

            // Inicializar la lista de proxies de workers
            List<WorkerPrx> workers = new ArrayList<>();

            // Intentar conectar a los Workers
            for (int i = 0; i < 10; i++) { // Cambia el número de workers según lo que necesites
                String workerIdentity = "Worker" + i;
                String endpoint = "default -h localhost -p " + (10001 + i);
                try {
                    ObjectPrx workerProxy = communicator.stringToProxy(workerIdentity + ":" + endpoint);
                    System.out.println("Intentando conectar al trabajador en: " + workerIdentity + " en " + endpoint);
                    workerProxy.ice_ping(); // Verificar la conexión con el worker
                    System.out.println("Conexión exitosa a " + workerIdentity);

                    // Agregar el proxy a la lista
                    workers.add(WorkerPrx.checkedCast(workerProxy));
                } catch (Exception e) {
                    System.err.println("Error al conectar a " + workerIdentity + ": " + e.getMessage());
                }
            }

            // Crear y registrar el objeto maestro, pasando la lista de workers
            MasterI master = new MasterI(workers); // Pasa la lista de workers al constructor
            adapter.add(master, Util.stringToIdentity("Master")); // Registra el objeto con el nombre "Master"

            communicator.waitForShutdown();
        } catch (Exception e) {
            System.err.println("Error durante la inicialización del servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

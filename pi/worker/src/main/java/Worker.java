import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class Worker {
    public static void main(String[] args) {
        // Establecer el puerto por defecto en caso de que no se proporcione.
        int port = 10001; // Valor por defecto

        // Verificar si se ha proporcionado un argumento para el puerto.
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]); // Leer el puerto desde los argumentos
            } catch (NumberFormatException e) {
                System.out.println("El puerto proporcionado no es válido. Usando el puerto por defecto: 10001.");
            }
        }

        try (Communicator communicator = Util.initialize(args)) {
            System.out.println("Registrando Worker en el puerto " + port + "...");

            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("WorkerAdapter", "default -p " + port);
            WorkerI worker = new WorkerI(); // Instancia de la implementación del Worker
            adapter.add(worker, Util.stringToIdentity("Worker" + (port - 10001))); // Ajusta la identidad con base en el puerto
            adapter.activate();

            System.out.println("Worker registrado y escuchando en el puerto " + port + ".");
            communicator.waitForShutdown();
        } catch (Exception e) {
            System.err.println("Error durante la inicialización del Worker: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

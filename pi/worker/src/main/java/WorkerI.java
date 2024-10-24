import java.util.Random;

import com.zeroc.Ice.Current;

import MonteCarlo.Worker;

public class WorkerI implements Worker {

    @Override
    public int countPointsInCircle(int numPoints, Current current) {
        System.out.println("Contando puntos en círculo: " + numPoints);
        int count = 0;
        Random rand = new Random();

        for (int i = 0; i < numPoints; i++) {
            double x = rand.nextDouble() * 2 - 1;
            double y = rand.nextDouble() * 2 - 1;

            if (x * x + y * y <= 1) {
                count++;
            }
        }

        System.out.println("Puntos dentro del círculo: " + count);
        return count;
    }

}

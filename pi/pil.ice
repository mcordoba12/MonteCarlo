// MonteCarlo.ice
module MonteCarlo {
    interface Worker {
        int countPointsInCircle(int numPoints); // Cuenta los puntos dentro del círculo
    }

    interface Master {
        float estimatePi(int totalPoints, int numWorkers); // Estima el valor de pi
    }
}


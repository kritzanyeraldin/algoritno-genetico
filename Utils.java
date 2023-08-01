package concurrent;

import concurrent.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    static final int POPULATION_SIZE = 20;
    static final int NUM_GENERACIONES = 5;
    static final double LOWER_BOUND = 0;
    static final double UPPER_BOUND = 20.0;
    static final double PROBABILIDAD_CRUZAMIENTO = 0.7;
    static final double PROBABILIDAD_MUTACION = 0.01;
    private static final Random random = new Random();

    static void serverLogger(String message) {
        System.out.println("[SERVIDOR] " + message);
    }

    static void clientLogger(int id, String message) {
        System.out.println("[CLIENTE " + id + "] " + message);
    }

    static void clientLogger(String message) {
        System.out.println("[CLIENTE] " + message);
    }

    static String populationToString(List<concurrent.Individual> population) {
        List<String> messages = new ArrayList<>();
        for (concurrent.Individual individual :
                population) {
            messages.add(String.format("%f/%f/%f", individual.x, individual.y, individual.fitness));
        }

        return String.join(",", messages);
    }

    static List<concurrent.Individual> stringToPopulation(String message) {
        String[] individuals = message.split(",");
        List<concurrent.Individual> population = new ArrayList<>();

        for (String individual :
                individuals) {
            String[] data = individual.split("/");
            population.add(new concurrent.Individual(Double.parseDouble(data[0]), Double.parseDouble(data[1])));
        }

        return population;
    }

    static double generateRandomDouble() {
        return LOWER_BOUND + (UPPER_BOUND - LOWER_BOUND) * random.nextDouble();
    }

    static List<concurrent.Individual> initializePopulation() {
        List<concurrent.Individual> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            double x = generateRandomDouble();
            double y = generateRandomDouble();
            population.add(new Individual(x, y));
        }
        return population;
    }

    static Individual seleccionarAleatorio(List<Individual> poblacion) {
        int index = random.nextInt(poblacion.size());
        return poblacion.get(index);
    }

    static Individual cruzar(Individual padre, Individual madre) {
        double x = (padre.x + madre.x) / 2.0;
        double y = (padre.y + madre.y) / 2.0;
        return new Individual(x, y);
    }

    static List<Individual> realizarCruzamiento(List<Individual> poblacion) {
        List<Individual> nuevaPoblacion = new ArrayList<>(poblacion);
        while (nuevaPoblacion.size() < POPULATION_SIZE) {
            Individual padre = seleccionarAleatorio(poblacion);
            Individual madre = seleccionarAleatorio(poblacion);

            if (random.nextDouble() < PROBABILIDAD_CRUZAMIENTO) {
                Individual hijo = cruzar(padre, madre);
                nuevaPoblacion.add(hijo);
            }
        }
        return nuevaPoblacion;
    }
}

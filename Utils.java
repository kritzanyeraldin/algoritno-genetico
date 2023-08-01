package concurrent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Utils {
    static final int POPULATION_SIZE = 20;
    static final int NUM_GENERACIONES = 300;
    static final double LOWER_BOUND = 0;
    static final double UPPER_BOUND = 20.0;
    static final double PROBABILIDAD_CRUZAMIENTO = 0.7;
    static final double PROBABILIDAD_MUTACION = 0.01;
    static final double PRECISION = 0.03;


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

    static String populationToString(List<Individual> population) {
        List<String> messages = new ArrayList<>();
        for (Individual individual :
                population) {
            messages.add(String.format("%f/%f/%f", individual.x, individual.y, individual.fitness));
        }

        return String.join(",", messages);
    }

    static List<Individual> stringToPopulation(String message) {
        String[] individuals = message.split(",");
        List<Individual> population = new ArrayList<>();

        for (String individual :
                individuals) {
            String[] data = individual.split("/");
            population.add(new Individual(Double.parseDouble(data[0]), Double.parseDouble(data[1])));
        }

        return population;
    }

    static double generateRandomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    static List<Individual> initializePopulation() {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            double x = generateRandomDouble(LOWER_BOUND, UPPER_BOUND);
            double y = generateRandomDouble(LOWER_BOUND, UPPER_BOUND);
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
        List<Individual> nuevaPoblacion = new ArrayList<>();

        while (nuevaPoblacion.size() < POPULATION_SIZE) {
            Individual padre = seleccionarAleatorio(poblacion);
            Individual madre = seleccionarAleatorio(poblacion);

            if (random.nextDouble() < PROBABILIDAD_CRUZAMIENTO) {
                Individual hijo = cruzar(padre, madre);
                nuevaPoblacion.add(hijo);
            }
        }

        populationToString(nuevaPoblacion);
        return nuevaPoblacion;
    }

    static void realizarMutacion(List<Individual> poblacion) {
        for (Individual individuo : poblacion) {
            if (random.nextDouble() < PROBABILIDAD_MUTACION) {
                mutar(individuo);
            }
        }
    }

    static void mutar(Individual individuo) {
        individuo.x += generateRandomDouble(-PRECISION, PRECISION);
        individuo.y += generateRandomDouble(-PRECISION, PRECISION);
    }

    static double funcion(double x, double y) {
        // Aquí debes definir la función que deseas optimizar
        // Ejemplo simple de prueba
        return Math.pow(x, 2) + Math.pow(y, 2);

        // Problema del final
        // return 1.23 * Math.sin((Math.pow(x, 2) + Math.pow(x, 2)) / 1.0563) * ((0.4 / (1 + 0.02 * (Math.pow(x + 20, 2) + Math.pow(y + 20, 2)))) + (0.2 / (1 + 0.5 * (Math.pow(x + 5, 2) + Math.pow(y + 25, 2)))) + (0.7 / (1 + 0.01 * (Math.pow(x, 2) + Math.pow(y - 30, 2)))) + (1 / (1 + 2 * (Math.pow(x + 30, 2) + Math.pow(y, 2)))) + (0.05 / (1 + 0.1 * (Math.pow(x - 30, 2) + Math.pow(y + 30, 2)))));
    }

    static void evaluarPoblacion(List<Individual> poblacion) {
        for (Individual individuo : poblacion) {
            individuo.fitness = funcion(individuo.x, individuo.y);
        }
    }

    static List<Individual> obtenerMejorPoblacion(List<List<Individual>> populations) {
        List<Double> averageScores = new ArrayList<>();

        for (List<Individual> population :
                populations) {
            double sum = 0;

            for (Individual individuo : population) {
                double fitness = funcion(individuo.x, individuo.y);
                individuo.fitness = fitness;
                sum += fitness;
            }

            averageScores.add(sum / population.size());
        }

        double maxAverageScore = averageScores.get(0);
        for (double averageScore :
                averageScores) {
            if (averageScore > maxAverageScore) {
                maxAverageScore = averageScore;
            }
        }

        return populations.get(averageScores.indexOf(maxAverageScore));
    }

    static List<Individual> seleccionarMejores(List<Individual> poblacion) {
        poblacion.sort((ind1, ind2) -> Double.compare(ind2.fitness, ind1.fitness));
        return poblacion.subList(0, POPULATION_SIZE / 2);
    }

    static Individual obtenerMejorIndividuo(List<Individual> poblacion) {
        return poblacion.stream()
                .max(Comparator.comparingDouble(ind -> ind.fitness))
                .orElse(null);
    }
}

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int NUM_INDIVIDUOS = 20;
    private static final int NUM_GENERACIONES = 400;
    private static final double PRECISION = 0.03;
    private static final double RANGO_MIN = 0;
    private static final double RANGO_MAX = 20.0;
    private static final double PROBABILIDAD_CRUZAMIENTO = 0.7;
    private static final double PROBABILIDAD_MUTACION = 0.01;

    private static final Random random = new Random();

    public static void main(String[] args) {
        List<Individuo> poblacion = inicializarPoblacion();

        for (int i = 0; i < NUM_GENERACIONES; i++) {
            poblacion = seleccionarMejores(poblacion);
            poblacion = realizarCruzamiento(poblacion);
            poblacion = realizarMutacion(poblacion);
            evaluarPoblacion(poblacion);
        }

        // Obtener el mejor individuo
        Individuo mejorIndividuo = obtenerMejorIndividuo(poblacion);
        System.out.println("El mejor individuo encontrado es:");
        System.out.println("x = " + mejorIndividuo.x);
        System.out.println("y = " + mejorIndividuo.y);
        System.out.println("Valor de la función: " + funcion(mejorIndividuo.x, mejorIndividuo.y));
    }

    // Clase que representa a un individuo
    static class Individuo {
        double x;
        double y;
        double fitness;

        Individuo(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    // Inicializa una población aleatoria de individuos
    private static List<Individuo> inicializarPoblacion() {
        List<Individuo> poblacion = new ArrayList<>();
        for (int i = 0; i < NUM_INDIVIDUOS; i++) {
            double x = generarAleatorio(RANGO_MIN, RANGO_MAX);
            double y = generarAleatorio(RANGO_MIN, RANGO_MAX);
            poblacion.add(new Individuo(x, y));
        }
        return poblacion;
    }

    // Genera un número aleatorio en un rango dado
    private static double generarAleatorio(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    // Evalúa la función objetivo para cada individuo de la población
    private static void evaluarPoblacion(List<Individuo> poblacion) {
        for (Individuo individuo : poblacion) {
            individuo.fitness = funcion(individuo.x, individuo.y);
        }
    }

    // Calcula el valor de la función objetivo para un par de valores (x, y)
    private static double funcion(double x, double y) {
        // Aquí debes definir la función que deseas optimizar
        // Por ejemplo: return x * x + y * y;
        return x * x + y * y;
    }

    // Selecciona los mejores individuos de la población actual
    private static List<Individuo> seleccionarMejores(List<Individuo> poblacion) {
        poblacion.sort((ind1, ind2) -> Double.compare(ind2.fitness, ind1.fitness));
        return poblacion.subList(0, NUM_INDIVIDUOS / 2);
    }

    // Realiza el cruzamiento entre los individuos de la población
    private static List<Individuo> realizarCruzamiento(List<Individuo> poblacion) {
        List<Individuo> nuevaPoblacion = new ArrayList<>(poblacion);
        while (nuevaPoblacion.size() < NUM_INDIVIDUOS) {
            Individuo padre = seleccionarAleatorio(poblacion);
            Individuo madre = seleccionarAleatorio(poblacion);

            if (random.nextDouble() < PROBABILIDAD_CRUZAMIENTO) {
                Individuo hijo = cruzar(padre, madre);
                nuevaPoblacion.add(hijo);
            }
        }
        return nuevaPoblacion;
    }

    // Realiza el cruzamiento entre dos individuos
    private static Individuo cruzar(Individuo padre, Individuo madre) {
        double x = (padre.x + madre.x) / 2.0;
        double y = (padre.y + madre.y) / 2.0;
        return new Individuo(x, y);
    }

    // Realiza la mutación en la población
    private static List<Individuo> realizarMutacion(List<Individuo> poblacion) {
        for (Individuo individuo : poblacion) {
            if (random.nextDouble() < PROBABILIDAD_MUTACION) {
                mutar(individuo);
            }
        }
        return poblacion;
    }

    // Realiza la mutación en un individuo
    private static void mutar(Individuo individuo) {
        individuo.x += generarAleatorio(-PRECISION, PRECISION);
        individuo.y += generarAleatorio(-PRECISION, PRECISION);
    }

    // Selecciona aleatoriamente un individuo de la población
    private static Individuo seleccionarAleatorio(List<Individuo> poblacion) {
        int index = random.nextInt(poblacion.size());
        return poblacion.get(index);
    }

    // Obtiene el mejor individuo de la población
    private static Individuo obtenerMejorIndividuo(List<Individuo> poblacion) {
        return poblacion.stream()
                .max(Comparator.comparingDouble(ind -> ind.fitness))
                .orElse(null);
    }
}

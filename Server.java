package concurrent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private final List<ClientThread> clients = new ArrayList<>();
    private String currentPopulation = Utils.populationToString(Utils.initializePopulation());
    private List<String> populations = new ArrayList<>();

    public String getCurrentPopulation() {
        return currentPopulation;
    }

    public void setCurrentPopulation(String currentPopulation) {
        this.currentPopulation = currentPopulation;
    }

    public void processBestPopulation() {
        // Obtenemos la mejor población en base a su score promedio
        List<List<Individual>> p = new ArrayList<>();

        for (String population :
                populations) {
            p.add(Utils.stringToPopulation(population));
        }

        List<Individual> bestPopulation = Utils.obtenerMejorPoblacion(p);
        setCurrentPopulation(Utils.populationToString(bestPopulation));

        // Limpiamos las poblaciones para la próxima iteración
        this.populations.clear();
    }

    public void addPopulation(String population) {
        this.populations.add(population);
    }

    public List<String> getPopulations() {
        return populations;
    }

    public void setPopulations(List<String> populations) {
        this.populations = populations;
    }

    private void stop() throws IOException {
        serverSocket.close();
    }

    private void start(int port, int clientsSize) {

        try {
            serverSocket = new ServerSocket(port);
            Utils.serverLogger("Corriendo en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                int clientIndex = clients.size();
                ClientThread clientThread = new ClientThread(clientSocket, clientIndex, this, currentPopulation);
                clients.add(clientThread);

                Utils.serverLogger("Nuevo cliente conectado (clientes conectados: " + (clientIndex + 1) + ")");

                if (clientsSize - 1 == clientIndex) break;
            }
            int generation = 0;

            long startTime=System.currentTimeMillis();
            while (generation < Utils.NUM_GENERACIONES) {
                System.out.println();
                System.out.println("Generación número " + generation);
                Utils.serverLogger("Mandando población: " + currentPopulation);

                // Inicia el ciclo de comunicación con los clientes
                for (ClientThread client : clients) {
                    client.run();
                }

                // Espera a que todos los clientes terminen su ejecución
                for (ClientThread client : clients) {
                    client.join();
                }

                generation++;
            }
            long endTime = System.currentTimeMillis();
            double elapsedTimeSeconds = (endTime - startTime) / 1000.0;

            List<Individual> p = Utils.stringToPopulation(currentPopulation);
            Individual mejorIndividuo = Utils.obtenerMejorIndividuo(p);
            System.out.println();
            System.out.println("El mejor individuo encontrado es:");
            System.out.println("x = " + mejorIndividuo.x);
            System.out.println("y = " + mejorIndividuo.y);
            System.out.println("Valor de la función: " + Utils.funcion(mejorIndividuo.x, mejorIndividuo.y));
            System.out.println("Tiempo final:"+ elapsedTimeSeconds);
        } catch (IOException e) {
            Utils.serverLogger("Error al iniciar el servidor: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Server().start(4444, 2);
    }
}

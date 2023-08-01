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
        this.currentPopulation = this.populations.get(0);

        // Limpiamos las poblaciones para la próxima iteración
//      this.populations.clear();
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

    private void start(int port) {

        try {
            int clientsSize = 1;
            serverSocket = new ServerSocket(port);
            Utils.serverLogger("Corriendo en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                int clientIndex = clients.size();
                ClientThread clientThread = new ClientThread(clientSocket, clientIndex, this);
                clients.add(clientThread);

                Utils.serverLogger("Nuevo cliente conectado (clientes conectados: " + (clientIndex + 1) + ")");

                if (clientsSize - 1 == clientIndex) break;
            }
            int cycleCount = 0;
            while (cycleCount < Utils.NUM_GENERACIONES) {
                Utils.serverLogger("Mandando población inicial: " + currentPopulation);

                // Inicia el ciclo de comunicación con los clientes
                for (ClientThread client : clients) {
                    client.run();
                }

                // Espera a que todos los clientes terminen su ejecución
                for (ClientThread client : clients) {
                    client.join();
                }

                // Elegir los mejores y actualizar la población actual
                //processBestPopulation();

                cycleCount++;
            }


            // Elegir los mejores y lo actualiza
//        for (ClientThread client:
//                clients) {
//          String population = client.getPopulation();
//          client.setPopulation(population);
//        }
        } catch (IOException e) {
            Utils.serverLogger("Error al iniciar el servidor: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Server().start(4444);
    }
}

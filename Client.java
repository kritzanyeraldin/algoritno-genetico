import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private boolean startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            return true;
        } catch (Exception e) {
            System.out.println("Error starting the connection: " + e.getMessage());

            return false;
        }
    }

    private void sendMessage(Object msg) {
        out.println(msg);


    }

    private String receiveMessage() throws IOException {
        return in.readLine();
    }

    private boolean stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();

            return true;
        } catch (IOException e) {
            System.out.println("Error stopping the connection: " + e.getMessage());

            return false;
        }
    }

    static public void create() throws IOException {
        Client client = new Client();
        boolean clientConnected = client.startConnection("127.0.0.1", 4444);

        if (!clientConnected) {
            Utils.clientLogger("Error al conectarse al servidor");
            return;
        }

        Utils.clientLogger("Se conectó al servidor correctamente");


        for (int i = 0; i < Utils.NUM_GENERACIONES; i++) {
            Utils.clientLogger("Esperando la población");
            String populationAsString = client.receiveMessage();
            Utils.serverLogger(populationAsString);
            Utils.clientLogger(populationAsString);


            List<Individual> population = Utils.stringToPopulation(populationAsString);
            Utils.evaluarPoblacion(population);
            Utils.seleccionarMejores(population);
            population = Utils.realizarCruzamiento(population);
            Utils.realizarMutacion(population);
            populationAsString = Utils.populationToString(population);
            System.out.println("Enviando...");
            Utils.clientLogger(populationAsString);
            client.sendMessage(populationAsString);
        }
    }

    public static void main(String[] args) throws IOException {
        create();
    }
}

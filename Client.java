package concurrent;

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

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        boolean clientConnected = client.startConnection("127.0.0.1", 4444);

        if (!clientConnected) {
            Utils.clientLogger("Error al conectarse al servidor");
            return;
        }

        Utils.clientLogger("Se conectó al servidor correctamente");


        for(int i=0; i<Utils.NUM_GENERACIONES;i++) {
            Utils.clientLogger("Esperando la población");
            String populationAsString = client.receiveMessage();

            Utils.serverLogger(populationAsString);
            List<Individual> population = Utils.stringToPopulation(populationAsString);
            population = Utils.realizarCruzamiento(population);
            populationAsString = Utils.populationToString(population);

            Utils.clientLogger(populationAsString);
            client.sendMessage(populationAsString);
        }
    }
}

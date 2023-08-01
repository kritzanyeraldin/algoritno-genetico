package concurrent;

import concurrent.Server;
import concurrent.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientThread extends Thread {
    private final int id;
    private final Socket clientSocket;
    private final Server server;

    public ClientThread(Socket socket, int id, Server server) {
        clientSocket = socket;
        this.id = id;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));



            //manda al cliente
            out.println(server.getCurrentPopulation());

            //lee mensaje de cliente
            String incomingPopulation = in.readLine();

            if (incomingPopulation != null) {
                Utils.clientLogger(id, incomingPopulation);
                server.setCurrentPopulation(incomingPopulation);
            }





//            while ((incomingPopulation = in.readLine()) != null) {
//                Utils.clientLogger(id, incomingPopulation);
//                server.addPopulation(incomingPopulation);
//            System.out.println("asdasdasdasdasd");
//            }

//                server.processBestPopulation();
//        out.println(incomingPopulation);
//      Utils.clientLogger(id, "Desconectado del servidor");
//      in.close();
//      out.close();
//      clientSocket.close();
        } catch (IOException e) {
            Utils.clientLogger(id, "Ocurrió un error en el cliente: " + e.getMessage());
        }
    }
}

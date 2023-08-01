package concurrent;

import java.io.IOException;

public class ClientsHandler {
    public static void main(String[] args) {
        int clientsSize = 2;

        for (int i = 0; i < clientsSize; i++) {
            Thread thread = new Thread(() -> {
                try {
                    Client.create();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            thread.start();
        }
    }
}

package dadarkwizard.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

class ThreadAcceptor implements Runnable {

    private ServerSocket serverSocket;
    private boolean exit = false;
    private HashMap<Integer, Client> clients = new HashMap<>();

    public ThreadAcceptor(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                Socket socket = serverSocket.accept();
                Client client = new Client(socket);
                client.start();
                clients.put(client.getId(), client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        exit = true;
        for (Client client : clients.values()) {
            client.stop();
        }
    }

    public HashMap<Integer, Client> getClients() {
        return clients;
    }
}

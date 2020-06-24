package dadarkwizard.server;

import dadarkwizard.server.exceptions.BadConfigurationException;
import dadarkwizard.server.messages.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server implements Runnable {
    private int port;
    private InetAddress address;
    private ThreadAcceptor threadAcceptor;
    private boolean exit = false;
    private BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    public Server(int port) {
        this.port = port;
        try {
            this.address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new BadConfigurationException("No local address!");
        }
    }

    public Server(int port, InetAddress address) {
        this.port = port;
        this.address = address;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port, 50, address);
            threadAcceptor = new ThreadAcceptor(serverSocket);
            threadAcceptor.start();
            Thread thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!exit) {
            for (Client client : threadAcceptor.getClients().values()) {
                Message message = client.getMessages().poll();
                while (message != null) {
                    input.offer(message);
                    message = client.getMessages().poll();
                }
            }
        }
    }

    public void stop() {
        exit = true;
        threadAcceptor.stop();
    }

    public BlockingQueue<Message> getMessages() {
        return input;
    }
}

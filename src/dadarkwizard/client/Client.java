package dadarkwizard.client;

import dadarkwizard.messages.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client implements Runnable {
    private Socket socket;
    private BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private ReadInput inputReader;
    private boolean exit = false;

    private InetAddress address;
    private int port;

    public Client(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(address, port);
            inputReader = new ReadInput(socket.getInputStream());
            inputReader.start();
            Thread thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                Message message = output.take();
                socket.getOutputStream().write(message.getBytes());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            socket.getOutputStream().close();
            socket.getInputStream().close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BlockingQueue<Message> getInputMessages() {
        return inputReader.getInputMessages();
    }

    public void sendMessage(Message message) {
        this.output.offer(message);
    }

    public void stop() {
        this.exit = true;
    }
}

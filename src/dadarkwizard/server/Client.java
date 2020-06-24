package dadarkwizard.server;

import dadarkwizard.messages.FatalErrorMessage;
import dadarkwizard.messages.Message;
import dadarkwizard.messages.MessageType;
import dadarkwizard.messages.encoding.Encoder;

import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


class Client implements Runnable {

    //region variables

    private static PriorityQueue<Integer> freeIds = new PriorityQueue<>();
    private static int lastID = 0;

    private int id;
    private Socket socket;
    private boolean exit = false;

    private LinkedBlockingQueue<Message> input = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Message> output = new LinkedBlockingQueue<>();

    private boolean receivingMessage = false;
    private int nextMessageLength;
    private MessageType nextMessageType;

    //endregion

    //region constructors

    public Client(Socket socket) {
        if (freeIds.isEmpty()) {
            id = lastID;
            lastID++;
        } else {
            id = freeIds.poll();
        }
    }

    //endregion

    //region threading

    public void start() {
        Thread worker = new Thread(this);
        worker.start();
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                //region receive

                if (!receivingMessage) {
                    if (socket.getInputStream().available() > 4) {
                        byte[] info = new byte[5];
                        if (socket.getInputStream().read(info) < 5) {
                            throw new IOException();
                        }
                        Encoder encoder = new Encoder(info);
                        nextMessageLength = encoder.getInt();
                        nextMessageType = MessageType.getMessageType(encoder.get());
                        receivingMessage = true;
                    }
                }

                if (receivingMessage && socket.getInputStream().available() >= nextMessageLength - 5) {
                    byte[] bytes = new byte[nextMessageLength - 5];
                    if (socket.getInputStream().read(bytes) != bytes.length) {
                        throw new IOException();
                    }

                    Message message = (Message) nextMessageType.getMessageClass()
                            .getConstructor(byte[].class)
                            .newInstance((Object) bytes);

                    boolean received = input.offer(message);
                    while (!received) {
                        received = input.offer(message);
                    }
                    receivingMessage = false;
                }

                //endregion

                //region send

                if (output.peek() != null) {
                    try {
                        Message message = output.take();
                        socket.getOutputStream().write(message.getBytes());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //endregion

            } catch (IOException e) {
                e.printStackTrace();
                socket = null;
                stop();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.exit = true;
        freeIds.add(this.id);
    }

    //endregion

    public void sendMessage(Message message) {
        boolean sent = false;
        while (!sent) {
            sent = output.offer(message);
        }
    }

    public BlockingQueue<Message> getMessages() {
        return input;
    }

    public int getId() {
        return this.id;
    }

}

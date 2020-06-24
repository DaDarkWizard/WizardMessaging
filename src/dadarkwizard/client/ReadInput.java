package dadarkwizard.client;

import dadarkwizard.messages.Message;
import dadarkwizard.messages.MessageType;
import dadarkwizard.messages.encoding.Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ReadInput implements Runnable {

    private BlockingQueue<Message> input = new LinkedBlockingQueue<>();
    private InputStream stream;
    private boolean exit = false;

    private int messageLength;
    private MessageType messageType;
    private boolean receivingMessage = false;

    public ReadInput(InputStream stream) {
        this.stream = stream;
    }

    public BlockingQueue<Message> getInputMessages() {
        return input;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                if (!receivingMessage) {
                    if (stream.available() >= 5) {
                        byte[] messageStart = new byte[5];
                        if (stream.read(messageStart) == 5) {
                            Encoder encoder = new Encoder(messageStart);
                            this.messageLength = encoder.getInt();
                            this.messageType = MessageType.getMessageType(encoder.get());
                            receivingMessage = true;
                        } else {
                            throw new IOException("Not enough read!");
                        }
                    }
                }

                if (receivingMessage) {
                    if (stream.available() >= messageLength - 5) {
                        byte[] messageBytes = new byte[messageLength - 5];
                        if (stream.read(messageBytes) == messageBytes.length) {
                            Message newMessage = (Message) messageType.getMessageClass()
                                    .getConstructor(byte[].class)
                                    .newInstance((Object) messageBytes);
                            boolean received = input.offer(newMessage);
                            while (!received) {
                                received = input.offer(newMessage);
                            }
                            receivingMessage = false;
                        } else {
                            throw new IOException("Not enough read!");
                        }
                    }
                }
            } catch (IOException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        exit = true;
    }
}

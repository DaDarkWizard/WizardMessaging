import dadarkwizard.messages.FatalErrorMessage;
import dadarkwizard.messages.Message;
import dadarkwizard.server.Server;

import java.net.Inet4Address;
import java.net.InetAddress;

public class MainServer {
    public static void main(String[] args) {
        Server server = new Server(25565);
        server.start();
        while (true) {
            //Do fancy server logic

            long startReadingTime = System.currentTimeMillis();
            //Read messages
            while (!server.getMessages().isEmpty() && System.currentTimeMillis() - startReadingTime < 1000) {
                Message message = server.getMessages().poll();
                //Do something with the message
            }

            Message message = new FatalErrorMessage("I haven't actually programmed a server, so go away.");
            server.sendEveryoneMessage(message);
        }
    }
}

package za.nmu.wrpv;

import za.nmu.wrpv.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientHandler {
    private final Socket client;
    private final int clientID;

    private ObjectInputStream ois;
    private ObjectOutputStream ous;

    private final BlockingQueue<Message> clientMessages;

    private final ClientReader clientReader;
    private ClientWriter clientWriter;
    public ClientHandler(Socket client, int clientID) {
        this.client = client;
        this.clientID = clientID;
        clientMessages = new LinkedBlockingDeque<>();

        clientReader = new ClientReader();
        clientReader.start();
    }

    private class ClientReader extends Thread{
        @Override
        public void run() {
            try {
                ous = new ObjectOutputStream(client.getOutputStream());
                ous.flush();
                ois = new ObjectInputStream(client.getInputStream());

                 clientWriter = new ClientWriter();
                 clientWriter.start();

                Message msg;
                do {
                    msg = (Message) ois.readObject();
                    msg.apply(ClientHandler.this);
                    if (msg instanceof Subscribe)
                        System.out.println(">>> SUBSCRIBED -> clientID = " + clientID + ", topic -> " + ((Subscribe) msg).topic);

                }while (true);
            } catch (IOException | ClassNotFoundException e) {
                ClientHandler.this.stop();
                e.printStackTrace();
            }
            finally {
                if (clientWriter != null)
                    clientWriter.interrupt();
            }
        }
    }

    private class ClientWriter extends Thread {
        @Override
        public void run() {
            try {
                do {
                    Message message = clientMessages.take();
                    System.out.println(">>> Publish -> clientID = " + clientID);
                    ous.writeObject(message);
                    ous.flush();

                }while (true);
            } catch (InterruptedException e) {
                e.printStackTrace();
                clientWriter = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void publish(Message<ClientHandler> message) {
        clientMessages.add(message);
    }

    public void stop() {
        System.out.println(">>> Stop -> clientID = " + getClientID());
        clientReader.interrupt();
    }

    public int getClientID() {
        return clientID;
    }
}

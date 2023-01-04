package za.nmu.wrpv;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) throws IOException {
        new Server();
    }
    public static final List<ClientHandler> handlers = new ArrayList<>();
    Server() throws IOException {
        int clientID = 0;
        ServerSocket server = new ServerSocket(5051);
        System.out.printf(">>> RUNNING -> port = %d",server.getLocalPort());

        do {
            Socket client = server.accept();
            clientID++;

            System.out.println("\n>>> CONNECTED -> clientID = " + clientID);

            handlers.add(new ClientHandler(client, clientID));
        } while (true);
    }

    public static byte[] toByte(String fileName) {
        File file = new File(fileName);
        if (file.exists() && !file.isDirectory()) {
            try {
                return Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

package za.nmu.wrpv;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Server {
    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException {
        new Server();
    }

    Server() throws IOException {
        int clientID = 0;
        ServerSocket server = new ServerSocket(5050);

        MenuItems.add(new Item("Cappuccino", "An espresso-based coffee drink.", toByte("cappuccino.jpg"), "cappuccino.jpg", 12.15, 0));
        MenuItems.add(new Item("Latte", "A shot of espresso and steamed milk with just a touch of foam.", toByte("latte.jpg"), "latte.jpg", 13.15, 0));
        MenuItems.add(new Item("Americano", "An espresso shot diluted in hot water.", toByte("americano.jpg"), "americano.jpg", 11.15, 0));
        MenuItems.add(new Item("Espresso", "An espresso shot can be served solo or used as the foundation.", null, "", 15.15, 0));
        MenuItems.add(new Item("Doppio", "A double shot of espresso.", null, "", 9.15, 0));
        MenuItems.add(new Item("Cortado", "A cortado is the perfect balance of espresso and warm steamed milk.", null, "", 19.15, 0));
        MenuItems.add(new Item("Red Eye", "A full cup of hot coffee with an espresso shot mixed in.", null, "", 14.15, 0));
        MenuItems.add(new Item("Galão", "Closely related to the latte and cappuccino but contains about twice as much foamed milk.", null, "", 13.15, 0));
        MenuItems.add(new Item("Lungo", "A lungo is a long-pull espresso.", null, "", 17.15, 0));
        MenuItems.add(new Item("Macchiato", "A happy medium between a cappuccino and a doppio.", null, "", 16.15, 0));

        System.out.printf(">>> RUNNING -> port = %d",server.getLocalPort());

        do {
            Socket client = server.accept();
            clientID++;

            System.out.println("\n>>> CONNECTED -> clientID = " + clientID);

            new ClientHandler(client, clientID);
        } while (true);
    }

    private static byte[] toByte(String fileName) {
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

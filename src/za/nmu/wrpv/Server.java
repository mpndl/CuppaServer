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
    public static void main(String[] args) throws IOException {
        new Server();
    }

    Server() throws IOException {
        int clientID = 0;
        ServerSocket server = new ServerSocket(5051);

        System.out.printf(">>> RUNNING -> port = %d",server.getLocalPort());

        do {
            Socket client = server.accept();
            clientID++;

            System.out.println("\n>>> CONNECTED -> clientID = " + clientID);

            new ClientHandler(client, clientID);
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

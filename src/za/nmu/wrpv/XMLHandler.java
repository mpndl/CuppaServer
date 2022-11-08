package za.nmu.wrpv;

import org.w3c.dom.DOMImplementation;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class XMLHandler {
    private final static String fileName = "log.xml";
    private final static String elementName = "orders";

    public static void appendToXML(Order order) throws FileNotFoundException, TransformerException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (!new File(fileName).exists())
                document = builder.newDocument();
            else document = builder.parse(new File(fileName));

            Element root = document.getDocumentElement();

            if (root == null) {
                root = document.createElement(elementName);
                document.appendChild(root);
            }

            Element element = createOrderElement(document, order.dateTime, order.telNum, order.items, order.total);
            root.appendChild(element);
            writeToXML(document, new FileOutputStream(fileName));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static void createXMLDocument() throws ParserConfigurationException, FileNotFoundException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        writeToXML(document, new FileOutputStream(fileName));
    }

    public static Element createTextElement(Document doc, String name, String text) {
        Text textNode = doc.createTextNode(text);
        Element element = doc.createElement(name);
        element.appendChild(textNode);
        return element;
    }

    public static Element createItemElement(Document doc, String name, String description, String imageName, double cost) {
        Element nameText = createTextElement(doc, "name", name);
        Element descriptionText = createTextElement(doc, "description", description);
        Element imageNameText = createTextElement(doc, "imageName", imageName);
        Element costText = createTextElement(doc, "cost", cost + "");

        Element itemElement = doc.createElement("item");
        itemElement.appendChild(nameText);
        itemElement.appendChild(descriptionText);
        itemElement.appendChild(imageNameText);
        itemElement.appendChild(costText);
        return itemElement;
    }

    public static Element createOrderElement(Document doc, Date date, String telNum, List<Item> items, double total) {
        String t = new SimpleDateFormat("HH:mm").format(date);
        String d = new SimpleDateFormat("yyyy-MM-dd").format(date);

        Element orderElement = doc.createElement(elementName);
        Element itemsElement = doc.createElement("items");
        for (Item item: items) {
            Element itemElement = createItemElement(doc, item.name, item.description, item.imageName, item.cost);
            itemsElement.appendChild(itemElement);
        }
        Element dateText = createTextElement(doc, "date", d);
        Element timeText = createTextElement(doc, "time", t);
        Element telNumText = createTextElement(doc, "telephoneNumber", telNum);
        Element totalText = createTextElement(doc, "total", total+ "");

        orderElement.appendChild(dateText);
        orderElement.appendChild(timeText);
        orderElement.appendChild(telNumText);
        orderElement.appendChild(itemsElement);
        orderElement.appendChild(totalText);

        return orderElement;
    }

    public static void writeToXML(Document doc, FileOutputStream fos) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        transformer.transform(new DOMSource(doc), new StreamResult(fos));
    }
}

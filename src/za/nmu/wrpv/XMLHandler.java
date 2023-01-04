package za.nmu.wrpv;

import org.w3c.dom.*;
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
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static za.nmu.wrpv.Helpers.*;

public class XMLHandler {
    private final static String fileName = "log.xml";
    private final static String elementName = "orders";

    public synchronized static List<Item> loadMenuFromXML(String fileName, Rate rate) {
        List<Item> items = new ArrayList<>();
        if (fileExists(fileName)) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new File(fileName));

                XPath xPath = XPathFactory.newInstance().newXPath();
                XPathExpression xPathExpression = xPath.compile("//item");
                NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    String description = element.getElementsByTagName("description").item(0).getTextContent();
                    String cost = element.getElementsByTagName("cost").item(0).getTextContent();

                    String imageName = "";
                    NodeList nl = element.getElementsByTagName("imageName");
                    if (nl.getLength() > 0) {
                        Node imgNode = nl.item(0);
                        if (imgNode != null) {
                            imageName = element.getElementsByTagName("imageName").item(0).getTextContent();
                        }
                    }
                    byte[] byteImage = Server.toByte(imageName);

                    double cD = rate.oneCur * Double.parseDouble(cost);

                    Item item = new Item(name, description, byteImage, imageName, cD, 0);
                    item.currencyCode = rate.code;

                    items.add(item);
                }
            } catch (XPathExpressionException | SAXException | ParserConfigurationException | IOException e) {
                e.printStackTrace();
            }
        }
        return items;
    }

    public synchronized static void appendToXML(Order order) throws FileNotFoundException, TransformerException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
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

    public synchronized static Element createTextElement(Document doc, String name, String text) {
        Text textNode = doc.createTextNode(text);
        Element element = doc.createElement(name);
        element.appendChild(textNode);
        return element;
    }

    public synchronized static Element createItemElement(Document doc, String name, String description, String imageName, double cost) {
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

    public synchronized static Element createOrderElement(Document doc, LocalDateTime dateTime, String telNum, List<Item> items, double total) {
        String t = getDefaultFormattedDate(dateTime);
        String d = getDefaultFormattedTime(dateTime);

        String currencyCode = items.get(0).currencyCode;

        Element orderElement = doc.createElement("order");
        Element itemsElement = doc.createElement("items");
        for (Item item: items) {
            Element itemElement = createItemElement(doc, item.name, item.description, item.imageName, item.cost);
            itemsElement.appendChild(itemElement);
        }
        Element dateText = createTextElement(doc, "date", d);
        Element timeText = createTextElement(doc, "time", t);
        Element telNumText = createTextElement(doc, "telephoneNumber", telNum);
        Element totalText = createTextElement(doc, "total", total+ "");
        Element currencyCodeText = createTextElement(doc, "currencyCode", currencyCode+ "");

        orderElement.appendChild(dateText);
        orderElement.appendChild(timeText);
        orderElement.appendChild(telNumText);
        orderElement.appendChild(itemsElement);
        orderElement.appendChild(totalText);
        orderElement.appendChild(currencyCodeText);

        return orderElement;
    }

    public synchronized static void writeToXML(Document doc, FileOutputStream fos) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        transformer.transform(new DOMSource(doc), new StreamResult(fos));
    }
}

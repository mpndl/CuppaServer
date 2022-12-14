package za.nmu.wrpv.messages;

import za.nmu.wrpv.ClientHandler;
import za.nmu.wrpv.Order;
import za.nmu.wrpv.XMLHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderPublish extends Publish implements Serializable {
    @Serial
    private final static long serialVersionUID = 41L;
    public static final String key = "order";
    public static Map<String, Thread> ackTreads = new ConcurrentHashMap<>();
    public static final long sleepDuration = 10000;

    public OrderPublish(Object publisher, String topic, Map<String, Object> params) {
        super(publisher, topic, params);
    }

    @Override
    public void apply(ClientHandler handler) {
        Order order = (Order)params.get(key);
        System.out.println(">>> Order " + publisher + " Received");
        order.items.forEach(item -> System.out.println("\t" + item.name + " -> subtotal=" + item.cost * item.quantity));
        System.out.println("\tTOTAL=" + order.total);
        try {
            XMLHandler.appendToXML(order);
        } catch (TransformerException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }

        Thread ackTread = new Thread(() -> {
            try {
                do {
                    Thread.sleep(sleepDuration);
                    handler.publish(new OrderReadyPublish(publisher, OrderReadyPublish.key + publisher, null));
                    System.out.println(">>> Notification Sent for Order "+publisher+" -> clientID = " + handler.getClientID());
                } while (true);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ackTreads.put(publisher + "" + handler.getClientID(), ackTread);
        System.out.println(">>> Processing Order " + publisher);
        ackTread.start();
    }
}

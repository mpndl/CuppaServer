package za.nmu.wrpv.messages;

import za.nmu.wrpv.ClientHandler;
import za.nmu.wrpv.Order;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public class OrderAcknowledgedPublish extends Publish implements Serializable {
    @Serial
    private final static long serialVersionUID = 51L;
    public static final String key = "acknowledge";
    public OrderAcknowledgedPublish(Object publisher, String topic, Map<String, Object> params) {
        super(publisher, topic, params);
    }

    @Override
    public void apply(ClientHandler handler) {
        System.out.print(">>> Seeking Thread For Order " + publisher + " -> ");
        System.out.println(OrderPublish.ackTreads.get(publisher.toString()));

        if (OrderPublish.ackTreads.containsKey(publisher.toString())) {
            OrderPublish.ackTreads.get(publisher.toString()).interrupt();
            OrderPublish.ackTreads.remove(publisher.toString());
        }
    }
}

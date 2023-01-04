package za.nmu.wrpv.messages;

import za.nmu.wrpv.Subscriber;

import java.io.Serial;
import java.io.Serializable;

public class OrderAcknowledgedSubscribe extends Subscribe implements Serializable {
    @Serial
    private static final long serialVersionUID = 50L;
    public static final String key = "acknowledge";
    public OrderAcknowledgedSubscribe(String topic, Subscriber subscriber) {
        super(topic, subscriber);
    }
}

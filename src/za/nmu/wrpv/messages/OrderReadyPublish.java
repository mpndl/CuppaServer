package za.nmu.wrpv.messages;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public class OrderReadyPublish extends Publish implements Serializable {
    public static final String key = "orderReady";
    @Serial
    private static final long serialVersionUID = 50L;
    public OrderReadyPublish(Object publisher, String topic, Map<String, Object> params) {
        super(publisher, topic, params);
    }
}

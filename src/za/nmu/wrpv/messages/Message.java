package za.nmu.wrpv.messages;

import java.io.Serial;
import java.io.Serializable;

public abstract class Message<H> implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    public static final String key = "message";
    public void apply(H handler) {}

    @Override
    public String toString() {
        return key;
    }
}

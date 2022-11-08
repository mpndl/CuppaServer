package za.nmu.wrpv.messages;

import za.nmu.wrpv.ClientHandler;

import java.io.Serial;
import java.util.List;

public class Stop extends Message<ClientHandler> {
    @Serial
    private static final long serialVersionUID = 4L;

    @Override
    public void apply(ClientHandler handler) {
        handler.stop();
        OrderPublish.ackTreads.values().forEach(Thread::interrupt);
    }
}

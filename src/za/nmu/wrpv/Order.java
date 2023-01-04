package za.nmu.wrpv;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Order implements Serializable {
    @Serial
    private final static long serialVersionUID = 42L;
    public static int id = -1;
    public LocalDateTime dateTime;
    public String telNum;
    public List<Item> items;
    public double total;
    public boolean acknowledged = false;
    public boolean ready = false;
}

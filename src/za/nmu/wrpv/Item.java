package za.nmu.wrpv;

import java.io.Serial;
import java.io.Serializable;

public class Item implements Serializable {
    @Serial
    private final static long serialVersionUID = 20L;
    public String name;
    public String description;
    public byte[] image;
    public String imageName;
    public double cost;
    public String currencyCode = "ZAR";
    public int quantity;

    public Item(String name, String description, byte[] image, String imageName, double cost, int quantity) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.imageName = imageName;
        this.cost = cost;
        this.quantity = quantity;
    }
}

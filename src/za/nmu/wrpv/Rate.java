package za.nmu.wrpv;

import java.io.IOException;
import java.util.Optional;

import static za.nmu.wrpv.Helpers.findAnyLineFromFile;

public class Rate {
    public final String description;
    public final String code;
    public final double oneCur;
    public final double inCur;

    public Rate(String description, String code, double oneCur, double inCur) {
        this.description = description;
        this.code = code;
        this.oneCur = oneCur;
        this.inCur = inCur;
    }

    public static Rate getRate(String file, String code) {
        try {
            Optional<String> line = findAnyLineFromFile(file, l -> l.split(",")[1].equals(code));
            return line.map(l -> {
                        String[] cells = l.split(",");
                        return new Rate(cells[0], code, Double.parseDouble(cells[2]), Double.parseDouble(cells[3]));
                    })
                    .orElseThrow(IOException::new);
        } catch (IOException e) {
            return new Rate("S.A. Rands", code, 1D, 1D);
        }
    }
}

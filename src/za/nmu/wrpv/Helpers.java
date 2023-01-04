package za.nmu.wrpv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Predicate;

public class Helpers {
    public static String getDefaultFormattedDate(LocalDateTime dateTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.from(dateTime).format(dateFormatter);
    }

    public static String getDefaultFormattedTime(LocalDateTime dateTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.from(dateTime).format(timeFormatter);
    }

    public static Optional<String> findAnyLineFromFile(String file, Predicate<String> predicate) throws IOException {
        return Files.lines(Paths.get(file))
                .filter(predicate)
                .findAny();
    }
    public synchronized static boolean fileExists(String filename) {
        File file = new File(filename);
        return file.exists();
    }
}

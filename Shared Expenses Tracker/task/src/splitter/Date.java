package splitter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Date {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    static int index;

    static LocalDate getDate(List<String> input) {
        LocalDate date;
        if (input.get(0).matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
            date = LocalDate.parse(input.get(0), formatter);
            index = 1;
        } else {
            date = LocalDate.now();
        }
        return date;
    }
}
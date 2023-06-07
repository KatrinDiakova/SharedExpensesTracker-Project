package splitter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CommandParser {
    private static final String INPUT_DELIMITER = " ";

    private static final Balance balance = new Balance();
    private static final BorrowRepay borrowRepay = new BorrowRepay();
    private static final Group group = new Group();
    private static final Purchase purchase = new Purchase();
    private static final Map<String, BigDecimal> balanceAmount = new HashMap<>();
    private static final Map<String, ArrayList<BalanceHistory>> balanceHistory = new HashMap<>();

    boolean parseUserInput(String input) {
        var command = Command.of(input);

        var split = List.of(input.split(INPUT_DELIMITER));
        switch (command) {
            case exit -> {
                return false;
            }
            case help -> Stream.of(Command.values()).forEach(System.out::println);
            case group -> group.parceData(split);
            case borrow, repay -> borrowRepay.parceData(split, balanceAmount, balanceHistory);
            case balance -> balance.parceData(split, balanceAmount, balanceHistory);
            case purchase -> purchase.parceData(split, balanceAmount, balanceHistory);
        }
        return true;
    }
}

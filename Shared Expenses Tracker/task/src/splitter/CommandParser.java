package splitter;

import splitter.command.BalanceProcessor;
import splitter.command.Command;
import splitter.command.CommandProcessor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CommandParser {
    private static final String INPUT_DELIMITER = " ";

    private final BorrowRepay borrowRepay = new BorrowRepay();
    private final Group group = new Group();
    private final Purchase purchase = new Purchase();
    private final Map<String, BigDecimal> balanceAmount = new HashMap<>();
    private final Map<String, ArrayList<BalanceHistory>> balanceHistory = new HashMap<>();

    private final Map<Command, CommandProcessor> processors = Map.of(
            Command.group, group::parceData,
            Command.help, (List<String> inputs) -> Stream.of(Command.values()).forEach(System.out::println),
            Command.borrow, (List<String> inputs) -> borrowRepay.parceData(inputs, balanceAmount, balanceHistory),
            Command.repay, (List<String> inputs) -> borrowRepay.parceData(inputs, balanceAmount, balanceHistory),
            Command.balance, new BalanceProcessor(),
            Command.purchase, (List<String> inputs) -> purchase.parceData(inputs, balanceAmount, balanceHistory)
    );

    boolean parseUserInput(String input) {
        var command = Command.of(input);
        if (command == Command.exit) {
            return false;
        }
        var inputList = List.of(input.split(INPUT_DELIMITER));
        processors.getOrDefault(command, (List<String> inputs) -> System.err.println()).process(inputList);
        return true;
    }
}

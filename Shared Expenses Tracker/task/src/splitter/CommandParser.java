package splitter;

import java.math.BigDecimal;
import java.util.*;

public class CommandParser {

    private static Balance balance = new Balance();
    private static BorrowRepay borrowRepay = new BorrowRepay();
    private static Group group = new Group();
    private static Purchase purchase = new Purchase();
    private static Map<String, BigDecimal> balanceAmount = new HashMap<>();
    private static Map<String, ArrayList<BalanceHistory>> balanceHistory = new HashMap<>();

    static void parseUserInput(List<String> input) {
        if (input.contains("exit")) {
            System.exit(0);
        } else if (input.contains("help")) {
            printHelp();
        } else if (input.contains("borrow") || input.contains("repay")) {
            borrowRepay.parceData(input, balanceAmount, balanceHistory);
        } else if (input.contains("balance")) {
            balance.parceData(input, balanceAmount, balanceHistory);
        } else if (input.contains("group")) {
            group.parceData(input);
        } else if (input.contains("purchase")) {
            purchase.parceData(input, balanceAmount, balanceHistory);
        } else {
            System.out.println("Unknown command. Print help to show commands list");
        }
    }

    static void printHelp() {
        List<String> menu = List.of(
                "balance",
                "borrow",
                "exit",
                "group",
                "help",
                "purchase",
                "repay"
        );

        for (String m : menu) {
            System.out.println(m);
        }
    }
}

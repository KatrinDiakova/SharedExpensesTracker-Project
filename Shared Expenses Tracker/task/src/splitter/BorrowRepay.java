package splitter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BorrowRepay {

    // длинный список аргументов, переместить списки в отдельные классы
    public void parceData(List<String> input, Map<String, BigDecimal> balanceAmount,
                          Map<String, ArrayList<BalanceHistory>> balanceHistory) {
        try {
            Date.index = 0;
            LocalDate date = Date.getDate(input);
            String type = input.get(Date.index);
            String personOne = input.get(Date.index + 1);
            String personTwo = input.get(Date.index + 2);
            BigDecimal amount = new BigDecimal(input.get(Date.index + 3)).setScale(2, RoundingMode.DOWN);
            String[] names = {personOne, personTwo};
            String nameKey = names[0] + "-" + names[1];
            Arrays.sort(names);
            String nameKeySort = names[0] + "-" + names[1];

            processTransaction(type, amount, nameKeySort, date, nameKey, balanceAmount, balanceHistory);
        } catch (Exception e) {
            System.out.println("Illegal command arguments");
        }
    }
     // слишком много агругемнов в методе, изменить!!!
    private static void processTransaction(String type, BigDecimal amount, String nameKeySort, LocalDate date, String nameKey,
                                           Map<String, BigDecimal> balanceAmount, Map<String, ArrayList<BalanceHistory>> balanceHistory) {

        ArrayList<BalanceHistory> balanceList = balanceHistory.getOrDefault(nameKeySort, new ArrayList<>());
        BigDecimal currentBalance = balanceAmount.getOrDefault(nameKeySort, BigDecimal.ZERO)
                .setScale(2, RoundingMode.DOWN);
        BigDecimal newBalance = BigDecimal.ZERO;
        if (type.equals("borrow")) {
            newBalance = nameKeySort.equals(nameKey) ? currentBalance.add(amount) : currentBalance.subtract(amount);
        } else if (type.equals("repay")) {
            newBalance = nameKeySort.equals(nameKey) ? currentBalance.subtract(amount) : currentBalance.add(amount);
        }
        balanceAmount.put(nameKeySort, newBalance.setScale(2, RoundingMode.DOWN));
        balanceList.add(new BalanceHistory(date, newBalance.setScale(2, RoundingMode.DOWN)));
        balanceHistory.put(nameKeySort, balanceList);
    }
}

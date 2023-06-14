package splitter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Purchase {

    BigDecimal minimumAmount = new BigDecimal("0.01");
    boolean haveRemainder = false;
    int extraPayers;

    void parceData(List<String> input, Map<String, BigDecimal> balanceAmount,
                   Map<String, ArrayList<BalanceHistory>> balanceHistory) {
        try {
            Date.index = 0;
            LocalDate date = Date.getDate(input);
            String payerPerson = input.get(Date.index + 1);
            BigDecimal totalPrice = new BigDecimal(input.get(Date.index + 3));

            Set<String> temporary = new TreeSet<>();
            Pattern namePattern = Pattern.compile("(?:(?<=\\+)|(?<![-\\w]))[A-Z]+(?!\\w)", Pattern.CASE_INSENSITIVE);
            for (int i = Date.index + 4; i < input.size(); i++) {
                Matcher matcherAdd = namePattern.matcher(input.get(i));
                while (matcherAdd.find()) {
                    if (matcherAdd.group().matches("[A-Z]+\\b")) {
                        List<String> names = Group.groupMap.get(matcherAdd.group());
                        if (names.isEmpty()) {
                            System.out.println("Group is empty");
                        } else {
                            temporary.addAll(names);
                        }
                    } else {
                        temporary.add(matcherAdd.group());
                    }
                }
            }

            Pattern patternName = Pattern.compile("(?:(?<=\\-))[A-Z]+(?!\\w)", Pattern.CASE_INSENSITIVE);
            for (int i = Date.index + 4; i < input.size(); i++) {
                Matcher matcherRemove = patternName.matcher(input.get(i));
                while (matcherRemove.find()) {
                    if (matcherRemove.group().matches("[A-Z]+\\b")) {
                        List<String> names = Group.groupMap.get(matcherRemove.group());
                        names.forEach(temporary::remove);
                    } else {
                        temporary.remove(matcherRemove.group());
                    }
                }
            }
            haveRemainder = false;
            processPurchase(date, payerPerson, totalPrice, temporary, balanceAmount, balanceHistory);
        } catch (Exception e) {
            System.out.println("Illegal command arguments");
        }
    }

    void processPurchase(LocalDate date, String payerPerson, BigDecimal totalPrice, Set<String> temporary, Map<String,
            BigDecimal> balanceAmount, Map<String, ArrayList<BalanceHistory>> balanceHistory) {

        BigDecimal remainder;
        //List<String> names = Group.groupMap.get(groupName);

        BigDecimal quantityPerson = new BigDecimal(temporary.size());
        BigDecimal sharedAmount = totalPrice.divide(quantityPerson, RoundingMode.FLOOR);

        if (!totalPrice.equals(sharedAmount.multiply(quantityPerson))) {
            remainder = totalPrice.subtract(sharedAmount.multiply(quantityPerson));
            extraPayers = remainder.divide(minimumAmount, RoundingMode.DOWN).intValue();
            haveRemainder = true;

        }
        int count = 0;
        for (String name : temporary) {
            if (!name.equals(payerPerson)) {
                String[] array = {payerPerson, name};
                String nameKey = array[0] + "-" + array[1];
                Arrays.sort(array);
                String nameKeySort = array[0] + "-" + array[1];
                ArrayList<BalanceHistory> balanceList = balanceHistory.getOrDefault(nameKeySort, new ArrayList<>());
                BigDecimal newBalance;
                BigDecimal currentBalance = balanceAmount.getOrDefault(nameKeySort, BigDecimal.ZERO);
                newBalance = nameKeySort.equals(nameKey) ? currentBalance.subtract(sharedAmount) : currentBalance.add(sharedAmount);

                if (haveRemainder && count < extraPayers) {
                    newBalance = nameKeySort.equals(nameKey) ? newBalance.subtract(minimumAmount) : newBalance.add(minimumAmount);
                    count++;
                }
                balanceAmount.put(nameKeySort, newBalance);
                balanceList.add(new BalanceHistory(date, newBalance));
                balanceHistory.put(nameKeySort, balanceList);
            }
        }
        //} else {
        //System.out.println("Group doesn't exist");
        //}
    }
}

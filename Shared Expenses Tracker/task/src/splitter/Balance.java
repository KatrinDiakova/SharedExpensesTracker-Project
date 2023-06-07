package splitter;
import java.math.*;
import java.time.LocalDate;
import java.util.*;

public class Balance {

    private static String type = "close";

    // длинный список аргументов, переместить списки в отдельные классы
    public void parceData(List<String> input, Map<String, BigDecimal> balanceAmount, Map<String, ArrayList<BalanceHistory>> balanceHistory) {
        Date.index = 0;
        LocalDate date = Date.getDate(input);
        if (input.size() > Date.index + 1) {
            if (input.get(Date.index + 1).equals("open") || input.get(Date.index + 1).equals("close")) {
                type = input.get(Date.index + 1);
            }
        }
        processBalance(date, balanceAmount, balanceHistory);
    }

    // слишком много агругемнов в методе, изменить!!!
    private static void processBalance(LocalDate inputDate, Map<String, BigDecimal> balanceAmount, Map<String, ArrayList<BalanceHistory>> balanceHistory) {
        try {
            List<String> repayment = new ArrayList<>();
            for (var key : balanceAmount.keySet()) {
                String[] name = key.split("-");
                ArrayList<BalanceHistory> history = balanceHistory.get(key);
                history.sort(Comparator.comparing(BalanceHistory::getDate));

                if (type.equals("open")) {
                    inputDate = inputDate.withDayOfMonth(1);
                }
                BigDecimal totalAmount = BigDecimal.ZERO;
                for (BalanceHistory balanceHist : history) {
                    LocalDate balanceDate = balanceHist.getDate();
                    if (type.equals("close") && !balanceDate.isAfter(inputDate) ||
                            type.equals("open") && balanceDate.isBefore(inputDate)) {
                        totalAmount = balanceHist.getBalance();
                    }
                }
                if (totalAmount.signum() == 0) {
                    repayment.add("No repayments");
                } else {
                    if (totalAmount.signum() == -1) {
                        repayment.add(String.format("%s owes %s %s", name[1], name[0], totalAmount.negate().setScale(2, RoundingMode.HALF_EVEN)));
                    } else {
                        repayment.add(String.format("%s owes %s %s", name[0], name[1], totalAmount.setScale(2, RoundingMode.HALF_EVEN)));
                    }
                }
            }
            Collections.sort(repayment);
            for (String result : repayment) {
                System.out.println(result);
            }
        } catch (Exception e) {
            System.out.println("Illegal command arguments" + e);
        }
    }
}
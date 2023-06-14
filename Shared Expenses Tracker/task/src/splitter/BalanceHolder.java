package splitter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BalanceHolder {

    private static final BalanceHolder instance = new BalanceHolder();

    private final Map<String, BigDecimal> amount = new HashMap<>();
    private final Map<String, ArrayList<BalanceHistory>> history = new HashMap<>();

    private BalanceHolder() {
    }

    public static BalanceHolder getInstance() {
        return instance;
    }

    public Map<String, BigDecimal> getAmount() {
        return amount;
    }

    public Map<String, ArrayList<BalanceHistory>> getHistory() {
        return history;
    }

}

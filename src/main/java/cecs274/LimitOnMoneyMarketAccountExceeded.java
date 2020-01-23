package cecs274;

public class LimitOnMoneyMarketAccountExceeded extends Exception {
    public LimitOnMoneyMarketAccountExceeded(){}
    public LimitOnMoneyMarketAccountExceeded(String message) {
        super(message);
    }
}

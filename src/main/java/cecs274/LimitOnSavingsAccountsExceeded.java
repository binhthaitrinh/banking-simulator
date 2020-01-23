package cecs274;

public class LimitOnSavingsAccountsExceeded extends Exception {
    public LimitOnSavingsAccountsExceeded(){}
    public LimitOnSavingsAccountsExceeded(String message) {
        super(message);
    }
}

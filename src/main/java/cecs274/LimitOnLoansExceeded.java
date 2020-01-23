package cecs274;

public class LimitOnLoansExceeded extends Exception {
    LimitOnLoansExceeded(){}
    LimitOnLoansExceeded(String message) {
        super(message);
    }
}

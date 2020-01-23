package cecs274;

public class InvalidInterestRateForLoan extends Exception {
    InvalidInterestRateForLoan() {}
    InvalidInterestRateForLoan(String message) {
        super(message);
    }
}

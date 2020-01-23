package cecs274;

public class InvalidSavingsInterestRate extends Exception {
    InvalidSavingsInterestRate() {}
    InvalidSavingsInterestRate(String message) {
        super(message);
    }
}

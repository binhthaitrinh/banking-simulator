package cecs274;

public class InsufficientFunds extends Exception {
    InsufficientFunds() {}
    InsufficientFunds(String message) {
        super(message);
    }
}

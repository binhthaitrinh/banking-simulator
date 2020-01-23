package cecs274;

public class CustomerDoNotOwnThisLoan extends Exception {
    CustomerDoNotOwnThisLoan() {}
    CustomerDoNotOwnThisLoan(String message) {
        super(message);
    }
}

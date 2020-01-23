package cecs274;

public class LoanHasNotBeenPaidOff extends Exception {
    LoanHasNotBeenPaidOff(){}
    LoanHasNotBeenPaidOff(String message) {
        super(message);
    }
}

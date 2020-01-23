package cecs274;

public class UnableToTransferFromMoneyMarket extends Exception {
    UnableToTransferFromMoneyMarket() {}
    UnableToTransferFromMoneyMarket(String message) {
        super(message);
    }
}

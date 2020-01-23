package cecs274;

public class CustomerDoNotOwnThisAccount extends Exception {
    CustomerDoNotOwnThisAccount(){}
    CustomerDoNotOwnThisAccount(String message) {
        super(message);
    }
}

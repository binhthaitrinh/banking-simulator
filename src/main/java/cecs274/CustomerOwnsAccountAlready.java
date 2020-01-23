/**
 * Name: Binh Trinh - 025497171
 * Class: CECS 274
 * Assignment: HW1 - Bank simulator
 * @author: Binh Trinh trinhthaibinh.ecom@gmail.com
 */

package cecs274;

/**
 * Exception Class to be thrown when the account is already in the list of accounts owned by customer
 */
public class CustomerOwnsAccountAlready extends Exception {
    public CustomerOwnsAccountAlready() {}

    /**
     * Create CustomerOwnsAccountAlready object to be thrown
     * @param message message that was passed when handling exception
     */
    public CustomerOwnsAccountAlready(String message) {
        super(message);
    }
}

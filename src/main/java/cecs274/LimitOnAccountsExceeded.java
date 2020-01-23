/**
 * Name: Binh Trinh - 025497171
 * Class: CECS 274
 * Assignment: HW1 - Bank simulator
 * @author: Binh Trinh trinhthaibinh.ecom@gmail.com
 */

package cecs274;

/**
 * Exception Class to be thrown when customer already has reached the limit on accounts they own
 */
public class LimitOnAccountsExceeded extends Exception {
    public LimitOnAccountsExceeded() {}

    /**
     * Create LimitOnAccountsExceeded object to be thrown
     * @param message message then was passed when exception is thrown
     */
    public LimitOnAccountsExceeded(String message) {
            super(message);
        }
}

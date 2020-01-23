/**
 * Name: Binh Trinh - 025497171
 * Class: CECS 274
 * Assignment: HW1 - Bank simulator
 * @author: Binh Trinh trinhthaibinh.ecom@gmail.com
 */

package cecs274;

/**
 * Exception Class to be thrown when money is transferred to account of different owner
 */
public class InvalidTransferAccount extends Exception {
    public InvalidTransferAccount() {}

    /**
     * Create InvalidTransferAccount object to be thrown
     * @param message message then was passed when InvalidTransferAccount exception is caught
     */
    public InvalidTransferAccount(String message) {
        super(message);
    }
}

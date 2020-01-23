/**
 * Name: Binh Trinh - 025497171
 * Class: CECS 274
 * Assignment: HW1 - Bank simulator
 * @author: Binh Trinh trinhthaibinh.ecom@gmail.com
 */

package cecs274;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

/**
 * The BankCustomer class models a banking customer. It represents the customer by his/her name, date of birth,
 * and list of account that this individual have
 * @author Binh Trinh trinhthaibinh.ecom@gmail.com
 */
public class BankCustomer implements Comparable<BankCustomer> {
    // Defining constants

    // Defining a formatter to format monetary value
    // Learned about this here: https://stackoverflow.com/questions/13791409/java-format-double-value-as-dollar-amount

    public static final NumberFormat formatter = NumberFormat.getCurrencyInstance();

    // Maximum number of accounts for each person

    public static final int MAX_ACCOUNT_ALLOWED = 5;
    public static final int MAX_LOAN_ALLOWED = 4;
    public static final int MAX_SAVINGS_ALLOWED = 2;
    public static final int MAX_MONEY_MARKET_ALLOWED = 2;

    //Static variable to keep track of customer number

    private static int startingCustomerNo = 0;

    // Instance variable defining BankCustomer objects

    private String firstName, lastName;
    private LocalDate dateOfBirth;
    private ArrayList<BankAccount> bankAccounts;
    private ArrayList<BankLoan> bankLoans;
    private int customerNo;

    // constructor

    /**
     * Create a Bank Customer object
     * Initially bankAccounts is empty.
     *
     * @param initialFirstName is first name of customer
     * @param initialLastName is last name of customer
     * @param initialDateOfBirth is date of birth of customer
     */
    public BankCustomer(String initialFirstName, String initialLastName, LocalDate initialDateOfBirth) {

        //Initialize instance variable from parameters passed to constructor
        startingCustomerNo++;
        customerNo = startingCustomerNo;
        firstName = initialFirstName;
        lastName = initialLastName;
        dateOfBirth = initialDateOfBirth;

        // Create an initially empty arrayList of 5 initial capacity

        bankAccounts = new ArrayList<>(MAX_ACCOUNT_ALLOWED);
        bankLoans = new ArrayList<>(MAX_LOAN_ALLOWED);
    }

    /**
     * Create a Bank Customer object based on first name and last name
     * @param initialFirstName first name of customer
     * @param initialLastName last name of customer
     */
    public BankCustomer(String initialFirstName, String initialLastName) {
        firstName = initialFirstName;
        lastName = initialLastName;
    }


    // accessor methods

    /**
     * Accessor method that returns customer's number
     * @return customer number
     */
    public int getCustomerNo() {
        return customerNo;
    }
    /**
     * Accessor that returns customer's first name
     * @return firstName as String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Accessor that returns customer's last name
     * @return lastName as String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Accessor that returns customer's full name
     * @return full name as String
     */
    public String getName() {
        return lastName + ", " + firstName;
    }

    /**
     * Accessor that returns the date of birth
     * @return dateOfBirth as LocalDate type
     */
    public LocalDate getDOB() {
        return dateOfBirth;
    }

    // Period Class found here: https://docs.oracle.com/javase/8/docs/api/java/time/Period.html
    /**
     * accessor that returns the current age in years
     * @return period between dateOfBirth and current date as integer
     */
    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Accessor that returns the number of accounts the customer owns
     * @return size of bankAccounts as int
     */
    public int getNumberOfAccounts() {
        return bankAccounts.size();
    }

    /**
     * Accessor that returns the number of loans customer is having
     * @return size of bankLoans as int
     */
    public int getNumberOfLoans() {
        return bankLoans.size();
    }

    /**
     * Accessor that returns the total amount of money in all accounts owned by customer, substracts total amount of loan
     * @return totalBalance as double
     */
    public double getTotalBalance() {

        // Initialize totalBalance to 0.0

        double totalBalance = 0;

        // Iterate through ArrayList and add balance of each account

        for (int i = 0; i < bankAccounts.size(); i++) {
            totalBalance += bankAccounts.get(i).getBalance();
        }

        for (int j = 0; j < bankLoans.size(); j++) {
            totalBalance -= bankLoans.get(j).getAmount();
        }

        return totalBalance;
    }

    /**
     * Methods used to compare this BankCustomer object with another
     * to see if they are equal
     * @param other other BankCustomer object
     * @return boolean value of whether 2 BankCustomer object are the same
     */
    public boolean equals(BankCustomer other) {
        return (this.firstName == other.firstName && this.lastName == other.lastName &&
                this.dateOfBirth == other.dateOfBirth && this.bankAccounts.equals(other.bankAccounts));
    }

    // mutator methods

    /**
     * Mutator that takes a BankAccount object as a parameter and adds it to bankAccounts the customer owns
     * @param newAccount is a new account of type BankAccount to add to bankAccounts of object
     * @throws LimitOnAccountsExceeded throw when trying to add the 6th account
     * @throws CustomerOwnsAccountAlready throw when customer already owns the account trying to add
     * @throws LimitOnSavingsAccountsExceeded throw when trying to add the 3rd savings account
     * @throws LimitOnMoneyMarketAccountExceeded throw when trying to add the 3rd money market account
     */
    public void add(BankAccount newAccount) throws LimitOnAccountsExceeded, CustomerOwnsAccountAlready,
            LimitOnSavingsAccountsExceeded, LimitOnMoneyMarketAccountExceeded {
        int savingsAccountCounter = 0;
        int moneyMarketAccountCounter = 0;

        // If customer already has 5 accounts, throw LimitOnAccountsExceeded exception

        if (bankAccounts.size() >= MAX_ACCOUNT_ALLOWED) {
            throw new LimitOnAccountsExceeded("Each customer is limited to owning a maximum of five accounts!. Request failed!");
        }

        // If customer already owns this account, throw CustomerOwnsAccountAlready exception

        if (bankAccounts.contains(newAccount)) {
            throw new CustomerOwnsAccountAlready("This customer already has this account. Request failed!");
        }

        // Count the number of savings and money market accounts customer currently owns

        for (BankAccount account : bankAccounts) {
            if (account.getType() == BankAccount.AccountType.S) {
                savingsAccountCounter++;
            }
            else if (account.getType() == BankAccount.AccountType.M) {
                moneyMarketAccountCounter++;
            }
        }

        // If customer already owns 2 savings account, and user is trying to add the 3rd savings account,
        // throw LimitOnSavingsAccountExceeded exception

        if ((savingsAccountCounter >= MAX_SAVINGS_ALLOWED) && (newAccount.getType() == BankAccount.AccountType.S)) {
            throw new LimitOnSavingsAccountsExceeded ("2 savings account reached. Request failed");
        }

        // If customer already owns 2 money market account, and user is trying to add the 3rd money market account,
        // throw LimitMoneyMarketAccountExceeded exception

        if (moneyMarketAccountCounter >= MAX_MONEY_MARKET_ALLOWED && newAccount.getType() == BankAccount.AccountType.M) {
            throw new LimitOnMoneyMarketAccountExceeded ("2 money market accounts reached. Request failed!");
        }

        // If no exception is thrown, proceed with the add

        bankAccounts.add(newAccount);
    }

    /**
     * Mutator that takes a BankLoan object as a parameter and adds it to bankLoans the customer owns
     * @param newLoan is an object of BankLoan to add to bankLoans list of bank loan
     * @throws LimitOnLoansExceeded throw when user is trying to add more loans than bank accounts
     */
    public void add(BankLoan newLoan) throws LimitOnLoansExceeded {

        // If user is trying to add more loans than bank accounts, throw LimitOnLoansExceeded exception

        if (bankLoans.size() >= (bankAccounts.size() -1)) {
            throw new LimitOnLoansExceeded ("Customer cannot have more loans than accounts they own. Request failed!");
        }

        // If no exception is thrown, proceed with the add
        bankLoans.add(newLoan);
    }

    /**
     * Mutator that takes a BankAccount object as parameter and remove it from bankAccounts the customer owns
     * @param closeAccount is an object of BankAccount to remove
     * @throws CustomerDoNotOwnThisAccount throw when user is trying to remove an account that customer does not have
     */
    public void close(BankAccount closeAccount) throws CustomerDoNotOwnThisAccount {

        // If user is trying to remove a bank account that customer does not own, throw CustomerDoNotOwnThisAccount exception

        if (!bankAccounts.contains(closeAccount)) {
            throw new CustomerDoNotOwnThisAccount("Customer does not own this account. Request failed");
        }

        // If no exception is thrown, proceed with the remove
        bankAccounts.remove(closeAccount);

    }

    /**
     * Mutator that takes a BankLoan object as parameter and remove it from bankLoans the customer has
     * @param closeLoan is an object of BankLoan to remove
     * @throws LoanHasNotBeenPaidOff throw when user is trying to remove a loan that customer has not paid off yet
     * @throws CustomerDoNotOwnThisLoan throw when user is trying to remove a loan that customer does not have
     */
    public void close(BankLoan closeLoan) throws LoanHasNotBeenPaidOff, CustomerDoNotOwnThisLoan {

        // If user is trying to remove a loan that customer has not paid off, throw LoanHasNotBeenPaidOff exception

        if (closeLoan.getAmount() > 0) {
            throw new LoanHasNotBeenPaidOff("Loan has not been paid off. Request failed!");
        }

        // If customer does not have this loan, throw CustomerDoNotOwnThisLoan exception

        if (!bankLoans.contains(closeLoan)) {
            throw new CustomerDoNotOwnThisLoan ("Customer does not own this loan. Request failed!");
        }

        // If no exception is thrown, proceed with the remove

        bankLoans.remove(closeLoan);
    }

    /**
     * Print information of bank accounts and bank loans associated with this customer
     */
    public void printAccounts() {
        for (BankAccount bankAccount:bankAccounts) {
            System.out.println(bankAccount);
        }
        for (BankLoan bankLoan:bankLoans) {
            System.out.println(bankLoan);
        }
    }


    // override method

    /**
     * Orveride toString() accessor
     * @return string
     */
    public String toString() {
        return String.format("%s %s owns %d accounts, %d loans with a total balance of %s", firstName, lastName,
                getNumberOfAccounts(), getNumberOfLoans(),
                formatter.format(getTotalBalance()));
    }

    /**
     * Compare between 2 BankCustomer object by the name
     * @param other other BankCustomer object
     * @return negative integer if this object's name comes before other's object name
     */
    public int compareTo(BankCustomer other) {
        return this.getName().toLowerCase().compareTo(other.getName().toLowerCase());
    }
}




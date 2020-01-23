/**
 * Name: Binh Trinh - 025497171
 * Class: CECS 274
 * Assignment: HW1 - Bank simulator
 * @author: Binh Trinh trinhthaibinh.ecom@gmail.com
 */

package cecs274;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

/**
 * The BankAccount class models a bank account. It represents a bank account by its owner,
 * type of account, balance, interest rate, and the date that account is opened
 * @author Binh Trinh trinhthaibinh.ecom@gmail.com
 */
public class BankAccount implements Comparable<BankAccount> {

    // Defining public fields

    // formatter to format double to monetary value as string
    // Learned about this here: https://stackoverflow.com/questions/13791409/java-format-double-value-as-dollar-amount

    public static final NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public static final double SAVINGS_INTEREST_RATE_MIN = 0.25;
    public static final double SAVINGS_INTEREST_RATE_MAX = 1.0;
    public static final double MONEY_MARKET_INTEREST_RATE_MIN = 1.0;
    public static final double VALID_CHECKING_INTEREST_RATE = 0.0;
    public static final double MONEY_MARKET_INTEREST_RATE_MAX = 2.0;



    // AccountType is an enum data type that contains 3 types of account

    public enum AccountType {
        C("Checking"),
        S("Savings"),
        M("Money Market");

        // Instance variables

        private String displayName;

        // Constructor

        AccountType(String displayName) {
            this.displayName = displayName;
        }

        /**
         * Accessor method that return String value of AccountType enum
         * @return AccountType enum as String
         */
        public String getDisplayName() {
            return this.displayName;
        }
    }

    // instance variables defining BankAccount objects

    private BankCustomer owner;
    private AccountType typeOfAccount;
    private double balance;
    private double interestRate;
    private LocalDate openedDate;
    private int accountNumber;
    private LocalDate lastAccrueInterestDate;
    private int customerNo;

    // constructors

    /**
     * Constructor that allow BankAccount objects to be created by providing values
     * for owner and initialBalance. The interest rate is initially set to 0.0.
     * The account type is initially Checking, and open date is current time.
     * startingNumber increments each time this constructor is called
     * accountNumber is set based on the number of account created
     * lastAccrueInterestRateDate is set to null
     * @param initialOwner is the owner that this BankAccount object belongs to
     * @param initialBalance is the balance of this BankAccount object
     * @throws LimitOnAccountsExceeded throw when trying to add the 6th account
     * @throws CustomerOwnsAccountAlready throw when customer already owns the account trying to add
     * @throws LimitOnMoneyMarketAccountExceeded throw when trying to add the 3rd money market account
     * @throws LimitOnSavingsAccountsExceeded throw when trying to add the 3rd savings account
     * @throws InvalidMoneyMarketInterestRate throw when trying to create a money market account with invalid interest rate
     * @throws InvalidSavingsInterestRate throw when trying to create a savings account with invalid interest rate
     * @throws InvalidCheckingInterestRate throw when trying to create a checking account with invalid interest rate
     */
    public BankAccount(BankCustomer initialOwner, double initialBalance) throws LimitOnAccountsExceeded,
            CustomerOwnsAccountAlready, LimitOnMoneyMarketAccountExceeded, LimitOnSavingsAccountsExceeded,
            InvalidMoneyMarketInterestRate, InvalidCheckingInterestRate, InvalidSavingsInterestRate {
        this(initialOwner, initialBalance, LocalDate.now());
    }

    public BankAccount(BankCustomer initialOwner, AccountType initialAccountType, double initialInterestRate) throws InvalidCheckingInterestRate, InvalidSavingsInterestRate, InvalidMoneyMarketInterestRate,
            LimitOnAccountsExceeded, CustomerOwnsAccountAlready, LimitOnMoneyMarketAccountExceeded, LimitOnSavingsAccountsExceeded {
        this(initialOwner, initialAccountType, 0.0, initialInterestRate, null, null);
    }

    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    /**
     * Constructor that allows BankAccount objects to be created by providing values
     * for owner, account type, balance, and interest rate. The open date is initially set to current time.
     * startingNumber increments each time this constructor is called
     * accountNumber is set based on the number of account created
     * lastAccrueInterestRateDate is set to null
     * @param initialOwner is the owner that this BankAccount object belongs to
     * @param initialBalance is the balance of this BankAccount object
     * @param initialDate is the opened date of this BankAccount object
     * @throws LimitOnAccountsExceeded throw when trying to add the 6th account
     * @throws CustomerOwnsAccountAlready throw when customer already owns the account trying to add
     * @throws LimitOnMoneyMarketAccountExceeded throw when trying to add the 3rd money market account
     * @throws LimitOnSavingsAccountsExceeded throw when trying to add the 3rd savings account
     * @throws InvalidMoneyMarketInterestRate throw when trying to create a money market account with invalid interest rate
     * @throws InvalidSavingsInterestRate throw when trying to create a savings account with invalid interest rate
     * @throws InvalidCheckingInterestRate throw when trying to create a checking account with invalid interest rate
     */
    public BankAccount(BankCustomer initialOwner, double initialBalance, LocalDate initialDate)
            throws LimitOnAccountsExceeded, CustomerOwnsAccountAlready, LimitOnMoneyMarketAccountExceeded,
            LimitOnSavingsAccountsExceeded, InvalidCheckingInterestRate, InvalidSavingsInterestRate, InvalidMoneyMarketInterestRate {

        this(initialOwner, AccountType.C, initialBalance, VALID_CHECKING_INTEREST_RATE, initialDate, null);
    }

    /**
     * Constructor that allows BankAccount objects to be created by providing values
     * for owner, account type, balance, interest rate and opened date.
     * startingNumber increments each time this constructor is called
     * accountNumber is set based on the number of account created
     * lastAccrueInterestRateDate is set to null
     * @param initialOwner is the owner that this BankAccount object belongs to
     * @param initialAccountType is the account type, using enum data type AccountType,
     *                           which can be either C, S, or M
     * @param initialBalance is the balance of this BankAccount object
     * @param initialInterestRate is the interest rate of this BankAccount object
     * @param initialDate is the opened date of this BankAccount object
     * @throws InvalidCheckingInterestRate throw when interest rate of checking account is not 0
     * @throws InvalidSavingsInterestRate throw when interest rate of savings account is not within 0.25 to 1.0
     * @throws InvalidMoneyMarketInterestRate throw when interest rate of money market account is not within 1.0 to 2.0
     * @throws LimitOnAccountsExceeded throw when trying to add the 6th account
     * @throws CustomerOwnsAccountAlready throw when customer already owns the account trying to add
     * @throws LimitOnMoneyMarketAccountExceeded throw when trying to add the 3rd money market account
     * @throws LimitOnSavingsAccountsExceeded throw when trying to add the 3rd savings account
     */
    public BankAccount(BankCustomer initialOwner, AccountType initialAccountType, double initialBalance,
                       double initialInterestRate, LocalDate initialDate) throws InvalidCheckingInterestRate,
            InvalidSavingsInterestRate, InvalidMoneyMarketInterestRate, LimitOnAccountsExceeded, CustomerOwnsAccountAlready,
            LimitOnMoneyMarketAccountExceeded, LimitOnSavingsAccountsExceeded {

        this(initialOwner, initialAccountType, initialBalance, initialInterestRate, initialDate, null);
    }

    /**
     * Constructor that allows BankAccount objects to be created by providing values of owner, account type, balance,
     * interest rate, opened date and the last time account was accrued
     * startingNumber increments each time this constructor is called
     * accountNumber is set based on the number of account created
     * @param initialOwner owner of this BankAccount object
     * @param initialAccountType account type, using enum data type of AccountType,
     *                           which can be either C, S, or M
     * @param initialBalance initial balance of account
     * @param initialInterestRate initial interest rate of account
     * @param initialDate opened date of the bank account
     * @param initialAccrueInterestDate last time account was accrued
     * @throws InvalidCheckingInterestRate throw when interest rate of checking account is not 0
     * @throws InvalidSavingsInterestRate throw when interest rate of savings account is not within 0.25 to 1.0
     * @throws InvalidMoneyMarketInterestRate throw when interest rate of money market account is not within 1.0 to 2.0
     * @throws LimitOnAccountsExceeded throw when trying to add the 6th account
     * @throws CustomerOwnsAccountAlready throw when customer already owns the account trying to add
     * @throws LimitOnMoneyMarketAccountExceeded throw when trying to add the 3rd money market account
     * @throws LimitOnSavingsAccountsExceeded throw when trying to add the 3rd savings account
     */
    public BankAccount(BankCustomer initialOwner, AccountType initialAccountType, double initialBalance,
                       double initialInterestRate, LocalDate initialDate, LocalDate initialAccrueInterestDate)
            throws InvalidCheckingInterestRate, InvalidSavingsInterestRate, InvalidMoneyMarketInterestRate,
            LimitOnAccountsExceeded, CustomerOwnsAccountAlready, LimitOnMoneyMarketAccountExceeded, LimitOnSavingsAccountsExceeded {

        typeOfAccount = initialAccountType;

        // If interest rate of each type of account is not within appropriate range, throw exception

        if (typeOfAccount == AccountType.C && initialInterestRate != VALID_CHECKING_INTEREST_RATE) {
            throw new InvalidCheckingInterestRate ("Invalid interest rate for checking account. Request failed");
        }
        else if (typeOfAccount == AccountType.S && (initialInterestRate < SAVINGS_INTEREST_RATE_MIN ||
                initialInterestRate >= SAVINGS_INTEREST_RATE_MAX)) {
            throw new InvalidSavingsInterestRate ("Invalid interest rate for savings account. Request failed");
        }
        else if (typeOfAccount == AccountType.M && (initialInterestRate < MONEY_MARKET_INTEREST_RATE_MIN ||
                initialInterestRate >= MONEY_MARKET_INTEREST_RATE_MAX)) {
            throw new InvalidMoneyMarketInterestRate ("Invalid interest rate for money market account. Request failed");
        }

        // If no exception is thrown, proceed

        interestRate = initialInterestRate;
        balance = initialBalance;
        owner = initialOwner;
        owner.add(this);
        openedDate = initialDate;
        lastAccrueInterestDate = initialAccrueInterestDate;
        accountNumber = 0;
        customerNo = 0;
    }

    /**
     * Constructor that allows BankAccount objects to be created by providing values of owner, account type, balance,
     * interest rate, opened date and the last time account was accrued
     * startingNumber increments each time this constructor is called
     * accountNumber is set based on the number of account created
     * @param initialOwner owner of this BankAccount object
     * @param initialAccountType account type, using enum data type of AccountType,
     *                           which can be either C, S, or M
     * @param initialBalance initial balance of account
     * @param initialInterestRate initial interest rate of account
     * @param initialDate opened date of the bank account
     * @param initialAccountNumber account number of bank account
     * @param initialCustomerNo customer number in the bank
     * @throws InvalidCheckingInterestRate throw when interest rate of checking account is not 0
     * @throws InvalidSavingsInterestRate throw when interest rate of savings account is not within 0.25 to 1.0
     * @throws InvalidMoneyMarketInterestRate throw when interest rate of money market account is not within 1.0 to 2.0
     * @throws LimitOnAccountsExceeded throw when trying to add the 6th account
     * @throws CustomerOwnsAccountAlready throw when customer already owns the account trying to add
     * @throws LimitOnMoneyMarketAccountExceeded throw when trying to add the 3rd money market account
     * @throws LimitOnSavingsAccountsExceeded throw when trying to add the 3rd savings account
     */
    public BankAccount(BankCustomer initialOwner, AccountType initialAccountType, double initialBalance,
                       double initialInterestRate, LocalDate initialDate, int initialAccountNumber, int initialCustomerNo)
            throws InvalidCheckingInterestRate, InvalidSavingsInterestRate, InvalidMoneyMarketInterestRate,
            LimitOnAccountsExceeded, CustomerOwnsAccountAlready, LimitOnMoneyMarketAccountExceeded, LimitOnSavingsAccountsExceeded {
        this(initialOwner, initialAccountType, initialBalance, initialInterestRate, initialDate, null);
        accountNumber = initialAccountNumber;
        customerNo = initialCustomerNo;
    }

    // accessors

    /**
     * Accessor that returns the BankCustomer who owns the account
     * @return owner as BankCustomer object
     */
    public BankCustomer getOwner() {
        return owner;
    }

    /**
     * Accessor that returns one of the enumerated values that represent the type of account it is
     * @return typeOfAccount as AccountType enum data type
     */
    public AccountType getType() {
        return typeOfAccount;
    }

    /**
     * Accessor that returns current balance on the account
     * @return balance as double
     */
    public double getBalance() {

        return balance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Method to return the opened date of this account
     * @return openedDate as LocalDate data type
     */
    public LocalDate getOpenedDate() {
        return openedDate;
    }

    /**
     * Accessor that returns account number
     * @return account number as int
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**
     * Acessor that returns customer number that account belongs to
     * @return customer number as int
     */
    public int getCustomerNo() {
        return customerNo;
    }

    // mutator

    /**
     * Mutator that accepts a monetary amount via its parameter and increases
     * the balance of the account by the amount only if the amount is positive
     * @param depositAmount is the amount to be deposited to the account
     * @throws IllegalArgumentException throw when deposit amount is negative or 0
     */
    public void deposit(double depositAmount) throws IllegalArgumentException {

        // If the amount is negative or 0, throw InvalidDepositAmount exception

        if (depositAmount <= 0) {
            throw new IllegalArgumentException(depositAmount + " is not a valid amount. Request failed!");
        }

        // If no exception is thrown, add newDeposit to current balance

        balance += depositAmount;
    }

    /**
     * Mutator that accepts amount via its parameter, and decreases the balance of the account by the amount.
     * @param withdrawAmount is the amount to be withdrawn
     * @throws IllegalArgumentException throw when there is not enough money in bank account
     * @throws InsufficientFunds throw when withdrawing more money than balance
     */
    public void withdraw(double withdrawAmount) throws IllegalArgumentException, InsufficientFunds {

        // check to see if balance is enough to be withdrawn, if not, throw InsufficientFunds exception

        if (withdrawAmount > balance) {
            throw new InsufficientFunds(String.format("Withdrawal of %s exceeds balance of %s",
                    formatter.format(withdrawAmount), formatter.format(getBalance())));
        }

        if (withdrawAmount < 0) {
            throw new IllegalArgumentException("Invalid withdrawal amount. Request failed!");
        }

        // If no exception is thrown, proceed the withdrawal

        balance -= withdrawAmount;

    }

    /**
     * Mutator that is defined with 2 parameters. The transfer only succeeds if this account has enough balance,
     * and money is transferred to another account of the same owner
     * @param transferAmount amount to be transferred
     * @param other account to be transferred to
     * @throws IllegalArgumentException throw when transferAmount is larger than account's balance
     * @throws InvalidTransferAccount throw when two accounts don't belong to the same owner
     * @throws InsufficientFunds throw when transfering more money than balance
     */
    public void transfer(double transferAmount, BankAccount other) throws
            IllegalArgumentException, InsufficientFunds, InvalidTransferAccount {

        // If transferAmount is a negative number, throw IllegalArgumentException

        if (transferAmount < 0) {
            throw new IllegalArgumentException("Invalid transfer amount. Request failed");
        }

        // throw InsufficientFunds exception if transferAmount is larger than account's balance

        else if (transferAmount > balance) {
            throw new InsufficientFunds (String.format("Transfer of %s exceeds balance of %s",
                    formatter.format(transferAmount), formatter.format(getBalance())));
        }

        // throw InvalidTransferAccount exception if two accounts don't belong to the same owner

        if (!this.owner.equals(other.owner)) {
            throw new InvalidTransferAccount("These 2 accounts does not belong to the same owner. Request failed!");
        }

        // if no exception is thrown, proceed with the transfer

        balance -= transferAmount;
        other.balance += transferAmount;
    }

    /**
     * Mutator that transfer money from an account that is not money market to pay loan
     * @param transferAmount amount to be transferred
     * @param loanAccount loan account to be transferred to
     * @throws IllegalArgumentException throw when transferAmount is negative
     * @throws UnableToTransferFromMoneyMarket throw when trying to transfer from money market account
     * @throws InvalidTransferAccount throw when trying to transfer to a loan that customer does not have
     * @throws InsufficientFunds throw when transferring more money than balance
     */
    public void transfer(double transferAmount, BankLoan loanAccount) throws
            IllegalArgumentException, UnableToTransferFromMoneyMarket, InvalidTransferAccount, InsufficientFunds {

        // If trying to transfer from money market account, throw UnableToTransferFromMoneyMarket exception

        if (this.getType() == AccountType.M) {
            throw new UnableToTransferFromMoneyMarket("Unable to transfer from money market account. Request failed");
        }

        // if transferAmount is negative, throw IllegalArgumentException

        if (transferAmount < 0) {
            throw new IllegalArgumentException("Invalid transfer amount.Request failed");
        }

        // if transferAmount is larger than the money this account currently has, throw IllegalArgumentException

        else if (transferAmount > balance) {
            throw new InsufficientFunds(String.format("Transfer of %s exceeds balance of %s",
                    formatter.format(transferAmount), formatter.format(getBalance())));
        }

        // If this customer does not own this loan, InvalidTransferAccount exception is thrown

        if (!this.owner.equals(loanAccount.getOwner())) {
            throw new InvalidTransferAccount("These 2 accounts does not belong to the same owner. Request failed!");
        }

        // If no exception is thrown, proceed

        balance -= transferAmount;
        loanAccount.makePayment(transferAmount);
    }

    /**
     * Mutator that increases balance of account by interest rate of account
     * @throws UnableToAccrueNow when user is trying to accrue an account before 30 days since the last time it was done
     */
    public void accrueInterest() throws UnableToAccrueNow {

        // If this is the first time the account is accrued, proceed

        if (lastAccrueInterestDate == null) {
            balance = balance + balance * interestRate/100;
            lastAccrueInterestDate = LocalDate.now();
        }

        // else, check to see if it passed 30 days since the last time it was accrued.
        // Proceed if it is, if not, throw UnableToAccrueNow exception

        else {
            if (ChronoUnit.DAYS.between(lastAccrueInterestDate, LocalDate.now()) >= 30) {
                balance = balance + balance * interestRate/100;
                lastAccrueInterestDate = LocalDate.now();
            }
            else {
                throw new UnableToAccrueNow ("Interest rate cannot be accrued until 30 days after the last time " +
                        "it was accrued. Request failed!");
            }
        }
    }

    // override method

    /**
     * Override toString() accessor
     * @return string formatted as instructed
     */
    public String toString() {
        return String.format("%s account #%d with balance %s owned by %s, interest rate %.2f%%",
                getType().getDisplayName(), getAccountNumber(),
                formatter.format(getBalance()), owner.getName(), interestRate);
    }

    /**
     * Comparision between 2 bank account's balance
     * @param other other BankAccount object
     * @return their difference as an int
     */
    public int compareTo(BankAccount other) {
        return (int)Math.ceil(this.getBalance() - other.getBalance());
    }

    /**
     * Comparator to compare 2 bank account's number
     */
    public static class BankAccountNumberComparator implements Comparator<BankAccount> {
        public int compare(BankAccount that, BankAccount other) {
            return that.getAccountNumber() - other.getAccountNumber();
        }
    }
}

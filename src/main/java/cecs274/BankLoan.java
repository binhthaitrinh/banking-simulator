package cecs274;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

/**
 * The BankAccount class models a bank account. It represents a bank account by its owner,
 * type of account, balance, interest rate, and the date that account is opened
 * @author Linette Murillo linette.murillo@student.csulb.edu
 */
public class BankLoan implements Comparable {

    // Defining public fields

    // formatter to format double to monetary value as string
    // Learned about this here: https://stackoverflow.com/questions/13791409/java-format-double-value-as-dollar-amount

    public static final NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public static final int MIN_LOAN_INTEREST_RATE = 5;
    public static final int MAX_LOAN_INTEREST_RATE = 20;

    // instance variables defining BankAccount objects

    private BankCustomer owner;
    private double loanAmount;
    private double interestRate;
    private LocalDate openedDate;
    private LocalDate paidOffDate;
    private int loanNumber;
    private LocalDate lastAccrueInterestDate;
    private int customerNo;

    /**
     * Constructor that allow BankLoan objects to be created by providing values
     * for owner, loan amount, interest rate, and open date and paid off date.
     * loanNumber increments each time this constructor is called
     * accrueInterestCounter is initially set to 0, and lastAccrueInterestRateDate is set to null
     * @param initialOwner owner of loan
     * @param initialLoanAmount loan amount of this BankLoan object
     * @param initialInterestRate interest rate of the loan
     * @throws InvalidInterestRateForLoan throw when initialInterestRate is not within 5% and 20%
     * @throws LimitOnLoansExceeded throw when number of loans exceeds number of accounts
     */
    public BankLoan(BankCustomer initialOwner, double initialLoanAmount, double initialInterestRate) throws
            InvalidInterestRateForLoan, LimitOnLoansExceeded {
        this(initialOwner, initialLoanAmount, initialInterestRate, LocalDate.now(), null);
    }

    /**
     * Constructor that allow BankLoan objects to be created by providing values
     * for owner, loan amount, interest rate, and open date. Paid off date is initially set to null.
     * startingLoanNumber increments each time this constructor is called
     * lastAccrueInterestRateDate is set to null
     * loanNumber is set based on the number of BankLoan objects created
     * @param initialOwner owner of loan
     * @param initialLoanAmount loan amount of this BankLoan object
     * @param initialInterestRate interest rate of the loan
     * @param initialOpenedDate open date of the loan
     * @throws InvalidInterestRateForLoan throw when initialInterestRate is not within 5% and 20%
     * @throws LimitOnLoansExceeded throw when number of loans exceeds number of accounts
     */
    public BankLoan(BankCustomer initialOwner, double initialLoanAmount, double initialInterestRate, LocalDate initialOpenedDate) throws
            InvalidInterestRateForLoan, LimitOnLoansExceeded {

        this(initialOwner, initialLoanAmount, initialInterestRate, initialOpenedDate, null);
    }

    public BankLoan(double initialLoanAmount) {
        loanAmount = initialLoanAmount;
    }

    /**
     * Constructor that allow BankLoan objects to be created by providing values
     * for owner, loan amount, interest rate, and open date. Paid off date is initially set to null.
     * startingLoanNumber increments each time this constructor is called
     * loanNumber is set based on the number of BankLoan objects created
     * @param initialOwner owner of loan
     * @param initialLoanAmount loan amount of this BankLoan object
     * @param initialInterestRate interest rate of the loan
     * @param initialOpenedDate open date of the loan
     * @param initialAccrueInterestDate last time loan was accrue
     * @throws InvalidInterestRateForLoan throw when initialInterestRate is not within 5% and 20%
     * @throws LimitOnLoansExceeded throw when number of loans exceeds number of accounts
     */
    public BankLoan(BankCustomer initialOwner, double initialLoanAmount, double initialInterestRate,
                    LocalDate initialOpenedDate, LocalDate initialAccrueInterestDate) throws
            InvalidInterestRateForLoan, LimitOnLoansExceeded {
        owner = initialOwner;
        owner.add(this);
        loanAmount = initialLoanAmount;

        // if initialInterestRate is not within 5% and 20%, throw InvalidInterestRateForLoan exception

        if (initialInterestRate < MIN_LOAN_INTEREST_RATE || initialInterestRate > MAX_LOAN_INTEREST_RATE) {
            throw new InvalidInterestRateForLoan ("Interest rate of loan must be within 5% and 20%");
        }

        // if no exception is thrown, set interestRate to initialInterestRate

        interestRate = initialInterestRate;
        openedDate = initialOpenedDate;
        paidOffDate = null;
        loanNumber = 0;
        customerNo = 0;
        lastAccrueInterestDate = initialAccrueInterestDate;
    }

    /**
     * Constructor that allow BankLoan objects to be created by providing values
     * for owner, loan amount, interest rate, and open date. Paid off date is initially set to null.
     * startingLoanNumber increments each time this constructor is called
     * loanNumber is set based on the number of BankLoan objects created
     * @param initialOwner owner of loan
     * @param initialAmount loan amount of this BankLoan object
     * @param initialInterestRate interest rate of the loan
     * @param initialOpenedDate open date of the loan
     * @param initialPaidOffDate paid off date of loan
     * @param initialLoanNumber loan number of loan
     * @throws InvalidInterestRateForLoan throw when initialInterestRate is not within 5% and 20%
     * @throws LimitOnLoansExceeded throw when number of loans exceeds number of accounts
     */
    public BankLoan(int initialLoanNumber, double initialAmount, double initialInterestRate,
                    LocalDate initialOpenedDate, LocalDate initialPaidOffDate, BankCustomer initialOwner,
                    int initialCustomerNo) throws InvalidInterestRateForLoan, LimitOnLoansExceeded {
        this(initialOwner, initialAmount, initialInterestRate, initialOpenedDate, null);
        loanNumber = initialLoanNumber;
        customerNo = initialCustomerNo;
        paidOffDate = initialPaidOffDate;
    }


    /**
     * Constructor that allow BankLoan objects to be created by providing values
     * for owner, loan amount, interest rate, and open date. Paid off date is initially set to null.
     * startingLoanNumber increments each time this constructor is called
     * loanNumber is set based on the number of BankLoan objects created
     * @param initialOwner owner of loan
     * @param initialAmount loan amount of this BankLoan object
     * @param initialInterestRate interest rate of the loan
     * @param initialOpenedDate open date of the loan
     * @param initialLoanNumber loan number of loan
     * @param initialCustomerNo customer number of Bank
     * @throws InvalidInterestRateForLoan throw when initialInterestRate is not within 5% and 20%
     * @throws LimitOnLoansExceeded throw when number of loans exceeds number of accounts
     */
    public BankLoan(BankCustomer initialOwner, double initialAmount, double initialInterestRate,
                    LocalDate initialOpenedDate, int initialLoanNumber, int initialCustomerNo)
            throws InvalidInterestRateForLoan, LimitOnLoansExceeded  {
        this(initialOwner, initialAmount, initialInterestRate, initialOpenedDate, null);
        loanNumber = initialLoanNumber;
        customerNo = initialCustomerNo;
    }

    // accessor

    /**
     * Accessor that return BankLoan's customer number
     * @return customer number as int
     */
    public int getCustomerNo() {
        return customerNo;
    }

    /**
     * Accessor that return BankCustomer object who has this loan
     * @return owner as BankCustomer object
     */
    public BankCustomer getOwner() {
        return owner;
    }

    /**
     * Accessor that return amount of this loan
     * @return loanAmount as double
     */
    public double getAmount() {
        return loanAmount;
    }

    /**
     * Accessor that returns open date of the loan
     * @return openedDate as LocalDate type
     */
    public LocalDate getDateOpened() {
        return openedDate;
    }

    /**
     * Accessor that returns the date the loan was paid off
     * @return paidOffDate as LocalDate type
     */
    public LocalDate getDatePaid() {
        return paidOffDate;
    }

    public int getLoanNumber() {
        return loanNumber;
    }

    public double getInterestRate() {
        return interestRate;
    }

    // mutator

    /**
     * mutator that accept an amount via its parameter, then decrease the balance of the loan
     * @param paymentAmount is amount to be paid to the loan
     * @throws IllegalArgumentException when paymentAmount is negative
     */
    public void makePayment(double paymentAmount) throws IllegalArgumentException{

        // if paymentAmount is negative, and loan has not been paid off throw IllegalArgumentException

        if (paymentAmount < 0 && paidOffDate == null) {
            throw new IllegalArgumentException("Invalid amount to be made payment");
        }

        // If paymentAmount > 0, and loan has not been paid off proceed, else do nothing

        if (paymentAmount > 0 && paidOffDate == null) {
            loanAmount -= paymentAmount;
            if (loanAmount == 0) {
                paidOffDate = LocalDate.now();
            }
        }
    }

    /**
     * mutator that accrue interest rate for bank loan
     * @throws UnableToAccrueNow if accrue within 30 day limit
     */
    public void accrueInterest() throws UnableToAccrueNow{

        // boolean to determine whether interest rate is successfully accrued

        boolean success;

        // If this is the first time the account is accrued, and loan has not been paid off proceed

        if (lastAccrueInterestDate == null && paidOffDate == null) {
            success = true;
        }

        // else, check to see if it passed 30 days since the last time it was accrued.
        // Proceed if it is and loan has not been paid off, if not, throw UnableToAccrueNow exception

        else {
            if (ChronoUnit.DAYS.between(lastAccrueInterestDate, LocalDate.now()) < 30 && paidOffDate == null) {
                throw new UnableToAccrueNow ("Interest rate cannot be accrued until 30 days after the last time " +
                        "it was accrued. Request failed!");
            }
            else {
                success = true;
            }
        }

        // set lastAccrueInterestDate to now if the account is successfully accrued

        if (success) {
            loanAmount = loanAmount + loanAmount * interestRate/100;
            lastAccrueInterestDate = LocalDate.now();
        }
    }

    /**
     * Overload toString() method
     * @return info about loan
     */
    public String toString() {
        return String.format("Loan #%d: %s owned by %s, interest rate %.1f%%.", loanNumber, formatter.format(loanAmount),
                getOwner().getName(), interestRate);
    }

    /**
     * Comparison with other BankLoan object's loan amount
     * @param otherObject other BankLoan object
     * @return the difference in loan amount
     */
    public int compareTo(Object otherObject) {
        BankLoan other = (BankLoan) otherObject;
        return (int)(this.getAmount() - other.getAmount());
    }

    /**
     * Comparator to compare 2 BankLoan number
     */
    public static class BankLoanNumberComparator implements Comparator<BankLoan> {
        public int compare(BankLoan that, BankLoan other) {
            return that.getLoanNumber() - other.getLoanNumber();
        }
    }
}

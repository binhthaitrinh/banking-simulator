/**
 * Name: Binh Trinh - 025497171
 *       Linette Murillo - 014441672
 * Class: CECS 274
 * Assignment: HW2 - Bank simulator
 */

package cecs274;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;

/**
 * Bank is a class that mimic real-world bank system that has Bank Customer, Bank Account, Bank Loan objects.
 *
 */

public class Bank {

    // Defining constant

    private static final int PRINT_LIMIT = 5;

    private static int nextAccountNumber = 100;
    private static int nextLoanNumber = 2000;

    private String bankName;
    private BST bankCustomerTree;
    private List<BankAccount> bankAccountList;
    private List<BankLoan> bankLoanList;
    private BST bankAccountTree;
    private BST bankLoanTree;
    private int numOfCustomer;
    private int numOfBankAccount;
    private int numOfBankLoan;
    private double totalBalance;
    private double totalLoan;
    private List<BankCustomer> bankCustomerList;

    /**
     * Default constructor that allow Bank object to be initialized
     */
    public Bank() {
        bankName = "";
        bankCustomerTree = new BST();
        bankAccountList = new LinkedList<>();
        bankLoanList = new ArrayList<>();
        bankAccountTree = new BST();
        bankLoanTree = new BST();
        bankCustomerList = new ArrayList<>();
        numOfCustomer = 0;
        numOfBankAccount = 0;
        numOfBankLoan = 0;
        totalBalance = 0.0;
        totalLoan = 0.0;
    }

    /**
     * Consructor that allows Bank object to be initialized from a file input containing Bank information
     * @param inputFile Input file that has Bank information
     * @throws FileNotFoundException when File is not found in system
     */
    public Bank(File inputFile) throws FileNotFoundException, InvalidSavingsInterestRate, InvalidCheckingInterestRate,
            InvalidMoneyMarketInterestRate, CustomerOwnsAccountAlready, LimitOnMoneyMarketAccountExceeded,
            LimitOnSavingsAccountsExceeded, LimitOnLoansExceeded, LimitOnAccountsExceeded, InvalidInterestRateForLoan {

        // Read input from file

        Scanner input = new Scanner(inputFile);

        // Get banking info from input file

        bankInfoScrapper(new Scanner(input.nextLine()));

        // Get bank customer info from file

        ArrayList<String> customerInfo = new ArrayList<>();
        for (int i = 0; i < numOfCustomer; i++) {
            customerInfo.add(input.nextLine());
        }
        customerInfoScrapper(customerInfo);

        // Get bank account info from file

        ArrayList<String> bankAccountInfo = new ArrayList<>();
        for (int i = 0; i < numOfBankAccount; i++) {
            bankAccountInfo.add(input.nextLine());
        }
        bankAccountScrapper(bankAccountInfo);

        // Get loan info from file

        ArrayList<String> bankLoanInfo = new ArrayList<>();
        while (input.hasNextLine()) {
            bankLoanInfo.add(input.nextLine());
        }
        bankLoanScrapper(bankLoanInfo);

        // Close input file after done reading

        input.close();
    }

    /**
     * helper function to read bank info for constructor
     * @param input Scanner object containing bank info
     */
    private void bankInfoScrapper(Scanner input) {
        input.useDelimiter(", ");
        while (input.hasNext()) {
            bankName = input.next();
            numOfCustomer = Integer.parseInt(input.next());
            numOfBankAccount = Integer.parseInt(input.next());
            numOfBankLoan = Integer.parseInt(input.next());
            totalBalance = Double.parseDouble(input.next());
            totalLoan = Double.parseDouble(input.next());
        }
    }

    /**
     * helper function to read customer info for constructor
     * @param customerInfo Scanner object containing customer info
     */
    private void customerInfoScrapper(ArrayList<String> customerInfo) {

        numOfCustomer = 0;

        // Initialize BST and List for bankCustomer

        bankCustomerList = new ArrayList<>();
        bankCustomerTree = new BST();

        // Read bank customer info from each line, create bank customer object, then add them to the list

        for(String eachCustomer: customerInfo) {
            Scanner eachCustomerScanner = new Scanner(eachCustomer);
            eachCustomerScanner.useDelimiter(",");

            String lastName = eachCustomerScanner.next();
            String firstName = eachCustomerScanner.next();
            LocalDate DOB = LocalDate.parse(eachCustomerScanner.next());

            BankCustomer bankCustomer = new BankCustomer(firstName, lastName, DOB);
            addBankCustomer(bankCustomer);
        }
    }

    /**
     * helper function to read bank account info for constructor
     * @param bankAccountInfo Scanner object containing bank account info
     */
    private void bankAccountScrapper(ArrayList<String> bankAccountInfo) throws InvalidCheckingInterestRate,
            InvalidMoneyMarketInterestRate, InvalidSavingsInterestRate, LimitOnAccountsExceeded, CustomerOwnsAccountAlready,
            LimitOnSavingsAccountsExceeded, LimitOnMoneyMarketAccountExceeded {

        numOfBankAccount = 0;
        totalBalance = 0;

        // Initialize BST and List for bank account

        bankAccountList = new LinkedList<>();
        bankAccountTree = new BST();

        // Read bank account info from each line, create bank account object, then add them to the list

        for (String eachAccount: bankAccountInfo) {
            Scanner eachAccountScanner = new Scanner(eachAccount);
            eachAccountScanner.useDelimiter(",");

            int accountNumber = Integer.parseInt(eachAccountScanner.next());
            if (nextAccountNumber < accountNumber) {
                nextAccountNumber = accountNumber;
            }
            String accountType = eachAccountScanner.next();
            double balance = Double.parseDouble(eachAccountScanner.next());
            double interestRate = Double.parseDouble(eachAccountScanner.next());
            LocalDate openedDate = LocalDate.parse(eachAccountScanner.next());
            int customerNo = Integer.parseInt(eachAccountScanner.next());

            BankAccount bankAccount = new BankAccount(bankCustomerList.get(customerNo - 1),
                    BankAccount.AccountType.valueOf(accountType), balance, interestRate, openedDate, accountNumber, customerNo);
            addBankAccount(bankAccount);
        }
    }

    /**
     * helper function to read bank loan for constructor
     * @param bankLoanInfo Scanner object containing bank loan info
     */
    private void bankLoanScrapper(ArrayList<String> bankLoanInfo) throws InvalidInterestRateForLoan, LimitOnLoansExceeded {
        numOfBankLoan = 0;
        totalLoan = 0;

        // Initialize BST and List for BankLoan object

        bankLoanList = new ArrayList<>();
        bankLoanTree = new BST();

        // Read bank loan info from each line, create bank loan object, then add them to the list

        for (String eachLoan: bankLoanInfo) {
            Scanner eachLoanScanner = new Scanner(eachLoan);
            eachLoanScanner.useDelimiter(",");

            int accountNumber = Integer.parseInt(eachLoanScanner.next());
            if (nextLoanNumber < accountNumber) {
                nextLoanNumber = accountNumber;
            }
            double balance = Double.parseDouble(eachLoanScanner.next());
            double interestRate = Double.parseDouble(eachLoanScanner.next());
            LocalDate givenDate = LocalDate.parse(eachLoanScanner.next());
            LocalDate paidOffDate = null;
            if (!eachLoanScanner.next().equals("")) {
                paidOffDate = LocalDate.parse(eachLoanScanner.next());
            }
            int customerNo = Integer.parseInt(eachLoanScanner.next());

            BankLoan bankLoan = new BankLoan(accountNumber, balance, interestRate, givenDate, paidOffDate,
                    bankCustomerList.get(customerNo - 1), customerNo);
            addBankLoan(bankLoan);
        }
    }

    /**
     * accessor method to get name of the bank
     * @return name of the bank
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * constructor method to set name of the bank
     * @param newBankName name of the bank passed as string
     */
    public void setBankName(String newBankName) {
        bankName = newBankName;
    }

    /**
     * Add BankAccount object into their appropriate list
     * @param bankAccount BankAccount object to be added
     */
    public void addBankAccount(BankAccount bankAccount) {
        sortedInsert(bankAccountList, bankAccount);
        bankAccountTree.insert(bankAccount);
        numOfBankAccount++;
        totalBalance += bankAccount.getBalance();
    }

    /**
     * Add BankLoan object into their appropriate list
     * @param bankLoan BankLoan object to be added
     */
    public void addBankLoan(BankLoan bankLoan) {
        sortedInsert(bankLoanList, bankLoan);
        bankLoanTree.insert(bankLoan);
        numOfBankLoan++;
        totalLoan += bankLoan.getAmount();
    }

    /**
     * Add BankCustomer into their appropriate list
     * @param bankCustomer BankCustomer object to be added
     */
    public void addBankCustomer(BankCustomer bankCustomer) {
        bankCustomerList.add(bankCustomer);
        bankCustomerTree.insert(bankCustomer);
        numOfCustomer++;
    }

    /**
     * Sorted insert BankAccount object to its List
     * @param list the list needed to be added
     * @param bankAccount object the be added
     */
    public void sortedInsert(List<BankAccount> list, BankAccount bankAccount) {
        if (list.isEmpty()) {
            list.add(bankAccount);
        }
        else {
            int i = 0;
            BankAccount.BankAccountNumberComparator abc = new BankAccount.BankAccountNumberComparator();
            while (i < list.size() && abc.compare(list.get(i), bankAccount) < 0) {
                i++;
            }
            if (i < list.size()) {
                list.add(i, bankAccount);
            }
            else {
                list.add(bankAccount);
            }
        }
    }

    /**
     * Sorted insert BankAccount object to its List
     * @param list the list needed to be added
     * @param bankLoan object the be added
     */
    public void sortedInsert(List<BankLoan> list, BankLoan bankLoan) {
        if (list.isEmpty()) {
            list.add(bankLoan);
        }
        else {
            int i = 0;
            BankLoan.BankLoanNumberComparator abc = new BankLoan.BankLoanNumberComparator();
            while (i < list.size() && abc.compare(list.get(i), bankLoan) < 0) {
                i++;
            }
            if (i < list.size()) {
                list.add(i, bankLoan);
            }
            else {
                list.add(bankLoan);
            }
        }
    }

    /**
     * Searches for and returns BankAccount in the inclusive range [lowerBound, upperBound]
     * @param lowerBound the value defining the smallest object in the range
     * @param upperBound the value defining the largest object in the range
     * @return a List of BankAccount objects in its BST that are in the inclusive range [lowerBound, upperBound]
     */
    public List<BankAccount> rangeSearchBankAccount(double lowerBound, double upperBound) {
        BankAccount lower = new BankAccount(lowerBound);
        BankAccount upper = new BankAccount(upperBound);
        List<Comparable> abc = bankAccountTree.getRange(lower, upper);
        ArrayList<BankAccount> result = new ArrayList<>(abc.size());
        for (int i = 0; i < abc.size(); i++) {
            result.add((BankAccount)abc.get(i));
        }
        return result;
    }

    /**
     * Searches for and returns BankLoan in the inclusive range [lowerBound, upperBound]
     * @param lowerBound the value defining the smallest object in the range
     * @param upperBound the value defining the largest object in the range
     * @return a List of BankLoan objects in its BST that are in the inclusive range [lowerBound, upperBound]
     */
    public List<BankLoan> rangeSearchBankLoan(double lowerBound, double upperBound) {
        BankLoan lower = new BankLoan(lowerBound);
        BankLoan upper = new BankLoan(upperBound);
        List<Comparable> abc = bankLoanTree.getRange(lower, upper);
        ArrayList<BankLoan> result = new ArrayList<>(abc.size());
        for (int i = 0; i < abc.size(); i++) {
            result.add((BankLoan)abc.get(i));
        }
        return result;
    }

    /**
     * Write Bank info data into file
     * @param out PrintWriter object for writing
     */
    public void writeData(PrintWriter out) {

        // Write Bank info into output file

        writeBankInfo(out);

        // Write Bank Customer info into output file

        writeBankCustomerInfo(out);

        // Write Bank Account info into output file

        writeBankAccountInfo(out);

        // Write Bank Loan info into output file

        writeBankLoanInfo(out);

        // Save and Close output file

        out.close();
    }

    /**
     * Helper function to write Bank Info into output file
     * @param out PrintWriter Object for writing
     */
    private void writeBankInfo(PrintWriter out) {
        out.printf("%s, %d, %d, %d, %.2f, %.2f%n", bankName, numOfCustomer, numOfBankAccount, numOfBankLoan, totalBalance, totalLoan);
    }

    /**
     * Helper function to write Bank Customer info into output file
     * @param out PrintWriter Object for writing
     */
    private void writeBankCustomerInfo(PrintWriter out) {
        if (!bankCustomerList.isEmpty()) {
            bankCustomerTree.buildBalancedBST();
            List<Object> breadthFirstList = bankCustomerTree.breadthFirstTraversal();
            for (int i = 0; i < breadthFirstList.size(); i++) {
                BankCustomer bankCustomer = (BankCustomer)breadthFirstList.get(i);
                out.printf("%s,%s,%s%n", bankCustomer.getLastName(), bankCustomer.getFirstName(), bankCustomer.getDOB());
            }
        }
    }

    /**
     * Helper function to write Bank Account info into output file
     * @param out PrintWriter Object for writing
     */
    private void writeBankAccountInfo(PrintWriter out) {
        if (!bankAccountList.isEmpty()) {
            bankAccountTree.buildBalancedBST();
            List<Object> breadthFirstList = bankAccountTree.breadthFirstTraversal();
            for (Object object: breadthFirstList) {
                BankAccount bankAccount = (BankAccount)object;
                out.printf("%d,%s,%.2f,%.2f,%s,%d%n", bankAccount.getAccountNumber(), bankAccount.getType(),
                        bankAccount.getBalance(), bankAccount.getInterestRate(), bankAccount.getOpenedDate(), bankAccount.getCustomerNo());
            }
        }
    }

    /**
     * Helper function to write Bank Loan info into output file
     * @param out PrintWriter Object for writing
     */
    private void writeBankLoanInfo(PrintWriter out) {
        if (!bankLoanList.isEmpty()) {
            bankLoanTree.buildBalancedBST();
            List<Object> breadthFirstList = bankLoanTree.breadthFirstTraversal();
            for (Object object: breadthFirstList) {
                BankLoan bankLoan = (BankLoan)object;
                String paidOffDate = "";
                if (bankLoan.getDatePaid() != null) {
                    paidOffDate = bankLoan.getDatePaid().toString();
                }
                out.printf("%d,%.2f,%.2f,%s,%s,%d%n", bankLoan.getLoanNumber(), bankLoan.getAmount(),
                        bankLoan.getInterestRate(), bankLoan.getDateOpened(), paidOffDate, bankLoan.getCustomerNo());
            }
        }
    }

    /**
     * Find BankAccount object in bankAccountList based on account number
     * @param accountNumber account number used to find BankAccount object
     * @return BankAccount object with account number passed in the function
     */
    public BankAccount findBankAccount(int accountNumber) {
        return binarySearchBankAccount(0, bankAccountList.size() - 1, accountNumber);
    }

    /**
     * Recursive helper function for findBankaccount()
     * @param start start index of the range
     * @param end end index of the range
     * @param accountNumber the account number to find
     * @return the BankAccount object at which the account number occurs, or null if not found
     */
    private BankAccount binarySearchBankAccount(int start, int end, int accountNumber) {
        if (start <= end) {
            int mid = (start + end) / 2;
            if (bankAccountList.get(mid).getAccountNumber() == accountNumber) {
                return bankAccountList.get(mid);
            }
            else if (bankAccountList.get(mid).getAccountNumber() < accountNumber) {
                return binarySearchBankAccount(mid + 1, end, accountNumber);
            }
            else {
                return binarySearchBankAccount(start, mid - 1, accountNumber);
            }
        }
        else {
            return null;
        }
    }

    /**
     * Find BankLoan object in its list based on loan number
     * @param loanNumber the loan number to be searched for
     * @return
     */
    public BankLoan findLoanAccount(int loanNumber) {
        return binarySearchForLoan(0, bankLoanList.size(), loanNumber);
    }

    /**
     * Recursive helper function for findLoanAccount()
     * @param start start index of the range
     * @param end end index of the range
     * @param accountNumber account number to be searched for
     * @return the BankLoan object at which the account number occurs, or null if not found
     */
    private BankLoan binarySearchForLoan(int start, int end, int accountNumber) {
        if (start <= end) {
            int mid = (start + end) / 2;
            if (bankLoanList.get(mid).getLoanNumber() == accountNumber) {
                return bankLoanList.get(mid);
            }
            else if (bankLoanList.get(mid).getLoanNumber() < accountNumber) {
                return binarySearchForLoan(mid + 1, end, accountNumber);
            }
            else {
                return binarySearchForLoan(start, mid - 1, accountNumber);
            }
        }
        else {
            return null;
        }
    }

    /**
     * Find BankCustomer object based on full name
     * @param fullName full name of bank customer
     * @return BankCustomer object that matches the name, or null of not found
     */
    public BankCustomer findBankCustomer(String fullName) {
        String firstName, lastName;
        String[] name = fullName.split(" ");
        if (name.length == 2) {

            firstName = name[0];
            lastName = name[1];
        }
        else {
            throw new IllegalArgumentException("Please enter full name. First name follows by Last name. For Example: Binh Trinh.");
        }
        BankCustomer bankCustomer = new BankCustomer(firstName, lastName);
        return (BankCustomer)bankCustomerTree.search(bankCustomer);
    }

    /**
     * Print accounts belong to a specific bank customer
     * @param bankCustomer bank customer object wishes to have its accounts printed
     */
    public void printBankCustomerAccounts(BankCustomer bankCustomer) {
        bankCustomer.printAccounts();
    }

    /**
     * Close a bank account, remove it from its list and BST
     * @param bankAccount bank account object to be closed
     * @throws CustomerDoNotOwnThisAccount if customer do not own the account to be closed
     */
    public void closeBankAccount(BankAccount bankAccount, BankCustomer bankCustomer) throws CustomerDoNotOwnThisAccount {
        totalBalance -= bankAccount.getBalance();
        numOfBankAccount--;
        bankCustomer.close(bankAccount);
        bankAccountList.remove(bankAccount);
        bankAccountTree.remove(bankAccount);
    }

    /**
     * Close a bank loan, remove it from its lists and BST
     * @param bankLoan bank loan object to be close
     * @throws LoanHasNotBeenPaidOff if the loan has not been paid off
     * @throws CustomerDoNotOwnThisLoan if customer do not own the loan
     */
    public void closeBankLoan(BankLoan bankLoan, BankCustomer bankCustomer) throws LoanHasNotBeenPaidOff, CustomerDoNotOwnThisLoan {
        totalLoan -= bankLoan.getAmount();
        numOfBankLoan--;
        bankCustomer.close(bankLoan);
        bankLoanList.remove(bankLoan);
        bankLoanTree.remove(bankLoan);
    }

    /**
     * Print list of BankCustomer object, limit by 5 objects
     */
    public void printBankCustomer() {
        if (bankCustomerList.size() > PRINT_LIMIT) {
            for (int i = 0; i < PRINT_LIMIT; i++) {
                System.out.println(bankCustomerList.get(i).getName());
            }
            System.out.println("...");

        }
        else {
            for (int i = 0; i < bankCustomerList.size(); i++) {
                System.out.println(bankCustomerList.get(i).getName());
            }
        }
    }

    /**
     * Print list of BankAccount object, limit by 5 objects
     */
    public void printBankAccount() {
        if (bankAccountList.size() > PRINT_LIMIT) {
            for (int i = 0; i < PRINT_LIMIT; i++) {
                System.out.println(bankAccountList.get(i));
            }
            System.out.println("...");
        }
        else {
            for (int i = 0; i < bankAccountList.size(); i++) {
                System.out.println(bankAccountList.get(i));
            }
        }
    }

    /**
     * Print list of BankLoan object, limit by 5 objects
     */
    public void printBankLoan() {
        if (bankLoanList.size() > PRINT_LIMIT) {
            for (int i = 0; i < PRINT_LIMIT; i++) {
                System.out.println(bankLoanList.get(i));
            }
            System.out.println("...");
        }
        else {
            for (int i = 0; i < bankLoanList.size(); i++) {
                System.out.println(bankLoanList.get(i));
            }
        }
    }

    /**
     * Deposit money in BankAccount object passed in parameter
     * @param bankAccount BankAccount object to be deposited
     * @param amount amount to deposit
     * @throws IllegalArgumentException when the amount is not valid
     */
    public void depositBankAccount(BankAccount bankAccount, double amount) throws IllegalArgumentException {
        bankAccount.deposit(amount);
        bankAccountTree.remove(bankAccount);
        bankAccountTree.insert(bankAccount);
    }

    /**
     * Withdraw money in BankAccount object passed in parameter
     * @param bankAccount BankAccount object to be withdrawn
     * @param amount amount to be withdrawn
     * @throws IllegalArgumentException when amount is not valid
     * @throws InsufficientFunds when account passed in does not have enough money
     */
    public void withDrawBankAccount(BankAccount bankAccount, double amount) throws IllegalArgumentException, InsufficientFunds {
        bankAccount.withdraw(amount);
        bankAccountTree.remove(bankAccount);
        bankAccountTree.insert(bankAccount);
    }

    /**
     * Transfer money from one BankAccount object to another
     * @param that BankAccount object to transfer from
     * @param other BankAccount object to transfer to
     * @param amount amount to be transferred
     * @throws InsufficientFunds when "that" does not have enough money
     * @throws InvalidTransferAccount when 2 accounts don't belong to the same owner
     */
    public void transferBankAccount(BankAccount that, BankAccount other, double amount) throws InsufficientFunds, InvalidTransferAccount {
        that.transfer(amount, other);
        bankAccountTree.remove(that);
        bankAccountTree.remove(other);
        bankAccountTree.insert(that);
        bankAccountTree.insert(other);
    }

    /**
     * Make payment to a BankLoan object
     * @param bankLoan the object to be made payment
     * @param amount amount of money to pay
     * @throws IllegalArgumentException
     */
    public void makePaymentBankLoan(BankLoan bankLoan, double amount) throws IllegalArgumentException {
        bankLoan.makePayment(amount);
        bankLoanTree.remove(bankLoan);
        bankLoanTree.insert(bankLoan);
    }

    /**
     * Attempt at generalizing adding method for BankCustomer, BankAccount, BankCustomer object to its lists
     * @param other object to be added
     */
    public void addObject(Object other){
        if (other instanceof BankCustomer){
            BankCustomer customer = (BankCustomer) other;
            bankCustomerTree.insert(customer);
        }
        if (other instanceof BankAccount){
            BankAccount blah = (BankAccount) other;
            BankAccount.BankAccountNumberComparator acountCheck = new BankAccount.BankAccountNumberComparator();
            if (!bankAccountList.contains(blah)){
                bankAccountTree.insert(blah);
            }
            if (bankAccountList.isEmpty()){
                bankAccountList.add(blah);
            }else if (bankAccountList.contains(blah)){
                throw new IllegalArgumentException("Error: You are trying to add the same account.");
            }else {
                int position = 0;
                int compare;
                boolean insert = false;
                while (!insert){
                    compare = acountCheck.compare(blah, bankAccountList.get(position));
                    if (compare < 0){
                        bankAccountList.add(position, blah);
                        insert = true;
                    }else if (compare > 0 && position == bankAccountList.size()-1){
                        bankAccountList.add(blah);
                        insert = true;
                    }
                    position++;
                }
            }
        }
        if (other instanceof BankLoan){
            BankLoan loan = (BankLoan) other;
            BankLoan.BankLoanNumberComparator loanCheck = new BankLoan.BankLoanNumberComparator();
            if (!bankLoanList.contains(loan)){
                bankLoanTree.insert(loan);
            }
            if (bankLoanList.isEmpty()){
                bankLoanList.add(loan);
            }else if (bankLoanList.contains(loan)){
                throw new IllegalArgumentException("Error: You are trying to add the same account.");
            }else {
                int position = 0;
                int compare;
                boolean insert = false;
                while (!insert){
                    compare = loanCheck.compare(loan, bankLoanList.get(position));
                    if (compare < 0){
                        bankLoanList.add(position, loan);
                        insert = true;
                    }else if (compare > 0 && position == bankLoanList.size()-1){
                        bankLoanList.add(loan);
                        insert = true;
                    }
                    position++;
                }
            }
        }
    }

    /**
     * Create Bank Account, add to its lists
     * @param bankCustomer customer of account
     * @param typeOfAccount type of account
     * @param balance balance of account
     * @param interestRate interest rate of account
     * @param openedDate open date of account
     * @return BankAccount object just created
     * @throws InvalidCheckingInterestRate throw when interest rate of checking account is not 0
     * @throws InvalidSavingsInterestRate throw when interest rate of savings account is not within 0.25 to 1.0
     * @throws InvalidMoneyMarketInterestRate throw when interest rate of money market account is not within 1.0 to 2.0
     * @throws LimitOnAccountsExceeded throw when trying to add the 6th account
     * @throws CustomerOwnsAccountAlready throw when customer already owns the account trying to add
     * @throws LimitOnMoneyMarketAccountExceeded throw when trying to add the 3rd money market account
     * @throws LimitOnSavingsAccountsExceeded throw when trying to add the 3rd savings account
     */
    public BankAccount createBankAccount(BankCustomer bankCustomer, BankAccount.AccountType typeOfAccount,
                                         double balance, double interestRate, LocalDate openedDate)
            throws InvalidSavingsInterestRate, InvalidCheckingInterestRate, InvalidMoneyMarketInterestRate,
            LimitOnMoneyMarketAccountExceeded, LimitOnSavingsAccountsExceeded, LimitOnAccountsExceeded, CustomerOwnsAccountAlready {
        nextAccountNumber++;
        BankAccount bankAccount = new BankAccount(bankCustomer, typeOfAccount, balance, interestRate, openedDate,
                nextAccountNumber, bankCustomer.getCustomerNo());
        addBankAccount(bankAccount);
        return bankAccount;
    }


    /**
     * Create BankCustomer, add it to its lists
     * @param firstName first name of customer
     * @param lastName last name of customer
     * @param DOB Date of birth
     * @return BankCustomer object just created
     */
    public BankCustomer createBankCustomer (String firstName, String lastName, LocalDate DOB) {
        BankCustomer bankCustomer = new BankCustomer(firstName, lastName, DOB);
        addBankCustomer(bankCustomer);
        return bankCustomer;
    }

    /**
     * Create bankLoan, add it to its lists
     * @param bankCustomer owner of loan
     * @param loanAmount amount of loan
     * @param interestRate interest rate of loan
     * @param openedDate open date of loan
     * @return BankLoan just created
     * @throws InvalidInterestRateForLoan throw when initialInterestRate is not within 5% and 20%
     * @throws LimitOnLoansExceeded throw when number of loans exceeds number of accounts
     */
    public BankLoan createBankLoan(BankCustomer bankCustomer, double loanAmount, double interestRate, LocalDate openedDate)
            throws LimitOnLoansExceeded, InvalidInterestRateForLoan {
        nextLoanNumber++;
        BankLoan bankLoan = new BankLoan(bankCustomer, loanAmount, interestRate, openedDate, nextLoanNumber, bankCustomer.getCustomerNo());
        addBankLoan(bankLoan);
        return bankLoan;
    }

}
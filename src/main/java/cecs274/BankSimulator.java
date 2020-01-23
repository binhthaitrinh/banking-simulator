package cecs274;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * BankSimulator stimulate an employing using a transaction application in a
 * bank He can make transaction, create new account, close account or range
 * search
 *
 * @author Binh Trinh
 */

public class BankSimulator {

    // Declare Bank Object

    public static Bank bank;

    // Scanner object to read input

    public static Scanner console = new Scanner(System.in);

    // Enum type that has Bank Loan, or Bank Account

    public enum AccountType {
        BANK_ACCOUNT("Bank Account"), BANK_LOAN("Bank Loan");

        private String displayName;

        AccountType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }

    public static boolean backHere = true;
    public static AccountType accountType = null;

    // Defining constant

    public static final String DASHES = ("-----------------------------------------");
    public static final String SEPARATOR = ("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    public static void main(String[] args) {

        if (args.length == 0) {
            bank = new Bank();

            System.out.print("How do you want to name the bank? ");
            String input = console.nextLine().trim();

            bank.setBankName(input);
            initializeApp();
            System.out.println("Thank you for using our service!");

            try {
                bank.writeData(new PrintWriter(input + ".txt"));
                System.out.printf("Data written to %s.txt%n", input);
            } catch (FileNotFoundException ex) {
                System.out.println("File not found");
            }
        } else {
            File input = new File(args[0]);

            try {
                bank = new Bank(input);
                initializeApp();
                System.out.println("Thank you for using our service!");
                System.out.println("Data written to " + args[0]);
                bank.writeData(new PrintWriter(args[0]));
            }

            catch (FileNotFoundException ex) {
                System.out.println("File not found");
            } catch (InvalidInterestRateForLoan | InvalidCheckingInterestRate | InvalidMoneyMarketInterestRate
                    | InvalidSavingsInterestRate | CustomerOwnsAccountAlready | LimitOnAccountsExceeded
                    | LimitOnLoansExceeded | LimitOnSavingsAccountsExceeded | LimitOnMoneyMarketAccountExceeded ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Print main menu to the screen
     */
    public static void mainMenuPrinter() {
        System.out.println("(P) Perform Banking Transactions");
        System.out.println("(N) Create a New Account");
        System.out.println("(C) Close an account");
        System.out.println("(R) Range search");
        System.out.println("(Q) to quit");
        System.out.print("Please make your choice: ");
    }

    /**
     * Initialize the app. Every interaction starts here
     */
    public static void initializeApp() {
        String choice;
        do {
            System.out.println(DASHES);
            System.out.printf("WELCOME TO %s%n", bank.getBankName());
            System.out.println(DASHES);
            mainMenuPrinter();
            choice = console.nextLine().trim().toLowerCase();

            if (choice.startsWith("p")) {
                performBankingTransaction();
            }

            else if (choice.startsWith("n")) {
                createNewAccountPrompt();
            }

            else if (choice.startsWith("c")) {
                closeAccount();
            }

            else if (choice.startsWith("r")) {
                rangeSearch();
            }
        } while (!choice.startsWith("q"));
    }

    /**
     * Range search main screen
     */
    public static void rangeSearch() {
        do {
            backHere = true;
            System.out.println(DASHES);
            System.out.println("Range Search");
            System.out.println(DASHES);
            accountType = accountTypePrompt();
            if (accountType != null) {
                if (accountType == AccountType.BANK_ACCOUNT) {
                    bankAccountRangeSearch();
                } else if (accountType == AccountType.BANK_LOAN) {
                    bankLoanRangeSearch();
                }
            }
        } while (backHere);
    }

    /**
     * Display range of BankLoan object within range
     */
    public static void bankLoanRangeSearch() {
        List<BankLoan> rangeList;
        double lowerBound, upperBound;
        String lowerBoundPrompt, upperBoundPrompt;

        lowerBoundPrompt = "What is the lower bound? ";
        lowerBound = prompt(lowerBoundPrompt);

        upperBoundPrompt = "What is the upper bound? ";
        upperBound = prompt(upperBoundPrompt);

        if (lowerBound < 0 || upperBound < 0) {
            System.out.println("Invalid input");
        } else {
            try {
                rangeList = bank.rangeSearchBankLoan(lowerBound, upperBound);
                System.out.println("This is your list!");
                for (int i = 0; i < rangeList.size(); i++) {
                    System.out.println(rangeList.get(i));
                }
            } catch (IllegalArgumentException | NoSuchElementException ex) {
                System.out.println(ex);
            }
        }
    }

    /**
     * Display range of BankAccount object within range
     */
    public static void bankAccountRangeSearch() {
        List<BankAccount> rangeList;
        double lowerBound, upperBound;
        String lowerBoundPrompt, upperBoundPrompt;

        lowerBoundPrompt = "What is the lower bound? ";
        lowerBound = prompt(lowerBoundPrompt);

        upperBoundPrompt = "What is the upper bound? ";
        upperBound = prompt(upperBoundPrompt);

        if (lowerBound < 0 || upperBound < 0) {
            System.out.println("Invalid input");
        } else {
            try {
                rangeList = bank.rangeSearchBankAccount(lowerBound, upperBound);
                System.out.println("This is your list!");
                for (int i = 0; i < rangeList.size(); i++) {
                    System.out.println(rangeList.get(i));
                }
            } catch (IllegalArgumentException | NoSuchElementException ex) {
                System.out.println(ex);
            }
        }
    }

    /**
     * Close account main menu
     */
    public static void closeAccount() {
        do {
            backHere = true;
            System.out.println(DASHES);
            System.out.println("Close Account.");
            System.out.println(DASHES);
            accountType = accountTypePrompt();
            if (accountType != null) {
                closeAccountPrompt();
            }
        } while (backHere);
    }

    /**
     * Prompt user for customer name to close account
     */
    public static void closeAccountPrompt() {
        String input;
        BankCustomer bankCustomer;
        String tryAgain;
        do {
            tryAgain = "n";
            bank.printBankCustomer();
            System.out.print("Name of customer. First name followed by last name: ");
            input = console.nextLine().trim().toLowerCase();

            if (!input.equals("q")) {
                try {
                    bankCustomer = bank.findBankCustomer(input);
                    if (bankCustomer != null) {
                        executeCloseAccount(bankCustomer);
                    } else {
                        System.out.print("Not found. Try again. Press N for No: ");
                        tryAgain = console.nextLine().trim().toLowerCase();
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                    System.out.print("Try again? Press Press N for No: ");
                    tryAgain = console.nextLine().trim().toLowerCase();
                }
            }
        } while (!input.startsWith("q") && !tryAgain.equals("n"));
    }

    /**
     * prompt user for account number to be closed
     * 
     * @param bankCustomer BankCustomer object to be closed account from
     */
    public static void executeCloseAccount(BankCustomer bankCustomer) {
        String tryAgain;
        do {
            tryAgain = "n";
            System.out.println("What account to close? ");
            bank.printBankCustomerAccounts(bankCustomer);

            if (accountType == AccountType.BANK_ACCOUNT) {
                BankAccount bankAccount = findBankAccount();

                if (bankAccount != null) {
                    try {
                        bank.closeBankAccount(bankAccount, bankCustomer);
                        System.out.println("Success!");
                        System.out.println("Available accounts");
                        bank.printBankCustomerAccounts(bankCustomer);
                        System.out.println("One more?");
                        tryAgain = console.nextLine().trim().toLowerCase();
                    } catch (CustomerDoNotOwnThisAccount ex) {
                        System.out.println(ex.getMessage());
                        System.out.print("Try again? Press N for No: ");
                        tryAgain = console.nextLine().trim().toLowerCase();
                    }
                }
            }

            else if (accountType == AccountType.BANK_LOAN) {
                BankLoan bankLoan = findBankLoan();

                if (bankLoan != null) {
                    try {
                        bank.closeBankLoan(bankLoan, bankCustomer);
                        System.out.println("Success!");
                        System.out.println("Available accounts");
                        bank.printBankCustomerAccounts(bankCustomer);
                        System.out.println("One more?");
                        tryAgain = console.nextLine().trim().toLowerCase();
                    } catch (CustomerDoNotOwnThisLoan | LoanHasNotBeenPaidOff ex) {
                        System.out.println(ex.getMessage());
                        System.out.print("Try again? Press N for No: ");
                        tryAgain = console.nextLine().trim().toLowerCase();
                    }
                }
            }

        } while (!tryAgain.startsWith("n"));
    }

    /**
     * Create new account main menu
     */
    public static void createNewAccountPrompt() {
        do {
            backHere = true;
            System.out.println(DASHES);
            System.out.println("Create a new Account");
            System.out.println(DASHES);
            accountType = accountTypePrompt();
            if (accountType == AccountType.BANK_ACCOUNT) {
                newBankAccountPrompt();
            } else if (accountType == AccountType.BANK_LOAN) {
                newBankAccountPrompt();
            }
        } while (backHere);
    }

    /**
     * create new bank account main menu
     */
    public static void newBankAccountPrompt() {
        String input;
        BankCustomer bankCustomer = null;

        do {
            System.out.println(DASHES);
            System.out.println(accountType.getDisplayName() + " Creator");
            System.out.println("Existing or new customer. q to quit");
            System.out.println(DASHES);
            input = console.nextLine().trim().toLowerCase();

            if (input.startsWith("e")) {
                bankCustomer = existingCustomer();
            }

            else if (input.startsWith("n")) {
                bankCustomer = newCustomer();
            }

            else if (input.startsWith("q")) {
                break;
            }

            if (bankCustomer != null) {
                String again;
                do {
                    again = "n";
                    if (accountType == AccountType.BANK_ACCOUNT) {
                        BankAccount bankAccount = createNewAccount(bankCustomer);
                        if (bankAccount != null) {
                            bank.printBankCustomerAccounts(bankCustomer);
                            System.out.println("again?");
                            again = console.nextLine().trim().toLowerCase();
                        }
                    } else if (accountType == AccountType.BANK_LOAN) {
                        BankLoan bankLoan = createNewLoanAccount(bankCustomer);
                        if (bankLoan != null) {

                            bank.printBankCustomerAccounts(bankCustomer);
                            System.out.println("one more?");
                            again = console.nextLine().trim().toLowerCase();
                        }
                    }

                } while (!again.startsWith("n"));
            }
        } while (!input.startsWith("q"));
    }

    /**
     * Create new bank loan prompt
     * 
     * @param bankCustomer Bank Customer object that wants to loan
     * @return BankLoan object just created
     */
    public static BankLoan createNewLoanAccount(BankCustomer bankCustomer) {
        String tryAgain;
        BankLoan result = null;
        double balance, interestRate;
        do {
            tryAgain = "n";
            String balancePrompt = "How much money do you want to loan ";
            balance = prompt(balancePrompt);

            if (balance != -1) {
                String interestPrompt = "How much interest? ";
                interestRate = prompt(interestPrompt);

                if (interestRate != -1) {
                    try {
                        result = bank.createBankLoan(bankCustomer, balance, interestRate, LocalDate.now());
                    } catch (InvalidInterestRateForLoan | LimitOnLoansExceeded ex) {
                        System.out.println(ex.getMessage());
                        System.out.print("Try again? Press N for No: ");
                        tryAgain = console.nextLine().trim().toLowerCase();
                    }
                }
            }
        } while (!tryAgain.startsWith("n"));
        return result;
    }

    /**
     * Create new bank account prompt
     * 
     * @param bankCustomer Bank Customer object that wants to create account
     * @return BankAccount object just created
     */
    public static BankAccount createNewAccount(BankCustomer bankCustomer) {
        String tryAgain;
        BankAccount result = null;
        BankAccount.AccountType typeOfBankAccount;
        double balance;
        double interestRate;
        do {
            tryAgain = "n";
            typeOfBankAccount = bankAccountTypePrompt();

            if (typeOfBankAccount != null) {
                String prompt = "How much money do you want to put in the account? ";
                balance = prompt(prompt);

                if (balance != -1) {
                    String prompt2 = "How much interest? ";
                    interestRate = prompt(prompt2);

                    if (interestRate != -1) {
                        try {
                            result = bank.createBankAccount(bankCustomer, typeOfBankAccount, balance, interestRate,
                                    LocalDate.now());
                        } catch (InvalidSavingsInterestRate | InvalidCheckingInterestRate
                                | InvalidMoneyMarketInterestRate | LimitOnMoneyMarketAccountExceeded
                                | LimitOnSavingsAccountsExceeded | LimitOnAccountsExceeded
                                | CustomerOwnsAccountAlready ex) {
                            System.out.println(ex.getMessage());
                            System.out.print("Try again? Press N for no: ");
                            tryAgain = console.nextLine().trim().toLowerCase();
                        }
                    }
                }
            }
        } while (!tryAgain.startsWith("n"));
        return result;
    }

    /**
     * Ask user whether it's checking, money market, or savings account
     * 
     * @return
     */
    public static BankAccount.AccountType bankAccountTypePrompt() {
        String choice;
        String tryAgain;
        BankAccount.AccountType result = null;

        do {
            tryAgain = "n";
            System.out.println("What type of account do you want to create? ");
            System.out.println("(C) Checking");
            System.out.println("(M) Money market");
            System.out.println("(S) Savings");
            System.out.print("Your choice: ");
            choice = console.nextLine().trim().substring(0, 1);

            try {
                result = BankAccount.AccountType.valueOf(choice.toUpperCase());
            }

            catch (IllegalArgumentException ex) {
                System.out.println("Invalid account type");
                System.out.print("Try again? Press N for No: ");
                tryAgain = console.nextLine().trim().toLowerCase();
            }
        } while (!tryAgain.startsWith("n"));
        return result;
    }

    /**
     * Ask user customer information to create a new Bank Customer Object
     * 
     * @return BankCustomer object just created
     */
    public static BankCustomer newCustomer() {
        BankCustomer bankCustomer = null;
        String firstName;
        String lastName;
        String DOB;
        LocalDate dateOfBirth;
        String again;

        do {
            again = "n";
            System.out.print("Please enter your first name. Q to quit: ");
            firstName = console.nextLine().trim();

            if (!firstName.equals("q")) {
                System.out.print("Please enter your last name. Q to quit ");
                lastName = console.nextLine().trim();

                if (!lastName.equals("q")) {
                    System.out.print("Please enter date of birth in format YYYY-MM-DD. Q to quit: ");
                    DOB = console.nextLine().trim();

                    if (!DOB.equals("q")) {
                        try {
                            dateOfBirth = LocalDate.parse(DOB);
                            bankCustomer = bank.createBankCustomer(firstName, lastName, dateOfBirth);

                        } catch (DateTimeParseException ex) {
                            System.out.print("Invalid date. Try again? Press N for no: ");
                            again = console.nextLine().trim().toLowerCase();
                        }
                    }
                }
            }
        } while (!again.startsWith("n"));
        return bankCustomer;

    }

    /**
     * ask user for full name if he picked existing customer
     * 
     * @return BankCustomer object that matches the name
     */
    public static BankCustomer existingCustomer() {
        BankCustomer bankCustomer = null;
        String input;
        String tryAgain;

        do {
            tryAgain = "n";
            bank.printBankCustomer();
            System.out.println("Please enter full name. First name follows by last name. Q to quit");
            input = console.nextLine().trim().toLowerCase();

            if (!input.startsWith("q")) {
                try {
                    bankCustomer = bank.findBankCustomer(input);

                    if (bankCustomer == null) {
                        System.out.print("Not found. Try again? Press N for no: ");
                        tryAgain = console.nextLine().trim().toLowerCase();
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                    System.out.print("Try again? Press N for no: ");
                    tryAgain = console.nextLine().trim().toLowerCase();
                }
            }
        } while (!input.startsWith("q") && !tryAgain.startsWith("n"));
        return bankCustomer;
    }

    /**
     * Perform banking transaction main menu
     */
    public static void performBankingTransaction() {
        do {
            backHere = true;
            System.out.println("PERFORM BANKING TRANSACTION");
            System.out.println(DASHES);
            accountType = accountTypePrompt();

            if (accountType == AccountType.BANK_ACCOUNT) {
                bank.printBankAccount();
                BankAccount bankAccount = findBankAccount();

                if (bankAccount != null) {
                    bankAccountTransaction(bankAccount);
                }
            }

            else if (accountType == AccountType.BANK_LOAN) {
                bank.printBankLoan();
                BankLoan bankLoan = findBankLoan();

                if (bankLoan != null) {
                    bankLoanTransaction(bankLoan);
                }
            }
        } while (backHere);
    }

    /**
     * BankAccount Transaction main menu
     * 
     * @param bankAccount account to be performed transactions
     */
    public static void bankAccountTransaction(BankAccount bankAccount) {
        String choice;
        do {
            System.out.println("BANK ACCOUNT TRANSACTION");
            System.out.println(DASHES);
            System.out.println("(V) View details");
            System.out.println("(D) Deposit");
            System.out.println("(W) Withdraw");
            System.out.println("(T) Transfer");
            System.out.println("(Q) To Quit");
            System.out.print("Your choice: ");
            choice = console.nextLine().trim().toLowerCase();

            if (choice.startsWith("v")) {
                System.out.println(SEPARATOR);
                System.out.println(bankAccount);
                System.out.println(SEPARATOR);
            }

            else if (choice.startsWith("d")) {
                depositBankAccount(bankAccount);
            }

            else if (choice.startsWith("w")) {
                withdrawBankAccount(bankAccount);
            }

            else if (choice.startsWith("t")) {
                transferBankAccount(bankAccount);
            }
        } while (!choice.startsWith("q"));
    }

    /**
     * BankLoan Transaction main menu
     * 
     * @param bankLoan BankLoan to be transacted
     */
    public static void bankLoanTransaction(BankLoan bankLoan) {
        String choice;
        do {
            System.out.println("BANK LOAN TRANSACTION");
            System.out.println(DASHES);
            System.out.println("(V) View details");
            System.out.println("(M) Make payment");
            System.out.println("(Q) Quit");
            choice = console.nextLine().trim().toLowerCase();

            if (choice.startsWith("v")) {
                System.out.println(DASHES);
                System.out.println(bankLoan);
                System.out.println(DASHES);
            }

            else if (choice.startsWith("m")) {
                makePayment(bankLoan);
            }
        } while (!choice.startsWith("q"));
    }

    /**
     * make payment prompt screen for bank loan
     * 
     * @param bankLoan BankLoan object to be made payment
     */
    public static void makePayment(BankLoan bankLoan) {
        String tryAgain, prompt;
        double amount;

        do {
            tryAgain = "n";
            prompt = "How much do you want to pay? ";
            amount = prompt(prompt);

            if (amount != -1) {
                try {
                    bank.makePaymentBankLoan(bankLoan, amount);
                    System.out.println(DASHES);
                    System.out.println("Successful. New balance: ");
                    System.out.println(bankLoan);
                    System.out.println(DASHES);
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                    System.out.print("Try again? Y/N ");
                    tryAgain = console.nextLine().trim().toLowerCase();
                }
            }
        } while (!tryAgain.startsWith("n"));
    }

    /**
     * transfer Bank Account prompt screen
     * 
     * @param bankAccount BankAccount to be transferred from
     */
    public static void transferBankAccount(BankAccount bankAccount) {
        String tryAgain, prompt;
        double amount;

        do {
            tryAgain = "n";
            System.out.println("Which account do you want to transfer to?");
            BankAccount another = findBankAccount();
            prompt = "How much money do you want to transfer? ";
            amount = prompt(prompt);

            try {
                bank.transferBankAccount(bankAccount, another, amount);
                System.out.println("New balance");
                System.out.println(bankAccount);
                System.out.println(another);
            }

            catch (InsufficientFunds | InvalidTransferAccount ex) {
                System.out.println(ex.getMessage());
                System.out.print("Try again? Y/N ");
                tryAgain = console.nextLine().trim().toLowerCase();
            }
        } while (!tryAgain.startsWith("n"));
    }

    /**
     * Deposit Bank Account Prompt Screen
     * 
     * @param bankAccount BankAccount to be deposited
     */
    public static void depositBankAccount(BankAccount bankAccount) {
        String tryAgain, prompt;
        double amount;

        do {
            tryAgain = "n";
            prompt = "How much do you want to deposit? ";
            amount = prompt(prompt);

            if (amount != -1) {
                try {
                    bank.depositBankAccount(bankAccount, amount);
                    System.out.println(DASHES);
                    System.out.println("Successful. New balance: ");
                    System.out.println(bankAccount);
                    System.out.println(DASHES);
                }

                catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                    System.out.print("Try again? Y/N ");
                    tryAgain = console.nextLine().trim().toLowerCase();
                }
            }
        } while (!tryAgain.startsWith("n"));
    }

    /**
     * Withdrawal BankAccount prompt screen
     * 
     * @param bankAccount BankAccount to be withdrawn
     */
    public static void withdrawBankAccount(BankAccount bankAccount) {
        String tryAgain, prompt;
        double amount;

        do {
            tryAgain = "n";
            prompt = "How much do you want to withdraw? ";
            amount = prompt(prompt);

            try {
                bank.withDrawBankAccount(bankAccount, amount);
                System.out.println("Success. New Balance: ");
                System.out.println(bankAccount);
            }

            catch (InsufficientFunds | IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                System.out.print("Try again? Y/N? ");
                tryAgain = console.nextLine().trim().toLowerCase();
            }
        } while (!tryAgain.startsWith("n"));
    }

    /**
     * Prompt and validate number
     * 
     * @param prompt message to be displayed on screen
     * @return a number got from user as double
     */
    public static double prompt(String prompt) {
        String input;
        double amount = -1;
        boolean stop;

        do {
            stop = false;
            System.out.print(prompt);
            input = console.nextLine().trim().toLowerCase();

            try {
                amount = Double.parseDouble(input);
                stop = true;
            }

            catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter a positive integer");
            }
        } while (!stop);
        return amount;
    }

    /**
     * Ask for type of account, bank account or bank loan
     * 
     * @return accountType
     */
    public static AccountType accountTypePrompt() {
        AccountType result = null;
        String input;
        String tryAgain;

        do {
            tryAgain = "n";
            System.out.println("(B) Bank Account?");
            System.out.println("(L) Bank Loan?");
            System.out.println("(Q) To Quit");
            System.out.print("Your choice: ");
            input = console.nextLine().trim().toLowerCase();

            if (input.startsWith("b")) {
                result = AccountType.BANK_ACCOUNT;
            }

            else if (input.startsWith("l")) {
                result = AccountType.BANK_LOAN;
            }

            else if (!input.startsWith("b") && !input.startsWith("l") & !input.startsWith("q")) {
                System.out.println("Wrong input");
                System.out.println("Enter B for Bank Account");
                System.out.println("Or L for Bank Loan");
                System.out.print("Try again? Y/N? ");
                tryAgain = console.nextLine().trim().toLowerCase();
            }

            else if (input.startsWith("q")) {
                backHere = false;
            }
        } while (!tryAgain.startsWith("n"));
        return result;
    }

    /**
     * Prompt for account number, then find bank account object
     * 
     * @return BankAccount object found
     */
    public static BankAccount findBankAccount() {
        BankAccount bankAccount = null;
        boolean exit = false;
        String again;

        do {
            again = "n";
            int accountNumber = accountNumberPrompt();

            if (accountNumber == -1) {
                exit = true;
            }

            else {
                bankAccount = bank.findBankAccount(accountNumber);
                if (bankAccount == null) {
                    System.out.println("BankAccount not found!");
                    System.out.print("Try again? Y/N? ");
                    again = console.nextLine().trim().toLowerCase();
                }
            }
        } while (!again.startsWith("n") && !exit);
        return bankAccount;
    }

    /**
     * Prompt for account number, then find bank loan object
     * 
     * @return BankLoan object found
     */
    public static BankLoan findBankLoan() {
        BankLoan bankLoan = null;
        boolean exit = false;
        String again;

        do {
            again = "n";
            int accountNumber = accountNumberPrompt();

            if (accountNumber == -1) {
                exit = true;
            }

            else {
                bankLoan = bank.findLoanAccount(accountNumber);
                if (bankLoan == null) {
                    System.out.println("BankLoan not found!");
                    System.out.print("Try again? Y/N? ");
                    again = console.nextLine().trim().toLowerCase();
                }
            }
        } while (!again.startsWith("n") && !exit);
        return bankLoan;
    }

    /**
     * Ask for account number
     * 
     * @return account number as an integer
     */
    public static int accountNumberPrompt() {
        String input;
        boolean stop = false;
        int accountNumber = -1;

        do {

            System.out.println("Please enter account number. -1 to quit");
            input = console.nextLine().trim();

            try {
                accountNumber = Integer.parseInt(input);

                if (accountNumber <= 0 && accountNumber != -1) {
                    System.out.println("Please enter a positive number");
                } else {
                    stop = true;
                }
            }

            catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter a positive number.");
            }
        } while (!stop);
        return accountNumber;
    }
}
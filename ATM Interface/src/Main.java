import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// BankAccount class representing a user's bank account
class BankAccount {
    private double balance;
    private String accountNumber;
    private String pin;

    // Constructor to initialize the account with an account number, PIN, and initial balance
    public BankAccount(String accountNumber, String pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        balance = initialBalance;
    }

    // Getter method to retrieve the account number
    public String getAccountNumber() {
        return accountNumber;
    }

    // Method to verify if the provided PIN matches the account's PIN
    public boolean verifyPin(String inputPin) {
        return pin.equals(inputPin);
    }

    // Getter method to retrieve the current balance
    public double getBalance() {
        return balance;
    }

    // Method to deposit a specified amount into the account
    public void deposit(double amount) {
        balance += amount;
    }

    // Method to withdraw a specified amount from the account
    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true; // Withdrawal successful
        }
        return false; // Insufficient funds for withdrawal
    }

    // Method to transfer a specified amount from this account to another account
    public void transfer(BankAccount destination, double amount) {
        if (withdraw(amount)) {
            destination.deposit(amount);
        }
    }

    // Method to log a transaction (for demonstration purposes, this just prints to console)
    public void logTransaction(String transactionType, double amount) {
        System.out.println("Transaction Type: " + transactionType + ", Amount: " + amount);
    }
}

// ATM class simulating an automated teller machine
class ATM {
    private Map<String, BankAccount> accounts; // Map to store bank accounts (account number -> account)
    private BankAccount currentAccount; // Current user's bank account
    private Scanner scanner;

    // Constructor to initialize the ATM with a map of bank accounts
    public ATM(Map<String, BankAccount> accounts) {
        this.accounts = accounts;
        scanner = new Scanner(System.in);
    }

    // Method to authenticate a user by checking account number and PIN
    public boolean authenticate(String accountNumber, String pin) {
        BankAccount account = accounts.get(accountNumber);
        if (account != null && account.verifyPin(pin)) {
            currentAccount = account;
            return true; // Authentication successful
        }
        return false; // Authentication failed
    }

    // Method to display the main menu of ATM options
    public void displayMenu() {
        System.out.println("Welcome to the ATM!");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
        System.out.print("\nOption: ");
    }

    // Method to execute the user's selected option
    public void executeOption(int option) {
        try {
            switch (option) {
                case 1:
                    System.out.println("Current Balance: " + currentAccount.getBalance());
                    break;
                case 2:
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    currentAccount.deposit(depositAmount);
                    currentAccount.logTransaction("Deposit", depositAmount);
                    System.out.println("Deposit successful.");
                    break;
                case 3:
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawalAmount = scanner.nextDouble();
                    boolean success = currentAccount.withdraw(withdrawalAmount);
                    if (success) {
                        currentAccount.logTransaction("Withdrawal", withdrawalAmount);
                        System.out.println("Withdrawal successful.");
                    } else {
                        System.out.println("Insufficient funds.");
                    }
                    break;
                case 4:
                    System.out.print("Enter destination account number: ");
                    String destinationAccountNumber = scanner.next();
                    System.out.print("Enter transfer amount: ");
                    double transferAmount = scanner.nextDouble();
                    BankAccount destinationAccount = accounts.get(destinationAccountNumber);
                    if (destinationAccount != null) {
                        currentAccount.transfer(destinationAccount, transferAmount);
                        currentAccount.logTransaction("Transfer", transferAmount);
                        System.out.println("Transfer successful.");
                    } else {
                        System.out.println("Destination account not found.");
                    }
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Map<String, BankAccount> accounts = new HashMap<>();
        accounts.put("123456", new BankAccount("123456", "1234", 1000.0));
        accounts.put("987654", new BankAccount("987654", "9876", 2000.0));

        ATM atm = new ATM(accounts);

        Scanner scanner = new Scanner(System.in);

        boolean authenticated = false;

        while (!authenticated) {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.next();

            System.out.print("Enter PIN: ");
            String pin = scanner.next();

            authenticated = atm.authenticate(accountNumber, pin);

            if (!authenticated) {
                System.out.println("Authentication failed. Invalid account number or PIN.");
            }
        }

        while (true) {
            try {
                atm.displayMenu();
                int option = scanner.nextInt();
                atm.executeOption(option);
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        }
    }
}



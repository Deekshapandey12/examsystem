package atm;
import java.util.*;

class User {
    private String userId;
    private String userPin;
    private double balance;
    private List<String> transactionHistory;

    public User(String userId, String userPin, double balance) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
        addTransaction("Account created with balance: ₹" + balance);
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPin() {
        return userPin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }
}

class ATM {
    private Map<String, User> users;
    private User currentUser;

    public ATM() {
        users = new HashMap<>();
        // Adding some default users
        users.put("user1", new User("user1", "pin1", 1000.00));
        users.put("user2", new User("user2", "pin2", 1500.00));
    }

    public boolean authenticateUser(String userId, String userPin) {
        User user = users.get(userId);
        if (user != null && user.getUserPin().equals(userPin)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public double checkBalance() {
        return currentUser.getBalance();
    }

    public void deposit(double amount) {
        double newBalance = currentUser.getBalance() + amount;
        currentUser.setBalance(newBalance);
        currentUser.addTransaction("Deposited: ₹" + amount);
        System.out.println("Successfully deposited ₹" + amount);
    }

    public void withdraw(double amount) {
        if (currentUser.getBalance() >= amount) {
            double newBalance = currentUser.getBalance() - amount;
            currentUser.setBalance(newBalance);
            currentUser.addTransaction("Withdrew: ₹" + amount);
            System.out.println("Successfully withdrew ₹" + amount);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void transfer(String toUserId, double amount) {
        User toUser = users.get(toUserId);
        if (toUser != null) {
            if (currentUser.getBalance() >= amount) {
                double newBalance = currentUser.getBalance() - amount;
                currentUser.setBalance(newBalance);
                currentUser.addTransaction("Transferred ₹" + amount + " to " + toUserId);

                double toUserNewBalance = toUser.getBalance() + amount;
                toUser.setBalance(toUserNewBalance);
                toUser.addTransaction("Received ₹" + amount + " from " + currentUser.getUserId());

                System.out.println("Successfully transferred ₹" + amount + " to " + toUserId);
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Recipient user not found.");
        }
    }

    public void showTransactionHistory() {
        List<String> transactions = currentUser.getTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (String transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("ATM Menu:");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw Money");
            System.out.println("3. Deposit Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    showTransactionHistory();
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    withdraw(withdrawAmount);
                    break;
                case 3:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    deposit(depositAmount);
                    break;
                case 4:
                    System.out.print("Enter user ID to transfer to: ");
                    String toUserId = scanner.next();
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    transfer(toUserId, transferAmount);
                    break;
                case 5:
                    logout();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATM atm = new ATM();

        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter User PIN: ");
        String userPin = scanner.nextLine();

        if (atm.authenticateUser(userId, userPin)) {
            System.out.println("Authentication successful.");
            atm.showMenu();
        } else {
            System.out.println("Invalid User ID or PIN.");
        }
    }
}
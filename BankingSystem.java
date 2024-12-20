class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }
    public synchronized void deposit(double amount) {
        balance += amount;
        System.out.println(Thread.currentThread().getName() + " - Deposited: " + amount + ", Current Balance: " + balance);
    }
    public synchronized void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " - Withdrew: " + amount + ", Current Balance: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() + " - Insufficient funds for withdrawal of: " + amount);
        }
    }
    public synchronized double getBalance() {
        return balance;
    }
}
class Transaction extends Thread {
    private final BankAccount account;
    private final String transactionType;
    private final double amount;

    public Transaction(BankAccount account, String transactionType, double amount) {
        this.account = account;
        this.transactionType = transactionType;
        this.amount = amount;
    }
    @Override
    public void run() {
        switch (transactionType) {
            case "deposit":
                account.deposit(amount);
                break;
            case "withdraw":
                account.withdraw(amount);
                break;
            default:
                System.out.println(Thread.currentThread().getName() + " - Invalid transaction type.");
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.err.println(Thread.currentThread().getName() + " - Interrupted while processing.");
        }
    }
}
public class BankingSystem {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(1000);

        Thread t1 = new Transaction(account, "deposit", 200);
        t1.setName("Thread 1");
        Thread t2 = new Transaction(account, "withdraw", 150);
        t2.setName("Thread 2");
        Thread t3 = new Transaction(account, "withdraw", 1100);
        t3.setName("Thread 3");

        t1.start();
        t2.start();
        t3.start();

        System.out.println("Final Account Balance: " + account.getBalance());
    }
}

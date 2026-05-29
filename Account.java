
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Account {

    private final String accountNumber;
    private String       holderName;
    private String       pin;               // stored as plain string (hash in production)
    private double       balance;
    private final List<Transaction> history = new ArrayList<>();

    public Account(String accountNumber, String holderName, String pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.holderName    = holderName;
        this.pin           = pin;
        this.balance       = initialBalance;
    }
    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void changePin(String newPin) {
        this.pin = newPin;
    }
    public void credit(double amount, String note) {
        balance += amount;
        history.add(new Transaction(Transaction.Type.DEPOSIT, amount, balance, note));
    }

    public void debit(double amount, String note) {
        balance -= amount;
        history.add(new Transaction(Transaction.Type.WITHDRAWAL, amount, balance, note));
    }

    public void transferOut(double amount, String toAccount) {
        balance -= amount;
        history.add(new Transaction(Transaction.Type.TRANSFER_OUT, amount, balance,
                "To: " + toAccount));
    }

    public void transferIn(double amount, String fromAccount) {
        balance += amount;
        history.add(new Transaction(Transaction.Type.TRANSFER_IN, amount, balance,
                "From: " + fromAccount));
    }
    public String getAccountNumber() { return accountNumber; }
    public String getHolderName()    { return holderName; }
    public double getBalance()       { return balance; }
    /** Package-private: only Bank uses this for persistence. */
    String getPin()                  { return pin; }
    public List<Transaction> getHistory() {
        return Collections.unmodifiableList(history);
    }
    public List<Transaction> getRecentHistory(int n) {
        int from = Math.max(0, history.size() - n);
        return Collections.unmodifiableList(history.subList(from, history.size()));
    }
    @Override
    public String toString() {
        return String.format("Account[%s | %s | Balance: %.2f]",
                accountNumber, holderName, balance);
    }
}


import java.io.*;
import java.util.*;
public class Bank {

    private final String dataFilePath;
    private final Map<String, Account> accounts = new LinkedHashMap<>();
    private static final String[][] SEED_ACCOUNTS = {
        // { accountNumber, holderName, PIN, initialBalance }
        { "100001", "Geethika Gunnam",  "1234", "50000.00" },
        { "100002", "Vani Daripelly",      "5678", "28500.00" },
        { "100003", "Mokshith thanne", "9999", "102000.00" },
        { "100004", "Keerthana Varanganti",     "0000", "7500.00"  },
    };

    public Bank(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        loadAccounts();
    }
    public Account authenticate(String accountNumber, String pin) {
        Account acc = accounts.get(accountNumber);
        if (acc != null && acc.validatePin(pin)) return acc;
        return null;
    }

    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void deposit(Account account, double amount) {
        account.credit(amount, "");
        saveAccounts();
    }

    public void withdraw(Account account, double amount) {
        account.debit(amount, "");
        saveAccounts();
    }

    public boolean transfer(Account from, String toAccountNumber, double amount) {
        Account to = accounts.get(toAccountNumber);
        if (to == null) return false;
        from.transferOut(amount, toAccountNumber);
        to.transferIn(amount, from.getAccountNumber());
        saveAccounts();
        return true;
    }

    private void loadAccounts() {
        File f = new File(dataFilePath);
        if (!f.exists()) {
            seedAccounts();
            saveAccounts();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;
                String accNum  = parts[0].trim();
                String name    = parts[1].trim();
                String pin     = parts[2].trim();
                double balance = Double.parseDouble(parts[3].trim());
                accounts.put(accNum, new Account(accNum, name, pin, balance));
            }
        } catch (IOException e) {
            System.err.println("[Bank] Failed to load accounts: " + e.getMessage());
            seedAccounts();
        }
    }

    public void saveAccounts() {
        try {
            File dir = new File(dataFilePath).getParentFile();
            if (dir != null && !dir.exists()) dir.mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(dataFilePath))) {
                pw.println("# ATM Account Data — DO NOT EDIT MANUALLY");
                pw.println("# Format: accountNumber|holderName|pin|balance");
                for (Account acc : accounts.values()) {
                    pw.printf("%s|%s|%s|%.2f%n",
                            acc.getAccountNumber(),
                            acc.getHolderName(),
                            acc.getPin(),       // see note below
                            acc.getBalance());
                }
            }
        } catch (IOException e) {
            System.err.println("[Bank] Failed to save accounts: " + e.getMessage());
        }
    }

    private void seedAccounts() {
        for (String[] row : SEED_ACCOUNTS) {
            Account acc = new Account(row[0], row[1], row[2],
                    Double.parseDouble(row[3]));
            accounts.put(row[0], acc);
        }
    }
}

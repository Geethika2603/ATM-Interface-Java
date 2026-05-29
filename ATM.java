
/**
 * ATM — Session controller and business rule enforcer.
 *
 * OOP concepts:
 *   - Composition: holds references to Bank, current Account.
 *   - Single Responsibility: business rules & session flow only.
 *   - Abstraction: callers just call atm.startSession(); internals are hidden.
 *
 * Business rules enforced here (NOT in Account or Bank):
 *   - Max 3 PIN attempts before card lockout.
 *   - Minimum withdrawal: ₹100  (multiples of 100).
 *   - Maximum single withdrawal: ₹20,000.
 *   - Transfer destination must exist and differ from self.
 *   - New PIN must be exactly 4 digits.
 */
public class ATM {

    // ── Configuration constants ───────────────────────────────────────────────
    private static final int    MAX_PIN_ATTEMPTS    = 3;
    private static final double MIN_WITHDRAWAL      = 100.0;
    private static final double MAX_WITHDRAWAL      = 20_000.0;
    private static final double WITHDRAWAL_MULTIPLE = 100.0;

    // ── Dependencies (injected via constructor) ───────────────────────────────
    private final Bank bank;

    // ── Session state ─────────────────────────────────────────────────────────
    private Account currentAccount = null;
    private boolean running        = true;

    public ATM(Bank bank) {
        this.bank = bank;
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    /**
     * Main loop: keeps the ATM running until the JVM exits.
     * Each iteration = one user session (card in → card out).
     */
    public void startSession() {
        Display.showWelcomeBanner();

        while (running) {
            if (!authenticateUser()) {
                // Either too many failures or user cancelled — loop again
                continue;
            }
            runMenu();
        }
    }

    // ── Authentication ────────────────────────────────────────────────────────

    private boolean authenticateUser() {
        String accountNumber = InputHandler.readAccountNumber();

        if (accountNumber.equalsIgnoreCase("quit")) {
            running = false;
            return false;
        }

        if (!bank.accountExists(accountNumber)) {
            Display.showError("Account not found. Please check the number and try again.");
            return false;
        }

        for (int attempt = 1; attempt <= MAX_PIN_ATTEMPTS; attempt++) {
            String pin = InputHandler.readPin("PIN");
            Account acc = bank.authenticate(accountNumber, pin);
            if (acc != null) {
                currentAccount = acc;
                Display.showInfo("Welcome, " + acc.getHolderName() + "!");
                return true;
            }
            int remaining = MAX_PIN_ATTEMPTS - attempt;
            if (remaining > 0) {
                Display.showPinAttempt(remaining);
            }
        }

        Display.showSessionTimeout();
        return false;
    }

    // ── Main menu loop ────────────────────────────────────────────────────────

    private void runMenu() {
        boolean inSession = true;
        while (inSession) {
            Display.showMainMenu(currentAccount);
            int choice = InputHandler.readMenuChoice();
            Display.blank();

            switch (choice) {
                case 1 -> doCheckBalance();
                case 2 -> doDeposit();
                case 3 -> doWithdraw();
                case 4 -> doTransfer();
                case 5 -> doHistory();
                case 6 -> doChangePin();
                case 0 -> {
                    Display.showGoodbye(currentAccount.getHolderName());
                    currentAccount = null;
                    inSession = false;
                }
                default -> Display.showInvalidOption();
            }
        }
    }

    // ── Operations ────────────────────────────────────────────────────────────

    private void doCheckBalance() {
        Display.showBalance(currentAccount);
    }

    private void doDeposit() {
        double amount = InputHandler.readAmount("deposit");
        if (amount < 0) { Display.showInfo("Deposit cancelled."); return; }

        bank.deposit(currentAccount, amount);
        Display.showDepositReceipt(amount, currentAccount.getBalance());
    }

    private void doWithdraw() {
        double amount = InputHandler.readAmount("withdraw");
        if (amount < 0) { Display.showInfo("Withdrawal cancelled."); return; }

        // ── Business rule checks ──────────────────────────────────────────────
        if (amount < MIN_WITHDRAWAL) {
            Display.showError(String.format(
                    "Minimum withdrawal is ₹%.0f.", MIN_WITHDRAWAL));
            return;
        }
        if (amount > MAX_WITHDRAWAL) {
            Display.showError(String.format(
                    "Maximum single withdrawal is ₹%.0f.", MAX_WITHDRAWAL));
            return;
        }
        if (amount % WITHDRAWAL_MULTIPLE != 0) {
            Display.showError(String.format(
                    "Amount must be a multiple of ₹%.0f.", WITHDRAWAL_MULTIPLE));
            return;
        }
        if (amount > currentAccount.getBalance()) {
            Display.showError("Insufficient funds.");
            return;
        }

        bank.withdraw(currentAccount, amount);
        Display.showWithdrawReceipt(amount, currentAccount.getBalance());
    }

    private void doTransfer() {
        String toAccNum = InputHandler.readLine("Recipient Account Number");

        if (toAccNum.equals(currentAccount.getAccountNumber())) {
            Display.showError("Cannot transfer to your own account.");
            return;
        }
        if (!bank.accountExists(toAccNum)) {
            Display.showError("Recipient account not found.");
            return;
        }

        double amount = InputHandler.readAmount("transfer");
        if (amount < 0) { Display.showInfo("Transfer cancelled."); return; }

        if (amount > currentAccount.getBalance()) {
            Display.showError("Insufficient funds.");
            return;
        }

        boolean ok = bank.transfer(currentAccount, toAccNum, amount);
        if (ok) {
            Display.showTransferReceipt(amount, toAccNum, currentAccount.getBalance());
        } else {
            Display.showError("Transfer failed. Please try again.");
        }
    }

    private void doHistory() {
        // Show up to 10 most recent transactions
        Display.showHistory(currentAccount, currentAccount.getRecentHistory(10));
    }

    private void doChangePin() {
        String currentPin = InputHandler.readPin("Current PIN");
        if (!currentAccount.validatePin(currentPin)) {
            Display.showError("Incorrect current PIN.");
            return;
        }

        String newPin = InputHandler.readPin("New PIN (4 digits)");
        if (!newPin.matches("\\d{4}")) {
            Display.showError("PIN must be exactly 4 digits.");
            return;
        }

        String confirmPin = InputHandler.readPin("Confirm New PIN");
        if (!newPin.equals(confirmPin)) {
            Display.showError("PINs do not match. PIN not changed.");
            return;
        }

        currentAccount.changePin(newPin);
        bank.saveAccounts();
        Display.showPinChangeSuccess();
    }
}

import java.util.List;
public class Display {

    private static final String DIVIDER  = "═".repeat(52);
    private static final String THIN_DIV = "─".repeat(52);
    private static final String BANK_NAME = "JAVA NATIONAL BANK";

    private Display() { /* utility class — no instantiation */ }

    // ── Banners ───────────────────────────────────────────────────────────────

    public static void showWelcomeBanner() {
        System.out.println("\n" + DIVIDER);
        System.out.println("  ██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗");
        System.out.println("  ██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║");
        System.out.println("  ██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║");
        System.out.println("  ██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║");
        System.out.println("  ╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║");
        System.out.println("   ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝");
        System.out.println(DIVIDER);
        System.out.printf("  %s — ATM SERVICE%n", BANK_NAME);
        System.out.println(DIVIDER);
        System.out.println("  Please insert your card (enter account number)");
        System.out.println(DIVIDER + "\n");
    }

    public static void showGoodbye(String holderName) {
        System.out.println("\n" + DIVIDER);
        System.out.printf("  Thank you for banking with us, %s!%n", holderName);
        System.out.println("  Please take your card. Have a great day!");
        System.out.println(DIVIDER + "\n");
    }

    public static void showSessionTimeout() {
        System.out.println("\n⚠  Session ended due to too many failed PIN attempts.");
    }

    // ── Menus ─────────────────────────────────────────────────────────────────

    public static void showMainMenu(Account account) {
        System.out.println("\n" + DIVIDER);
        System.out.printf("  Account: %-10s  |  %s%n",
                account.getAccountNumber(), account.getHolderName());
        System.out.printf("  Balance: ₹ %,.2f%n", account.getBalance());
        System.out.println(THIN_DIV);
        System.out.println("  [1] Check Balance");
        System.out.println("  [2] Deposit");
        System.out.println("  [3] Withdraw");
        System.out.println("  [4] Transfer Funds");
        System.out.println("  [5] Transaction History");
        System.out.println("  [6] Change PIN");
        System.out.println("  [0] Exit / Logout");
        System.out.println(DIVIDER);
        System.out.print("  Select an option: ");
    }

    // ── Balance ───────────────────────────────────────────────────────────────

    public static void showBalance(Account account) {
        System.out.println("\n" + THIN_DIV);
        System.out.println("  BALANCE ENQUIRY");
        System.out.println(THIN_DIV);
        System.out.printf("  Account   : %s%n",   account.getAccountNumber());
        System.out.printf("  Holder    : %s%n",   account.getHolderName());
        System.out.printf("  Balance   : ₹ %,.2f%n", account.getBalance());
        System.out.println(THIN_DIV);
    }

    // ── Receipts ──────────────────────────────────────────────────────────────

    public static void showDepositReceipt(double amount, double newBalance) {
        System.out.println("\n" + THIN_DIV);
        System.out.println("  ✔  DEPOSIT SUCCESSFUL");
        System.out.printf("  Amount Deposited : ₹ %,.2f%n", amount);
        System.out.printf("  New Balance      : ₹ %,.2f%n", newBalance);
        System.out.println(THIN_DIV);
    }

    public static void showWithdrawReceipt(double amount, double newBalance) {
        System.out.println("\n" + THIN_DIV);
        System.out.println("  ✔  WITHDRAWAL SUCCESSFUL");
        System.out.printf("  Amount Withdrawn : ₹ %,.2f%n", amount);
        System.out.printf("  New Balance      : ₹ %,.2f%n", newBalance);
        System.out.println(THIN_DIV);
        System.out.println("  Please collect your cash.");
        System.out.println(THIN_DIV);
    }

    public static void showTransferReceipt(double amount, String toAccount, double newBalance) {
        System.out.println("\n" + THIN_DIV);
        System.out.println("  ✔  TRANSFER SUCCESSFUL");
        System.out.printf("  Amount Transferred : ₹ %,.2f%n", amount);
        System.out.printf("  To Account         : %s%n",       toAccount);
        System.out.printf("  New Balance        : ₹ %,.2f%n",  newBalance);
        System.out.println(THIN_DIV);
    }

    public static void showPinChangeSuccess() {
        System.out.println("\n  ✔  PIN changed successfully.");
    }

    // ── Transaction History ───────────────────────────────────────────────────

    public static void showHistory(Account account, List<Transaction> transactions) {
        System.out.println("\n" + DIVIDER);
        System.out.printf("  TRANSACTION HISTORY — Account %s%n", account.getAccountNumber());
        System.out.println(DIVIDER);
        if (transactions.isEmpty()) {
            System.out.println("  No transactions recorded this session.");
        } else {
            System.out.printf("  %-12s   %-11s   %-14s   %s%n",
                    "Type", "Amount", "Balance After", "Date & Time");
            System.out.println(THIN_DIV);
            for (Transaction t : transactions) {
                System.out.println("  " + t);
            }
        }
        System.out.println(DIVIDER);
    }

    // ── Errors & Prompts ──────────────────────────────────────────────────────

    public static void showError(String message) {
        System.out.println("\n  ✘  ERROR: " + message);
    }

    public static void showInfo(String message) {
        System.out.println("  ℹ  " + message);
    }

    public static void showPrompt(String label) {
        System.out.printf("  %s: ", label);
    }

    public static void showPinAttempt(int remaining) {
        System.out.printf("  Incorrect PIN. %d attempt(s) remaining.%n", remaining);
    }

    public static void showInvalidOption() {
        System.out.println("  ✘  Invalid option. Please try again.");
    }

    public static void blank() {
        System.out.println();
    }
}

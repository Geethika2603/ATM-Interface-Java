
import java.util.Scanner;
public class InputHandler {
    private static final Scanner SCANNER = new Scanner(System.in);

    private InputHandler() { /* utility class */ }
    public static int readMenuChoice() {
        String line = SCANNER.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    public static String readLine(String prompt) {
        Display.showPrompt(prompt);
        return SCANNER.nextLine().trim();
    }
    public static String readAccountNumber() {
        Display.showPrompt("Account Number");
        String input = SCANNER.nextLine().trim();
        return input;
    }
    public static String readPin(String label) {
    Display.showPrompt(label);
    return SCANNER.nextLine().trim();
}

    public static double readAmount(String action) {
        while (true) {
            Display.showPrompt("Enter amount to " + action + " (or 'cancel')");
            String line = SCANNER.nextLine().trim();
            if (line.equalsIgnoreCase("cancel")) return -1;
            try {
                double amount = Double.parseDouble(line);
                if (amount <= 0) {
                    Display.showError("Amount must be greater than zero.");
                    continue;
                }
                if (amount > 10_000_000) {
                    Display.showError("Amount exceeds maximum allowed per transaction.");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                Display.showError("Invalid amount. Enter a number (e.g. 500 or 1500.50).");
            }
        }
    }

    public static void close() {
        SCANNER.close();
    }
}

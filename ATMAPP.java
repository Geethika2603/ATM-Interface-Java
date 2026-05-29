
/**
 * ATMAPP — Application entry point.
 *
 * Responsibility: Wire dependencies together and start the session.
 * Contains ZERO business logic — that is ATM's job.
 *
 * Dependency order:
 *   Bank is created first (loads data file).
 *   ATM receives Bank via constructor (dependency injection).
 *   ATM.startSession() drives everything from here.
 */
public class ATMAPP {

    public static void main(String[] args) {
        // Resolve data file path relative to working directory
        String dataFile = "data/accounts.dat";

        Bank bank = new Bank(dataFile);
        ATM  atm  = new ATM(bank);

        // Graceful shutdown hook — save accounts if JVM is killed (Ctrl+C)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bank.saveAccounts();
            System.out.println("\n[ATM] Accounts saved. Machine shutdown complete.");
        }));

        atm.startSession();
        InputHandler.close();
    }
}
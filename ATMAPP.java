public class ATMAPP {

    public static void main(String[] args) {
        // Resolve data file path relative to working directory
        String dataFile = "data/accounts.dat";
        Bank bank = new Bank(dataFile);
        ATM  atm  = new ATM(bank);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bank.saveAccounts();
            System.out.println("\n[ATM] Accounts saved. Machine shutdown complete.");
        }));
        atm.startSession();
        InputHandler.close();
    }
}

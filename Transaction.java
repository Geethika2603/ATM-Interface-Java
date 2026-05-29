
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TRANSACTION — Immutable value object.
 *
 * OOP concepts: Immutability (all fields final), encapsulation (private fields).
 * Responsibility: Represent one completed financial event. Never changes after creation.
 */
public final class Transaction {

    public enum Type {
        DEPOSIT("Deposit"),
        WITHDRAWAL("Withdrawal"),
        TRANSFER_OUT("Transfer Out"),
        TRANSFER_IN("Transfer In");

        private final String label;
        Type(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

    private final Type type;
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime timestamp;
    private final String note;          // e.g. "To: 100002" for transfers

    public Transaction(Type type, double amount, double balanceAfter, String note) {
        this.type         = type;
        this.amount       = amount;
        this.balanceAfter = balanceAfter;
        this.note         = note;
        this.timestamp    = LocalDateTime.now();
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public Type   getType()         { return type; }
    public double getAmount()       { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getNote()         { return note; }
    public String getTimestamp()    { return timestamp.format(FORMATTER); }

    @Override
    public String toString() {
        String sign = (type == Type.DEPOSIT || type == Type.TRANSFER_IN) ? "+" : "-";
        return String.format("%-12s | %s%-10.2f | Bal: %10.2f | %s %s",
                type.getLabel(), sign, amount, balanceAfter,
                timestamp.format(FORMATTER), note.isEmpty() ? "" : "| " + note);
    }
}
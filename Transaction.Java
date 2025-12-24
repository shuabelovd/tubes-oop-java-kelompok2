import java.time.LocalDate;
import java.io.Serializable;

class Transaction implements Serializable {
    private LocalDate date;
    private TransactionCategory category;
    private String Keterangan;
    private double amount;
    private String description;
    public Transaction(
            LocalDate date,
            TransactionCategory category,
            String Keterangan,
            double amount,
            String description
    ) throws InvalidTransactionException {
        if (date == null) {
            throw new InvalidTransactionException("Tanggal tidak boleh kosong!");
        }
        if (amount < 0) {
            throw new InvalidTransactionException("Nominal tidak boleh negatif!");
        }
        if (Keterangan == null || Keterangan.trim().isEmpty()) {
            throw new InvalidTransactionException("Keterangan tidak boleh kosong!");
        }
        this.date = date;
        this.category = category;
        this.Keterangan = Keterangan;
        this.amount = amount;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }
    public TransactionCategory getCategory() {
        return category;
    }
    public String Keterangan() {
        return Keterangan;
    }
    public double getAmount() {
        return amount;
    }
    public String getDescription() {
        return description;
    }
}

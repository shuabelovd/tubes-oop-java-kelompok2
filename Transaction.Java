import java.time.LocalDate;
import java.io.Serializable;

class Transaction implements Serializable {
    private LocalDate date;
    private TransactionCategory category;
    private String description;
    private double amount;
    public Transaction(
            LocalDate date,
            TransactionCategory category,
            String description,
            double amount
    ) throws InvalidTransactionException {
        if (date == null) {
            throw new InvalidTransactionException("Silahkan pilih tanggal");
        }
        if (amount < 0) {
            throw new InvalidTransactionException("Nominal tidak boleh kosong");
        }
        if (Keterangan == null || Keterangan.trim().isEmpty()) {
            throw new InvalidTransactionException("Keterangan tidak boleh kosong!");
        }
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }
    public TransactionCategory getCategory() {
        return category;
    }
    public String description() {
        return description;
    }
    public double getAmount() {
        return amount;
    }
    }
}

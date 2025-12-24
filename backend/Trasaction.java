package backend;
import java.time.LocalDate;

class Transaction implements  {
    private LocalDate date;
    private TransactionCategory category;
    private String description;
    private double amount;
   

    public Transaction(LocalDate date, TransactionCategory category, String description, 
                      double amount) 
                      throws InvalidTransactionException {
        if (amount < 0) {
            throw new InvalidTransactionException("Amount cannot be negative!");
        }
        if (date == null) {
            throw new InvalidTransactionException("Date cannot be null!");
        }
        
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
       
    }

    public LocalDate getDate() { return date; }
    public TransactionCategory getCategory() { return category; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    }
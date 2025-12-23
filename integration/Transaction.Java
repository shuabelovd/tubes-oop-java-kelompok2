package tubes1;
import java.time.LocalDate;
import java.io.Serializable;

class Transaction implements Serializable {
    private LocalDate date;
    private TransactionCategory category;
    private String subCategory;
    private double amount;
    private String description;
   

    public Transaction(LocalDate date, TransactionCategory category, String subCategory, 
                      double amount, String description) 
                      throws InvalidTransactionException {
        if (amount < 0) {
            throw new InvalidTransactionException("Amount cannot be negative!");
        }
        if (date == null) {
            throw new InvalidTransactionException("Date cannot be null!");
        }
        
        this.date = date;
        this.category = category;
        this.subCategory = subCategory;
        this.amount = amount;
        this.description = description;
       
    }

    public LocalDate getDate() { return date; }
    public TransactionCategory getCategory() { return category; }
    public String getSubCategory() { return subCategory; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    }
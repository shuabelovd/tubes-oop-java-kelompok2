package backend;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class TransactionManager {
    private List<Transaction> transactions;
    private static final String DATA_FILE = "transactions.dat";
    
    public TransactionManager() {
        this.transactions = new ArrayList<>();
    }
      
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        saveData();
    }
    
    public void removeTransaction(int index){
        if (index >= 0 && index < transactions.size()){
            transactions.remove(index);
            saveData();
        }
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> getTransactionsByMonth(int year, int month) {
        return transactions.stream()
            .filter(t -> t.getDate().getYear() == year && t.getDate().getMonthValue() == month)
            .collect(Collectors.toList());
    }

    public double getTotalIncome(int year, int month) {
        return getTransactionsByMonth(year, month).stream()
            .filter(t -> t.getCategory() == TransactionCategory.PEMASUKAN)
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

    public double getTotalExpense(int year, int month) {
        return getTransactionsByMonth(year, month).stream()
            .filter(t -> t.getCategory() == TransactionCategory.PENGELUARAN)
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

    public double getTotalSavings(int year, int month) {
        return getTransactionsByMonth(year, month).stream()
            .filter(t -> t.getCategory() == TransactionCategory.SAVING)
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

    public Map<String, Double> getExpensesByCategory(int year, int month) {
        return getTransactionsByMonth(year, month).stream()
            .filter(t -> t.getCategory() == TransactionCategory.PENGELUARAN)
            .collect(Collectors.groupingBy(
                Transaction::getDescription,
                Collectors.summingDouble(Transaction::getAmount)
            ));
    }

    public Map<String, Double> getIncomeByCategory(int year, int month) {
        return getTransactionsByMonth(year, month).stream()
            .filter(t -> t.getCategory() == TransactionCategory.PEMASUKAN)
            .collect(Collectors.groupingBy(
                Transaction::getDescription,
                Collectors.summingDouble(Transaction::getAmount)
            ));
    }

    public Map<String, Double> getSavingsByCategory(int year, int month) {
        return getTransactionsByMonth(year, month).stream()
            .filter(t -> t.getCategory() == TransactionCategory.SAVING)
            .collect(Collectors.groupingBy(
                Transaction::getDescription,
                Collectors.summingDouble(Transaction::getAmount)
            ));
    }
    
    private void saveData(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))){
            oos.writeObject(transactions);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadData(){
        File file = new File(DATA_FILE);
        if (file.exists()){
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))){
                transactions = (List<Transaction>) ois.readObject();
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                transactions = new ArrayList<>();
            }
        }
    }
}
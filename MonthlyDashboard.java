
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MonthlyDashboard extends JPanel{
    private TransactionManager transactionManager;
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> yearCombo;
    private JLabel totalIncomeLabel;
    private JLabel totalExpenseLabel;
    private JLabel totalSavingsLabel;
    private JLabel remainingLabel;
    private PieChartPanel expenseChartPanel;
    private PieChartPanel incomeChartPanel;
    private PieChartPanel savingsChartPanel;

    public MonthlyDashboard(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Summary Cards (Row 0)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        mainPanel.add(createSummaryCard("Total Pemasukkan", "Rp0.00", new Color(76, 175, 80), true), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(createSummaryCard("Total Pengeluaran", "Rp0.00", new Color(244, 67, 54), false), gbc);
        
        gbc.gridx = 2;
        mainPanel.add(createSummaryCard("Total Tabungan", "Rp0.00", new Color(33, 150, 243), false), gbc);
        
        gbc.gridx = 3;
        mainPanel.add(createSummaryCard("Sisa", "Rp0.00", new Color(255, 152, 0), false), gbc);

        // Charts (Row 1)
        gbc.gridy = 1;
        gbc.gridx = 0; gbc.gridwidth = 2;
        expenseChartPanel = new PieChartPanel("Pengeluaran");
        mainPanel.add(createChartCard(expenseChartPanel, new Color(244, 67, 54)), gbc);

        gbc.gridx = 2; gbc.gridwidth = 2;
        incomeChartPanel = new PieChartPanel("Pemasukkan");
        mainPanel.add(createChartCard(incomeChartPanel, new Color(76, 175, 80)), gbc);

        // Savings Chart (Row 2)
        gbc.gridy = 2;
        gbc.gridx = 0; gbc.gridwidth = 4;
        savingsChartPanel = new PieChartPanel("Tabungan");
        mainPanel.add(createChartCard(savingsChartPanel, new Color(33, 150, 243)), gbc);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        
        refresh();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(103, 58, 183));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Dashboard Bulanan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.WEST);

        // Month and Year Selector
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        selectorPanel.setBackground(new Color(103, 58, 183));

        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        monthCombo.addActionListener(e -> refresh());
        
        Integer[] years = new Integer[20];
int currentYear = LocalDate.now().getYear();
for (int i = 0; i < 20; i++) {
    years[i] = currentYear - 10 + i;
}
        yearCombo = new JComboBox<>(years);
        yearCombo.setSelectedItem(currentYear);
        yearCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearCombo.addActionListener(e -> refresh());

       
       
        JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        rightPanel.add(new JLabel("📅"));
        rightPanel.add(monthCombo);
        rightPanel.add(yearCombo);
        
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createSummaryCard(String title, String value, Color color, boolean isIncome) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 3, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(color.darker());

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        if (isIncome) totalIncomeLabel = valueLabel;
        else if (title.contains("Pengeluaran")) totalExpenseLabel = valueLabel;
        else if (title.contains("Tabungan")) totalSavingsLabel = valueLabel;
        else if (title.contains("Sisa")) remainingLabel = valueLabel;

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createChartCard(PieChartPanel chartPanel, Color borderColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(borderColor, 2, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.add(chartPanel, BorderLayout.CENTER);
        return card;
    }

    public void refresh() {
        int month = monthCombo.getSelectedIndex() + 1;
        int year = (Integer) yearCombo.getSelectedItem();

        double income = transactionManager.getTotalIncome(year, month);
        double expense = transactionManager.getTotalExpense(year, month);
        double savings = transactionManager.getTotalSavings(year, month);
        double remaining = income - expense - savings;

        totalIncomeLabel.setText(String.format("Rp%.2f", income));
        totalExpenseLabel.setText(String.format("Rp%.2f", expense));
        totalSavingsLabel.setText(String.format("Rp%.2f", savings));
        remainingLabel.setText(String.format("Rp%.2f", remaining));

        expenseChartPanel.updateData(transactionManager.getExpensesByCategory(year, month));
        incomeChartPanel.updateData(transactionManager.getIncomeByCategory(year, month));
        savingsChartPanel.updateData(transactionManager.getSavingsByCategory(year, month));
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Dashboard sebagai CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                int month = monthCombo.getSelectedIndex() + 1;
                int year = (Integer) yearCombo.getSelectedItem();
                
                writer.println("Dashboard Bulanan - " + monthCombo.getSelectedItem() + " " + year);
                writer.println();
                writer.println("Ringkasan");
                writer.println("Total Pemasukan,Rp" + transactionManager.getTotalIncome(year, month));
                writer.println("Total Pengeluaran,Rp" + transactionManager.getTotalExpense(year, month));
                writer.println("Total Tabungan,Rp" + transactionManager.getTotalSavings(year, month));
                writer.println("Sisa,Rp" + (transactionManager.getTotalIncome(year, month) - 
                              transactionManager.getTotalExpense(year, month) - 
                              transactionManager.getTotalSavings(year, month)));
                writer.println();
                
                writer.println("Pengeluaran per Kategori");
                Map<String, Double> expenses = transactionManager.getExpensesByCategory(year, month);
                for (Map.Entry<String, Double> entry : expenses.entrySet()) {
                    writer.println(entry.getKey() + ",Rp" + entry.getValue());
                }
                writer.println();
                
                writer.println("Pemasukan per Kategori");
                Map<String, Double> income = transactionManager.getIncomeByCategory(year, month);
                for (Map.Entry<String, Double> entry : income.entrySet()) {
                    writer.println(entry.getKey() + ",Rp" + entry.getValue());
                }
                
                JOptionPane.showMessageDialog(this, "Dashboard berhasil diekspor!", 
                                            "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Gagal mengekspor data: " + e.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

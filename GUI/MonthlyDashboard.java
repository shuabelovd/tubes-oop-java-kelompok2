package GUI;
import GUI.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;

public class MonthlyDashboard extends JPanel {
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
        setBackground(new Color(255, 228, 239));

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(243, 158, 182));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Summary Cards
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        mainPanel.add(createSummaryCard("Total Pemasukan", "Rp0.00", new Color(76, 175, 80), true), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(createSummaryCard("Total Pengeluaran", "Rp0.00", new Color(244, 67, 54), false), gbc);
        
        gbc.gridx = 2;
        mainPanel.add(createSummaryCard("Total Tabungan", "Rp0.00", new Color(33, 150, 243), false), gbc);
        
        gbc.gridx = 3;
        mainPanel.add(createSummaryCard("Sisa", "Rp0.00", new Color(255, 152, 0), false), gbc);

        // Charts
        gbc.gridy = 1;
        gbc.gridx = 0; gbc.gridwidth = 2;
        expenseChartPanel = new PieChartPanel("Pengeluaran");
        mainPanel.add(createChartCard(expenseChartPanel, new Color(244, 67, 54)), gbc);

        gbc.gridx = 2; gbc.gridwidth = 2;
        incomeChartPanel = new PieChartPanel("Pemasukan");
        mainPanel.add(createChartCard(incomeChartPanel, new Color(76, 175, 80)), gbc);

        gbc.gridy = 2;
        gbc.gridx = 0; gbc.gridwidth = 4;
        savingsChartPanel = new PieChartPanel("Tabungan");
        mainPanel.add(createChartCard(savingsChartPanel, new Color(33, 150, 243)), gbc);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        
        refresh();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(253, 181, 206));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Rekap Bulanan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(19, 36, 64));
        panel.add(titleLabel, BorderLayout.WEST);

        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        selectorPanel.setBackground(new Color(253, 181, 206));

        String[] months = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", 
                          "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
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

        selectorPanel.add(new JLabel("📅"));
        selectorPanel.add(monthCombo);
        selectorPanel.add(yearCombo);
        
        panel.add(selectorPanel, BorderLayout.EAST);

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
}

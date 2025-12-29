package GUI;

import GUI.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionView extends JPanel {
    private TransactionManager transactionManager;
    private MainFrame mainFrame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> yearCombo;
    private JCheckBox filterCheckBox;

    public TransactionView(TransactionManager transactionManager, MainFrame mainFrame) {
        this.transactionManager = transactionManager;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(184, 219, 128));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel headerLabel = new JLabel("Daftar Transaksi");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.BLACK);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(new Color(184, 219, 128));

        // Checkbox Filter
        filterCheckBox = new JCheckBox("Filter per Bulan");
        filterCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterCheckBox.setForeground(Color.BLACK);
        filterCheckBox.setBackground(new Color(184, 219, 128));
        filterCheckBox.addActionListener(e -> {
            monthCombo.setEnabled(filterCheckBox.isSelected());
            yearCombo.setEnabled(filterCheckBox.isSelected());
            refresh();
        });

        // Month ComboBox
        String[] months = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", 
                          "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        monthCombo.setEnabled(false);
        monthCombo.addActionListener(e -> {
            if (filterCheckBox.isSelected()) {
                refresh();
            }
        });

        // Year ComboBox
        Integer[] years = new Integer[20];
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 20; i++) {
            years[i] = currentYear - 10 + i;
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setSelectedItem(currentYear);
        yearCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearCombo.setEnabled(false);
        yearCombo.addActionListener(e -> {
            if (filterCheckBox.isSelected()) {
                refresh();
            }
        });

        // Edit Button
        JButton editButton = new JButton("Edit");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editButton.setBackground(new Color(33, 150, 243));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setBorderPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.addActionListener(e -> editSelectedTransaction());

        // Delete Button
        JButton deleteButton = new JButton("Hapus");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> deleteSelectedTransaction());

        // Add components to filter panel
        filterPanel.add(filterCheckBox);
        filterPanel.add(monthCombo);
        filterPanel.add(yearCombo);
        filterPanel.add(editButton);
        filterPanel.add(deleteButton);

        headerPanel.add(filterPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"Tanggal", "Kategori", "Keterangan", "Jumlah"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(184, 219, 128));
        table.getTableHeader().setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(184, 219, 128));
        table.setSelectionForeground(Color.BLACK);

        // Column Width
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Custom Cell Renderer for Amount Column
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                                                                 isSelected, hasFocus, row, column);
                String type = (String) table.getValueAt(row, 1);
                if (!isSelected) {
                    if (type.equals("Pemasukan")) {
                        c.setForeground(new Color(76, 175, 80));
                    } else if (type.equals("Pengeluaran")) {
                        c.setForeground(new Color(244, 67, 54));
                    } else {
                        c.setForeground(new Color(33, 150, 243));
                    }
                } else {
                    c.setForeground(Color.BLACK);
                }
                ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<Transaction> transactions;
        
        if (filterCheckBox.isSelected()) {
            int month = monthCombo.getSelectedIndex() + 1;
            int year = (Integer) yearCombo.getSelectedItem();
            transactions = transactionManager.getTransactionsByMonth(year, month);
        } else {
            transactions = transactionManager.getAllTransactions();
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Transaction t : transactions) {
            tableModel.addRow(new Object[]{
                t.getDate().format(formatter),
                t.getCategory().getDisplayName(),
                t.getDescription(),
                String.format("Rp%.2f", t.getAmount())
            });
        }
    }

    private void editSelectedTransaction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Silakan pilih transaksi yang ingin diedit!", 
                "Tidak Ada Pilihan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Transaction> allTransactions = transactionManager.getAllTransactions();
        Transaction toEdit;
        int actualIndex;
        
        if (filterCheckBox.isSelected()) {
            int month = monthCombo.getSelectedIndex() + 1;
            int year = (Integer) yearCombo.getSelectedItem();
            List<Transaction> filteredTransactions = transactionManager.getTransactionsByMonth(year, month);
            
            toEdit = filteredTransactions.get(selectedRow);
            actualIndex = allTransactions.indexOf(toEdit);
        } else {
            toEdit = allTransactions.get(selectedRow);
            actualIndex = selectedRow;
        }
        
        mainFrame.switchToInputPanel(toEdit, actualIndex);
    }

    private void deleteSelectedTransaction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Silakan pilih transaksi yang ingin dihapus!", 
                "Tidak Ada Pilihan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus transaksi ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            List<Transaction> allTransactions = transactionManager.getAllTransactions();
            
            if (filterCheckBox.isSelected()) {
                int month = monthCombo.getSelectedIndex() + 1;
                int year = (Integer) yearCombo.getSelectedItem();
                List<Transaction> filteredTransactions = transactionManager.getTransactionsByMonth(year, month);
                
                Transaction toDelete = filteredTransactions.get(selectedRow);
                int indexInAll = allTransactions.indexOf(toDelete);
                transactionManager.removeTransaction(indexInAll);
            } else {
                transactionManager.removeTransaction(selectedRow);
            }
            
            refresh();
            mainFrame.refreshAllPanels();
            
            JOptionPane.showMessageDialog(this, 
                "Transaksi berhasil dihapus!", 
                "Berhasil", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

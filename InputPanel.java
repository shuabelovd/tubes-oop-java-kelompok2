import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Calendar;
import com.toedter.calendar.JDateChooser;

 public class InputPanel extends JFrame{
     private JDateChooser dateChooser;
    private JComboBox<TransactionCategory> categoryCombo;
    private JTextField descriptionField;
    private JTextField amountField;
    private TransactionManager transactionManager;
    private MainFrame mainFrame;
    

    public InputPanel(TransactionManager transactionManager, MainFrame mainFrame) {
        this.transactionManager = transactionManager;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(255, 248, 240));

        // Header with teddy bear
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 235, 205));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("Input Data Transaksi");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(139, 69, 19));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center panel with form
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(new Color(255, 248, 240));
        centerPanel.setBorder(new EmptyBorder(40, 100, 40, 100));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 182, 193), 3, true),
            new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Tanggal
        // Tanggal
        addFormField(formPanel, gbc, 0, " Tanggal:", new Color(255, 160, 122));
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        dateChooser.setPreferredSize(new Dimension(300, 35));
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(dateChooser, gbc);
    

        // Kategori
        addFormField(formPanel, gbc, 1, "Kategori:", new Color(255, 182, 193));
        categoryCombo = new JComboBox<>(TransactionCategory.values());
        categoryCombo.setPreferredSize(new Dimension(300, 35));
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(categoryCombo, gbc);

        // Keterangan
addFormField(formPanel, gbc, 2, "Keterangan:", new Color(255, 218, 185));
this.descriptionField = createStyledTextField();
gbc.gridx = 1;
gbc.gridy = 2;
formPanel.add(this.descriptionField, gbc);

// Nominal
addFormField(formPanel, gbc, 3, "Nominal:", new Color(255, 228, 196));
this.amountField = createStyledTextField();
gbc.gridx = 1;
gbc.gridy = 3;
formPanel.add(this.amountField, gbc);
        

        // Submit Button
        JButton submitButton = new JButton("Input");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitButton.setPreferredSize(new Dimension(200, 45));
        submitButton.setBackground(new Color(139, 69, 19));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> handleSubmit());

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(submitButton, gbc);

        centerPanel.add(formPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, Color color) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel.setForeground(color.darker());
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(jLabel, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void handleSubmit() {
        try {
            Date selectedDate = dateChooser.getDate();
            if (selectedDate == null) {
                throw new InvalidTransactionException("Silahkan pilih tanggal!");
            }

        LocalDate date = selectedDate.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();

            TransactionCategory category = (TransactionCategory) categoryCombo.getSelectedItem();
            String descriptionText = descriptionField.getText().trim();
            String amountText = amountField.getText().trim();
           

            if (descriptionText.isEmpty()) {
                throw new InvalidTransactionException("Keterangan tidak boleh kosong!");
            }
            if (amountText.isEmpty()) {
                throw new InvalidTransactionException("Nominal tidak boleh kosong!");
            }

            double amount = Double.parseDouble(amountText);
            
            Transaction transaction = new Transaction(date, category, descriptionText, amount);
            transactionManager.addTransaction(transaction);

            JOptionPane.showMessageDialog(this, 
                "✅ Transaction added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            mainFrame.refreshAllPanels();

        } catch (InvalidTransactionException ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error: " + ex.getMessage(), 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error: Amount must be a valid number!", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
    dateChooser.setDate(new Date());
    categoryCombo.setSelectedIndex(0);
    descriptionField.setText("");
    amountField.setText("");

}

}

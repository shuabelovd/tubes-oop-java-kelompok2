/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tubes1;
import javax.swing.*;
import java.awt.*;


class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private InputPanel inputPanel;
    private MonthlyDashboard monthlyDashboard;
    private TransactionView transactionView;
    private TransactionManager transactionManager;

    public MainFrame() {
        transactionManager = new TransactionManager();
        
        setTitle("Kalkulator Keuangan Pribadi");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabbed pane with colorful design
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(255, 248, 240));

        // Create panels
        inputPanel = new InputPanel(transactionManager, this);
        monthlyDashboard = new MonthlyDashboard(transactionManager);
        transactionView = new TransactionView(transactionManager, this);

        // Add tabs with icons
        tabbedPane.addTab("Input Data", inputPanel);
        tabbedPane.addTab("Dashboard Bulanan", monthlyDashboard);
        tabbedPane.addTab("Tampilan Transaksi", transactionView);

        add(tabbedPane);
        setVisible(true);
    }

    public void refreshAllPanels() {
        monthlyDashboard.refresh();
        transactionView.refresh();
    }

    public void switchToTransactionView() {
        tabbedPane.setSelectedIndex(2);
    }
    
    public void switchToInputPanel(Transaction transaction, int index) {
        inputPanel.setEditMode(transaction, index);
        tabbedPane.setSelectedIndex(0);
    }
}

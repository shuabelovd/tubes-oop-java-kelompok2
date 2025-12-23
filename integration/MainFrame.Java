package tubes1;

import javax.swing.*;
import java.awt.*;

class MainFrame {
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

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(255, 248, 240));

        inputPanel = new InputPanel(transactionManager, this);
        monthlyDashboard = new MonthlyDashboard(transactionManager);
        transactionView = new TransactionView(transactionManager, this);

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
}

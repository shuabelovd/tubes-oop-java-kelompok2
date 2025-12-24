package GUI;
import java.awt.*;
import javax.swing.JPanel;
import java.util.Map;

public class PieChart extends JPanel{
     private String title;
    private Map<String, Double> data;

    private Color[] colors = {
        new Color(244, 67, 54),
        new Color(233, 30, 99),
        new Color(156, 39, 176),
        new Color(103, 58, 183),
        new Color(63, 81, 181),
        new Color(33, 150, 243),
        new Color(3, 169, 244),
        new Color(0, 188, 212)
    };

    public PieChartPanel(String title) {
        this.title = title;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 300));
    }

    public void updateData(Map<String, Double> data) {
        this.data = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        // ===== TITLE =====
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.setColor(Color.BLACK);
        g2d.drawString(title, 10, 25);

        if (data == null || data.isEmpty()) {
            drawNoData(g2d);
            return;
        }

        double total = data.values().stream().mapToDouble(Double::doubleValue).sum();
        if (total == 0) {
            drawNoData(g2d);
            return;
        }

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2 + 10;
        int radius = Math.min(getWidth(), getHeight()) / 3;

        int startAngle = 0;
        int colorIndex = 0;

        // ===== DRAW PIE + LABEL =====
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            double value = entry.getValue();
            int arcAngle = (int) Math.round((value / total) * 360);

            g2d.setColor(colors[colorIndex % colors.length]);
            g2d.fillArc(centerX - radius, centerY - radius,
                        radius * 2, radius * 2,
                        startAngle, arcAngle);

            // ===== LABEL DI DALAM PIE =====
            double midAngle = Math.toRadians(startAngle + arcAngle / 2.0);
            int labelX = (int) (centerX + Math.cos(midAngle) * radius * 0.6);
            int labelY = (int) (centerY - Math.sin(midAngle) * radius * 0.6);

            int percent = (int) Math.round((value / total) * 100);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2d.drawString(entry.getKey() + " " + percent + "%",
                           labelX - 15, labelY);

            startAngle += arcAngle;
            colorIndex++;
        }

        // ===== LEGEND =====
        int legendY = centerY + radius + 25;
        colorIndex = 0;

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            g2d.setColor(colors[colorIndex % colors.length]);
            g2d.fillRect(20, legendY, 15, 15);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2d.drawString(
                entry.getKey() + ": Rp" + String.format("%,.2f", entry.getValue()),
                40, legendY + 12
            );

            legendY += 20;
            colorIndex++;
        }
    }

    private void drawNoData(Graphics2D g2d) {
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2d.setColor(Color.GRAY);
        g2d.drawString("Tidak ada data",
                       getWidth() / 2 - 50,
                       getHeight() / 2);
    }
}

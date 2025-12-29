package GUI;
import java.awt.*;
import java.util.Map;
import javax.swing.JPanel;

public class PieChart extends JPanel {
    private String title;
    private Map<String, Double> data;

    private Color[] colors = {
        new Color(244, 67, 54), new Color(233, 30, 99),
        new Color(156, 39, 176), new Color(103, 58, 183),
        new Color(63, 81, 181), new Color(33, 150, 243),
        new Color(3, 169, 244), new Color(0, 188, 212),
        new Color(0, 150, 136), new Color(76, 175, 80),
        new Color(139, 195, 74), new Color(205, 220, 57),
        new Color(255, 235, 59), new Color(255, 193, 7),
        new Color(255, 152, 0), new Color(255, 87, 34)
    };

    public PieChart(String title) {
        this.title = title;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 400));
    }

    public void updateData(Map<String, Double> data) {
        this.data = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Title
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
        int centerY = (getHeight() / 2) - 20;
        int radius = Math.min(getWidth(), getHeight()) / 4;

        int startAngle = 0;
        int colorIndex = 0;

        // Draw Pie
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            double value = entry.getValue();
            int arcAngle = (int) Math.round((value / total) * 360);

            g2d.setColor(colors[colorIndex % colors.length]);
            g2d.fillArc(centerX - radius, centerY - radius,
                        radius * 2, radius * 2,
                        startAngle, arcAngle);

            // Label inside pie
            double midAngle = Math.toRadians(startAngle + arcAngle / 2.0);
            int labelX = (int) (centerX + Math.cos(midAngle) * radius * 0.6);
            int labelY = (int) (centerY - Math.sin(midAngle) * radius * 0.6);

            int percent = (int) Math.round((value / total) * 100);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
            String percentText = percent + "%";
            g2d.drawString(percentText, labelX - 12, labelY);

            startAngle += arcAngle;
            colorIndex++;
        }

        // Legend - diperbaiki agar semua masuk
        int legendY = centerY + radius + 30;
        int legendX = 20;
        int maxLegendHeight = getHeight() - legendY - 20;
        int itemsPerColumn = Math.max(1, maxLegendHeight / 20);
        
        colorIndex = 0;
        int itemCount = 0;
        
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            // Pindah ke kolom baru jika perlu
            if (itemCount > 0 && itemCount % itemsPerColumn == 0) {
                legendX += 250;
                legendY = centerY + radius + 30;
            }
            
            g2d.setColor(colors[colorIndex % colors.length]);
            g2d.fillRect(legendX, legendY, 15, 15);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            String legendText = entry.getKey() + ": Rp" + String.format("%,.2f", entry.getValue());
            
            // Potong text jika terlalu panjang
            if (legendText.length() > 30) {
                legendText = legendText.substring(0, 27) + "...";
            }
            
            g2d.drawString(legendText, legendX + 20, legendY + 12);

            legendY += 20;
            colorIndex++;
            itemCount++;
        }
    }

    private void drawNoData(Graphics2D g2d) {
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2d.setColor(Color.GRAY);
        g2d.drawString("Tidak ada data", getWidth() / 2 - 50, getHeight() / 2);
    }
}
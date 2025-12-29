package OOPRevisii;

import java.awt.Color;

public enum TransactionCategory {
    PEMASUKAN("Pemasukan", new Color(76, 175, 80)),
    PENGELUARAN("Pengeluaran", new Color(244, 67, 54)),
    SAVING("Tabungan", new Color(33, 150, 243));

    private final String displayName;
    private final Color color;

    TransactionCategory(String displayName, Color color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() { return displayName; }
    public Color getColor() { return color; }

    @Override
    public String toString() { return displayName; }
}
package org.perfcake.pc4idea.api.editor.swing;

import org.perfcake.pc4idea.impl.editor.editor.tablemodel.PropertiesTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by Stanislav Kaleta on 5/14/15.
 */
public class EditorTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (!(table.getModel() instanceof PropertiesTableModel)) {
            return c;
        }
        PropertiesTableModel model = (PropertiesTableModel) table.getModel();
        if (!model.hasDefinedProperties()) {
            return c;
        }
        float bgBrightness = 1f;
        float fgBrightness = 0.5f;
        float bgSaturation = 0.05f;
        float fgSaturation = 0.9f;

        boolean isHighlighted = model.isUserProperty(row);
        String info = model.getPropertyInfo(row);

        if (isHighlighted) {
            Font f = c.getFont();
            c.setFont(new Font(f.getName(), Font.BOLD, f.getSize()));
            bgSaturation = 0.4f;
        }

        switch (info){
            case "ok":
                c.setBackground(Color.getHSBColor(140/ 360f, bgSaturation, bgBrightness));
                c.setForeground(Color.getHSBColor(140 / 360f, fgSaturation, fgBrightness));
                break;
            case "wrong":
            case "redundant":
                c.setBackground(Color.getHSBColor(0 / 360f, bgSaturation, bgBrightness));
                c.setForeground(Color.getHSBColor(0 / 360f, fgSaturation, fgBrightness));
                break;
            case "missing":
                c.setBackground(Color.getHSBColor(240 / 360f, bgSaturation, bgBrightness));
                c.setForeground(Color.getHSBColor(240 / 360f, fgSaturation, fgBrightness));
                break;
            case "default":
                c.setBackground(Color.getHSBColor(0 / 360f, 0f, bgBrightness));
                c.setForeground(Color.getHSBColor(0 / 360f, 0f, fgBrightness));
                break;
        }

        return c;
    }
}

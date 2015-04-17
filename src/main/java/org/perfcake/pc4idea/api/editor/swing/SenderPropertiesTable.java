package org.perfcake.pc4idea.api.editor.swing;

import com.intellij.ui.table.JBTable;
import org.perfcake.model.Property;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.PropertiesTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/15/15.
 */
public class SenderPropertiesTable extends JBTable {
    private List<Property> modelPropertyList = new ArrayList<>();
    private List<Property> structurePropertyList = new ArrayList<>();

    public SenderPropertiesTable() {
        this.setModel(new PropertiesTableModel());
        this.getTableHeader().setReorderingAllowed(false);


        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                PropertiesTableModel model = (PropertiesTableModel) table.getModel();
                String hint = (String) model.getValueAt(row, 2);
                float bgBrightness = 1f;
                float fgBrightness = 0.5f;
                float bgSaturation = 0.1f;
                float fgSaturation = 0.9f;
                switch (hint) {
                    case "ok":
                        c.setBackground(Color.getHSBColor(140 / 360f, bgSaturation, bgBrightness));
                        c.setForeground(Color.getHSBColor(140 / 360f, fgSaturation, fgBrightness));
                        break;
                    case "wrong":
                        c.setBackground(Color.getHSBColor(0 / 360f, bgSaturation, bgBrightness));
                        c.setForeground(Color.getHSBColor(0 / 360f, fgSaturation, fgBrightness));
                        break;
                    case "default":
                        c.setBackground(Color.getHSBColor(0 / 360f, 0f, bgBrightness));
                        c.setForeground(Color.getHSBColor(0 / 360f, 0f, fgBrightness));
                        break;
                    case "missing":
                        c.setBackground(Color.getHSBColor(240 / 360f, bgSaturation, bgBrightness));
                        c.setForeground(Color.getHSBColor(240 / 360f, fgSaturation, fgBrightness));
                        break;
                }

                return c;
            }
        };

        this.getColumnModel().getColumn(0).setCellRenderer(defaultRenderer);
        this.getColumnModel().getColumn(1).setCellRenderer(defaultRenderer);
        this.getColumnModel().getColumn(2).setCellRenderer(defaultRenderer);

        ((DefaultTableCellRenderer) this.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = SenderPropertiesTable.this.getSelectedRow();
                if (selectedRow >= 0) {
                    /*TODO edit*/
                }
            }
        });
    }


    public void setModelProperties(List<Property> modelPropertyList) {
        this.modelPropertyList = modelPropertyList;
        update();
    }

    public void setStructureProperties(List<Property> structurePropertyList) {
        this.structurePropertyList = structurePropertyList;
        update();
    }

    public List<Property> getProperties() {
        return modelPropertyList;
    }

    private void update() {
        List<Property> tableProperties = new ArrayList<>();
        List<String> tableHints = new ArrayList<>();

        for (Property modelP : modelPropertyList) {
            boolean isValidProperty = false;
            for (Property structureP : structurePropertyList) {
                if (modelP.getName().equals(structureP.getName())) {
                    isValidProperty = true;
                }
            }
            tableProperties.add(modelP);
            if (isValidProperty) {
                tableHints.add("ok");
            } else {
                tableHints.add("wrong");
            }
        }

        for (Property structureP : structurePropertyList) {
            boolean isSet = false;
            for (Property modelP : modelPropertyList) {
                if (structureP.getName().equals(modelP.getName())) {
                    isSet = true;
                }
            }
            if (!isSet) {
                tableProperties.add(structureP);
                if (structureP.getValue() != null && !structureP.getValue().equals("")) {
                    tableHints.add("default");
                } else {
                    tableHints.add("missing");
                }
            }
        }

        ((PropertiesTableModel) this.getModel()).updateModel(tableHints, tableProperties);
        this.revalidate();
        this.repaint();
    }
}

package org.perfcake.pc4idea.api.editor.swing;

import com.intellij.ui.table.JBTable;
import org.perfcake.model.Property;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.StructurePropertiesTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/15/15.
 */
public class StructurePropertiesTable extends JBTable {
    private List<Property> modelPropertyList = new ArrayList<>();
    private List<Property> structurePropertyList = new ArrayList<>();

    public StructurePropertiesTable() {
        this.setModel(new StructurePropertiesTableModel(new ArrayList<>(), new ArrayList<>()));
        this.getTableHeader().setReorderingAllowed(false);


        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                StructurePropertiesTableModel model = (StructurePropertiesTableModel) table.getModel();
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
    }


    public void setModelProperties(List<Property> modelPropertyList) {
        this.modelPropertyList = modelPropertyList;
        update();
    }

    public void setStructureProperties(List<Property> structurePropertyList) {
        this.structurePropertyList = structurePropertyList;
        update();
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

        ((StructurePropertiesTableModel) this.getModel()).getHints().addAll(tableHints);
        ((StructurePropertiesTableModel) this.getModel()).getProperties().addAll(tableProperties);
        this.revalidate();
        this.repaint();
    }
}

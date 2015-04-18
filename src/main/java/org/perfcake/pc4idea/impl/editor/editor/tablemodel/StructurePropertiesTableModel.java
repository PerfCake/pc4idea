package org.perfcake.pc4idea.impl.editor.editor.tablemodel;

import org.perfcake.model.Property;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/15/15.
 */
public class StructurePropertiesTableModel extends AbstractTableModel {
    private List<String> hints = new ArrayList<>();
    private List<Property> propertyList = new ArrayList<>();

    public StructurePropertiesTableModel(List<String> hints, List<Property> propertyList) {
        this.hints.addAll(hints);
        this.propertyList.addAll(propertyList);
    }

    public List<Property> getProperties() {
        return propertyList;
    }

    public List<String> getHints() {
        return hints;
    }

    @Override
    public int getRowCount() {
        return propertyList.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Property property = propertyList.get(rowIndex);
        String hint = hints.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return property.getName();
            case 1:
                return property.getValue();
            case 2:
                return hint;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Property Name";
            case 1:
                return "Property Value";
            case 2:
                return "Hint";
            default:
                return "";
        }
    }
}

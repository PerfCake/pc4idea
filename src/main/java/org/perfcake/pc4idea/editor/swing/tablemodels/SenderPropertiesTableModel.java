package org.perfcake.pc4idea.editor.swing.tablemodels;

import org.perfcake.model.Property;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/15/15.
 */
public class SenderPropertiesTableModel extends AbstractTableModel {
    private List<String> hints = new ArrayList<>();
    private List<Property> propertyList = new ArrayList<>();

    public void updateModel(List<String> hints, List<Property> propertyList) {
        if (hints.size() == propertyList.size()) {
            this.hints = hints;
            this.propertyList = propertyList;
        }
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

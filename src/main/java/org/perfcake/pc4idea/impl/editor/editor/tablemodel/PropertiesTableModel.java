package org.perfcake.pc4idea.impl.editor.editor.tablemodel;

import org.perfcake.model.Property;
import org.perfcake.pc4idea.api.editor.editor.tablemodel.EditorTableModel;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.editor.component.PropertyEditor;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/17/15.
 */
public class PropertiesTableModel extends AbstractTableModel implements EditorTableModel {
    private List<Property> propertyList = new ArrayList<>();
    private List<String> structurePropertyNames = null;

    public PropertiesTableModel(List<Property> properties) {
        propertyList.addAll(properties);
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setStructureProperties(List<Property> structureProperties) {
        structurePropertyNames = new ArrayList<>();
        for (Property p : structureProperties) {
            structurePropertyNames.add(p.getName());
        }
    }

    public Boolean isPropertyValid(int row) {
        if (structurePropertyNames == null) {
            return null;
        } else {
            boolean isValid = false;
            for (String structPropName : structurePropertyNames) {
                if (structPropName.equals(propertyList.get(row).getName())) {
                    isValid = true;
                }
            }
            return isValid;
        }
    }

    @Override
    public int getRowCount() {
        return propertyList.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Property property = propertyList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return property.getName();
            case 1:
                return property.getValue();
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
            default:
                return "";
        }
    }

    @Override
    public void reorderRows(int fromIndex, int toIndex) {
        Property property = propertyList.get(fromIndex);
        propertyList.remove(property);
        propertyList.add(toIndex, property);
    }

    @Override
    public void addRow() {
        PropertyEditor editor;
        if (structurePropertyNames != null) {
            String[] hints = new String[]{};
            hints = structurePropertyNames.toArray(hints);
            editor = new PropertyEditor(hints);
        } else {
            editor = new PropertyEditor();
        }
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Property property = editor.getProperty();
            propertyList.add(property);
            fireTableDataChanged();
        }
    }

    @Override
    public void editRow(int row) {
        PropertyEditor editor;
        if (structurePropertyNames != null) {
            String[] hints = new String[]{};
            hints = structurePropertyNames.toArray(hints);
            editor = new PropertyEditor(hints);
        } else {
            editor = new PropertyEditor();
        }
        editor.setProperty(propertyList.get(row));
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            propertyList.set(row, editor.getProperty());
            fireTableDataChanged();
        }
    }

    @Override
    public void deleteRow(int row) {
        propertyList.remove(row);
        fireTableDataChanged();
    }
}

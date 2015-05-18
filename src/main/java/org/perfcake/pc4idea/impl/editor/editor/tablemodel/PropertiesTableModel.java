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
    private List<Property> dataUserPropertyList = new ArrayList<>();
    private List<Property> dataDefinedPropertyList = new ArrayList<>();
    private List<RowProperty> visiblePropertyList = new ArrayList<>();

    public PropertiesTableModel() {

    }

    public void setUserProperties(List<Property> userProperties) {
        this.dataUserPropertyList = userProperties;
        updateVisibleProperties();
    }

    public void setDefinedProperties(List<Property> definedProperties) {
        this.dataDefinedPropertyList = definedProperties;
        updateVisibleProperties();
    }


    public Boolean isUserProperty(int row) {
        return visiblePropertyList.get(row).userProperty;
    }

    public String getPropertyInfo(int row){
        return visiblePropertyList.get(row).info;
    }

    public List<Property> getUserProperties(){
        return dataUserPropertyList;
    }

    public boolean hasDefinedProperties(){
        /*TODO or via constructor*/
        return !dataDefinedPropertyList.isEmpty();
    }

    private void updateVisibleProperties(){
        visiblePropertyList.clear();

        if (dataDefinedPropertyList.isEmpty()){
            for (Property userP : dataUserPropertyList){
                RowProperty rowP = new RowProperty();
                rowP.property = userP;
                rowP.userProperty = true;
                rowP.info = "-";
                visiblePropertyList.add(rowP);
            }
            return;
        }

        List<String> userPropertyNames = new ArrayList<>();

        for (Property userP : dataUserPropertyList){
            RowProperty rowP = new RowProperty();
            rowP.property = userP;
            rowP.userProperty = true;

            boolean isDefined = false;
            for (Property definedP : dataDefinedPropertyList){
                if (definedP.getName().equals(userP.getName())){
                    isDefined = true;
                }
            }
            if (isDefined) {
                boolean isRedundant = false;
                for (String name : userPropertyNames){
                    if (userP.getName().equals(name)){
                        isRedundant = true;
                    }
                }
                rowP.info = (isRedundant) ? "redundant" : "ok";
            } else {
                rowP.info = "wrong";
            }
            userPropertyNames.add(rowP.property.getName());
            visiblePropertyList.add(rowP);
        }

        for (Property definedP : dataDefinedPropertyList){
            RowProperty rowP = new RowProperty();
            rowP.property = definedP;
            rowP.userProperty = false;

            boolean isSet = false;
            for (Property userP : dataUserPropertyList) {
                if (definedP.getName().equals(userP.getName())) {
                    isSet = true;
                }
            }
            if (!isSet) {
                if (definedP.getValue() != null && !definedP.getValue().equals("")) {
                    rowP.info = "default";
                } else {
                    rowP.info = "missing";
                }
                visiblePropertyList.add(rowP);
            }
        }

        fireTableDataChanged();
    }

    private void updateDataProperties(){
        dataUserPropertyList.clear();

        for (RowProperty rowP : visiblePropertyList){
            if (rowP.userProperty){
                dataUserPropertyList.add(rowP.property);
            }
        }
    }

    @Override
    public int getRowCount() {
        return visiblePropertyList.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RowProperty rowProperty = visiblePropertyList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowProperty.property.getName();
            case 1:
                return rowProperty.property.getValue();
            case 2:
                return rowProperty.info;
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
                return "Value";
            case 2:
                return "Info";
            default:
                return "";
        }
    }

    @Override
    public void reorderRows(int fromIndex, int toIndex) {
        RowProperty property = visiblePropertyList.get(fromIndex);
        visiblePropertyList.remove(property);
        visiblePropertyList.add(toIndex, property);
        updateDataProperties();
    }

    @Override
    public void addRow() {
        PropertyEditor editor;
        String[] hints = new String[dataDefinedPropertyList.size()];
        for (int p=0;p<dataDefinedPropertyList.size();p++){
            hints[p] = dataDefinedPropertyList.get(p).getName();
        }
        editor = new PropertyEditor(hints);

        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Property property = editor.getProperty();
            dataUserPropertyList.add(property);
            setUserProperties(dataUserPropertyList);
        }
    }

    @Override
    public void editRow(int row) {
        PropertyEditor editor;
        String[] hints = new String[dataDefinedPropertyList.size()];
        for (int p=0;p<dataDefinedPropertyList.size();p++){
            hints[p] = dataDefinedPropertyList.get(p).getName();
        }
        editor = new PropertyEditor(hints);

        editor.setProperty(visiblePropertyList.get(row).property);
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            RowProperty rowP = new RowProperty();
            rowP.property = editor.getProperty();
            rowP.userProperty = true;
            rowP.info = "-";
            visiblePropertyList.set(row, rowP);
            updateDataProperties();
            updateVisibleProperties();
        }
    }

    @Override
    public void deleteRow(int row) {
        visiblePropertyList.remove(row);
        updateDataProperties();
        updateVisibleProperties();
    }

    private class RowProperty {
        private Property property;
        private boolean userProperty;
        private String info;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RowProperty that = (RowProperty) o;

            if (property != null ? !property.equals(that.property) : that.property != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = property != null ? property.hashCode() : 0;
            return result;
        }
    }
}

package org.perfcake.pc4idea.editor.editors;

import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.EditorTablePanel;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 19.10.2014
 */
public class PropertiesEditor extends AbstractEditor {
    private EditorTablePanel tablePanelProperties;

    public PropertiesEditor(){
        initComponents();
    }

    private void initComponents(){
        tablePanelProperties = new EditorTablePanel(new PropertiesTableModel(new ArrayList<>())) {
            @Override
            public void tableClickedActionPerformed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = tablePanelProperties.getTable().getSelectedRow();
                        if (selectedRow >= 0) {
                            PropertyEditor propertyEditor = new PropertyEditor();
                            propertyEditor.setProperty(((PropertiesTableModel) tablePanelProperties.getTable().getModel()).getPropertyList().get(selectedRow));
                            ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
                            dialog.show();
                            if (dialog.getExitCode() == 0) {
                                ((PropertiesTableModel) tablePanelProperties.getTable().getModel()).getPropertyList().set(selectedRow, propertyEditor.getProperty());
                                tablePanelProperties.getTable().repaint();
                            }
                        }
                    }
                }
            }

            @Override
            public void buttonAddActionPerformed(ActionEvent e) {
                PropertyEditor propertyEditor = new PropertyEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Property property = propertyEditor.getProperty();
                    ((PropertiesTableModel)tablePanelProperties.getTable().getModel()).getPropertyList().add(property);
                    tablePanelProperties.getTable().repaint();
                    tablePanelProperties.getTable().revalidate();
                }
            }

            @Override
            public void buttonEditActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelProperties.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    PropertyEditor propertyEditor = new PropertyEditor();
                    propertyEditor.setProperty(((PropertiesTableModel) tablePanelProperties.getTable().getModel()).getPropertyList().get(selectedRow));
                    ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
                    dialog.show();
                    if (dialog.getExitCode() == 0) {
                        ((PropertiesTableModel) tablePanelProperties.getTable().getModel()).getPropertyList().set(selectedRow, propertyEditor.getProperty());
                        tablePanelProperties.getTable().repaint();
                    }
                }
            }

            @Override
            public void buttonDeleteActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelProperties.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    ((PropertiesTableModel)tablePanelProperties.getTable().getModel()).getPropertyList().remove(selectedRow);
                    tablePanelProperties.getTable().repaint();
                    tablePanelProperties.getTable().revalidate();
                }
            }
        };

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addComponent(tablePanelProperties));
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(tablePanelProperties));
    }

    public void setObjProperties(Scenario.Properties properties){
        this.setListProperties(properties.getProperty());
    }

    public Scenario.Properties getObjProperties(){
        Scenario.Properties newProperties = new Scenario.Properties();
        newProperties.getProperty().addAll(this.getListProperties());
        return newProperties;
    }

    public void setListProperties(List<Property> properties){
        tablePanelProperties.getTable().setModel(new PropertiesTableModel(properties));
    }

    public List<Property> getListProperties(){
        return ((PropertiesTableModel)tablePanelProperties.getTable().getModel()).getPropertyList();
    }

    @Override
    public String getTitle(){
        return "Properties Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        // always valid
        return null;
    }

    private class PropertiesTableModel extends AbstractTableModel {
        private List<Property> propertyList = new ArrayList<>();

        private PropertiesTableModel(List<Property> properties){
            propertyList.addAll(properties);
        }

        public List<Property> getPropertyList(){
            return propertyList;
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
            switch (columnIndex){
                case 0: return property.getName();
                case 1: return property.getValue();
                default: return null;
            }
        }
        @Override
        public String getColumnName(int columnIndex){
            switch (columnIndex){
                case 0: return "Property Name";
                case 1: return "Property Value";
                default: return "";
            }
        }
    }
}

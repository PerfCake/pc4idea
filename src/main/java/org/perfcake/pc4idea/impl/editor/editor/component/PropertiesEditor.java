package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.editor.swing.EditorTablePanel;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.PropertiesTableModel;

import javax.swing.*;
import java.util.List;

/**
 * User: Stanislav Kaleta
 * Date: 19.10.2014
 */
public class PropertiesEditor extends AbstractEditor {
    private EditorTablePanel propertiesTablePanel;

    public PropertiesEditor() {
        initComponents();
    }

    private void initComponents() {
        propertiesTablePanel = new EditorTablePanel(new PropertiesTableModel());

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(propertiesTablePanel));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(propertiesTablePanel));
    }

    public void setObjProperties(Scenario.Properties properties) {
        this.setListProperties(properties.getProperty());
    }

    public Scenario.Properties getObjProperties() {
        Scenario.Properties newProperties = new Scenario.Properties();
        newProperties.getProperty().addAll(this.getListProperties());
        return newProperties;
    }

    public void setListProperties(List<Property> properties) {
        PropertiesTableModel model = (PropertiesTableModel) propertiesTablePanel.getTableModel();
        model.setUserProperties(properties);
    }

    public List<Property> getListProperties() {
        return ((PropertiesTableModel) propertiesTablePanel.getTableModel()).getUserProperties();
    }

    public void setDefinedProperties(List<Property> properties) {
        PropertiesTableModel model = (PropertiesTableModel) propertiesTablePanel.getTableModel();
        model.setDefinedProperties(properties);
    }

    @Override
    public String getTitle() {
        return "Properties Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        // always valid
        return null;
    }
}

package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.ScrollPaneFactory;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.editor.swing.EditorTablePanel;
import org.perfcake.pc4idea.api.editor.swing.StructurePropertiesTable;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.PropertiesTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 19.10.2014
 */
public class PropertiesEditor extends AbstractEditor {
    private EditorTablePanel propertiesTablePanel;
    private StructurePropertiesTable structurePropertiesTable;
    private JScrollPane structPropTableScrollPane;

    public PropertiesEditor() {
        initComponents();
    }

    private void initComponents() {
        propertiesTablePanel = new EditorTablePanel(new PropertiesTableModel(new ArrayList<>()));

        structurePropertiesTable = new StructurePropertiesTable();
        structPropTableScrollPane = ScrollPaneFactory.createScrollPane(structurePropertiesTable);
        structPropTableScrollPane.setVisible(false);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(propertiesTablePanel)
                .addComponent(structPropTableScrollPane));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(propertiesTablePanel)
                .addGap(5)
                .addComponent(structPropTableScrollPane, 100, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE));
    }

    public void setObjProperties(Scenario.Properties properties) {
        this.setListProperties(properties.getProperty());

        /*TODO if Properties has structure properties*/
        structurePropertiesTable.setStructureProperties(new ArrayList<>());
    }

    public Scenario.Properties getObjProperties() {
        Scenario.Properties newProperties = new Scenario.Properties();
        newProperties.getProperty().addAll(this.getListProperties());
        return newProperties;
    }

    public void setListProperties(List<Property> properties) {
        PropertiesTableModel model = new PropertiesTableModel(properties);
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                structurePropertiesTable.setModelProperties(((PropertiesTableModel) propertiesTablePanel.getTableModel()).getPropertyList());
            }
        });
        propertiesTablePanel.setTableModel(model);

        structurePropertiesTable.setModelProperties(properties);
    }

    public List<Property> getListProperties() {
        return ((PropertiesTableModel) propertiesTablePanel.getTableModel()).getPropertyList();
    }

    public void setStructureProperties(List<Property> properties) {
        structurePropertiesTable.setStructureProperties(properties);
        ((PropertiesTableModel) propertiesTablePanel.getTableModel()).setStructureProperties(properties);
        if (properties.size() > 0) {
            structPropTableScrollPane.setVisible(true);
        } else {
            structPropTableScrollPane.setVisible(false);
        }
        this.repaint();
        this.revalidate();
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

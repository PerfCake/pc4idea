package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.editor.swing.EditorTablePanel;
import org.perfcake.pc4idea.api.util.PerfCakeReflectUtil;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.PeriodsTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 22.10.2014
 */
public class DestinationEditor extends AbstractEditor {
    private JLabel labelDestinationType;
    private JLabel labelEnabled;
    private JComboBox comboBoxDestinationType;
    private JCheckBox checkBoxEnabled;
    private EditorTablePanel tablePanelPeriods;
    private PropertiesEditor panelProperties;

    private Module module;

    public DestinationEditor(Module module) {
        this.module = module;
        initComponents();
    }

    private void initComponents(){
        labelDestinationType = new JLabel("Destination type:");
        labelEnabled = new JLabel("Enabled:");
        String[] destinations = new PerfCakeReflectUtil(module).findComponentClassNames(PerfCakeReflectUtil.DESTINATION);
        comboBoxDestinationType = new ComboBox(new DefaultComboBoxModel<>(destinations));
        comboBoxDestinationType.setSelectedIndex(-1);
        comboBoxDestinationType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = (String) comboBoxDestinationType.getSelectedItem();
                PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
                List<Property> structureProp = reflectUtil.findComponentProperties(PerfCakeReflectUtil.DESTINATION, className);
                panelProperties.setStructureProperties(structureProp);
            }
        });
        checkBoxEnabled = new JCheckBox();
        checkBoxEnabled.setSelected(true);

        tablePanelPeriods = new EditorTablePanel(new PeriodsTableModel(new ArrayList<>()));

        panelProperties = new PropertiesEditor();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelDestinationType,GroupLayout.PREFERRED_SIZE,100,GroupLayout.PREFERRED_SIZE)
                .addComponent(comboBoxDestinationType))
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelEnabled,GroupLayout.PREFERRED_SIZE,100,GroupLayout.PREFERRED_SIZE)
                .addComponent(checkBoxEnabled))
            .addComponent(tablePanelPeriods)
            .addComponent(panelProperties));
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                .addComponent(labelDestinationType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addComponent(comboBoxDestinationType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
            .addGap(10)
            .addGroup(layout.createParallelGroup()
                .addComponent(labelEnabled, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addComponent(checkBoxEnabled, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
            .addGap(10)
            .addComponent(tablePanelPeriods)
            .addGap(10)
            .addComponent(panelProperties));
    }

    public void setDestination(Scenario.Reporting.Reporter.Destination destination){
        comboBoxDestinationType.setSelectedItem(destination.getClazz());
        checkBoxEnabled.setSelected(destination.isEnabled());
        tablePanelPeriods.setTableModel(new PeriodsTableModel(destination.getPeriod()));
        panelProperties.setListProperties(destination.getProperty());

        PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
        List<Property> structureProp = reflectUtil.findComponentProperties(PerfCakeReflectUtil.DESTINATION, destination.getClazz());
        panelProperties.setStructureProperties(structureProp);
    }

    public Scenario.Reporting.Reporter.Destination getDestination(){
        Scenario.Reporting.Reporter.Destination newDestination = new Scenario.Reporting.Reporter.Destination();
        newDestination.setClazz((String)comboBoxDestinationType.getSelectedItem());
        newDestination.setEnabled(checkBoxEnabled.isSelected());
        newDestination.getPeriod().addAll(((PeriodsTableModel) tablePanelPeriods.getTableModel()).getPeriodList());
        newDestination.getProperty().addAll(panelProperties.getListProperties());
        return newDestination;
    }

    @Override
    public String getTitle(){
        return "Destination Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        return (comboBoxDestinationType.getSelectedIndex() == -1) ?
                new ValidationInfo("Destination type isn't selected") : null;
    }
}

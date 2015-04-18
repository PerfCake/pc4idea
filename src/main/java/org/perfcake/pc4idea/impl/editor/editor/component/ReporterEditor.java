package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.editor.swing.EditorTablePanel;
import org.perfcake.pc4idea.api.util.PerfCakeReflectUtil;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.DestinationsTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 25.10.2014
 */
public class ReporterEditor extends AbstractEditor {
    private JComboBox comboBoxReporterType;
    private JCheckBox checkBoxEnabled;
    private EditorTablePanel tablePanelDestinations;
    private PropertiesEditor panelProperties;

    private Module module;

    public ReporterEditor(Module module) {
        this.module = module;
        initComponents();
    }

    private void initComponents(){
        JLabel labelReporterType = new JLabel("Reporter type:");
        JLabel labelEnabled = new JLabel("Enabled:");

        String[] reporters = new PerfCakeReflectUtil(module).findComponentClassNames(PerfCakeReflectUtil.REPORTER);

        comboBoxReporterType = new ComboBox(new DefaultComboBoxModel<>(reporters));
        comboBoxReporterType.setSelectedIndex(-1);
        comboBoxReporterType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = (String) comboBoxReporterType.getSelectedItem();
                PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
                List<Property> structureProp = reflectUtil.findComponentProperties(PerfCakeReflectUtil.REPORTER, className);
                panelProperties.setStructureProperties(structureProp);
            }
        });
        checkBoxEnabled = new JCheckBox();
        checkBoxEnabled.setSelected(true);

        tablePanelDestinations = new EditorTablePanel(new DestinationsTableModel(new ArrayList<>(), module));

        panelProperties = new PropertiesEditor();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelReporterType,GroupLayout.PREFERRED_SIZE,100,GroupLayout.PREFERRED_SIZE)
                .addComponent(comboBoxReporterType))
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelEnabled,GroupLayout.PREFERRED_SIZE,100,GroupLayout.PREFERRED_SIZE)
                .addComponent(checkBoxEnabled))
            .addComponent(tablePanelDestinations)
            .addComponent(panelProperties));
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                .addComponent(labelReporterType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addComponent(comboBoxReporterType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
            .addGap(10)
            .addGroup(layout.createParallelGroup()
                .addComponent(labelEnabled, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addComponent(checkBoxEnabled, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
            .addGap(10)
            .addComponent(tablePanelDestinations)
            .addGap(10)
            .addComponent(panelProperties));
    }

    public void setReporter(Scenario.Reporting.Reporter reporter){
        comboBoxReporterType.setSelectedItem(reporter.getClazz());
        checkBoxEnabled.setSelected(reporter.isEnabled());
        tablePanelDestinations.setTableModel(new DestinationsTableModel(reporter.getDestination(), module));
        panelProperties.setListProperties(reporter.getProperty());

        PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
        List<Property> structureProp = reflectUtil.findComponentProperties(PerfCakeReflectUtil.REPORTER, reporter.getClazz());
        panelProperties.setStructureProperties(structureProp);
    }

    public Scenario.Reporting.Reporter getReporter(){
        Scenario.Reporting.Reporter newReporter = new Scenario.Reporting.Reporter();
        newReporter.setClazz((String)comboBoxReporterType.getSelectedItem());
        newReporter.setEnabled(checkBoxEnabled.isSelected());
        newReporter.getDestination().addAll(((DestinationsTableModel) tablePanelDestinations.getTableModel()).getDestinationList());
        newReporter.getProperty().addAll(panelProperties.getListProperties());
        return newReporter;
    }

    @Override
    public String getTitle(){
        return "Reporter Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        return (comboBoxReporterType.getSelectedIndex() == -1) ?
                new ValidationInfo("Reporter type isn't selected") : null;
    }
}

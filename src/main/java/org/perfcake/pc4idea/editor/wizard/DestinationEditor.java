package org.perfcake.pc4idea.editor.wizard;

import com.intellij.openapi.ui.ComboBox;
import org.perfcake.model.Scenario;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 22.10.2014
 */
public class DestinationEditor extends JPanel {
    private JLabel labelDestinationType;
    private JLabel labelEnabled;
    private JComboBox comboBoxDestinationType;
    private JCheckBox checkBoxEnabled;
    private PeriodsEditor panelPeriods;
    private PropertiesEditor panelProperties;

    public DestinationEditor(){
        initComponents();
        this.setPreferredSize(new Dimension(350,0));
    }

    private void initComponents(){
        labelDestinationType = new JLabel("Destination type:");
        labelEnabled = new JLabel("Enabled:");
        comboBoxDestinationType = new ComboBox();
        comboBoxDestinationType.addItem("ConsoleDestination");        /*TODO load from classpath?*/
        comboBoxDestinationType.addItem("CsvDestination");
        comboBoxDestinationType.addItem("Log4jDestination");
        checkBoxEnabled = new JCheckBox();
        panelPeriods = new PeriodsEditor();
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
            .addComponent(panelPeriods)
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
            .addComponent(panelPeriods)
            .addGap(10)
            .addComponent(panelProperties));
    }

    public void setDestination(Scenario.Reporting.Reporter.Destination destination){
        comboBoxDestinationType.setSelectedItem(destination.getClazz());
        checkBoxEnabled.setSelected(destination.isEnabled());
        panelPeriods.setPeriods(destination.getPeriod());
        panelProperties.setProperties(destination.getProperty());
    }

    public boolean areInsertedValuesValid() {
        //boolean areValid = false;
        /*TODO check validity of inserted data - if false OK button disabled*/
        return true;
    }

    public Scenario.Reporting.Reporter.Destination getDestination(){
        Scenario.Reporting.Reporter.Destination newDestination = new Scenario.Reporting.Reporter.Destination();
        newDestination.setClazz((String)comboBoxDestinationType.getSelectedItem());
        newDestination.setEnabled(checkBoxEnabled.isSelected());
        newDestination.getPeriod().addAll(panelPeriods.getPeriods());
        newDestination.getProperty().addAll(panelProperties.getProperties());
        return newDestination;
    }
}

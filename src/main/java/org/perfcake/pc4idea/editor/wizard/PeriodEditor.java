package org.perfcake.pc4idea.editor.wizard;

import com.intellij.openapi.ui.ComboBox;
import org.perfcake.model.Scenario;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 22.10.2014
 */
public class PeriodEditor extends JPanel {
    private JLabel labelType;
    private JLabel labelValue;
    private JComboBox comboBoxType;
    private JTextField textFieldValue;

    public PeriodEditor() {
        initComponents();
    }

    private void initComponents(){
        labelType = new JLabel("Period type:");
        labelValue = new JLabel("Value:");
        comboBoxType = new ComboBox();
        comboBoxType.addItem("iteration");
        comboBoxType.addItem("time");
        comboBoxType.addItem("percentage");
        textFieldValue = new JTextField();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelType, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxType))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelValue, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldValue)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)));
    }

    public void setPeriod(Scenario.Reporting.Reporter.Destination.Period period){
        comboBoxType.setSelectedItem(period.getType());
        textFieldValue.setText(period.getValue());
    }

    public Scenario.Reporting.Reporter.Destination.Period getPeriod(){
        Scenario.Reporting.Reporter.Destination.Period newPeriod = new Scenario.Reporting.Reporter.Destination.Period();
        newPeriod.setType((String)comboBoxType.getSelectedItem());
        newPeriod.setValue(textFieldValue.getText());
        return newPeriod;
    }
}

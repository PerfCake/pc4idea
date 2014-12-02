package org.perfcake.pc4idea.editor.designer.editors;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Scenario;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 2.12.2014
 */
public class PeriodEditor extends  AbstractEditor{
    private JLabel labelPeriodType;
    private JLabel labelPeriodValue;
    private JComboBox comboBoxPeriodType;
    private JTextField textFieldPeriodValue;

    public PeriodEditor() {
        initComponents();
    }

    private void initComponents(){
        labelPeriodType = new JLabel("Period type:");
        labelPeriodValue = new JLabel("Value:");
        comboBoxPeriodType = new ComboBox();
        comboBoxPeriodType.addItem("iteration");
        comboBoxPeriodType.addItem("time");
        comboBoxPeriodType.addItem("percentage");
        comboBoxPeriodType.setSelectedIndex(-1);
        textFieldPeriodValue = new JTextField();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelPeriodType, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxPeriodType))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelPeriodValue, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldPeriodValue)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelPeriodType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxPeriodType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelPeriodValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldPeriodValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)));
    }

    public void setPeriod(Scenario.Reporting.Reporter.Destination.Period period){
        comboBoxPeriodType.setSelectedItem(period.getType());
        textFieldPeriodValue.setText(period.getValue());
    }

    public Scenario.Reporting.Reporter.Destination.Period getPeriod(){
        Scenario.Reporting.Reporter.Destination.Period newPeriod = new Scenario.Reporting.Reporter.Destination.Period();
        newPeriod.setType((String) comboBoxPeriodType.getSelectedItem());
        newPeriod.setValue(textFieldPeriodValue.getText());
        return newPeriod;
    }

    @Override
    public String getTitle(){
        return "Period Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        ValidationInfo info = null;
        if (textFieldPeriodValue.getText().isEmpty()){
            info = new ValidationInfo("Text field can't be empty");
        }
        if (comboBoxPeriodType.getSelectedIndex() == -1){
            info = new ValidationInfo("Period type isn't selected");
        }
        return info;
    }
}

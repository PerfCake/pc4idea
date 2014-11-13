package org.perfcake.pc4idea.editor.designer.editors;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.common.EditorTablePanel;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
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

    public DestinationEditor(){
        initComponents();
    }

    private void initComponents(){
        labelDestinationType = new JLabel("Destination type:");
        labelEnabled = new JLabel("Enabled:");
        comboBoxDestinationType = new ComboBox();
        comboBoxDestinationType.addItem("ConsoleDestination");        /*TODO load from classpath?*/
        comboBoxDestinationType.addItem("CsvDestination");
        comboBoxDestinationType.addItem("Log4jDestination");
        checkBoxEnabled = new JCheckBox();

        tablePanelPeriods = new EditorTablePanel(new PeriodsTableModel(new ArrayList<Scenario.Reporting.Reporter.Destination.Period>())) {
            @Override
            public void tableClickedActionPerformed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = tablePanelPeriods.getTable().getSelectedRow();
                        if (selectedRow >= 0) {
                            PeriodEditor periodEditor = new PeriodEditor();
                            periodEditor.setPeriod(((PeriodsTableModel) tablePanelPeriods.getTable().getModel()).getPeriodList().get(selectedRow));
                            ScenarioDialogEditor dialog = new ScenarioDialogEditor(periodEditor);
                            dialog.show();
                            if (dialog.getExitCode() == 0) {
                                ((PeriodsTableModel) tablePanelPeriods.getTable().getModel()).getPeriodList().set(selectedRow, periodEditor.getPeriod());
                                tablePanelPeriods.getTable().repaint();
                            }
                        }
                    }
                }
            }

            @Override
            public void buttonAddActionPerformed(ActionEvent e) {
                PeriodEditor periodEditor = new PeriodEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(periodEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Scenario.Reporting.Reporter.Destination.Period period = periodEditor.getPeriod();
                    ((PeriodsTableModel)tablePanelPeriods.getTable().getModel()).getPeriodList().add(period);
                    tablePanelPeriods.getTable().repaint();
                    tablePanelPeriods.getTable().revalidate();
                }
            }

            @Override
            public void buttonEditActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelPeriods.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    PeriodEditor periodEditor = new PeriodEditor();
                    periodEditor.setPeriod(((PeriodsTableModel) tablePanelPeriods.getTable().getModel()).getPeriodList().get(selectedRow));
                    ScenarioDialogEditor dialog = new ScenarioDialogEditor(periodEditor);
                    dialog.show();
                    if (dialog.getExitCode() == 0) {
                        ((PeriodsTableModel) tablePanelPeriods.getTable().getModel()).getPeriodList().set(selectedRow, periodEditor.getPeriod());
                        tablePanelPeriods.getTable().repaint();
                    }
                }
            }

            @Override
            public void buttonDeleteActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelPeriods.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    ((PeriodsTableModel)tablePanelPeriods.getTable().getModel()).getPeriodList().remove(selectedRow);
                    tablePanelPeriods.getTable().repaint();
                    tablePanelPeriods.getTable().revalidate();
                }
            }
        };

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
        tablePanelPeriods.getTable().setModel(new PeriodsTableModel(destination.getPeriod()));
        panelProperties.setListProperties(destination.getProperty());
    }

    public Scenario.Reporting.Reporter.Destination getDestination(){
        Scenario.Reporting.Reporter.Destination newDestination = new Scenario.Reporting.Reporter.Destination();
        newDestination.setClazz((String)comboBoxDestinationType.getSelectedItem());
        newDestination.setEnabled(checkBoxEnabled.isSelected());
        newDestination.getPeriod().addAll(((PeriodsTableModel)tablePanelPeriods.getTable().getModel()).getPeriodList());
        newDestination.getProperty().addAll(panelProperties.getListProperties());
        return newDestination;
    }

    @Override
    public String getTitle(){
        return "Destination Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        // always valid
        return null;
    }

    private class PeriodsTableModel extends AbstractTableModel {
        private java.util.List<Scenario.Reporting.Reporter.Destination.Period> periodList = new ArrayList<>();

        private PeriodsTableModel(List<Scenario.Reporting.Reporter.Destination.Period> periods){
            periodList.addAll(periods);
        }

        public List<Scenario.Reporting.Reporter.Destination.Period> getPeriodList(){
            return periodList;
        }

        @Override
        public int getRowCount() {
            return periodList.size();
        }
        @Override
        public int getColumnCount() {
            return 2;
        }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Scenario.Reporting.Reporter.Destination.Period period = periodList.get(rowIndex);
            switch (columnIndex){
                case 0: return period.getType();
                case 1: return period.getValue();
                default: return null;
            }
        }
        @Override
        public String getColumnName(int columnIndex){
            switch (columnIndex){
                case 0: return "Period Type";
                case 1: return "Period Value";
                default: return "";
            }
        }
    }

    private class PeriodEditor extends AbstractEditor {
        private JLabel labelType;
        private JLabel labelValue;
        private JComboBox comboBoxType;
        private JTextField textFieldValue;

        private PeriodEditor() {
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

        @Override
        public String getTitle(){
            return "Period Editor";
        }

        @Override
        public ValidationInfo areInsertedValuesValid() {
            return (textFieldValue.getText().isEmpty()) ?
                    new ValidationInfo("Text fields can't be empty") : null;
        }
    }
}

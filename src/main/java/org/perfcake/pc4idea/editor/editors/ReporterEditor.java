package org.perfcake.pc4idea.editor.editors;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
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
 * Date: 25.10.2014
 */
public class ReporterEditor extends AbstractEditor {
    private JComboBox comboBoxReporterType;
    private JCheckBox checkBoxEnabled;
    private EditorTablePanel tablePanelDestinations;
    private PropertiesEditor panelProperties;

    public ReporterEditor(){
        initComponents();
    }

    private void initComponents(){
        JLabel labelReporterType = new JLabel("Reporter type:");
        JLabel labelEnabled = new JLabel("Enabled:");
        comboBoxReporterType = new ComboBox();
        comboBoxReporterType.addItem("ThroughputStatsReporter");          /*TODO from classpath*/
        comboBoxReporterType.addItem("MemoryUsageReporter");
        comboBoxReporterType.addItem("ResponseTimeStatsReporter");
        comboBoxReporterType.addItem("WarmUpReporter");
        comboBoxReporterType.setSelectedIndex(-1);
        checkBoxEnabled = new JCheckBox();
        checkBoxEnabled.setSelected(true);

        tablePanelDestinations = new EditorTablePanel(new DestinationsTableModel(new ArrayList<Scenario.Reporting.Reporter.Destination>())) {
            @Override
            public void tableClickedActionPerformed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = tablePanelDestinations.getTable().getSelectedRow();
                        if (selectedRow >= 0) {
                            DestinationEditor destinationEditor = new DestinationEditor();
                            destinationEditor.setDestination(((DestinationsTableModel) tablePanelDestinations.getTable().getModel()).getDestinationList().get(selectedRow));
                            ScenarioDialogEditor dialog = new ScenarioDialogEditor(destinationEditor);
                            dialog.show();
                            if (dialog.getExitCode() == 0) {
                                ((DestinationsTableModel) tablePanelDestinations.getTable().getModel()).getDestinationList().set(selectedRow, destinationEditor.getDestination());
                                tablePanelDestinations.getTable().repaint();
                            }
                        }
                    }
                }
            }

            @Override
            public void buttonAddActionPerformed(ActionEvent e) {
                DestinationEditor destinationEditor = new DestinationEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(destinationEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Scenario.Reporting.Reporter.Destination destination = destinationEditor.getDestination();
                    ((DestinationsTableModel)tablePanelDestinations.getTable().getModel()).getDestinationList().add(destination);
                    tablePanelDestinations.getTable().repaint();
                    tablePanelDestinations.getTable().revalidate();
                }
            }

            @Override
            public void buttonEditActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelDestinations.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    DestinationEditor destinationEditor = new DestinationEditor();
                    destinationEditor.setDestination(((DestinationsTableModel) tablePanelDestinations.getTable().getModel()).getDestinationList().get(selectedRow));
                    ScenarioDialogEditor dialog = new ScenarioDialogEditor(destinationEditor);
                    dialog.show();
                    if (dialog.getExitCode() == 0) {
                        ((DestinationsTableModel) tablePanelDestinations.getTable().getModel()).getDestinationList().set(selectedRow, destinationEditor.getDestination());
                        tablePanelDestinations.getTable().repaint();
                    }
                }
            }

            @Override
            public void buttonDeleteActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelDestinations.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    ((DestinationsTableModel)tablePanelDestinations.getTable().getModel()).getDestinationList().remove(selectedRow);
                    tablePanelDestinations.getTable().repaint();
                    tablePanelDestinations.getTable().revalidate();
                }
            }
        };

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
        tablePanelDestinations.getTable().setModel(new DestinationsTableModel(reporter.getDestination()));
        panelProperties.setListProperties(reporter.getProperty());
    }

    public Scenario.Reporting.Reporter getReporter(){
        Scenario.Reporting.Reporter newReporter = new Scenario.Reporting.Reporter();
        newReporter.setClazz((String)comboBoxReporterType.getSelectedItem());
        newReporter.setEnabled(checkBoxEnabled.isSelected());
        newReporter.getDestination().addAll(((DestinationsTableModel) tablePanelDestinations.getTable().getModel()).getDestinationList());
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

    private class DestinationsTableModel extends AbstractTableModel {
        private List<Scenario.Reporting.Reporter.Destination> destinationList = new ArrayList<>();

        private DestinationsTableModel(List<Scenario.Reporting.Reporter.Destination> destinations){
            destinationList.addAll(destinations);
        }

        public List<Scenario.Reporting.Reporter.Destination> getDestinationList(){
            return destinationList;
        }

        @Override
        public int getRowCount() {
            return destinationList.size();
        }
        @Override
        public int getColumnCount() {
            return 2;
        }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Scenario.Reporting.Reporter.Destination destination = destinationList.get(rowIndex);
            switch (columnIndex){
                case 0: return destination.isEnabled();
                case 1: return destination.getClazz();
                default: return null;
            }
        }
        @Override
        public String getColumnName(int columnIndex){
            switch (columnIndex){
                case 0: return "Enabled";
                case 1: return "Destination type";
                default: return "";
            }
        }
    }
}

package org.perfcake.pc4idea.editor.wizard;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;
import org.perfcake.model.Scenario;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
        panelProperties.setListProperties(destination.getProperty());
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
        newDestination.getProperty().addAll(panelProperties.getListProperties());
        return newDestination;
    }

    private class PeriodsEditor extends JPanel {
        private JTable tablePeriods;
        private JScrollPane scrollPaneTablePeriods;
        private JButton buttonAddPeriod;
        private JButton buttonEditPeriod;
        private JButton buttonDeletePeriod;

        private PeriodsEditor(){
            initComponents();
            this.setPreferredSize(new Dimension(350,0));
        }

        private void initComponents(){
            tablePeriods = new JBTable();
            scrollPaneTablePeriods = ScrollPaneFactory.createScrollPane(tablePeriods);
            tablePeriods.setModel(new PeriodsTableModel());
            tablePeriods.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                        if (e.getClickCount() == 2) {
                            PeriodEditor periodEditor = new PeriodEditor();
                            int selectedRow = tablePeriods.getSelectedRow();
                            periodEditor.setPeriod(((PeriodsTableModel)tablePeriods.getModel()).getPeriods().get(selectedRow));
                            ComponentEditor editor = new ComponentEditor("Period Editor",periodEditor);
                            editor.show();
                            if (editor.getExitCode() == 0) {
                                ((PeriodsTableModel)tablePeriods.getModel()).getPeriods().set(selectedRow, periodEditor.getPeriod());
                                tablePeriods.repaint();
                            }
                        }
                    }
                }
            });
            tablePeriods.getTableHeader().setReorderingAllowed(false);
            ((DefaultTableCellRenderer)tablePeriods.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

            buttonAddPeriod = new JButton("Add");
            buttonAddPeriod.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PeriodEditor periodEditor = new PeriodEditor();
                    ComponentEditor editor = new ComponentEditor("Period Editor",periodEditor);
                    editor.show();
                    if (editor.getExitCode() == 0) {
                        Scenario.Reporting.Reporter.Destination.Period period = periodEditor.getPeriod();
                        ((PeriodsTableModel)tablePeriods.getModel()).getPeriods().add(period);
                        tablePeriods.repaint();
                        scrollPaneTablePeriods.revalidate();
                    }
                }
            });
            buttonEditPeriod = new JButton("Edit");
            buttonEditPeriod.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = tablePeriods.getSelectedRow();
                    if (selectedRow >= 0) {
                        PeriodEditor periodEditor = new PeriodEditor();
                        periodEditor.setPeriod(((PeriodsTableModel) tablePeriods.getModel()).getPeriods().get(selectedRow));
                        ComponentEditor editor = new ComponentEditor("Period Editor",periodEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            ((PeriodsTableModel) tablePeriods.getModel()).getPeriods().set(selectedRow, periodEditor.getPeriod());
                            tablePeriods.repaint();
                        }
                    }
                }
            });
            buttonDeletePeriod = new JButton("Delete");
            buttonDeletePeriod.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = tablePeriods.getSelectedRow();
                    if (selectedRow >= 0) {
                        ((PeriodsTableModel)tablePeriods.getModel()).getPeriods().remove(selectedRow);
                        tablePeriods.repaint();
                        scrollPaneTablePeriods.revalidate();
                    }
                }
            });

            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                    .addComponent(scrollPaneTablePeriods)
                    .addGap(5)
                    .addGroup(layout.createParallelGroup()
                            .addComponent(buttonAddPeriod, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonEditPeriod, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonDeletePeriod, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)));
            layout.setVerticalGroup(layout.createParallelGroup()
                    .addComponent(scrollPaneTablePeriods)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(buttonAddPeriod)
                            .addComponent(buttonEditPeriod)
                            .addComponent(buttonDeletePeriod)));
        }

        public void setPeriods(java.util.List<Scenario.Reporting.Reporter.Destination.Period> periods){
            PeriodsTableModel model = new PeriodsTableModel();
            model.addPeriods(periods);
            tablePeriods.setModel(model);
        }

        public java.util.List<Scenario.Reporting.Reporter.Destination.Period> getPeriods(){
            return ((PeriodsTableModel)tablePeriods.getModel()).getPeriods();
        }

        private class PeriodsTableModel extends AbstractTableModel {
            private java.util.List<Scenario.Reporting.Reporter.Destination.Period> listPeriods = new ArrayList<>();

            private PeriodsTableModel(){}

            public void addPeriods(java.util.List<Scenario.Reporting.Reporter.Destination.Period> periods){
                listPeriods.addAll(periods);
            }

            public java.util.List<Scenario.Reporting.Reporter.Destination.Period> getPeriods(){
                return listPeriods;
            }

            @Override
            public int getRowCount() {
                return listPeriods.size();
            }
            @Override
            public int getColumnCount() {
                return 2;
            }
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Scenario.Reporting.Reporter.Destination.Period period = listPeriods.get(rowIndex);
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

        private class ComponentEditor extends DialogWrapper {
            private JPanel centerPanel;

            private ComponentEditor(String title, JPanel centerPanel){
                super(false);
                setTitle(title);
                this.centerPanel = centerPanel;
                this.setResizable(true);
                init();
            }
            @Nullable
            @Override
            protected JComponent createCenterPanel() {
                return centerPanel;
            }
        }

        private class PeriodEditor extends JPanel {
            private JLabel labelType;
            private JLabel labelValue;
            private JComboBox comboBoxType;
            private JTextField textFieldValue;

            private PeriodEditor() {
                initComponents();
                this.setPreferredSize(new Dimension(350,0));
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
    }
}

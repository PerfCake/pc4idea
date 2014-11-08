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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 25.10.2014
 */
public class ReporterEditor extends JPanel {
    private JLabel labelReporterType;
    private JLabel labelEnabled;
    private JComboBox comboBoxReporterType;
    private JCheckBox checkBoxEnabled;
    private DestinationsEditor panelDestinations;
    private PropertiesEditor panelProperties;

    public ReporterEditor(){
        initComponents();
        this.setPreferredSize(new Dimension(350,0));
    }

    private void initComponents(){
        labelReporterType = new JLabel("Reporter type:");
        labelEnabled = new JLabel("Enabled:");
        comboBoxReporterType = new ComboBox();
        comboBoxReporterType.addItem("ResponseTimeReporter");
        comboBoxReporterType.addItem("WarmUpReporter");
        comboBoxReporterType.addItem("MemoryUsageReporter");
        comboBoxReporterType.addItem("AverageThroughputReporter");
        comboBoxReporterType.addItem("WindowResponseTimeReporter");
        checkBoxEnabled = new JCheckBox();
        panelDestinations = new DestinationsEditor();
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
            .addComponent(panelDestinations)
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
            .addComponent(panelDestinations)
            .addGap(10)
            .addComponent(panelProperties));
    }

    public void setReporter(Scenario.Reporting.Reporter reporter){
        comboBoxReporterType.setSelectedItem(reporter.getClazz());
        checkBoxEnabled.setSelected(reporter.isEnabled());
        panelDestinations.setDestinations(reporter.getDestination());
        panelProperties.setListProperties(reporter.getProperty());
    }

    public boolean areInsertedValuesValid() {
        //boolean areValid = false;
        /*TODO check validity of inserted data - if false OK button disabled*/
        return true;
    }

    public Scenario.Reporting.Reporter getReporter(){
        Scenario.Reporting.Reporter newReporter = new Scenario.Reporting.Reporter();
        newReporter.setClazz((String)comboBoxReporterType.getSelectedItem());
        newReporter.setEnabled(checkBoxEnabled.isSelected());
        newReporter.getDestination().addAll(panelDestinations.getDestinations());
        newReporter.getProperty().addAll(panelProperties.getListProperties());
        return newReporter;
    }

    private class DestinationsEditor extends JPanel {
        private JTable tableDestinations;
        private JScrollPane scrollPaneTableDestinations;
        private JButton buttonAddDestination;
        private JButton buttonEditDestination;
        private JButton buttonDeleteDestination;

        private DestinationsEditor(){this.initComponents();}

        private void initComponents(){
            tableDestinations = new JBTable();
            scrollPaneTableDestinations = ScrollPaneFactory.createScrollPane(tableDestinations);
            tableDestinations.setModel(new DestinationsTableModel());
            tableDestinations.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                        if (e.getClickCount() == 2) {
                            DestinationEditor destinationEditor = new DestinationEditor();
                            int selectedRow = tableDestinations.getSelectedRow();
                            destinationEditor.setDestination(((DestinationsTableModel) tableDestinations.getModel()).getDestinations().get(selectedRow));
                            ComponentEditor editor = new ComponentEditor("Destination Editor",destinationEditor);
                            editor.show();
                            if (editor.getExitCode() == 0) {
                                ((DestinationsTableModel)tableDestinations.getModel()).getDestinations().set(selectedRow, destinationEditor.getDestination());
                                tableDestinations.repaint();
                            }
                        }
                    }
                }
            });
            tableDestinations.getTableHeader().setReorderingAllowed(false);
            ((DefaultTableCellRenderer)tableDestinations.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
            buttonAddDestination = new JButton("Add");
            buttonAddDestination.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DestinationEditor destinationEditor = new DestinationEditor();
                    ComponentEditor editor = new ComponentEditor("Destination Editor",destinationEditor);
                    editor.show();
                    if (editor.getExitCode() == 0) {
                        Scenario.Reporting.Reporter.Destination destination = destinationEditor.getDestination();
                        ((DestinationsTableModel)tableDestinations.getModel()).getDestinations().add(destination);
                        tableDestinations.repaint();
                        scrollPaneTableDestinations.revalidate();
                    }
                }
            });
            buttonEditDestination = new JButton("Edit");
            buttonEditDestination.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = tableDestinations.getSelectedRow();
                    if (selectedRow >= 0) {
                        DestinationEditor destinationEditor = new DestinationEditor();
                        destinationEditor.setDestination(((DestinationsTableModel) tableDestinations.getModel()).getDestinations().get(selectedRow));
                        ComponentEditor editor = new ComponentEditor("Destination Editor",destinationEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            ((DestinationsTableModel) tableDestinations.getModel()).getDestinations().set(selectedRow, destinationEditor.getDestination());
                            tableDestinations.repaint();
                        }
                    }
                }
            });
            buttonDeleteDestination = new JButton("Delete");
            buttonDeleteDestination.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = tableDestinations.getSelectedRow();
                    if (selectedRow >= 0) {
                        ((DestinationsTableModel)tableDestinations.getModel()).getDestinations().remove(selectedRow);
                        tableDestinations.repaint();
                        scrollPaneTableDestinations.revalidate();
                    }
                }
            });

            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(scrollPaneTableDestinations)
                .addGap(5)
                .addGroup(layout.createParallelGroup()
                    .addComponent(buttonAddDestination, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonEditDestination, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonDeleteDestination, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)));
            layout.setVerticalGroup(layout.createParallelGroup()
                .addComponent(scrollPaneTableDestinations)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(buttonAddDestination)
                    .addComponent(buttonEditDestination)
                    .addComponent(buttonDeleteDestination)));
        }

        public void setDestinations(List<Scenario.Reporting.Reporter.Destination> destinations){
            DestinationsTableModel model = new DestinationsTableModel();
            model.addDestinations(destinations);
            tableDestinations.setModel(model);
        }

        public List<Scenario.Reporting.Reporter.Destination> getDestinations(){
            return ((DestinationsTableModel)tableDestinations.getModel()).getDestinations();
        }

        private class DestinationsTableModel extends AbstractTableModel {
            private List<Scenario.Reporting.Reporter.Destination> listDestinations = new ArrayList<>();

            private DestinationsTableModel(){}

            public void addDestinations(List<Scenario.Reporting.Reporter.Destination> destinations){
                listDestinations.addAll(destinations);
            }

            public List<Scenario.Reporting.Reporter.Destination> getDestinations(){
                return listDestinations;
            }

            @Override
            public int getRowCount() {
                return listDestinations.size();
            }
            @Override
            public int getColumnCount() {
                return 2;
            }
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Scenario.Reporting.Reporter.Destination destination = listDestinations.get(rowIndex);
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

    }
}

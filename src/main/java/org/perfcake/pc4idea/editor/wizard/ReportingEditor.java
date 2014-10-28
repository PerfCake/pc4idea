package org.perfcake.pc4idea.editor.wizard;

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
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 26.10.2014
 */
public class ReportingEditor extends JPanel {
    private ReportersEditor panelReporters;
    private PropertiesEditor panelProperties;

    public ReportingEditor(){
        initComponents();
        this.setPreferredSize(new Dimension(350,0));
    }

    private void initComponents(){
        panelReporters = new ReportersEditor();
        panelProperties = new PropertiesEditor();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
            .addComponent(panelReporters)
            .addComponent(panelProperties));
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(panelReporters)
            .addGap(10)
            .addComponent(panelProperties));
    }

    public void setReporting(Scenario.Reporting reporting){
        panelReporters.setReporters(reporting.getReporter());
        panelProperties.setListProperties(reporting.getProperty());
    }

    public boolean areInsertedValuesValid() {
        //boolean areValid = false;
        /*TODO check validity of inserted data - if false OK button disabled*/
        return true;
    }

    public Scenario.Reporting getReporting(){
        Scenario.Reporting newReporting = new Scenario.Reporting();
        newReporting.getReporter().addAll(panelReporters.getReporters());
        newReporting.getProperty().addAll(panelProperties.getListProperties());
        return newReporting;
    }

    private class ReportersEditor extends JPanel {
        private JTable tableReporters;
        private JScrollPane scrollPaneTableReporters;
        private JButton buttonAddReporter;
        private JButton buttonEditReporter;
        private JButton buttonDeleteReporter;

        private ReportersEditor(){this.initComponents();}

        private void initComponents(){
            tableReporters = new JBTable();
            scrollPaneTableReporters = ScrollPaneFactory.createScrollPane(tableReporters);
            tableReporters.setModel(new ReportersTableModel());
            tableReporters.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                        if (e.getClickCount() == 2) {
                            ReporterEditor reporterEditor = new ReporterEditor();
                            int selectedRow = tableReporters.getSelectedRow();
                            reporterEditor.setReporter(((ReportersTableModel) tableReporters.getModel()).getReporters().get(selectedRow));
                            ComponentEditor editor = new ComponentEditor("Reporter Editor", reporterEditor);
                            editor.show();
                            if (editor.getExitCode() == 0) {
                                ((ReportersTableModel) tableReporters.getModel()).getReporters().set(selectedRow, reporterEditor.getReporter());
                                tableReporters.repaint();
                            }
                        }
                    }
                }
            });
            tableReporters.getTableHeader().setReorderingAllowed(false);
            ((DefaultTableCellRenderer)tableReporters.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
            buttonAddReporter = new JButton("Add");
            buttonAddReporter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReporterEditor reporterEditor = new ReporterEditor();
                    ComponentEditor editor = new ComponentEditor("Reporter Editor", reporterEditor);
                    editor.show();
                    if (editor.getExitCode() == 0) {
                        Scenario.Reporting.Reporter reporter = reporterEditor.getReporter();
                        ((ReportersTableModel)tableReporters.getModel()).getReporters().add(reporter);
                        tableReporters.repaint();
                        scrollPaneTableReporters.revalidate();
                    }
                }
            });
            buttonEditReporter = new JButton("Edit");
            buttonEditReporter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = tableReporters.getSelectedRow();
                    if (selectedRow >= 0) {
                        ReporterEditor reporterEditor = new ReporterEditor();
                        reporterEditor.setReporter(((ReportersTableModel) tableReporters.getModel()).getReporters().get(selectedRow));
                        ComponentEditor editor = new ComponentEditor("Reporter Editor", reporterEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            ((ReportersTableModel) tableReporters.getModel()).getReporters().set(selectedRow, reporterEditor.getReporter());
                            tableReporters.repaint();
                        }
                    }
                }
            });
            buttonDeleteReporter = new JButton("Delete");
            buttonDeleteReporter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = tableReporters.getSelectedRow();
                    if (selectedRow >= 0) {
                        ((ReportersTableModel)tableReporters.getModel()).getReporters().remove(selectedRow);
                        tableReporters.repaint();
                        scrollPaneTableReporters.revalidate();
                    }
                }
            });

            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                    .addComponent(scrollPaneTableReporters)
                    .addGap(5)
                    .addGroup(layout.createParallelGroup()
                            .addComponent(buttonAddReporter, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonEditReporter, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonDeleteReporter, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)));
            layout.setVerticalGroup(layout.createParallelGroup()
                    .addComponent(scrollPaneTableReporters)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(buttonAddReporter)
                            .addComponent(buttonEditReporter)
                            .addComponent(buttonDeleteReporter)));
        }

        public void setReporters(List<Scenario.Reporting.Reporter> reporters){
            ReportersTableModel model = new ReportersTableModel();
            model.addReporters(reporters);
            tableReporters.setModel(model);
        }

        public List<Scenario.Reporting.Reporter> getReporters(){
            return ((ReportersTableModel)tableReporters.getModel()).getReporters();
        }

        private class ReportersTableModel extends AbstractTableModel {
            private List<Scenario.Reporting.Reporter> listReporters = new ArrayList<>();

            private ReportersTableModel(){}

            public void addReporters(List<Scenario.Reporting.Reporter> reporters){
                listReporters.addAll(reporters);
            }

            public List<Scenario.Reporting.Reporter> getReporters(){
                return listReporters;
            }

            @Override
            public int getRowCount() {
                return listReporters.size();
            }
            @Override
            public int getColumnCount() {
                return 2;
            }
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Scenario.Reporting.Reporter reporter = listReporters.get(rowIndex);
                switch (columnIndex){
                    case 0: return reporter.isEnabled();
                    case 1: return reporter.getClazz();
                    default: return null;
                }
            }
            @Override
            public String getColumnName(int columnIndex){
                switch (columnIndex){
                    case 0: return "Enabled";
                    case 1: return "Reporter type";
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

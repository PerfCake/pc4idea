package org.perfcake.pc4idea.editor.wizard;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;
import org.perfcake.model.Scenario;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 22.10.2014
 */
public class PeriodsEditor extends JPanel {
    private JTable tablePeriods;
    private JScrollPane scrollPaneTablePeriods;
    private JButton buttonAddPeriod;
    private JButton buttonEditPeriod;
    private JButton buttonDeletePeriod;

    public PeriodsEditor(){
        initComponents();
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

    public void setPeriods(List<Scenario.Reporting.Reporter.Destination.Period> periods){
        PeriodsTableModel model = new PeriodsTableModel();
        model.addPeriods(periods);
        tablePeriods.setModel(model);
    }

    public List<Scenario.Reporting.Reporter.Destination.Period> getPeriods(){
        return ((PeriodsTableModel)tablePeriods.getModel()).getPeriods();
    }

    private class PeriodsTableModel extends AbstractTableModel {
        private List<Scenario.Reporting.Reporter.Destination.Period> listPeriods = new ArrayList<>();

        private PeriodsTableModel(){}

        public void addPeriods(List<Scenario.Reporting.Reporter.Destination.Period> periods){
            listPeriods.addAll(periods);
        }

        public List<Scenario.Reporting.Reporter.Destination.Period> getPeriods(){
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
}

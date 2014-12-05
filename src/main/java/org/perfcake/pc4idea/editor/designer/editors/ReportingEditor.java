package org.perfcake.pc4idea.editor.designer.editors;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.Messages;
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
 * Date: 26.10.2014
 */
public class ReportingEditor extends AbstractEditor {
    private EditorTablePanel tablePanelReporters;
    private PropertiesEditor panelProperties;

    private boolean warningShown = false;

    public ReportingEditor(){
        initComponents();
    }

    private void initComponents(){
        tablePanelReporters = new EditorTablePanel(new ReportersTableModel(new ArrayList<Scenario.Reporting.Reporter>())) {
            @Override
            public void tableClickedActionPerformed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = tablePanelReporters.getTable().getSelectedRow();
                        if (selectedRow >= 0) {
                            ReporterEditor reporterEditor = new ReporterEditor();

                            reporterEditor.setReporter(((ReportersTableModel) tablePanelReporters.getTable().getModel()).getReporterList().get(selectedRow));
                            ScenarioDialogEditor dialog = new ScenarioDialogEditor(reporterEditor);
                            dialog.show();
                            if (dialog.getExitCode() == 0) {
                                ((ReportersTableModel) tablePanelReporters.getTable().getModel()).getReporterList().set(selectedRow, reporterEditor.getReporter());
                                tablePanelReporters.getTable().repaint();
                            }
                        }
                    }
                }
            }

            @Override
            public void buttonAddActionPerformed(ActionEvent e) {
                ReporterEditor reporterEditor = new ReporterEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(reporterEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Scenario.Reporting.Reporter reporter = reporterEditor.getReporter();
                    ((ReportersTableModel)tablePanelReporters.getTable().getModel()).getReporterList().add(reporter);
                    tablePanelReporters.getTable().repaint();
                    tablePanelReporters.getTable().revalidate();
                }
            }

            @Override
            public void buttonEditActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelReporters.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    ReporterEditor reporterEditor = new ReporterEditor();
                    reporterEditor.setReporter(((ReportersTableModel) tablePanelReporters.getTable().getModel()).getReporterList().get(selectedRow));
                    ScenarioDialogEditor dialog = new ScenarioDialogEditor(reporterEditor);
                    dialog.show();
                    if (dialog.getExitCode() == 0) {
                        ((ReportersTableModel) tablePanelReporters.getTable().getModel()).getReporterList().set(selectedRow, reporterEditor.getReporter());
                        tablePanelReporters.getTable().repaint();
                    }
                }
            }

            @Override
            public void buttonDeleteActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelReporters.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    ((ReportersTableModel)tablePanelReporters.getTable().getModel()).getReporterList().remove(selectedRow);
                    tablePanelReporters.getTable().repaint();
                    tablePanelReporters.getTable().revalidate();
                }
            }
        };

        panelProperties = new PropertiesEditor();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
            .addComponent(tablePanelReporters)
            .addComponent(panelProperties));
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(tablePanelReporters)
            .addGap(10)
            .addComponent(panelProperties));
    }

    public void setReporting(Scenario.Reporting reporting){
        tablePanelReporters.getTable().setModel(new ReportersTableModel(reporting.getReporter()));
        panelProperties.setListProperties(reporting.getProperty());
    }

    public Scenario.Reporting getReporting(){
        Scenario.Reporting newReporting = new Scenario.Reporting();
        newReporting.getReporter().addAll(((ReportersTableModel) tablePanelReporters.getTable().getModel()).getReporterList());
        newReporting.getProperty().addAll(panelProperties.getListProperties());
        return newReporting;
    }

    @Override
    public String getTitle(){
        return "Reporting Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        ValidationInfo info = null;
        boolean noneReporter = ((ReportersTableModel) tablePanelReporters.getTable().getModel()).getReporterList().isEmpty();

        if (noneReporter && !panelProperties.getListProperties().isEmpty() && !warningShown){
            int result = Messages.showYesNoDialog("There are no reporters, but there are some properties\n" +
                                                  "and its not valid. If you continue, properties will be\n" +
                                                  "removed.\n\n" +
                            "Would you like to continue?",
                    "Removing Properties", AllIcons.General.WarningDialog);
            if (result != 0){
                info = new ValidationInfo("OK Interrupted...");
                warningShown = true;
            }
        }

        return info;
    }

    private class ReportersTableModel extends AbstractTableModel {
        private List<Scenario.Reporting.Reporter> reporterList = new ArrayList<>();

        private ReportersTableModel(List<Scenario.Reporting.Reporter> reporters){
            reporterList.addAll(reporters);
        }

        public List<Scenario.Reporting.Reporter> getReporterList(){
            return reporterList;
        }

        @Override
        public int getRowCount() {
            return reporterList.size();
        }
        @Override
        public int getColumnCount() {
            return 2;
        }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Scenario.Reporting.Reporter reporter = reporterList.get(rowIndex);
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
}

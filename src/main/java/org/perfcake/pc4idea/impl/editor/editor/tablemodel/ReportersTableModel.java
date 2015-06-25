package org.perfcake.pc4idea.impl.editor.editor.tablemodel;

import com.intellij.openapi.module.Module;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.tablemodel.EditorTableModel;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.editor.component.ReporterEditor;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/17/15.
 */
public class ReportersTableModel extends AbstractTableModel implements EditorTableModel {
    private List<Scenario.Reporting.Reporter> reporterList = new ArrayList<>();
    private Module module;

    public ReportersTableModel(List<Scenario.Reporting.Reporter> reporters, Module module) {
        reporterList.addAll(reporters);
        this.module = module;
    }

    public List<Scenario.Reporting.Reporter> getReporterList() {
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
        switch (columnIndex) {
            case 0:
                return reporter.getClazz();
            case 1:
                return reporter.isEnabled();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Reporter type";
            case 1:
                return "Enabled";
            default:
                return "";
        }
    }

    @Override
    public void reorderRows(int fromIndex, int toIndex) {
        Scenario.Reporting.Reporter reporter = reporterList.get(fromIndex);
        reporterList.remove(fromIndex);
        reporterList.add(toIndex, reporter);
    }

    @Override
    public void addRow() {
        ReporterEditor editor = new ReporterEditor(module);
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Reporting.Reporter reporter = editor.getReporter();
            reporterList.add(reporter);
            fireTableDataChanged();
        }
    }

    @Override
    public void editRow(int row) {
        ReporterEditor editor = new ReporterEditor(module);
        editor.setReporter(reporterList.get(row));
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Reporting.Reporter reporter = editor.getReporter();
            reporterList.set(row, reporter);
            fireTableDataChanged();
        }
    }

    @Override
    public void deleteRow(int row) {
        reporterList.remove(row);
        fireTableDataChanged();
    }
}

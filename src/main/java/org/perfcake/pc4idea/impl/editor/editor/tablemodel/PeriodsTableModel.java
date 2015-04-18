package org.perfcake.pc4idea.impl.editor.editor.tablemodel;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.tablemodel.EditorTableModel;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.editor.component.PeriodEditor;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/17/15.
 */
public class PeriodsTableModel extends AbstractTableModel implements EditorTableModel {
    private List<Scenario.Reporting.Reporter.Destination.Period> periodList = new ArrayList<>();

    public PeriodsTableModel(List<Scenario.Reporting.Reporter.Destination.Period> periods) {
        periodList.addAll(periods);
    }

    public List<Scenario.Reporting.Reporter.Destination.Period> getPeriodList() {
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
        switch (columnIndex) {
            case 0:
                return period.getType();
            case 1:
                return period.getValue();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Period Type";
            case 1:
                return "Period Value";
            default:
                return "";
        }
    }

    @Override
    public void reorderRows(int fromIndex, int toIndex) {
        Scenario.Reporting.Reporter.Destination.Period period = periodList.get(fromIndex);
        periodList.remove(fromIndex);
        periodList.add(toIndex, period);
    }

    @Override
    public void addRow() {
        PeriodEditor editor = new PeriodEditor();
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Reporting.Reporter.Destination.Period period = editor.getPeriod();
            periodList.add(period);
            fireTableDataChanged();
        }
    }

    @Override
    public void editRow(int row) {
        PeriodEditor editor = new PeriodEditor();
        editor.setPeriod(periodList.get(row));
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Reporting.Reporter.Destination.Period period = editor.getPeriod();
            periodList.set(row, period);
            fireTableDataChanged();
        }
    }

    @Override
    public void deleteRow(int row) {
        periodList.remove(row);
        fireTableDataChanged();
    }
}

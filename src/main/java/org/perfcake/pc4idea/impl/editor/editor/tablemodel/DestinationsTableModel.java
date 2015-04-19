package org.perfcake.pc4idea.impl.editor.editor.tablemodel;

import com.intellij.openapi.module.Module;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.tablemodel.EditorTableModel;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.editor.component.DestinationEditor;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/17/15.
 */
public class DestinationsTableModel extends AbstractTableModel implements EditorTableModel {
    private List<Scenario.Reporting.Reporter.Destination> destinationList = new ArrayList<>();
    private Module module;

    public DestinationsTableModel(List<Scenario.Reporting.Reporter.Destination> destinations, Module module) {
        destinationList.addAll(destinations);
        this.module = module;
    }

    public List<Scenario.Reporting.Reporter.Destination> getDestinationList() {
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
        switch (columnIndex) {
            case 0:
                return destination.isEnabled();
            case 1:
                return destination.getClazz();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Enabled";
            case 1:
                return "Destination type";
            default:
                return "";
        }
    }

    @Override
    public void reorderRows(int fromIndex, int toIndex) {
        Scenario.Reporting.Reporter.Destination destination = destinationList.get(fromIndex);
        destinationList.remove(fromIndex);
        destinationList.add(toIndex, destination);
    }

    @Override
    public void addRow() {
        DestinationEditor editor = new DestinationEditor(module);
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Reporting.Reporter.Destination destination = editor.getDestination();
            destinationList.add(destination);
            fireTableDataChanged();
        }
    }

    @Override
    public void editRow(int row) {
        DestinationEditor editor = new DestinationEditor(module);
        editor.setDestination(destinationList.get(row));
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Reporting.Reporter.Destination destination = editor.getDestination();
            destinationList.set(row, destination);
            fireTableDataChanged();
        }
    }

    @Override
    public void deleteRow(int row) {
        destinationList.remove(row);
        fireTableDataChanged();
    }
}

package org.perfcake.pc4idea.impl.editor.editor.tablemodel;

import org.perfcake.model.Header;
import org.perfcake.pc4idea.api.editor.editor.tablemodel.EditorTableModel;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.editor.component.HeaderEditor;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/17/15.
 */
public class HeadersTableModel extends AbstractTableModel implements EditorTableModel {
    private List<Header> headerList = new ArrayList<>();

    public HeadersTableModel(List<Header> headers) {
        headerList.addAll(headers);
    }

    public List<Header> getHeaderList() {
        return headerList;
    }

    @Override
    public int getRowCount() {
        return headerList.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Header header = headerList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return header.getName();
            case 1:
                return header.getValue();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Header Name";
            case 1:
                return "Header Value";
            default:
                return "";
        }
    }

    @Override
    public void reorderRows(int fromIndex, int toIndex) {
        Header header = headerList.get(fromIndex);
        headerList.remove(fromIndex);
        headerList.add(toIndex, header);
    }

    @Override
    public void addRow() {
        HeaderEditor editor = new HeaderEditor();
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Header header = editor.getHeader();
            headerList.add(header);
            fireTableDataChanged();
        }
    }

    @Override
    public void editRow(int row) {
        HeaderEditor editor = new HeaderEditor();
        editor.setHeader(headerList.get(row));
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Header header = editor.getHeader();
            headerList.set(row, header);
            fireTableDataChanged();
        }
    }

    @Override
    public void deleteRow(int row) {
        headerList.remove(row);
        fireTableDataChanged();
    }
}

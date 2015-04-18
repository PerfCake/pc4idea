package org.perfcake.pc4idea.api.editor.editor.tablemodel;

/**
 * Created by Stanislav Kaleta on 4/17/15.
 */
public interface EditorTableModel {
    public void reorderRows(int fromIndex, int toIndex);

    public void addRow();

    public void editRow(int row);

    public void deleteRow(int row);
}

package org.perfcake.pc4idea.api.editor.editor.tablemodel;

/**
 * Provides methods like reorder, add, edit and remove row for table model.
 * Every table model in dialog editors should implements this interface.
 *
 * @author Stanislav Kaleta
 */
public interface EditorTableModel {

    /**
     * Performs reorder action. Moves row to new location and shifts rest rows by one place.
     *
     * @param fromIndex index of the row which will be moved
     * @param toIndex index of the row which will be replaced with selected row
     */
    public void reorderRows(int fromIndex, int toIndex);

    /**
     * Creates new row and inserts it after last row in the table.
     */
    public void addRow();

    /**
     * Edits selected row and reflects changes made to the table.
     *
     * @param row index of the row which will be edited
     */
    public void editRow(int row);

    /**
     * Removes selected row from the table.
     *
     * @param row index of the row which will be removed from the table
     */
    public void deleteRow(int row);
}
